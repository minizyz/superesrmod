package com.superesrmod.mixin;
import com.superesrmod.upscale.UpscaleManager;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Inject(method = "render", at = @At("HEAD"))
    private void onRenderHead(CallbackInfo ci) { UpscaleManager.getInstance().onFrameRenderPre(); }
    @Inject(method = "render", at = @At("RETURN"))
    private void onRenderReturn(CallbackInfo ci) { UpscaleManager.getInstance().onFrameRenderPost(); }
}
