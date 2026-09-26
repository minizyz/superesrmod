package com.superesrmod;

import com.superesrmod.config.ModConfig;
import com.superesrmod.platform.PlatformHelper;
import com.superesrmod.upscale.UpscaleManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SuperESRMod —— 在 NeoForge 中提供 NVIDIA DLSS 与 AMD FSR 超分辨率及帧生成支持。
 */
@Mod(SuperESRMod.MOD_ID)
public class SuperESRMod {

    public static final String MOD_ID = "superesrmod";
    public static final Logger LOGGER = LoggerFactory.getLogger("SuperESRMod");

    public SuperESRMod(IEventBus modEventBus, ModContainer container) {
        container.registerConfig(Type.CLIENT, ModConfig.SPEC);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.addListener(this::onClientSetup);
            container.registerExtensionPoint(IConfigScreenFactory.class,
                    (mc, parent) -> new net.neoforged.neoforge.client.gui.ConfigurationScreen(container, parent));
        }

        LOGGER.info("[SuperESRMod] 构造完成，平台检测: {}", PlatformHelper.detectPlatformSummary());
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            LOGGER.info("[SuperESRMod] 客户端初始化开始");
            UpscaleManager.getInstance().init();
            NeoForge.EVENT_BUS.register(UpscaleManager.getInstance());
            LOGGER.info("[SuperESRMod] 客户端初始化完成，可用超分类型: {}",
                    UpscaleManager.getInstance().getAvailableTypes());
        });
    }
}
