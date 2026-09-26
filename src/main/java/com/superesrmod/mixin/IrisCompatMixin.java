package com.superesrmod.mixin;

import com.superesrmod.SuperESRMod;
import com.superesrmod.upscale.UpscaleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "net.coderbot.iris.pipeline.DeferredWorldRenderingPipeline", remap = false)
public abstract class IrisCompatMixin {

    @Inject(method = "renderPhase", at = @At("TAIL"), require = 0)
    private void afterIrisPhaseRender(CallbackInfo ci) {
        if (UpscaleManager.getInstance().isIrisLoaded()) {
            SuperESRMod.LOGGER.debug("[IrisCompat] Iris shader pass done");
        }
    }
}
