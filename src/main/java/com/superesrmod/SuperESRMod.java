package com.superesrmod;

import com.superesrmod.platform.PlatformHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SuperESRMod implements ClientModInitializer {
    public static final String MOD_ID = "superesrmod";
    public static final Logger LOGGER = LoggerFactory.getLogger("SuperESRMod");
    public static Minecraft CLIENT;

    @Override
    public void onInitializeClient() {
        LOGGER.info("[SuperESRMod] init, platform: {}", PlatformHelper.detectPlatformSummary());
        CLIENT = Minecraft.getInstance();
        SuperESRKeybind.register();
        ClientTickEvents.END_CLIENT_TICK.register(client -> SuperESRKeybind.tick());
        LOGGER.info("[SuperESRMod] done. Press G to open options.");
    }
}
