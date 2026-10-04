package com.superesrmod.integration;

import com.superesrmod.gui.UpscaleOptionsScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screens.Screen;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    @SuppressWarnings({"rawtypes","unchecked"})
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (ConfigScreenFactory) parent -> new UpscaleOptionsScreen((Screen) parent);
    }
}
