package com.superesrmod.mixin;

import com.superesrmod.gui.UpscaleOptionsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin {
    @Inject(method = "init", at = @At("TAIL"), require = 0)
    private void onInit(CallbackInfo ci) {
        try {
            TitleScreen self = (TitleScreen) (Object) this;
            this.addRenderableWidget(
                Button.builder(Component.literal("SuperESR"), btn -> {
                    Minecraft.getInstance().setScreen(new UpscaleOptionsScreen(self));
                }).bounds(self.width - 110, self.height - 30, 100, 20).build()
            );
        } catch (Throwable ignored) {}
    }
}
