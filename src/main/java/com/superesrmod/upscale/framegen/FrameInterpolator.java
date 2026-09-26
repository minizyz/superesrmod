package com.superesrmod.upscale.framegen;

import com.mojang.blaze3d.pipeline.RenderTarget;

/**
 * 帧插值器接口。每种插帧算法实现此接口。
 *
 * <p>与 DLSS3 FG / FSR3 FG 不同，这些简单算法不需要运动向量或原生 SDK，
 * 仅通过颜色缓冲混合实现视觉上的帧率提升。</p>
 */
public interface FrameInterpolator {

    FrameGenType getType();

    void init(int width, int height);

    void interpolate(RenderTarget currentFrame, RenderTarget outTarget, long presentCount);

    void destroy();
}
