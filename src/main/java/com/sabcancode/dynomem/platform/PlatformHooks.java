package com.sabcancode.dynomem.platform;

import net.fabricmc.loader.api.FabricLoader;

public class PlatformHooks implements IPlatformHooks {
    @Override
    public String computeBlockstateCacheFieldName() {
        return FabricLoader.getInstance()
                .getMappingResolver()
                .mapFieldName(
                        "intermediary",
                        "net.minecraft.class_4970$class_4971",
                        "field_24736",
                        "Lnet/minecraft/class_4970$class_4971$class_3752;"
                );
    }
}
