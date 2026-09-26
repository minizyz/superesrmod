package com.superesrmod.upscale.framegen;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.superesrmod.SuperESRMod;
import org.lwjgl.opengl.GL30;

/**
 * 简单帧混合插值器（Frame Blending）。
 *
 * <p>保存上一帧颜色缓冲，在偶数 present 时输出
 * prev * blend + current * (1-blend) 的混合结果，
 * 视觉上等效帧率翻倍，代价是轻微运动模糊。全平台可用。</p>
 */
public class BlendInterpolator implements FrameInterpolator {

    protected RenderTarget previousFrame;
    protected int width, height;
    protected boolean hasPrevious = false;

    @Override public FrameGenType getType() { return FrameGenType.FRAME_BLEND; }

    @Override
    public void init(int width, int height) {
        this.width = width; this.height = height;
        if (previousFrame != null) previousFrame.destroyBuffers();
        previousFrame = new RenderTarget(false, "superesrmod_blend_prev");
        previousFrame.createBuffers(width, height, false);
        hasPrevious = false;
        SuperESRMod.LOGGER.info("[BlendFG] initialized {}x{}", width, height);
    }

    @Override
    public void interpolate(RenderTarget currentFrame, RenderTarget outTarget, long presentCount) {
        if (previousFrame == null) return;
        float blendFactor = resolveBlendFactor();
        boolean insertFrame = (presentCount % 2 == 1);

        if (insertFrame && hasPrevious) {
            renderBlend(currentFrame, previousFrame, outTarget, blendFactor);
        } else {
            blitFrame(currentFrame, outTarget);
        }
        copyFrame(currentFrame, previousFrame);
        hasPrevious = true;
    }

    protected float resolveBlendFactor() { return 0.5f; }

    protected void renderBlend(RenderTarget a, RenderTarget b, RenderTarget out, float t) {
        out.bindWrite(true);
        RenderSystem.activeTexture(org.lwjgl.opengl.GL11.GL_TEXTURE0);
        RenderSystem.bindTexture(a.colorTextureId);
        RenderSystem.activeTexture(org.lwjgl.opengl.GL11.GL_TEXTURE1);
        RenderSystem.bindTexture(b.colorTextureId);
        com.superesrmod.upscale.fsr.FSRFullscreenQuad.draw();
    }

    protected void blitFrame(RenderTarget src, RenderTarget dst) {
        dst.bindWrite(true);
        RenderSystem.bindTexture(src.colorTextureId);
        com.superesrmod.upscale.fsr.FSRFullscreenQuad.draw();
    }

    protected void copyFrame(RenderTarget src, RenderTarget dst) {
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, src.colorTextureId);
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, dst.colorTextureId);
        GL30.glBlitFramebuffer(0, 0, width, height, 0, 0, width, height,
                GL30.GL_COLOR_BUFFER_BIT, GL30.GL_NEAREST);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    @Override public void destroy() {
        if (previousFrame != null) { previousFrame.destroyBuffers(); previousFrame = null; }
    }
}
