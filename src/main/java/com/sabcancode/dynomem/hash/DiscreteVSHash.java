package com.sabcancode.dynomem.hash;

import it.unimi.dsi.fastutil.Hash;
import com.sabcancode.dynomem.mixin.accessors.BitSetDVSAccess;
import com.sabcancode.dynomem.mixin.accessors.DiscreteVSAccess;
import com.sabcancode.dynomem.mixin.accessors.SubShapeAccess;
import net.minecraft.util.shape.VoxelSet;

import java.util.Objects;

public class DiscreteVSHash implements Hash.Strategy<VoxelSet> {
    public static final DiscreteVSHash INSTANCE = new DiscreteVSHash();

    @Override
    public int hashCode(VoxelSet shape) {
        return hashCode((DiscreteVSAccess) shape);
    }

    public int hashCode(DiscreteVSAccess o) {
        int result = o.getXSize();
        result = 31 * result + o.getYSize();
        result = 31 * result + o.getZSize();
        if (o instanceof SubShapeAccess access) {
            result = 31 * result + access.getStartX();
            result = 31 * result + access.getStartY();
            result = 31 * result + access.getStartZ();
            result = 31 * result + access.getEndX();
            result = 31 * result + access.getEndY();
            result = 31 * result + access.getEndZ();
            result = 31 * result + hashCode((DiscreteVSAccess) access.getParent());
            return result;
        } else if (o instanceof BitSetDVSAccess access) {
            result = 31 * result + access.getMinX();
            result = 31 * result + access.getMinY();
            result = 31 * result + access.getMinZ();
            result = 31 * result + access.getMaxX();
            result = 31 * result + access.getMaxY();
            result = 31 * result + access.getMaxZ();
            result = 31 * result + Objects.hashCode(access.getStorage());
            return result;
        } else {
            return 31 * result + Objects.hashCode(o);
        }
    }

    @Override
    public boolean equals(VoxelSet a, VoxelSet b) {
        return equals((DiscreteVSAccess) a, (DiscreteVSAccess) b);
    }

    public boolean equals(DiscreteVSAccess a, DiscreteVSAccess b) {
        if (a == b) return true;
        if (a == null || b == null) return false;
        if (a.getClass() != b.getClass()) return false;
        if (a.getXSize() != b.getXSize() || a.getYSize() != b.getYSize() || a.getZSize() != b.getZSize()) return false;
        if (a instanceof SubShapeAccess accessA) {
            SubShapeAccess accessB = (SubShapeAccess) b;
            return accessA.getEndX() == accessB.getEndX() &&
                    accessA.getEndY() == accessB.getEndY() &&
                    accessA.getEndZ() == accessB.getEndZ() &&
                    accessA.getStartX() == accessB.getStartX() &&
                    accessA.getStartY() == accessB.getStartY() &&
                    accessA.getStartZ() == accessB.getStartZ() &&
                    equals((DiscreteVSAccess) accessA.getParent(), (DiscreteVSAccess) accessB.getParent());
        } else if (a instanceof BitSetDVSAccess accessA) {
            BitSetDVSAccess accessB = (BitSetDVSAccess) b;
            return accessA.getMaxX() == accessB.getMaxX() &&
                    accessA.getMaxY() == accessB.getMaxY() &&
                    accessA.getMaxZ() == accessB.getMaxZ() &&
                    accessA.getMinX() == accessB.getMinX() &&
                    accessA.getMinY() == accessB.getMinY() &&
                    accessA.getMinZ() == accessB.getMinZ() &&
                    accessA.getStorage().equals(accessB.getStorage());
        } else {
            return a.equals(b);
        }
    }
}
