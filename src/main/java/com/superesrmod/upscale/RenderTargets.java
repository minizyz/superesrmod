package com.superesrmod.upscale;
import com.superesrmod.SuperESRMod;
import com.superesrmod.gl.GLFramebuffer;
import org.lwjgl.opengl.GL30;
public class RenderTargets {
    private GLFramebuffer lowResTarget;
    private int renderWidth, renderHeight, screenWidth, screenHeight;
    public void rebuild(int screenWidth, int screenHeight, float scale) {
        this.screenWidth = screenWidth; this.screenHeight = screenHeight;
        this.renderWidth = Math.max(1, Math.round(screenWidth * scale));
        this.renderHeight = Math.max(1, Math.round(screenHeight * scale));
        destroy();
        lowResTarget = new GLFramebuffer(renderWidth, renderHeight, true);
        lowResTarget.clear(0, 0, 0, 0);
        SuperESRMod.LOGGER.info("[RenderTargets] {}x{} -> {}x{}", screenWidth, screenHeight, renderWidth, renderHeight);
    }
    public int bind() { if (lowResTarget == null) return 0; int prev = GL30.glGetInteger(GL30.GL_FRAMEBUFFER_BINDING); lowResTarget.bind(); return prev; }
    public GLFramebuffer getLowResTarget() { return lowResTarget; }
    public int getRenderWidth() { return renderWidth; }
    public int getRenderHeight() { return renderHeight; }
    public int getScreenWidth() { return screenWidth; }
    public int getScreenHeight() { return screenHeight; }
    public boolean isDirty() { return lowResTarget == null; }
    public void destroy() { if (lowResTarget != null) { lowResTarget.destroy(); lowResTarget = null; } }
}
