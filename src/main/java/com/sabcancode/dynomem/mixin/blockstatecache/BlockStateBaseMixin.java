package com.sabcancode.dynomem.mixin.blockstatecache;

import com.sabcancode.dynomem.impl.BlockStateCacheImpl;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class BlockStateBaseMixin {
    @Shadow
    protected abstract BlockState asBlockState();

    @Inject(method = "initShapeCache", at = @At("HEAD"))
    public void cacheStateHead(CallbackInfo ci) {
        BlockStateCacheImpl.deduplicateCachePre((AbstractBlock.AbstractBlockState)(Object)this);
    }

    @Inject(method = "initShapeCache", at = @At("TAIL"))
    public void cacheStateTail(CallbackInfo ci) {
        BlockStateCacheImpl.deduplicateCachePost((AbstractBlock.AbstractBlockState)(Object)this);
    }
}
