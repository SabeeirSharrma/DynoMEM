package com.sabcancode.dynomem.mixin.threaddetec;

import com.sabcancode.dynomem.common.config.DynoMEMConfig;
import com.sabcancode.dynomem.common.config.DynoMEMMixinConfig;

public class Config extends DynoMEMMixinConfig {
    public Config() {
        super(DynoMEMConfig.THREADING_DETECTOR, LithiumSupportState.INCOMPATIBLE, true);
    }
}
