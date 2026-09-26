package com.superesrmod.gui;

import com.superesrmod.config.ModConfig;
import com.superesrmod.upscale.UpscaleManager;

/**
 * 超分设置入口。后续通过 Cloth Config 或 Fabric 屏幕 API 实现完整 UI。
 */
public class UpscaleOptionsScreen {

    public static void cycleUpscaleType() {
        var current = ModConfig.UPSCALE_TYPE;
        ModConfig.UPSCALE_TYPE = switch (current) {
            case OFF -> ModConfig.UpscaleTypeConfig.FSR1;
            case FSR1 -> ModConfig.UpscaleTypeConfig.DLSS;
            case DLSS -> ModConfig.UpscaleTypeConfig.FSR2;
            case FSR2 -> ModConfig.UpscaleTypeConfig.OFF;
        };
        UpscaleManager.getInstance().reloadProcessor();
    }

    public static void toggleFrameGen() {
        ModConfig.FRAME_GEN_TYPE = (ModConfig.FRAME_GEN_TYPE == ModConfig.FrameGenTypeConfig.OFF)
                ? ModConfig.FrameGenTypeConfig.DYNAMIC_BLEND
                : ModConfig.FrameGenTypeConfig.OFF;
        UpscaleManager.getInstance().getFrameGenManager().reload();
    }
}
