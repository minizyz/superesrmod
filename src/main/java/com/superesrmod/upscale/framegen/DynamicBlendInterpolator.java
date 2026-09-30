package com.superesrmod.upscale.framegen;
import com.superesrmod.SuperESRMod;
public class DynamicBlendInterpolator extends BlendInterpolator {
    private float lastMotionLevel = 0.0f;
    @Override public FrameGenType getType() { return FrameGenType.DYNAMIC_BLEND; }
    @Override protected float resolveBlendFactor() {
        float motion = 0.2f;
        lastMotionLevel = motion;
        return 0.5f * (1.0f - motion) + 0.05f * motion;
    }
    @Override public void destroy() { super.destroy(); SuperESRMod.LOGGER.info("[DynamicBlendFG] destroyed, motion={}", lastMotionLevel); }
}
