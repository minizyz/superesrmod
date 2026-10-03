package com.superesrmod.gui;

import com.superesrmod.config.ModConfig;
import com.superesrmod.upscale.UpscaleManager;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class UpscaleOptionsScreen extends Screen {
    private final Screen parent;
    public UpscaleOptionsScreen(Screen parent) { super(Component.translatable("superesrmod.title")); this.parent = parent; }

    private Component t(String k) { return Component.translatable(k); }
    private Component t(String k, Object... a) { return Component.translatable(k, a); }

    private String upscaleName() { return switch (ModConfig.UPSCALE_TYPE) { case OFF -> "superesrmod.upscale.off"; case FSR1 -> "superesrmod.upscale.fsr1"; case FSR2 -> "superesrmod.upscale.fsr2"; case DLSS -> "superesrmod.upscale.dlss"; }; }
    private String frameGenName() { return switch (ModConfig.FRAME_GEN_TYPE) { case OFF -> "superesrmod.fs.off"; case FRAME_BLEND -> "superesrmod.fs.frame_blend"; case DYNAMIC_BLEND -> "superesrmod.fs.dynamic_blend"; case DLSS3_FG -> "superesrmod.fs.dlss3_fg"; case FSR3_FG -> "superesrmod.fs.fsr3_fg"; }; }
    private String qualityName() { return switch (ModConfig.FSR_QUALITY_MODE) { case QUALITY -> "superesrmod.quality.quality"; case BALANCED -> "superesrmod.quality.balanced"; case PERFORMANCE -> "superesrmod.quality.performance"; case ULTRA_PERFORMANCE -> "superesrmod.quality.ultra_performance"; }; }

    @Override
    protected void init() {
        int cx = this.width / 2, y = this.height / 4, dy = 24;
        this.addRenderableWidget(Button.builder(t("superesrmod.upscale", t(upscaleName())), btn -> {
            ModConfig.UpscaleTypeConfig[] v = ModConfig.UpscaleTypeConfig.values();
            ModConfig.UPSCALE_TYPE = v[(ModConfig.UPSCALE_TYPE.ordinal() + 1) % v.length];
            btn.setMessage(t("superesrmod.upscale", t(upscaleName())));
            UpscaleManager.getInstance().reloadProcessor();
        }).bounds(cx - 110, y, 220, 20).build());

        this.addRenderableWidget(Button.builder(t("superesrmod.framegen", t(frameGenName())), btn -> {
            ModConfig.FrameGenTypeConfig[] v = ModConfig.FrameGenTypeConfig.values();
            ModConfig.FRAME_GEN_TYPE = v[(ModConfig.FRAME_GEN_TYPE.ordinal() + 1) % v.length];
            btn.setMessage(t("superesrmod.framegen", t(frameGenName())));
        }).bounds(cx - 110, y + dy, 220, 20).build());

        this.addRenderableWidget(Button.builder(t("superesrmod.scale", (float) ModConfig.INTERNAL_SCALE), btn -> {
            double s = ModConfig.INTERNAL_SCALE - 0.1; if (s < 0.33) s = 1.0;
            ModConfig.INTERNAL_SCALE = s;
            btn.setMessage(t("superesrmod.scale", (float) s));
            UpscaleManager.getInstance().reloadProcessor();
        }).bounds(cx - 110, y + dy*2, 220, 20).build());

        this.addRenderableWidget(Button.builder(t("superesrmod.fsr_quality", t(qualityName())), btn -> {
            ModConfig.FSRQualityMode[] v = ModConfig.FSRQualityMode.values();
            ModConfig.FSR_QUALITY_MODE = v[(ModConfig.FSR_QUALITY_MODE.ordinal() + 1) % v.length];
            btn.setMessage(t("superesrmod.fsr_quality", t(qualityName())));
            UpscaleManager.getInstance().reloadProcessor();
        }).bounds(cx - 110, y + dy*3, 220, 20).build());

        this.addRenderableWidget(Button.builder(t("superesrmod.sharpness", (float) ModConfig.SHARPNESS), btn -> {
            double s = ModConfig.SHARPNESS + 0.5; if (s > 3.0) s = 0.0;
            ModConfig.SHARPNESS = s;
            btn.setMessage(t("superesrmod.sharpness", (float) s));
        }).bounds(cx - 110, y + dy*4, 220, 20).build());

        this.addRenderableWidget(Button.builder(t("superesrmod.done"), btn -> this.onClose()).bounds(cx - 110, y + dy*5 + 4, 220, 20).build());
    }

    @Override public void onClose() { ModConfig.save(); this.minecraft.setScreen(parent); }
}
