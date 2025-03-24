package me.syncwrld.minecraft.launchmania.listener;

import lombok.AccessLevel;
import lombok.Getter;
import me.syncwrld.minecraft.launchmania.LaunchmaniaPlugin;
import org.bukkit.event.Listener;

/*
 * © "2024" - wrldDevelopment
 * www.syncwrld.me
 * <p>
 * buy ur system with me - cheap, fast and reliable.
 */

@Getter(AccessLevel.PUBLIC)
public abstract class AbstractListener implements Listener {

    private final LaunchmaniaPlugin plugin;

    public AbstractListener(LaunchmaniaPlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public LaunchmaniaPlugin plugin() {
        return plugin;
    }

}
