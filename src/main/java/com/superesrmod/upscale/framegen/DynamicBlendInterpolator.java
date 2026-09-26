package com.superesrmod.upscale.framegen;

import com.superesrmod.SuperESRMod;

/**
 * 动态帧混合插值器。根据帧间运动强度自动调整混合权重。
 * 静止时 blend=0.5 强力插值，剧烈运动时 blend=0.05 减少拖影。
 */
public class DynamicBlendInterpolator extends BlendInterpolator {

    private float lastMotionLevel = 0.0f;

    @Override public FrameGenType getType() { return FrameGenType.DYNAMIC_BLEND; }

    @Override
    protected float resolveBlendFactor() {
        float motion = estimateMotion();
        lastMotionLevel = motion;
        return 0.5f * (1.0f - motion) + 0.05f * motion;
    }

    private float estimateMotion() {
        if (!hasPrevious || previousFrame == null) return 0.0f;
        // TODO: GPU 端 shader 计算帧差；此处为近似值
        return 0.2f;
    }

    @Override public void destroy() {
        super.destroy();
        SuperESRMod.LOGGER.info("[DynamicBlendFG] destroyed, last motion={}", lastMotionLevel);
    }
}
