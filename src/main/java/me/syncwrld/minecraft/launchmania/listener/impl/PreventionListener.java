package me.syncwrld.minecraft.launchmania.listener.impl;

import me.syncwrld.minecraft.launchmania.LaunchmaniaPlugin;
import me.syncwrld.minecraft.launchmania.cache.LauncherCache;
import me.syncwrld.minecraft.launchmania.listener.AbstractListener;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class PreventionListener extends AbstractListener {
	
	private final LauncherCache launcherCache;
	
	public PreventionListener(LaunchmaniaPlugin plugin, LauncherCache launcherCache) {
		super(plugin);
		this.launcherCache = launcherCache;
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		if (!plugin().getConfig().getBoolean("disable-fall-damage")) return;
		if (!(event.getEntity() instanceof Player)) return;
		
		final EntityDamageEvent.DamageCause cause = event.getCause();
		if (cause == EntityDamageEvent.DamageCause.FALL) {
			final Player player = (Player) event.getEntity();
			boolean hasLauncher = scanForLauncher(player);
			
			if (hasLauncher) {
				event.setDamage(0);
				event.setCancelled(true);
				
				player.setFallDistance(0);
			}
		}
	}
	
	private boolean scanForLauncher(Player player) {
		final Location playerLoc = player.getLocation();
		final int baseY = playerLoc.getBlockY();
		final int x = playerLoc.getBlockX();
		final int z = playerLoc.getBlockZ();
		
		final World world = playerLoc.getWorld();
		if (world == null) return false;
		
		for (int y = baseY; y >= baseY - 1 && y >= 0; y--) {
			if (launcherCache.contains(new Location(world, x, y, z))) {
				return true;
			}
		}
		
		return false;
	}
	
}
