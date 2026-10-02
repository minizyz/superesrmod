package com.superesrmod.config;

public class ModConfig {

    // ---- 超分类型（默认关闭，避免闪屏；用户手动开启） ----
    public static volatile UpscaleTypeConfig UPSCALE_TYPE = UpscaleTypeConfig.OFF;
    public static volatile double INTERNAL_SCALE = 0.67;
    public static volatile boolean ENABLE_SHARPNESS = true;
    public static volatile double SHARPNESS = 1.0;

    // ---- 帧生成 ----
    public static volatile FrameGenTypeConfig FRAME_GEN_TYPE = FrameGenTypeConfig.OFF;

    // ---- 高级 ----
    public static volatile FSRQualityMode FSR_QUALITY_MODE = FSRQualityMode.BALANCED;
    public static volatile boolean ENABLE_DEBUG_OVERLAY = false;

    public enum UpscaleTypeConfig { OFF, DLSS, FSR1, FSR2 }
    public enum FrameGenTypeConfig { OFF, FRAME_BLEND, DYNAMIC_BLEND, DLSS3_FG, FSR3_FG }
    public enum FSRQualityMode {
        QUALITY(0.67f), BALANCED(0.59f), PERFORMANCE(0.50f), ULTRA_PERFORMANCE(0.33f);
        public final float scale;
        FSRQualityMode(float scale) { this.scale = scale; }
    }
}
