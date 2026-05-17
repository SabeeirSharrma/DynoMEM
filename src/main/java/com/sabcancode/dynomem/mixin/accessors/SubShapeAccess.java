package com.sabcancode.dynomem.mixin.accessors;

import net.minecraft.util.shape.VoxelSet;
import net.minecraft.util.shape.CroppedVoxelSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CroppedVoxelSet.class)
public interface SubShapeAccess extends DiscreteVSAccess {
    @Accessor
    VoxelSet getParent();

    @Accessor("minX")
    int getStartX();

    @Accessor("minY")
    int getStartY();

    @Accessor("minZ")
    int getStartZ();

    @Accessor("maxX")
    int getEndX();

    @Accessor("maxY")
    int getEndY();

    @Accessor("maxZ")
    int getEndZ();
}
