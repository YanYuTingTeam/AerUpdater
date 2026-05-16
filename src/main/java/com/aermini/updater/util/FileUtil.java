package com.aermini.updater.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {
    public static boolean isNewer(Path source, Path target) throws IOException {
        if (!Files.exists(target)) return true;
        long sTime = Files.getLastModifiedTime(source).toMillis();
        long tTime = Files.getLastModifiedTime(target).toMillis();
        return sTime > tTime + 2000;
    }

    public static String getGameType() {
        String folderName = new File(System.getProperty("user.dir")).getName();
        String[] parts = folderName.split("_");
        return (parts.length >= 2) ? parts[1] : null;
    }
}