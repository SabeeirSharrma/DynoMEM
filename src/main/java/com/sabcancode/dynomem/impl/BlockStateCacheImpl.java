package com.sabcancode.dynomem.impl;

import com.google.common.base.Suppliers;
import it.unimi.dsi.fastutil.booleans.BooleanArrays;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import com.sabcancode.dynomem.ducks.BlockStateCacheAccess;
import com.sabcancode.dynomem.hash.ArrayVoxelShapeHash;
import com.sabcancode.dynomem.hash.VoxelShapeHash;
import com.sabcancode.dynomem.mixin.accessors.ArrayVSAccess;
import com.sabcancode.dynomem.mixin.accessors.SliceShapeAccess;
import com.sabcancode.dynomem.common.Constants;
import net.minecraft.block.AbstractBlock;
import net.minecraft.util.shape.ArrayVoxelShape;
import net.minecraft.util.shape.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockStateCacheImpl {
    public static final Map<ArrayVSAccess, ArrayVSAccess> CACHE_COLLIDE = new Object2ObjectOpenCustomHashMap<>(
            ArrayVoxelShapeHash.INSTANCE
    );
    public static final Map<boolean[], boolean[]> CACHE_FACE_STURDY = new Object2ObjectOpenCustomHashMap<>(
            BooleanArrays.HASH_STRATEGY
    );

    // Get the cache from a blockstate via reflection since Mixin doesn't handle private inner classes well
    private static final Supplier<Function<AbstractBlock.AbstractBlockState, BlockStateCacheAccess>> GET_CACHE = Suppliers.memoize(() -> {
        try {
            final String cacheName = Constants.PLATFORM_HOOKS.computeBlockstateCacheFieldName();
            final Field cacheField = AbstractBlock.AbstractBlockState.class.getDeclaredField(cacheName);
            cacheField.setAccessible(true);
            MethodHandle getter = MethodHandles.lookup().unreflectGetter(cacheField);
            return state -> {
                try {
                    return (BlockStateCacheAccess) getter.invoke(state);
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
            };
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    });

    private static final ThreadLocal<BlockStateCacheAccess> LAST_CACHE = new ThreadLocal<>();

    public static void deduplicateCachePre(AbstractBlock.AbstractBlockState state) {
        LAST_CACHE.set(GET_CACHE.get().apply(state));
    }

    public static void deduplicateCachePost(AbstractBlock.AbstractBlockState state) {
        BlockStateCacheAccess newCache = GET_CACHE.get().apply(state);
        if (newCache != null) {
            final BlockStateCacheAccess oldCache = LAST_CACHE.get();
            deduplicateCollisionShape(newCache, oldCache);
            deduplicateFaceSturdyArray(newCache, oldCache);
            LAST_CACHE.remove();
        }
    }

    private static void deduplicateCollisionShape(
            BlockStateCacheAccess newCache, @Nullable BlockStateCacheAccess oldCache
    ) {
        VoxelShape dedupedCollisionShape;
        if (oldCache != null && VoxelShapeHash.INSTANCE.equals(
                oldCache.dynomem$getCollisionShape(), newCache.dynomem$getCollisionShape()
        )) {
            dedupedCollisionShape = oldCache.dynomem$getCollisionShape();
        } else {
            dedupedCollisionShape = newCache.dynomem$getCollisionShape();
            if (dedupedCollisionShape instanceof ArrayVSAccess access) {
                dedupedCollisionShape = (VoxelShape) CACHE_COLLIDE.computeIfAbsent(access, Function.identity());
            }
        }
        replaceInternals(dedupedCollisionShape, newCache.dynomem$getCollisionShape());
        newCache.dynomem$setCollisionShape(dedupedCollisionShape);
    }

    private static void deduplicateFaceSturdyArray(
            BlockStateCacheAccess newCache, @Nullable BlockStateCacheAccess oldCache
    ) {
        boolean[] dedupedFaceSturdy;
        if (oldCache != null && Arrays.equals(oldCache.dynomem$getFaceSturdy(), newCache.dynomem$getFaceSturdy())) {
            dedupedFaceSturdy = oldCache.dynomem$getFaceSturdy();
        } else {
            dedupedFaceSturdy = CACHE_FACE_STURDY.computeIfAbsent(newCache.dynomem$getFaceSturdy(), Function.identity());
        }
        newCache.dynomem$setFaceSturdy(dedupedFaceSturdy);
    }

    private static void replaceInternals(VoxelShape toKeep, VoxelShape toReplace) {
        if (toKeep instanceof ArrayVoxelShape keepArray && toReplace instanceof ArrayVoxelShape replaceArray) {
            replaceInternals(keepArray, replaceArray);
        }
    }

    public static void replaceInternals(ArrayVoxelShape toKeep, ArrayVoxelShape toReplace) {
        if (toKeep == toReplace) return;
        ArrayVSAccess toReplaceAccess = (ArrayVSAccess) toReplace;
        ArrayVSAccess toKeepAccess = (ArrayVSAccess) toKeep;
        toReplaceAccess.setXPoints(toKeepAccess.getXPoints());
        toReplaceAccess.setYPoints(toKeepAccess.getYPoints());
        toReplaceAccess.setZPoints(toKeepAccess.getZPoints());
        toReplaceAccess.setFaces(toKeepAccess.getFaces());
        toReplaceAccess.setShape(toKeepAccess.getShape());
    }

    @Nullable
    private static VoxelShape getRenderShape(@Nullable VoxelShape[] projected) {
        if (projected != null) {
            for (VoxelShape side : projected) {
                if (side instanceof SliceShapeAccess slice) {
                    return slice.getDelegate();
                }
            }
        }
        return null;
    }
}
