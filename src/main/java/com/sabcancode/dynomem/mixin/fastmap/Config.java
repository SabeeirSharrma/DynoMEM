package com.sabcancode.dynomem.mixin.fastmap;

import com.sabcancode.dynomem.common.config.DynoMEMConfig;
import com.sabcancode.dynomem.common.config.DynoMEMMixinConfig;

public class Config extends DynoMEMMixinConfig {
    public Config() {
        super(DynoMEMConfig.NEIGHBOR_LOOKUP);
    }
}
