package com.superesrmod.upscale.fsr;
import com.superesrmod.gl.GLFramebuffer;
import com.superesrmod.gl.GLShader;
import org.lwjgl.opengl.GL30;
public class FSR1Shader {
    private final String name;
    private GLShader shader;
    public FSR1Shader(String name) { this.name = name; reload(); }
    private void reload() {
        try {
            String shortName = name.contains(":") ? name.substring(name.indexOf(':') + 1) : name;
            shader = new GLShader("assets/superesrmod/shaders/core/" + shortName + ".fsh");
        } catch (Exception e) { com.superesrmod.SuperESRMod.LOGGER.error("[FSR1Shader] {} failed: {}", name, e); }
    }
    public void bind() { if (shader != null) shader.bind(); }
    public void unbind() { GLShader.unbind(); }
    public void setInputTexture(String uniformName, int textureId) { if (shader != null) shader.setTexture(uniformName, 0, textureId); }
    public void setUniform(String name, float a, float b) { if (shader != null) shader.setUniform(name, a, b); }
    public void setUniform(String name, float v) { if (shader != null) shader.setUniform(name, v); }
    public void renderToFbo(int fbo, int width, int height) { GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo); GL30.glViewport(0, 0, width, height); FSRFullscreenQuad.draw(); }
    public void renderTo(GLFramebuffer target) { renderToFbo(target.getFbo(), target.getWidth(), target.getHeight()); }
    public void close() { if (shader != null) { shader.destroy(); shader = null; } }
}
