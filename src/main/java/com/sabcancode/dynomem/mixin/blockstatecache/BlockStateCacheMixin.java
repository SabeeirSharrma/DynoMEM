package com.sabcancode.dynomem.mixin.blockstatecache;

import com.sabcancode.dynomem.ducks.BlockStateCacheAccess;
import net.minecraft.util.shape.VoxelShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net.minecraft.block.AbstractBlock$AbstractBlockState$ShapeCache")
public class BlockStateCacheMixin implements BlockStateCacheAccess {
    @Shadow
    @Final
    @Mutable
    protected VoxelShape collisionShape;

    @Shadow
    @Final
    @Mutable
    private boolean[] solidSides;

    @Override
    public VoxelShape dynomem$getCollisionShape() {
        return this.collisionShape;
    }

    @Override
    public void dynomem$setCollisionShape(VoxelShape newShape) {
        this.collisionShape = newShape;
    }

    @Override
    public boolean[] dynomem$getFaceSturdy() {
        return solidSides;
    }

    @Override
    public void dynomem$setFaceSturdy(final boolean[] newFaceSturdyArray) {
        this.solidSides = newFaceSturdyArray;
    }
}
