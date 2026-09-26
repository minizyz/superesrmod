package com.superesrmod.upscale;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.superesrmod.SuperESRMod;

public class RenderTargets {

    private RenderTarget lowResTarget;
    private int renderWidth, renderHeight, screenWidth, screenHeight;

    public void rebuild(int screenWidth, int screenHeight, float scale) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.renderWidth = Math.max(1, Math.round(screenWidth * scale));
        this.renderHeight = Math.max(1, Math.round(screenHeight * scale));
        destroy();
        lowResTarget = new RenderTarget(true, SuperESRMod.MOD_ID + "_lowres");
        lowResTarget.createBuffers(renderWidth, renderHeight, true);
        lowResTarget.setClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        SuperESRMod.LOGGER.info("[RenderTargets] {}x{} -> {}x{} (scale {})",
                screenWidth, screenHeight, renderWidth, renderHeight, scale);
    }

    public void bind() { if (lowResTarget != null) lowResTarget.bindWrite(true); }

    public void unbind(RenderTarget mainTarget) {
        if (lowResTarget != null) lowResTarget.unbindWrite();
        if (mainTarget != null) mainTarget.bindWrite(true);
    }

    public RenderTarget getLowResTarget() { return lowResTarget; }
    public int getRenderWidth() { return renderWidth; }
    public int getRenderHeight() { return renderHeight; }
    public int getScreenWidth() { return screenWidth; }
    public int getScreenHeight() { return screenHeight; }
    public boolean isDirty() { return lowResTarget == null; }

    public void destroy() {
        if (lowResTarget != null) { lowResTarget.destroyBuffers(); lowResTarget = null; }
    }
}
