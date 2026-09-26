package com.superesrmod.upscale.dlss;

import com.superesrmod.SuperESRMod;
import com.superesrmod.platform.NativeLibraryLoader;
import com.superesrmod.platform.PlatformHelper;
import java.io.File;

public final class DLSSLibraryLoader {
    private static boolean loaded = false;
    private DLSSLibraryLoader() {}

    public static synchronized boolean tryLoad() {
        if (loaded) return true;
        if (!PlatformHelper.isWindows()) { SuperESRMod.LOGGER.info("[DLSS] non-Windows, unavailable"); return false; }
        try {
            System.loadLibrary("nvngx_dlss");
            loaded = true;
            SuperESRMod.LOGGER.info("[DLSS] loaded from system PATH");
            return true;
        } catch (UnsatisfiedLinkError e) {
            SuperESRMod.LOGGER.debug("[DLSS] not in PATH: {}", e.getMessage());
        }
        try {
            File extracted = NativeLibraryLoader.extractNative(
                    "natives/" + PlatformHelper.classifier() + "/", "nvngx_dlss.dll");
            if (extracted != null) {
                System.load(extracted.getAbsolutePath());
                loaded = true;
                SuperESRMod.LOGGER.info("[DLSS] loaded from jar natives: {}", extracted);
                return true;
            }
        } catch (Throwable t) {
            SuperESRMod.LOGGER.warn("[DLSS] bundled native load failed: {}", t.getMessage());
        }
        SuperESRMod.LOGGER.warn("[DLSS] nvngx_dlss.dll not found. Install via NVIDIA App or place in mods/superesrmod/natives/");
        return false;
    }
}
