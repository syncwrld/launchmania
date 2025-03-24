package me.syncwrld.minecraft.launchmania.task;

import me.syncwrld.minecraft.launchmania.LaunchmaniaPlugin;
import me.syncwrld.minecraft.launchmania.cache.LauncherCache;
import org.bukkit.Bukkit;

public class VisualEffectDisplayTask extends AbstractRunnable {
	
	private final LauncherCache launcherCache;
	
	public VisualEffectDisplayTask(LaunchmaniaPlugin plugin, LauncherCache launcherCache) {
		super(plugin);
		this.launcherCache = launcherCache;
	}
	
	@Override
	public void tick() {
		launcherCache.values().forEach(location -> {
			plugin().particleNativeAPI().LIST_1_8.VILLAGER_HAPPY
			  .packet(true,
			    location.clone()
			      .add(0.5, 1.1, 0.5)
//			      .subtract(0, 0, 0.5)
			  )
			  .sendInRadiusTo(Bukkit.getOnlinePlayers(), plugin().getConfig().getInt("visual-effect.radius"));
		});
	}
	
}
