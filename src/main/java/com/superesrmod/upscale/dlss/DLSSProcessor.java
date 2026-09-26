package com.superesrmod.upscale.dlss;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.superesrmod.SuperESRMod;
import com.superesrmod.upscale.UpscaleProcessor;
import com.superesrmod.upscale.UpscaleType;

public class DLSSProcessor implements UpscaleProcessor {

    private long dlssContext = 0L;
    private int screenWidth, screenHeight, renderWidth, renderHeight;
    private boolean nativeReady = false;
    private int frameIndex = 0;

    @Override public UpscaleType getType() { return UpscaleType.DLSS; }

    @Override
    public void init(int screenWidth, int screenHeight, int renderWidth, int renderHeight) {
        this.screenWidth = screenWidth; this.screenHeight = screenHeight;
        this.renderWidth = renderWidth; this.renderHeight = renderHeight;
        if (!nativeReady) {
            nativeReady = DLSSLibraryLoader.tryLoad();
            if (!nativeReady) { SuperESRMod.LOGGER.error("[DLSS] native lib load failed"); return; }
        }
        dlssContext = DLSSNative.nvCreateContext(screenWidth, screenHeight, renderWidth, renderHeight);
        if (dlssContext == 0L) { SuperESRMod.LOGGER.error("[DLSS] context creation failed"); return; }
        int[] min = new int[2], max = new int[2];
        DLSSNative.nvGetRecommendedResolutionRange(dlssContext, min, max);
        SuperESRMod.LOGGER.info("[DLSS] session {}x{} -> {}x{}", renderWidth, renderHeight, screenWidth, screenHeight);
    }

    @Override public RenderTarget prepare() { return null; }

    @Override
    public void postWorldRender(int colorTexture, int depthTexture, RenderTarget outTarget) {
        if (dlssContext == 0L || !nativeReady) return;
        float jitterX = ((frameIndex % 2) == 0) ? 0.5f : -0.5f;
        float jitterY = ((frameIndex % 2) == 0) ? -0.25f : 0.25f;
        jitterX /= renderWidth; jitterY /= renderHeight;
        DLSSNative.nvExecute(dlssContext, colorTexture, depthTexture, -1,
                outTarget.colorTextureId, renderWidth, renderHeight, screenWidth, screenHeight,
                jitterX, jitterY, frameIndex);
        frameIndex++;
    }

    @Override public int getRenderWidth() { return renderWidth; }
    @Override public int getRenderHeight() { return renderHeight; }

    @Override
    public void destroy() {
        if (dlssContext != 0L) { DLSSNative.nvDestroyContext(dlssContext); dlssContext = 0L; }
        nativeReady = false;
    }
}
