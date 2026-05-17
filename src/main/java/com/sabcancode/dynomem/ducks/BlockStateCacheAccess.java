package com.sabcancode.dynomem.ducks;

import net.minecraft.util.shape.VoxelShape;

/**
 * Access interface for BlockStateBase.Cache (inner class).
 * Mixin doesn't handle setters for fields of inner classes well, so we use this duck interface.
 */
public interface BlockStateCacheAccess {
    VoxelShape dynomem$getCollisionShape();

    void dynomem$setCollisionShape(VoxelShape newShape);

    boolean[] dynomem$getFaceSturdy();

    void dynomem$setFaceSturdy(boolean[] newFaceSturdyArray);
}
