package com.superesrmod;

import com.superesrmod.platform.PlatformHelper;
import com.superesrmod.upscale.UpscaleManager;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SuperESRMod —— Fabric 模组入口。
 */
public class SuperESRMod implements ClientModInitializer {

    public static final String MOD_ID = "superesrmod";
    public static final Logger LOGGER = LoggerFactory.getLogger("SuperESRMod");

    @Override
    public void onInitializeClient() {
        LOGGER.info("[SuperESRMod] init, platform: {}", PlatformHelper.detectPlatformSummary());
        UpscaleManager.getInstance().init();
        LOGGER.info("[SuperESRMod] done, available upscalers: {}",
                UpscaleManager.getInstance().getAvailableTypes());
    }
}
