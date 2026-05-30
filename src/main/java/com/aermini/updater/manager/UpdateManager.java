package com.aermini.updater.manager;

import com.aermini.updater.AerUpdater;
import com.aermini.updater.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicBoolean;

public class UpdateManager {
    private final AerUpdater plugin;

    public UpdateManager(AerUpdater plugin) {
        this.plugin = plugin;
    }

    public void checkAndApply() {
        String gameType = FileUtil.getGameType();
        if (gameType == null) return;

        File sourceDir = new File(plugin.getConfigManager().getUpdatePath(), gameType);
        File pluginsDir = plugin.getDataFolder().getParentFile();
        File bukkitUpdateDir = new File(pluginsDir, "update");

        if (!sourceDir.exists()) return;

        AtomicBoolean hasUpdate = new AtomicBoolean(false);
        Path sourcePath = sourceDir.toPath();
        Path pluginsPath = pluginsDir.toPath();

        try {
            Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String name = file.getFileName().toString();
                    if (name.toLowerCase().contains("aerupdater")) return FileVisitResult.CONTINUE;

                    Path target = pluginsPath.resolve(sourcePath.relativize(file));
                    if (FileUtil.isNewer(file, target)) {
                        if (name.toLowerCase().endsWith(".jar") && Files.exists(target)) {
                            if (!bukkitUpdateDir.exists()) bukkitUpdateDir.mkdirs();
                            Files.copy(file, bukkitUpdateDir.toPath().resolve(name), 
                                StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                        } else {
                            Files.createDirectories(target.getParent());
                            Files.copy(file, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                        }
                        plugin.getLogger().info("UPDATER > 准备替换: " + name);
                        hasUpdate.set(true);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (hasUpdate.get()) {
            plugin.getLogger().info("UPDATER > 检测到更新，正在重启服务器");
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    Runtime.getRuntime().halt(0);
                } catch (Exception ignored) {}
            }).start();
            
            System.exit(0);
        }
    }
}