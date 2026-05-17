package com.sabcancode.dynomem.mixin.blockstatecache;

import com.sabcancode.dynomem.common.config.DynoMEMConfig;
import com.sabcancode.dynomem.common.config.DynoMEMMixinConfig;

public class Config extends DynoMEMMixinConfig {
    public Config() {
        super(DynoMEMConfig.DEDUP_BLOCKSTATE_CACHE);
    }
}
