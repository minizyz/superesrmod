package com.superesrmod.upscale.framegen;

public enum FrameGenType {
    OFF("Off"), DLSS3_FG("DLSS 3 FG"), FSR3_FG("FSR 3 FG");
    public final String displayName;
    FrameGenType(String d) { this.displayName = d; }
}
