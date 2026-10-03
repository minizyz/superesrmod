package com.superesrmod;

import com.superesrmod.gui.UpscaleOptionsScreen;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class SuperESRKeybind {
    private static KeyBinding openKey;
    public static void register() {
        openKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.superesrmod.open_options",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            "category.superesrmod.general"
        ));
    }
    public static void tick() {
        if (openKey != null && openKey.wasPressed() && SuperESRMod.CLIENT != null) {
            SuperESRMod.CLIENT.setScreen(new UpscaleOptionsScreen(SuperESRMod.CLIENT.currentScreen));
        }
    }
}
