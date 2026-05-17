package com.sabcancode.dynomem.mixin.accessors;

import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.SlicedVoxelShape;
import net.minecraft.util.shape.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SlicedVoxelShape.class)
public interface SliceShapeAccess extends VoxelShapeAccess {
    @Accessor("shape")
    VoxelShape getDelegate();

    @Accessor
    Direction.Axis getAxis();
}
