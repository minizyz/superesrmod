package com.superesrmod.mixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
@Pseudo
@Mixin(targets = "net.minecraft.client.renderer.LevelRenderer", remap = false)
public abstract class LevelRendererMixin {}
