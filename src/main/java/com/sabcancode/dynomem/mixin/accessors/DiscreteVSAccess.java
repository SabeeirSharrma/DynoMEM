package com.sabcancode.dynomem.mixin.accessors;

import net.minecraft.util.shape.VoxelSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VoxelSet.class)
public interface DiscreteVSAccess {
    @Accessor("sizeX")
    int getXSize();

    @Accessor("sizeY")
    int getYSize();

    @Accessor("sizeZ")
    int getZSize();
}
