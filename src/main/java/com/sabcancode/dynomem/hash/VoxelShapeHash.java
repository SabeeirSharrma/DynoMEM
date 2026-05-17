package com.sabcancode.dynomem.hash;

import it.unimi.dsi.fastutil.Hash;
import com.sabcancode.dynomem.mixin.accessors.ArrayVSAccess;
import com.sabcancode.dynomem.mixin.accessors.SliceShapeAccess;
import com.sabcancode.dynomem.mixin.accessors.VoxelShapeAccess;
import net.minecraft.util.shape.SimpleVoxelShape;
import net.minecraft.util.shape.VoxelShape;

public class VoxelShapeHash implements Hash.Strategy<VoxelShape> {
    public static final VoxelShapeHash INSTANCE = new VoxelShapeHash();

    @Override
    public int hashCode(VoxelShape o) {
        return hashCode((VoxelShapeAccess) o);
    }

    public int hashCode(VoxelShapeAccess o) {
        if (o instanceof SliceShapeAccess access) {
            return SliceShapeHash.INSTANCE.hashCode(access);
        } else if (o instanceof ArrayVSAccess access) {
            return ArrayVoxelShapeHash.INSTANCE.hashCode(access);
        } else if (isCubeShape(o)) {
            return DiscreteVSHash.INSTANCE.hashCode(o.getShape());
        } else {
            return o.hashCode();
        }
    }

    @Override
    public boolean equals(VoxelShape a, VoxelShape b) {
        return equals((VoxelShapeAccess) a, (VoxelShapeAccess) b);
    }

    public boolean equals(VoxelShapeAccess a, VoxelShapeAccess b) {
        if (a == b) return true;
        if (a == null || b == null) return false;
        if (a.getClass() != b.getClass()) return false;
        if (a instanceof SliceShapeAccess accessA) {
            return SliceShapeHash.INSTANCE.equals(accessA, (SliceShapeAccess) b);
        } else if (a instanceof ArrayVSAccess accessA) {
            return ArrayVoxelShapeHash.INSTANCE.equals(accessA, (ArrayVSAccess) b);
        } else if (isCubeShape(a)) {
            return DiscreteVSHash.INSTANCE.equals(a.getShape(), b.getShape());
        } else {
            return a.equals(b);
        }
    }

    private static boolean isCubeShape(Object o) {
        return o instanceof SimpleVoxelShape;
    }
}
