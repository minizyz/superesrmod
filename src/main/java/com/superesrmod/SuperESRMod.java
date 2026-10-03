package com.superesrmod;

import com.superesrmod.config.ModConfig;
import com.superesrmod.platform.PlatformHelper;
import net.fabricmc.api.ClientModInitializer;
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
        ModConfig.load();
        LOGGER.info("[SuperESRMod] done.");
    }
}
