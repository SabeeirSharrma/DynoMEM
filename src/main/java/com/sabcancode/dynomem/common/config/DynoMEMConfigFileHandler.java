package com.sabcancode.dynomem.common.config;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DynoMEMConfigFileHandler {
    private static final String CONFIG_NAME = "dynomem.mixin.properties";

    public void readAndUpdateConfig(List<DynoMEMConfig.Option> options) throws IOException {
        Path configDir = FabricLoader.getInstance().getConfigDir();
        Path config = configDir.resolve(CONFIG_NAME);
        if (!Files.exists(config)) {
            try {
                Files.createDirectories(configDir);
            } catch (FileAlreadyExistsException x) {
                if (!Files.isDirectory(configDir)) {
                    throw new IOException("Config dir exists, but is not a directory?", x);
                }
            }
            Files.createFile(config);
        }
        Properties propsInFile = new Properties();
        propsInFile.load(Files.newInputStream(config));
        Object2BooleanMap<String> existingOptions = new Object2BooleanOpenHashMap<>();
        for (String key : propsInFile.stringPropertyNames()) {
            existingOptions.put(key, Boolean.parseBoolean(propsInFile.getProperty(key)));
        }
        List<String> newLines = new ArrayList<>();
        Object2BooleanMap<String> actualOptions = new Object2BooleanOpenHashMap<>();
        for (DynoMEMConfig.Option o : options) {
            final boolean value = existingOptions.getOrDefault(o.getName(), o.getDefaultValue());
            actualOptions.put(o.getName(), value);
            newLines.add("# " + o.getComment());
            newLines.add(o.getName() + " = " + value);
        }
        for (DynoMEMConfig.Option o : options) {
            o.set(actualOptions::getBoolean);
        }
        Files.write(config, newLines);
    }
}
