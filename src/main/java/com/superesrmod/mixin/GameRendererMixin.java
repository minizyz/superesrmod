package com.superesrmod.mixin;

import com.superesrmod.upscale.UpscaleManager;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Inject(method = "render", at = @At("HEAD"), require = 0)
    private void onRenderHead(CallbackInfo ci) {
        try {
            UpscaleManager mgr = UpscaleManager.getInstance();
            if (!mgr.isLateInitialized()) mgr.lateInit();
            mgr.onFrameRenderPre();
        } catch (Throwable ignored) {}
    }
    @Inject(method = "render", at = @At("RETURN"), require = 0)
    private void onRenderReturn(CallbackInfo ci) {
        try { UpscaleManager.getInstance().onFrameRenderPost(); } catch (Throwable ignored) {}
    }
}
