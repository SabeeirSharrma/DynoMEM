package com.sabcancode.dynomem.mixin.accessors;

import net.minecraft.util.shape.VoxelSet;
import net.minecraft.util.shape.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VoxelShape.class)
public interface VoxelShapeAccess {
    @Accessor("voxels")
    VoxelSet getShape();

    @Accessor("shapeCache")
    @Nullable
    VoxelShape[] getFaces();

    @Accessor("voxels")
    @Mutable
    void setShape(VoxelSet newPart);

    @Accessor("shapeCache")
    void setFaces(@Nullable VoxelShape[] newCache);
}
