package com.superesrmod.gui;

import com.superesrmod.config.ModConfig;
import com.superesrmod.upscale.UpscaleManager;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class UpscaleOptionsScreen extends Screen {
    private final Screen parent;
    public UpscaleOptionsScreen(Screen parent) { super(Component.literal("SuperESRMod")); this.parent = parent; }

    @Override
    protected void init() {
        int cx = this.width / 2, y = this.height / 4;
        this.addRenderableWidget(Button.builder(Component.literal("Upscale: " + ModConfig.UPSCALE_TYPE), btn -> {
            ModConfig.UpscaleTypeConfig[] v = ModConfig.UpscaleTypeConfig.values();
            ModConfig.UPSCALE_TYPE = v[(ModConfig.UPSCALE_TYPE.ordinal() + 1) % v.length];
            btn.setMessage(Component.literal("Upscale: " + ModConfig.UPSCALE_TYPE));
            UpscaleManager.getInstance().reloadProcessor();
        }).bounds(cx - 100, y, 200, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("Frame Gen: " + ModConfig.FRAME_GEN_TYPE), btn -> {
            ModConfig.FrameGenTypeConfig[] v = ModConfig.FrameGenTypeConfig.values();
            ModConfig.FRAME_GEN_TYPE = v[(ModConfig.FRAME_GEN_TYPE.ordinal() + 1) % v.length];
            btn.setMessage(Component.literal("Frame Gen: " + ModConfig.FRAME_GEN_TYPE));
        }).bounds(cx - 100, y + 24, 200, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("Render Scale: " + String.format("%.2f", ModConfig.INTERNAL_SCALE)), btn -> {
            double s = ModConfig.INTERNAL_SCALE - 0.1;
            if (s < 0.33) s = 1.0;
            ModConfig.INTERNAL_SCALE = s;
            btn.setMessage(Component.literal("Render Scale: " + String.format("%.2f", s)));
            UpscaleManager.getInstance().reloadProcessor();
        }).bounds(cx - 100, y + 48, 200, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("Done"), btn -> this.onClose()).bounds(cx - 100, y + 80, 200, 20).build());
    }

    @Override public void onClose() { ModConfig.save(); this.minecraft.setScreen(parent); }
}
