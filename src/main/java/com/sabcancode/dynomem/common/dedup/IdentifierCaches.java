package com.sabcancode.dynomem.common.dedup;

import com.sabcancode.dynomem.common.DynoMEMMod;

public class IdentifierCaches {
    public static final DeduplicationCache<String> NAMESPACES = new DeduplicationCache<>();
    public static final DeduplicationCache<String> PATH = new DeduplicationCache<>();

    public static void printDebug() {
        DynoMEMMod.LOGGER.info("[[[ Identifier de-duplication statistics ]]]");
        DynoMEMMod.LOGGER.info("Namespace cache: {}", NAMESPACES);
        DynoMEMMod.LOGGER.info("Path cache: {}", PATH);
    }
}
