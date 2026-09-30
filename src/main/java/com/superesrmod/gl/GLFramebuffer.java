package com.superesrmod.gl;
import org.lwjgl.opengl.GL30;
public class GLFramebuffer {
    private int fbo, colorTexture, depthRenderbuffer;
    private final int width, height;
    private final boolean hasDepth;
    public GLFramebuffer(int width, int height, boolean hasDepth) {
        this.width = width; this.height = height; this.hasDepth = hasDepth; create();
    }
    private void create() {
        fbo = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
        colorTexture = GL30.glGenTextures();
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, colorTexture);
        GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA8, width, height, 0, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, 0);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_S, GL30.GL_CLAMP_TO_EDGE);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_T, GL30.GL_CLAMP_TO_EDGE);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL30.GL_TEXTURE_2D, colorTexture, 0);
        if (hasDepth) {
            depthRenderbuffer = GL30.glGenRenderbuffers();
            GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthRenderbuffer);
            GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL30.GL_DEPTH_COMPONENT24, width, height);
            GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthRenderbuffer);
        } else { depthRenderbuffer = 0; }
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }
    public void bind() { GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo); GL30.glViewport(0, 0, width, height); }
    public static void unbind() { GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0); }
    public void clear(float r, float g, float b, float a) { bind(); GL30.glClearColor(r,g,b,a); int m = GL30.GL_COLOR_BUFFER_BIT; if (hasDepth) m |= GL30.GL_DEPTH_BUFFER_BIT; GL30.glClear(m); }
    public int getColorTexture() { return colorTexture; }
    public int getFbo() { return fbo; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public void destroy() { if (colorTexture!=0) GL30.glDeleteTextures(colorTexture); if (depthRenderbuffer!=0) GL30.glDeleteRenderbuffers(depthRenderbuffer); if (fbo!=0) GL30.glDeleteFramebuffers(fbo); }
}
