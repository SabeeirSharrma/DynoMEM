package com.sabcancode.dynomem.fastmap;

import net.minecraft.util.math.MathHelper;

/**
 * A bitmask-based implementation of a FastMapKey. This reduces the density of data in the value matrix, but allows
 * accessing values with only some bitwise operations, which are much faster than integer division
 */
public record BinaryFastMapKey(int numValues, byte firstBitInValue, byte firstBitAfterValue) implements FastMapKey {

    public static BinaryFastMapKey create(int mapFactor, int numValues) {
        if (!MathHelper.isPowerOfTwo(mapFactor)) {
            throw new IllegalArgumentException("mapFactor must be power of 2");
        }
        final int addedFactor = MathHelper.smallestEncompassingPowerOfTwo(numValues);
        if (numValues > addedFactor || addedFactor >= 2 * numValues) {
            throw new IllegalStateException("Unexpected factor values");
        }
        final int setBitInBaseFactor = floorLog2(mapFactor);
        final int setBitInAddedFactor = floorLog2(addedFactor);
        if (setBitInBaseFactor + setBitInAddedFactor > 31) {
            throw new IllegalStateException("Too many bits required");
        }
        return new BinaryFastMapKey(
                numValues, (byte) setBitInBaseFactor, (byte) (setBitInBaseFactor + setBitInAddedFactor)
        );
    }

    @Override
    public int replaceIn(int mapIndex, int valueIndex) {
        if (valueIndex >= numValues) {
            return -1;
        }
        final int keepMask = ~lowestNBits(firstBitAfterValue) | lowestNBits(firstBitInValue);
        return (keepMask & mapIndex) | toPartialMapIndex(valueIndex);
    }

    @Override
    public int toPartialMapIndex(int internalIndex) {
        return internalIndex << firstBitInValue;
    }

    @Override
    public int getFactorToNext() {
        return 1 << (firstBitAfterValue - firstBitInValue);
    }

    @Override
    public int getIndexIn(int mapIndex) {
        return (mapIndex >> firstBitInValue) & lowestNBits((byte) (firstBitAfterValue - firstBitInValue));
    }

    private static int lowestNBits(byte n) {
        if (n >= Integer.SIZE) {
            return -1;
        } else {
            return (1 << n) - 1;
        }
    }

    private static int floorLog2(int value) {
        return Integer.SIZE - 1 - Integer.numberOfLeadingZeros(value);
    }
}
