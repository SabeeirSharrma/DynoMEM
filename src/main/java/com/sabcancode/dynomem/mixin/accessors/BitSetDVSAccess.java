package com.sabcancode.dynomem.mixin.accessors;

import net.minecraft.util.shape.BitSetVoxelSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.BitSet;

@Mixin(BitSetVoxelSet.class)
public interface BitSetDVSAccess extends DiscreteVSAccess {
    @Accessor
    BitSet getStorage();

    @Accessor
    int getMinX();

    @Accessor
    int getMinY();

    @Accessor
    int getMinZ();

    @Accessor
    int getMaxX();

    @Accessor
    int getMaxY();

    @Accessor
    int getMaxZ();
}
