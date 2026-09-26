package com.superesrmod.upscale;

public enum UpscaleType {
    OFF("Off", false),
    DLSS("DLSS", true),
    FSR1("FSR 1", false),
    FSR2("FSR 2/3", true);

    public final String displayName;
    public final boolean requiresNative;

    UpscaleType(String displayName, boolean requiresNative) {
        this.displayName = displayName;
        this.requiresNative = requiresNative;
    }
}
