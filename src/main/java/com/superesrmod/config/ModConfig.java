package com.superesrmod.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ModConfig {

    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.EnumValue<UpscaleTypeConfig> UPSCALE_TYPE;
    public static final ModConfigSpec.DoubleValue INTERNAL_SCALE;
    public static final ModConfigSpec.BooleanValue ENABLE_SHARPNESS;
    public static final ModConfigSpec.DoubleValue SHARPNESS;

    public static final ModConfigSpec.EnumValue<FrameGenTypeConfig> FRAME_GEN_TYPE;
    public static final ModConfigSpec.BooleanValue FG_ENABLE_MOTION_VECTORS;

    public static final ModConfigSpec.BooleanValue DLSS_AUTO_GAME_RESHADER;
    public static final ModConfigSpec.EnumValue<FSRQualityMode> FSR_QUALITY_MODE;
    public static final ModConfigSpec.BooleanValue ENABLE_DEBUG_OVERLAY;

    static {
        var builder = new ModConfigSpec.Builder();

        builder.push("upscaling");
        UPSCALE_TYPE = builder
                .comment("超分辨率类型: OFF / DLSS / FSR1 / FSR2")
                .defineEnum("upscaleType", UpscaleTypeConfig.FSR1);
        INTERNAL_SCALE = builder
                .comment("内部渲染缩放比 (0.25 ~ 1.0)")
                .defineInRange("internalScale", 0.67, 0.25, 1.0);
        ENABLE_SHARPNESS = builder
                .comment("启用 RCAS 锐化")
                .define("enableSharpness", true);
        SHARPNESS = builder
                .comment("锐化强度 0.0 ~ 2.0")
                .defineInRange("sharpness", 1.0, 0.0, 2.0);
        builder.pop();

        builder.push("framegen");
        FRAME_GEN_TYPE = builder
                .comment("帧生成类型: OFF / DLSS3_FG / FSR3_FG")
                .defineEnum("frameGenType", FrameGenTypeConfig.OFF);
        FG_ENABLE_MOTION_VECTORS = builder
                .comment("为帧生成启用运动向量渲染")
                .define("enableMotionVectors", true);
        builder.pop();

        builder.push("advanced");
        DLSS_AUTO_GAME_RESHADER = builder
                .comment("DLSS 自动游戏内优化")
                .define("dlssAutoGameReShade", true);
        FSR_QUALITY_MODE = builder
                .comment("FSR 质量模式")
                .defineEnum("fsrQualityMode", FSRQualityMode.BALANCED);
        ENABLE_DEBUG_OVERLAY = builder
                .comment("调试信息 overlay")
                .define("enableDebugOverlay", false);
        builder.pop();

        SPEC = builder.build();
    }

    public enum UpscaleTypeConfig { OFF, DLSS, FSR1, FSR2 }
    public enum FrameGenTypeConfig { OFF, DLSS3_FG, FSR3_FG }

    public enum FSRQualityMode {
        QUALITY(0.67f), BALANCED(0.59f), PERFORMANCE(0.50f), ULTRA_PERFORMANCE(0.33f);
        public final float scale;
        FSRQualityMode(float scale) { this.scale = scale; }
    }
}
