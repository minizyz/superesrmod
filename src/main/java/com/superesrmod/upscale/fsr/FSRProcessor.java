package com.superesrmod.upscale.fsr;
import com.superesrmod.SuperESRMod;
import com.superesrmod.config.ModConfig;
import com.superesrmod.gl.GLFramebuffer;
import com.superesrmod.upscale.UpscaleProcessor;
import com.superesrmod.upscale.UpscaleType;
import org.lwjgl.opengl.GL30;
public class FSRProcessor implements UpscaleProcessor {
    private final UpscaleType type;
    private FSR1Shader easuShader, rcasShader;
    private GLFramebuffer intermediateTarget;
    private int screenWidth, screenHeight, renderWidth, renderHeight;
    public FSRProcessor(UpscaleType type) { this.type = type; }
    @Override public UpscaleType getType() { return type; }
    @Override public void init(int screenWidth, int screenHeight, int renderWidth, int renderHeight) {
        this.screenWidth = screenWidth; this.screenHeight = screenHeight;
        this.renderWidth = renderWidth; this.renderHeight = renderHeight;
        if (easuShader == null) { easuShader = new FSR1Shader("superesrmod:fsr_easu"); rcasShader = new FSR1Shader("superesrmod:fsr_rcas"); }
        if (intermediateTarget != null) intermediateTarget.destroy();
        intermediateTarget = new GLFramebuffer(screenWidth, screenHeight, false);
        SuperESRMod.LOGGER.info("[FSR] {} init {}x{} -> {}x{}", type, renderWidth, renderHeight, screenWidth, screenHeight);
    }
    @Override public GLFramebuffer prepare() { return null; }
    @Override public void postWorldRender(GLFramebuffer lowRes, int outputFbo) {
        if (easuShader == null || lowRes == null) return;
        easuShader.bind();
        easuShader.setInputTexture("InputTexture", lowRes.getColorTexture());
        easuShader.setUniform("InputSize", (float) renderWidth, (float) renderHeight);
        easuShader.setUniform("OutputSize", (float) screenWidth, (float) screenHeight);
        easuShader.setUniform("Sharpness", (float) ModConfig.SHARPNESS);
        easuShader.renderTo(intermediateTarget);
        easuShader.unbind();
        if (ModConfig.ENABLE_SHARPNESS && rcasShader != null) {
            rcasShader.bind();
            rcasShader.setInputTexture("InputTexture", intermediateTarget.getColorTexture());
            rcasShader.setUniform("OutputSize", (float) screenWidth, (float) screenHeight);
            rcasShader.setUniform("Sharpness", (float) ModConfig.SHARPNESS);
            rcasShader.renderToFbo(outputFbo, screenWidth, screenHeight);
            rcasShader.unbind();
        } else {
            GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, intermediateTarget.getFbo());
            GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, outputFbo);
            GL30.glBlitFramebuffer(0, 0, screenWidth, screenHeight, 0, 0, screenWidth, screenHeight, GL30.GL_COLOR_BUFFER_BIT, GL30.GL_LINEAR);
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        }
    }
    @Override public int getRenderWidth() { return renderWidth; }
    @Override public int getRenderHeight() { return renderHeight; }
    @Override public void destroy() {
        if (intermediateTarget != null) { intermediateTarget.destroy(); intermediateTarget = null; }
        if (easuShader != null) { easuShader.close(); easuShader = null; }
        if (rcasShader != null) { rcasShader.close(); rcasShader = null; }
    }
}
