package com.superesrmod.upscale.framegen;
import com.superesrmod.SuperESRMod;
import com.superesrmod.config.ModConfig;
import com.superesrmod.platform.PlatformHelper;
import net.minecraft.client.Minecraft;
public class FrameGenManager {
    private FrameGenType type = FrameGenType.OFF;
    private FrameInterpolator interpolator;
    private long presentCount = 0;
    private boolean active = false;
    public void init() { reload(); }
    public void reload() {
        if (interpolator != null) { interpolator.destroy(); interpolator = null; }
        var cfg = ModConfig.FRAME_GEN_TYPE;
        this.type = switch (cfg) {
            case OFF -> FrameGenType.OFF;
            case FRAME_BLEND -> FrameGenType.FRAME_BLEND;
            case DYNAMIC_BLEND -> FrameGenType.DYNAMIC_BLEND;
            case DLSS3_FG -> PlatformHelper.isWindows() ? FrameGenType.DLSS3_FG : FrameGenType.OFF;
            case FSR3_FG -> FrameGenType.FSR3_FG;
        };
        this.active = type != FrameGenType.OFF;
        if (active) {
            interpolator = switch (type) {
                case FRAME_BLEND -> new BlendInterpolator();
                case DYNAMIC_BLEND -> new DynamicBlendInterpolator();
                case DLSS3_FG, FSR3_FG -> { SuperESRMod.LOGGER.warn("[FrameGen] {} SDK not ready, fallback", type); yield new DynamicBlendInterpolator(); }
                case OFF -> null;
            };
            if (interpolator != null) {
                Minecraft mc = Minecraft.getInstance();
                interpolator.init(mc.getWindow().getWidth(), mc.getWindow().getHeight());
            }
        }
        SuperESRMod.LOGGER.info("[FrameGen] type={} active={}", type, active);
    }
    public void apply(int currentFbo) {
        if (!active || interpolator == null) return;
        presentCount++;
        interpolator.interpolate(currentFbo, currentFbo, presentCount);
    }
    public boolean isActive() { return active; }
    public FrameGenType getType() { return type; }
    public void destroy() { if (interpolator != null) { interpolator.destroy(); interpolator = null; } }
}
