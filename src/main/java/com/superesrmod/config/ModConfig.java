package com.superesrmod.config;

/**
 * SuperESRMod 客户端配置（Fabric 无内置配置系统，用静态字段）。
 */
public class ModConfig {

    public static volatile UpscaleTypeConfig UPSCALE_TYPE = UpscaleTypeConfig.FSR1;
    public static volatile double INTERNAL_SCALE = 0.67;
    public static volatile boolean ENABLE_SHARPNESS = true;
    public static volatile double SHARPNESS = 1.0;

    public static volatile FrameGenTypeConfig FRAME_GEN_TYPE = FrameGenTypeConfig.OFF;

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
