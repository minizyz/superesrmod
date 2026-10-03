package com.superesrmod;

import com.superesrmod.gui.UpscaleOptionsScreen;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.util.InputMappings;
import org.lwjgl.glfw.GLFW;

public class SuperESRKeybind {
    private static KeyMapping openKey;
    public static void register() {
        openKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.superesrmod.open_options",
            InputMappings.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            "category.superesrmod.general"
        ));
    }
    public static void tick() {
        if (openKey != null && openKey.consumeClick() && SuperESRMod.CLIENT != null) {
            SuperESRMod.CLIENT.setScreen(new UpscaleOptionsScreen(SuperESRMod.CLIENT.screen));
        }
    }
}
