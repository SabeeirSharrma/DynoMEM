package com.sabcancode.dynomem.mixin.accessors;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.util.shape.ArrayVoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ArrayVoxelShape.class)
public interface ArrayVSAccess extends VoxelShapeAccess {
    @Accessor("xPoints")
    @Mutable
    void setXPoints(DoubleList newPoints);

    @Accessor("yPoints")
    @Mutable
    void setYPoints(DoubleList newPoints);

    @Accessor("zPoints")
    @Mutable
    void setZPoints(DoubleList newPoints);

    @Accessor("xPoints")
    DoubleList getXPoints();

    @Accessor("yPoints")
    DoubleList getYPoints();

    @Accessor("zPoints")
    DoubleList getZPoints();
}
