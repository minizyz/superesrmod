package com.superesrmod.upscale.framegen;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.superesrmod.SuperESRMod;
import com.superesrmod.config.ModConfig;
import com.superesrmod.platform.PlatformHelper;
import net.minecraft.client.Minecraft;

/**
 * 帧生成管理器。支持：
 * - FRAME_BLEND: 简单 50/50 帧混合，全平台
 * - DYNAMIC_BLEND: 动态权重混合，全平台
 * - DLSS3_FG / FSR3_FG: 原生 SDK 时域插值（暂降级为动态混合）
 */
public class FrameGenManager {

    private FrameGenType type = FrameGenType.OFF;
    private FrameInterpolator interpolator;
    private long presentCount = 0;
    private boolean active = false;

    public void init() { reload(); }

    public void reload() {
        if (interpolator != null) { interpolator.destroy(); interpolator = null; }

        var cfg = ModConfig.FRAME_GEN_TYPE.get();
        this.type = switch (cfg) {
            case OFF -> FrameGenType.OFF;
            case FRAME_BLEND -> FrameGenType.FRAME_BLEND;
            case DYNAMIC_BLEND -> FrameGenType.DYNAMIC_BLEND;
            case DLSS3_FG -> PlatformHelper.isWindows() ? FrameGenType.DLSS3_FG : FrameGenType.OFF;
            case FSR3_FG -> FrameGenType.FSR3_FG;
        };
        this.active = type != FrameGenType.OFF;

        if (active) {
            interpolator = switch (type) {
                case FRAME_BLEND -> new BlendInterpolator();
                case DYNAMIC_BLEND -> new DynamicBlendInterpolator();
                case DLSS3_FG, FSR3_FG -> {
                    SuperESRMod.LOGGER.warn("[FrameGen] {} 原生 SDK 未就绪，降级为动态帧混合", type);
                    yield new DynamicBlendInterpolator();
                }
                case OFF -> null;
            };
            if (interpolator != null) {
                Minecraft mc = Minecraft.getInstance();
                interpolator.init(mc.getWindow().getWidth(), mc.getWindow().getHeight());
            }
        }
        SuperESRMod.LOGGER.info("[FrameGen] type={}, active={}", type, active);
    }

    public void apply(RenderTarget currentTarget) {
        if (!active || interpolator == null) return;
        presentCount++;
        interpolator.interpolate(currentTarget, currentTarget, presentCount);
    }

    public boolean isActive() { return active; }
    public FrameGenType getType() { return type; }

    public void destroy() {
        if (interpolator != null) { interpolator.destroy(); interpolator = null; }
    }
}
