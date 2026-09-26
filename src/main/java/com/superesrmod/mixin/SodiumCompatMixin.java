package com.superesrmod.mixin;

import com.superesrmod.SuperESRMod;
import com.superesrmod.upscale.UpscaleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "net.caffeinemc.mods.sodium.client.render.SodiumWorldRenderer", remap = false)
public abstract class SodiumCompatMixin {

    @Inject(method = "renderWorld", at = @At("TAIL"), require = 0)
    private void afterSodiumWorldRender(CallbackInfo ci) {
        if (UpscaleManager.getInstance().isSodiumLoaded()) {
            SuperESRMod.LOGGER.debug("[SodiumCompat] Sodium world render done");
        }
    }
}
