package com.superesrmod.upscale.framegen;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.superesrmod.SuperESRMod;
import com.superesrmod.config.ModConfig;
import com.superesrmod.platform.PlatformHelper;

public class FrameGenManager {

    private FrameGenType type = FrameGenType.OFF;
    private RenderTarget previousColorTarget;
    private boolean active = false;

    public void init() { reload(); }

    public void reload() {
        var cfg = ModConfig.FRAME_GEN_TYPE.get();
        this.type = switch (cfg) {
            case OFF -> FrameGenType.OFF;
            case DLSS3_FG -> PlatformHelper.isWindows() ? FrameGenType.DLSS3_FG : FrameGenType.OFF;
            case FSR3_FG -> FrameGenType.FSR3_FG;
        };
        this.active = type != FrameGenType.OFF;
        SuperESRMod.LOGGER.info("[FrameGen] type={}, active={}", type, active);
    }

    public void apply(RenderTarget currentTarget) {
        if (!active) return;
        if (previousColorTarget == null) {
            previousColorTarget = new RenderTarget(false, "superesrmod_fg_prev");
            previousColorTarget.createBuffers(currentTarget.width, currentTarget.height, false);
        }
        // TODO: copy currentTarget -> previousColorTarget, call native FG SDK
    }

    public boolean isActive() { return active; }
    public FrameGenType getType() { return type; }

    public void destroy() {
        if (previousColorTarget != null) { previousColorTarget.destroyBuffers(); previousColorTarget = null; }
    }
}
