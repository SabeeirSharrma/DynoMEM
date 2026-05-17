package com.sabcancode.dynomem.mixin.threaddetec;

import com.sabcancode.dynomem.ducks.SmallThreadDetectable;
import com.sabcancode.dynomem.common.SmallThreadingDetector;
import net.minecraft.util.thread.LockHelper;
import net.minecraft.world.chunk.PalettedContainer;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PalettedContainer.class)
public class PalettedContainerMixin implements SmallThreadDetectable {
    @Shadow
    @Final
    @Mutable
    private LockHelper lockHelper;

    @Unique
    private byte dynomem$threadingState = UNLOCKED;

    @Inject(
            method = "<init>*",
            at = @At("TAIL")
    )
    public void redirectBuildThreadingDetector(CallbackInfo ci) {
        this.lockHelper = null;
    }

    /**
     * @reason The vanilla LockHelper field is null now, replaced by SmallThreadingDetector
     * @author DynoMEM
     */
    @Overwrite
    public void lock() {
        SmallThreadingDetector.acquire(this, "PalettedContainer");
    }

    /**
     * @reason The vanilla LockHelper field is null now, replaced by SmallThreadingDetector
     * @author DynoMEM
     */
    @Overwrite
    public void unlock() {
        SmallThreadingDetector.release(this);
    }

    @Override
    public byte dynomem$getState() {
        return dynomem$threadingState;
    }

    @Override
    public void dynomem$setState(byte newState) {
        dynomem$threadingState = newState;
    }
}
