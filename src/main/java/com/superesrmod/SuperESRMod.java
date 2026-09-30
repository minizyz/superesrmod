package com.superesrmod;

import com.superesrmod.platform.PlatformHelper;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SuperESRMod implements ClientModInitializer {

    public static final String MOD_ID = "superesrmod";
    public static final Logger LOGGER = LoggerFactory.getLogger("SuperESRMod");

    @Override
    public void onInitializeClient() {
        LOGGER.info("[SuperESRMod] init, platform: {}", PlatformHelper.detectPlatformSummary());
        // 不在此处创建渲染资源——Window/GL 尚未就绪。
        // 实际初始化由 GameRendererMixin 在第一帧渲染时调用 UpscaleManager.lateInit()。
        LOGGER.info("[SuperESRMod] entry done, waiting for first frame to init render pipeline");
    }
}
