package com.aermini.updater.listener;

import com.aermini.updater.AerUpdater;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

public class DynamicEventListener implements Listener {
    private final AerUpdater plugin;

    public DynamicEventListener(AerUpdater plugin) {
        this.plugin = plugin;
    }

    public void registerEvents() {
        for (String eventClassName : plugin.getConfigManager().getUpdateEvents()) {
            try {
                @SuppressWarnings("unchecked")
                Class<? extends Event> eventClass = (Class<? extends Event>) Class.forName(eventClassName);
                Bukkit.getPluginManager().registerEvent(
                    eventClass, 
                    this, 
                    EventPriority.MONITOR, 
                    new EventExecutor() {
                        @Override
                        public void execute(Listener listener, Event event) {
                            plugin.getUpdateManager().checkAndApply();
                        }
                    }, 
                    plugin
                );
                plugin.getLogger().info("UPDATER > 已监听事件 " + eventClassName);
            } catch (ClassNotFoundException e) {
                plugin.getLogger().warning("UPDATER > 无法找到事件 " + eventClassName);
            } catch (ClassCastException e) {
                plugin.getLogger().warning("UPDATER > " + eventClassName + " 不是有效的event");
            }
        }
    }
}