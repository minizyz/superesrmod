package com.superesrmod.upscale.framegen;

/**
 * 帧生成（Frame Generation）类型。
 */
public enum FrameGenType {
    OFF("关"),
    FRAME_BLEND("帧混合"),
    DYNAMIC_BLEND("动态帧混合"),
    DLSS3_FG("DLSS 3 FG"),
    FSR3_FG("FSR 3 FG");

    public final String displayName;
    FrameGenType(String displayName) { this.displayName = displayName; }
}
