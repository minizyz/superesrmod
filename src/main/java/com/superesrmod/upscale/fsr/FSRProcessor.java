package com.superesrmod.upscale.fsr;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.superesrmod.SuperESRMod;
import com.superesrmod.config.ModConfig;
import com.superesrmod.upscale.UpscaleProcessor;
import com.superesrmod.upscale.UpscaleType;

public class FSRProcessor implements UpscaleProcessor {

    private final UpscaleType type;
    private FSR1Shader easuShader, rcasShader;
    private RenderTarget intermediateTarget;
    private int screenWidth, screenHeight, renderWidth, renderHeight;

    public FSRProcessor(UpscaleType type) { this.type = type; }

    @Override public UpscaleType getType() { return type; }

    @Override
    public void init(int screenWidth, int screenHeight, int renderWidth, int renderHeight) {
        this.screenWidth = screenWidth; this.screenHeight = screenHeight;
        this.renderWidth = renderWidth; this.renderHeight = renderHeight;
        if (easuShader == null) {
            easuShader = new FSR1Shader("superesrmod:fsr_easu");
            rcasShader = new FSR1Shader("superesrmod:fsr_rcas");
        }
        if (intermediateTarget != null) intermediateTarget.destroyBuffers();
        intermediateTarget = new RenderTarget(true, "superesrmod_fsr_intermediate");
        intermediateTarget.createBuffers(screenWidth, screenHeight, false);
        SuperESRMod.LOGGER.info("[FSR] {} {}x{} -> {}x{}", type.displayName, renderWidth, renderHeight, screenWidth, screenHeight);
    }

    @Override public RenderTarget prepare() { return null; }

    @Override
    public void postWorldRender(int colorTexture, int depthTexture, RenderTarget outTarget) {
        if (easuShader == null) return;
        easuShader.bind();
        easuShader.setInputTexture("InputTexture", colorTexture);
        easuShader.setUniform("InputSize", renderWidth, renderHeight);
        easuShader.setUniform("OutputSize", screenWidth, screenHeight);
        easuShader.setUniform("Sharpness", (float) ModConfig.SHARPNESS);
        easuShader.renderTo(intermediateTarget);
        easuShader.unbind();

        if (ModConfig.ENABLE_SHARPNESS) {
            rcasShader.bind();
            rcasShader.setInputTexture("InputTexture", intermediateTarget.colorTextureId);
            rcasShader.setUniform("OutputSize", screenWidth, screenHeight);
            rcasShader.setUniform("Sharpness", (float) ModConfig.SHARPNESS);
            rcasShader.renderTo(outTarget);
            rcasShader.unbind();
        }
    }

    @Override public int getRenderWidth() { return renderWidth; }
    @Override public int getRenderHeight() { return renderHeight; }

    @Override
    public void destroy() {
        if (intermediateTarget != null) { intermediateTarget.destroyBuffers(); intermediateTarget = null; }
        if (easuShader != null) { easuShader.close(); easuShader = null; }
        if (rcasShader != null) { rcasShader.close(); rcasShader = null; }
    }
}
