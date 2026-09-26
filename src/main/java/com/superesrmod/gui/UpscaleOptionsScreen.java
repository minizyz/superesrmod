package com.superesrmod.gui;

import com.superesrmod.config.ModConfig;
import com.superesrmod.upscale.UpscaleManager;
import com.superesrmod.upscale.UpscaleType;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

public class UpscaleOptionsScreen {

    public static Screen create(Screen parent) {
        List<OptionInstance<?>> options = List.of(
            new OptionInstance<>(
                "superesrmod.options.upscale_type",
                OptionInstance.noTooltip(),
                (caption, value) -> Component.literal(value.displayName),
                new OptionInstance.Enum<>(List.of(UpscaleType.OFF, UpscaleType.DLSS, UpscaleType.FSR1, UpscaleType.FSR2), null),
                UpscaleManager.getInstance().getActiveType(),
                (type) -> {
                    ModConfig.UPSCALE_TYPE.set(configValue(type));
                    UpscaleManager.getInstance().reloadProcessor();
                }
            )
        );

        return net.minecraft.client.gui.screens.options.OptionsSubScreen.subMenu(
                parent,
                Component.translatable("superesrmod.options.title"),
                options);
    }

    private static ModConfig.UpscaleTypeConfig configValue(UpscaleType t) {
        return switch (t) {
            case OFF -> ModConfig.UpscaleTypeConfig.OFF;
            case DLSS -> ModConfig.UpscaleTypeConfig.DLSS;
            case FSR1 -> ModConfig.UpscaleTypeConfig.FSR1;
            case FSR2 -> ModConfig.UpscaleTypeConfig.FSR2;
        };
    }
}
