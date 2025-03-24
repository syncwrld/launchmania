package me.syncwrld.minecraft.launchmania.listener.impl;

import com.cryptomorin.xseries.XItemStack;
import com.cryptomorin.xseries.messages.ActionBar;
import me.syncwrld.minecraft.launchmania.LaunchmaniaPlugin;
import me.syncwrld.minecraft.launchmania.cache.BuilderCache;
import me.syncwrld.minecraft.launchmania.cache.LauncherCache;
import me.syncwrld.minecraft.launchmania.config.SettingsHolder;
import me.syncwrld.minecraft.launchmania.listener.AbstractListener;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.stream.Collectors;

public class BuildListener extends AbstractListener {
	
	private final BuilderCache builderCache;
	private final LauncherCache launcherCache;
	private final SettingsHolder settingsHolder;
	
	public BuildListener(LaunchmaniaPlugin plugin, BuilderCache builderCache, LauncherCache launcherCache, SettingsHolder settingsHolder) {
		super(plugin);
		this.builderCache = builderCache;
		this.launcherCache = launcherCache;
		this.settingsHolder = settingsHolder;
	}
	
	@EventHandler
	public void onLauncherPlace(BlockPlaceEvent event) {
		final Player player = event.getPlayer();
		final Block block = event.getBlock();
		final Location location = block.getLocation();
		if (notBuilding(player)) return;
		
		final Material blockType = block.getType();
		if (!settingsHolder.allowed(blockType)) {
			ActionBar.sendActionBar(player, "§cIgnoring block placement (not in whitelist).");
			return;
		}
		
		ActionBar.sendActionBar(player, "§aNew launcher created at " + keyOf(location));
		launcherCache.insert(location);
	}
	
	@EventHandler
	public void onLauncherBreak(BlockBreakEvent event) {
		final Player player = event.getPlayer();
		final Location location = event.getBlock().getLocation();
		
		if (!isLauncher(location)) return;
		if (notBuilding(player)) {
			ActionBar.sendActionBar(player, "§4You can't destroy this launcher if you are not building! §7(/pad)");
			event.setExpToDrop(0);
			event.setCancelled(true);
			return;
		}
		
		ActionBar.sendActionBar(player, "§cLauncher destroyed at " + keyOf(location));
		launcherCache.remove(location);
	}
	
	private boolean notBuilding(Player player) {
		return !builderCache.contains(player);
	}
	
	private boolean isLauncher(Location location) {
		return launcherCache.contains(location);
	}
	
	private String keyOf(Location location) {
		return launcherCache.toKey(location);
	}
	
}
