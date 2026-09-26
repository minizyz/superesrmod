package com.superesrmod.platform;

import com.superesrmod.SuperESRMod;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class NativeLibraryLoader {

    private NativeLibraryLoader() {}

    public static File extractNative(String resourceDir, String filename) {
        String resourcePath = resourceDir + filename;
        try (InputStream in = NativeLibraryLoader.class.getResourceAsStream("/" + resourcePath)) {
            if (in == null) return null;
            Path tmpDir = Files.createDirectories(
                    Path.of(System.getProperty("java.io.tmpdir"), "superesrmod_natives"));
            File outFile = tmpDir.resolve(filename).toFile();
            if (outFile.exists() && outFile.length() > 0) return outFile;
            try (FileOutputStream out = new FileOutputStream(outFile)) { in.transferTo(out); }
            outFile.deleteOnExit();
            SuperESRMod.LOGGER.info("[NativeLoader] 已解压 {} -> {}", resourcePath, outFile);
            return outFile;
        } catch (Exception e) {
            SuperESRMod.LOGGER.warn("[NativeLoader] 解压 {} 失败: {}", resourcePath, e.toString());
            return null;
        }
    }
}
