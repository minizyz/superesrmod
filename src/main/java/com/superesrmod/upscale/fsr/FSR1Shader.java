package com.superesrmod.upscale.fsr;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

public class FSR1Shader {

    private final String name;
    private ShaderInstance shader;

    public FSR1Shader(String name) { this.name = name; reload(); }

    private void reload() {
        try {
            Minecraft mc = Minecraft.getInstance();
            ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(
                    "superesrmod", "core/" + name.substring(name.indexOf(':') + 1) + ".json");
            shader = new ShaderInstance(mc.getResourceManager(), loc,
                    net.minecraft.client.renderer.VertexFormat.POSITION);
        } catch (Exception e) {
            com.superesrmod.SuperESRMod.LOGGER.error("[FSR1Shader] load {} failed: {}", name, e.toString());
        }
    }

    public void bind() { if (shader != null) shader.apply(); }
    public void unbind() { if (shader != null) shader.clear(); }

    public void setInputTexture(String uniformName, int textureId) {
        RenderSystem.activeTexture(com.mojang.blaze3d.platform.GlConst.GL_TEXTURE0);
        RenderSystem.bindTexture(textureId);
    }

    public void setUniform(String name, float a, float b) {
        if (shader == null) return;
        var u = shader.safeGetUniform(name);
        if (u != null) u.set(a, b);
    }

    public void renderTo(RenderTarget target) {
        target.bindWrite(true);
        FSRFullscreenQuad.draw();
    }

    public void close() { if (shader != null) { shader.close(); shader = null; } }
}
