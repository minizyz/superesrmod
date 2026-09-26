package com.superesrmod.upscale;

import com.mojang.blaze3d.pipeline.RenderTarget;

public interface UpscaleProcessor {
    UpscaleType getType();
    void init(int screenWidth, int screenHeight, int renderWidth, int renderHeight);
    RenderTarget prepare();
    void postWorldRender(int colorTexture, int depthTexture, RenderTarget outTarget);
    int getRenderWidth();
    int getRenderHeight();
    void destroy();
}
