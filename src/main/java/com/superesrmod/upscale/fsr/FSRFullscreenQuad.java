package com.superesrmod.upscale.fsr;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import org.lwjgl.opengl.GL11;

public final class FSRFullscreenQuad {
    private FSRFullscreenQuad() {}
    public static void draw() {
        RenderSystem.depthFunc(GL11.GL_ALWAYS);
        RenderSystem.disableDepthTest();
        Tesselator t = Tesselator.getInstance();
        BufferBuilder buf = t.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION);
        buf.addVertex(-1.0f, -1.0f, 0.0f);
        buf.addVertex( 3.0f, -1.0f, 0.0f);
        buf.addVertex(-1.0f,  3.0f, 0.0f);
        t.end();
        RenderSystem.depthFunc(GL11.GL_LEQUAL);
        RenderSystem.enableDepthTest();
    }
}
