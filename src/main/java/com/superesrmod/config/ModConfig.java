package com.superesrmod.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.superesrmod.SuperESRMod;
import net.fabricmc.loader.api.FabricLoader;
import java.io.*;
import java.nio.file.*;

/** 配置文件：config/superesrmod.json */
public class ModConfig {
    public static volatile UpscaleTypeConfig UPSCALE_TYPE = UpscaleTypeConfig.OFF;
    public static volatile double INTERNAL_SCALE = 0.67;
    public static volatile boolean ENABLE_SHARPNESS = true;
    public static volatile double SHARPNESS = 1.0;
    public static volatile FrameGenTypeConfig FRAME_GEN_TYPE = FrameGenTypeConfig.OFF;
    public static volatile FSRQualityMode FSR_QUALITY_MODE = FSRQualityMode.BALANCED;
    public static volatile boolean ENABLE_DEBUG_OVERLAY = false;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Path path() { return FabricLoader.getInstance().getConfigDir().resolve("superesrmod.json"); }

    public static void load() {
        try {
            Path p = path();
            if (!Files.exists(p)) { save(); return; }
            try (Reader r = Files.newBufferedReader(p)) {
                Data d = GSON.fromJson(r, Data.class);
                if (d != null) d.apply();
            }
        } catch (Exception e) { SuperESRMod.LOGGER.warn("[Config] load: {}", e.toString()); }
    }
    public static void save() {
        try {
            Path p = path();
            Files.createDirectories(p.getParent());
            try (Writer w = Files.newBufferedWriter(p)) { GSON.toJson(new Data(), w); }
        } catch (Exception e) { SuperESRMod.LOGGER.warn("[Config] save: {}", e.toString()); }
    }

    private static class Data {
        String upscale = UPSCALE_TYPE.name();
        double scale = INTERNAL_SCALE;
        String frameGen = FRAME_GEN_TYPE.name();
        String fsrQuality = FSR_QUALITY_MODE.name();
        void apply() {
            try { UPSCALE_TYPE = UpscaleTypeConfig.valueOf(upscale); } catch (Exception ignored) {}
            INTERNAL_SCALE = scale;
            try { FRAME_GEN_TYPE = FrameGenTypeConfig.valueOf(frameGen); } catch (Exception ignored) {}
            try { FSR_QUALITY_MODE = FSRQualityMode.valueOf(fsrQuality); } catch (Exception ignored) {}
        }
    }

    public enum UpscaleTypeConfig { OFF, DLSS, FSR1, FSR2 }
    public enum FrameGenTypeConfig { OFF, FRAME_BLEND, DYNAMIC_BLEND, DLSS3_FG, FSR3_FG }
    public enum FSRQualityMode {
        QUALITY(0.67f), BALANCED(0.59f), PERFORMANCE(0.50f), ULTRA_PERFORMANCE(0.33f);
        public final float scale;
        FSRQualityMode(float scale) { this.scale = scale; }
    }
}
