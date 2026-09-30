package com.superesrmod.upscale.dlss;
import com.superesrmod.SuperESRMod;
import com.superesrmod.gl.GLFramebuffer;
import com.superesrmod.upscale.UpscaleProcessor;
import com.superesrmod.upscale.UpscaleType;
public class DLSSProcessor implements UpscaleProcessor {
    private long dlssContext = 0L;
    private int screenWidth, screenHeight, renderWidth, renderHeight;
    private boolean nativeReady = false;
    private int frameIndex = 0;
    @Override public UpscaleType getType() { return UpscaleType.DLSS; }
    @Override public void init(int screenWidth, int screenHeight, int renderWidth, int renderHeight) {
        this.screenWidth = screenWidth; this.screenHeight = screenHeight;
        this.renderWidth = renderWidth; this.renderHeight = renderHeight;
        if (!nativeReady) {
            nativeReady = DLSSLibraryLoader.tryLoad();
            if (!nativeReady) { SuperESRMod.LOGGER.error("[DLSS] native lib load failed"); return; }
        }
        dlssContext = DLSSNative.nvCreateContext(screenWidth, screenHeight, renderWidth, renderHeight);
        if (dlssContext == 0L) { SuperESRMod.LOGGER.error("[DLSS] context create failed"); return; }
        SuperESRMod.LOGGER.info("[DLSS] context created {}x{} -> {}x{}", renderWidth, renderHeight, screenWidth, screenHeight);
    }
    @Override public GLFramebuffer prepare() { return null; }
    @Override public void postWorldRender(GLFramebuffer lowRes, int outputFbo) {
        if (dlssContext == 0L || !nativeReady || lowRes == null) return;
        float jitterX = ((frameIndex % 2) == 0) ? 0.5f : -0.5f;
        float jitterY = ((frameIndex % 2) == 0) ? -0.25f : 0.25f;
        jitterX /= renderWidth; jitterY /= renderHeight;
        DLSSNative.nvExecute(dlssContext, lowRes.getColorTexture(), -1, -1, outputFbo,
                renderWidth, renderHeight, screenWidth, screenHeight, jitterX, jitterY, frameIndex);
        frameIndex++;
    }
    @Override public int getRenderWidth() { return renderWidth; }
    @Override public int getRenderHeight() { return renderHeight; }
    @Override public void destroy() {
        if (dlssContext != 0L) { DLSSNative.nvDestroyContext(dlssContext); dlssContext = 0L; }
        nativeReady = false;
    }
}
