package com.superesrmod.upscale;
import com.superesrmod.gl.GLFramebuffer;
public interface UpscaleProcessor {
    UpscaleType getType();
    void init(int screenWidth, int screenHeight, int renderWidth, int renderHeight);
    GLFramebuffer prepare();
    void postWorldRender(GLFramebuffer lowRes, int outputFbo);
    int getRenderWidth();
    int getRenderHeight();
    void destroy();
}
