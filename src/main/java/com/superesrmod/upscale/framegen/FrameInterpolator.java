package com.superesrmod.upscale.framegen;
public interface FrameInterpolator {
    FrameGenType getType();
    void init(int width, int height);
    void interpolate(int currentFbo, int outputFbo, long presentCount);
    void destroy();
}
