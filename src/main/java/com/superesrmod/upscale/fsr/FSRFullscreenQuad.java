package com.superesrmod.upscale.fsr;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public final class FSRFullscreenQuad {
    private static int vao = 0;
    private FSRFullscreenQuad() {}
    public static void draw() {
        if (vao == 0) vao = GL30.glGenVertexArrays();
        int prevVao = GL11.glGetInteger(GL30.GL_VERTEX_ARRAY_BINDING);
        GL30.glBindVertexArray(vao);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL20.glDrawArrays(GL20.GL_TRIANGLES, 0, 3);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL30.glBindVertexArray(prevVao);
    }
}
