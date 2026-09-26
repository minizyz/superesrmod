package com.superesrmod.platform;

import com.superesrmod.SuperESRMod;
import net.neoforged.fml.loading.LoadingModList;
import org.lwjgl.opengl.GL11;

import java.util.Locale;

public final class PlatformHelper {

    private static Boolean windows, linux, android, arm64, dlssAvailable, fsr2Available;
    private static String gpuVendor;

    private PlatformHelper() {}

    public static boolean isWindows() {
        if (windows == null) windows = System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("win");
        return windows;
    }

    public static boolean isLinux() {
        if (linux == null) {
            String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
            linux = os.contains("linux") && !os.contains("android");
        }
        return linux;
    }

    public static boolean isAndroid() {
        if (android == null) {
            android = System.getProperty("java.vendor", "").toLowerCase(Locale.ROOT).contains("android")
                   || System.getenv("ANDROID_ROOT") != null;
        }
        return android;
    }

    public static boolean is64Bit() { return System.getProperty("os.arch").contains("64"); }

    public static boolean isArm64() {
        if (arm64 == null) {
            String a = System.getProperty("os.arch").toLowerCase(Locale.ROOT);
            arm64 = a.contains("aarch64") || a.contains("arm64");
        }
        return arm64;
    }

    public static String classifier() {
        String os = isAndroid() ? "android" : isWindows() ? "windows" : isLinux() ? "linux" : "unknown";
        String arch = isArm64() ? "arm64" : is64Bit() ? "x86_64" : "x86";
        return os + "-" + arch;
    }

    public static String getGpuVendor() {
        if (gpuVendor == null) {
            try { gpuVendor = GL11.glGetString(GL11.GL_VENDOR); }
            catch (Throwable t) { gpuVendor = "Unknown"; }
            if (gpuVendor == null) gpuVendor = "Unknown";
        }
        return gpuVendor;
    }

    public static boolean isNvidia() { return getGpuVendor().toLowerCase(Locale.ROOT).contains("nvidia"); }
    public static boolean isAmd() { String v = getGpuVendor().toLowerCase(Locale.ROOT); return v.contains("amd") || v.contains("ati"); }
    public static boolean isIntel() { return getGpuVendor().toLowerCase(Locale.ROOT).contains("intel"); }

    public static boolean isDLSSAvailable() {
        if (dlssAvailable == null) dlssAvailable = isWindows() && isNvidia();
        return dlssAvailable;
    }

    public static boolean isFSR2Available() {
        if (fsr2Available == null) fsr2Available = true;
        return fsr2Available;
    }

    public static boolean isModLoaded(String modId) {
        return LoadingModList.get().getModFileById(modId) != null;
    }

    public static String detectPlatformSummary() {
        return String.format("OS=%s, arch=%s, GPU=%s, DLSS=%s, FSR2=%s, Sodium=%s, Iris=%s",
                isWindows() ? "Windows" : isLinux() ? "Linux" : isAndroid() ? "Android" : "Other",
                System.getProperty("os.arch"), getGpuVendor(),
                isDLSSAvailable(), isFSR2Available(),
                isModLoaded("embeddium") || isModLoaded("sodium"), isModLoaded("iris"));
    }
}
