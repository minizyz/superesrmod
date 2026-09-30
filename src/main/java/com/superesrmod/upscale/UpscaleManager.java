package com.superesrmod.upscale;
import com.superesrmod.SuperESRMod;
import com.superesrmod.config.ModConfig;
import com.superesrmod.gl.GLFramebuffer;
import com.superesrmod.platform.PlatformHelper;
import com.superesrmod.upscale.dlss.DLSSProcessor;
import com.superesrmod.upscale.fsr.FSRProcessor;
import com.superesrmod.upscale.framegen.FrameGenManager;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL30;
import java.util.EnumSet;
import java.util.Set;
public class UpscaleManager {
    private static volatile UpscaleManager INSTANCE;
    public static UpscaleManager getInstance() {
        if (INSTANCE == null) synchronized (UpscaleManager.class) {
            if (INSTANCE == null) INSTANCE = new UpscaleManager();
        }
        return INSTANCE;
    }
    private final RenderTargets renderTargets = new RenderTargets();
    private UpscaleProcessor activeProcessor;
    private FrameGenManager frameGenManager;
    private boolean initialized = false, sodiumLoaded = false, irisLoaded = false;
    private int savedFbo = 0;
    private UpscaleManager() {}
    public void init() {
        if (initialized) return;
        sodiumLoaded = PlatformHelper.isModLoaded("embeddium") || PlatformHelper.isModLoaded("sodium");
        irisLoaded = PlatformHelper.isModLoaded("iris");
        frameGenManager = new FrameGenManager();
        reloadProcessor();
        initialized = true;
        SuperESRMod.LOGGER.info("[UpscaleManager] init Sodium={} Iris={}", sodiumLoaded, irisLoaded);
    }
    public void reloadProcessor() {
        UpscaleType desired = configToType(ModConfig.UPSCALE_TYPE);
        if (desired == UpscaleType.DLSS && !PlatformHelper.isDLSSAvailable()) desired = UpscaleType.FSR1;
        if (desired == UpscaleType.FSR2 && !PlatformHelper.isFSR2Available()) desired = UpscaleType.FSR1;
        if (activeProcessor != null && activeProcessor.getType() == desired) return;
        if (activeProcessor != null) { activeProcessor.destroy(); activeProcessor = null; }
        switch (desired) {
            case DLSS -> activeProcessor = new DLSSProcessor();
            case FSR1, FSR2 -> activeProcessor = new FSRProcessor(desired);
            case OFF -> activeProcessor = null;
        }
        if (activeProcessor != null) {
            Minecraft mc = Minecraft.getInstance();
            float scale = resolveScale();
            int sw = mc.getWindow().getWidth(), sh = mc.getWindow().getHeight();
            renderTargets.rebuild(sw, sh, scale);
            activeProcessor.init(sw, sh, renderTargets.getRenderWidth(), renderTargets.getRenderHeight());
        }
        SuperESRMod.LOGGER.info("[UpscaleManager] processor -> {}", desired);
    }
    private float resolveScale() {
        float base = (float) ModConfig.INTERNAL_SCALE;
        if (activeProcessor instanceof FSRProcessor) base = ModConfig.FSR_QUALITY_MODE.scale;
        return Math.min(base, 1.0f);
    }
    private UpscaleType configToType(ModConfig.UpscaleTypeConfig c) {
        return switch (c) {
            case OFF -> UpscaleType.OFF;
            case DLSS -> UpscaleType.DLSS;
            case FSR1 -> UpscaleType.FSR1;
            case FSR2 -> UpscaleType.FSR2;
        };
    }
    public boolean onFrameRenderPre() {
        if (activeProcessor == null) return false;
        Minecraft mc = Minecraft.getInstance();
        int sw = mc.getWindow().getWidth(), sh = mc.getWindow().getHeight();
        if (renderTargets.isDirty() || renderTargets.getScreenWidth() != sw || renderTargets.getScreenHeight() != sh) {
            renderTargets.rebuild(sw, sh, resolveScale());
            activeProcessor.init(sw, sh, renderTargets.getRenderWidth(), renderTargets.getRenderHeight());
        }
        savedFbo = renderTargets.bind();
        return true;
    }
    public void onFrameRenderPost() {
        if (activeProcessor == null) return;
        GLFramebuffer lowRes = renderTargets.getLowResTarget();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, savedFbo);
        if (lowRes == null) return;
        activeProcessor.postWorldRender(lowRes, savedFbo);
        if (frameGenManager != null && frameGenManager.isActive()) frameGenManager.apply(savedFbo);
    }
    public Set<UpscaleType> getAvailableTypes() {
        EnumSet<UpscaleType> set = EnumSet.of(UpscaleType.OFF, UpscaleType.FSR1);
        if (PlatformHelper.isDLSSAvailable()) set.add(UpscaleType.DLSS);
        if (PlatformHelper.isFSR2Available()) set.add(UpscaleType.FSR2);
        return set;
    }
    public UpscaleType getActiveType() { return activeProcessor == null ? UpscaleType.OFF : activeProcessor.getType(); }
    public RenderTargets getRenderTargets() { return renderTargets; }
    public FrameGenManager getFrameGenManager() { return frameGenManager; }
    public boolean isSodiumLoaded() { return sodiumLoaded; }
    public boolean isIrisLoaded() { return irisLoaded; }
}
