package com.aermini.updater.manager;

import com.aermini.updater.AerUpdater;
import java.util.List;

public class ConfigManager {
    private final AerUpdater plugin;
    private String updatePath;
    private List<String> updateEvents;

    public ConfigManager(AerUpdater plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.updatePath = plugin.getConfig().getString("path", "D:/YYT-260116/update/");
        this.updateEvents = plugin.getConfig().getStringList("update-events");
    }

    public String getUpdatePath() { return updatePath; }
    public List<String> getUpdateEvents() { return updateEvents; }
}