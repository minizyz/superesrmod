package com.superesrmod.upscale.framegen;
import com.superesrmod.SuperESRMod;
import com.superesrmod.gl.GLFramebuffer;
import com.superesrmod.gl.GLShader;
import com.superesrmod.upscale.fsr.FSRFullscreenQuad;
import org.lwjgl.opengl.GL30;
public class BlendInterpolator implements FrameInterpolator {
    protected GLFramebuffer currentFrame, previousFrame;
    protected GLShader blendShader;
    protected int width, height;
    protected boolean hasPrevious = false;
    @Override public FrameGenType getType() { return FrameGenType.FRAME_BLEND; }
    @Override public void init(int width, int height) {
        this.width = width; this.height = height;
        if (currentFrame != null) currentFrame.destroy();
        if (previousFrame != null) previousFrame.destroy();
        currentFrame = new GLFramebuffer(width, height, false);
        previousFrame = new GLFramebuffer(width, height, false);
        if (blendShader == null) blendShader = new GLShader("assets/superesrmod/shaders/core/frame_blend.fsh");
        hasPrevious = false;
        SuperESRMod.LOGGER.info("[BlendFG] init {}x{}", width, height);
    }
    @Override public void interpolate(int currentFbo, int outputFbo, long presentCount) {
        if (currentFrame == null || previousFrame == null) return;
        blitFrame(currentFbo, currentFrame.getFbo());
        float blendFactor = resolveBlendFactor();
        if ((presentCount % 2 == 1) && hasPrevious) {
            renderBlend(currentFrame.getColorTexture(), previousFrame.getColorTexture(), outputFbo, blendFactor);
        }
        blitFrame(currentFrame.getFbo(), previousFrame.getFbo());
        hasPrevious = true;
    }
    protected float resolveBlendFactor() { return 0.5f; }
    protected void renderBlend(int currentTex, int prevTex, int outFbo, float t) {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, outFbo);
        GL30.glViewport(0, 0, width, height);
        if (blendShader != null) {
            blendShader.bind();
            blendShader.setTexture("CurrentFrame", 0, currentTex);
            blendShader.setTexture("PreviousFrame", 1, prevTex);
            blendShader.setUniform("BlendFactor", t);
            FSRFullscreenQuad.draw();
            GLShader.unbind();
        }
    }
    protected void blitFrame(int srcFbo, int dstFbo) {
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, srcFbo);
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, dstFbo);
        GL30.glBlitFramebuffer(0, 0, width, height, 0, 0, width, height, GL30.GL_COLOR_BUFFER_BIT, GL30.GL_LINEAR);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }
    @Override public void destroy() {
        if (currentFrame != null) { currentFrame.destroy(); currentFrame = null; }
        if (previousFrame != null) { previousFrame.destroy(); previousFrame = null; }
        if (blendShader != null) { blendShader.destroy(); blendShader = null; }
    }
}
