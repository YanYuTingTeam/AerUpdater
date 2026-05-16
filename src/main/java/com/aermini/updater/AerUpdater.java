package com.aermini.updater;

import com.aermini.updater.listener.DynamicEventListener;
import com.aermini.updater.manager.ConfigManager;
import com.aermini.updater.manager.UpdateManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AerUpdater extends JavaPlugin {
    private ConfigManager configManager;
    private UpdateManager updateManager;

    @Override
    public void onLoad() {
        this.configManager = new ConfigManager(this);
        this.updateManager = new UpdateManager(this);
        this.updateManager.checkAndApply();
    }

    @Override
    public void onEnable() {
        DynamicEventListener listener = new DynamicEventListener(this);
        listener.registerEvents();
        
        getLogger().info("AerUpdater 已就绪");
    }

    public ConfigManager getConfigManager() { return configManager; }
    public UpdateManager getUpdateManager() { return updateManager; }
}