package com.superesrmod.upscale.fsr;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
public final class FSRFullscreenQuad {
    private FSRFullscreenQuad() {}
    public static void draw() { GL11.glDisable(GL11.GL_DEPTH_TEST); GL20.glDrawArrays(GL20.GL_TRIANGLES, 0, 3); GL11.glEnable(GL11.GL_DEPTH_TEST); }
}
