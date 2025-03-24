package me.syncwrld.minecraft.launchmania.task.impl;

import com.cryptomorin.xseries.XSound;
import me.syncwrld.minecraft.launchmania.LaunchmaniaPlugin;
import me.syncwrld.minecraft.launchmania.cache.LauncherCache;
import me.syncwrld.minecraft.launchmania.task.AbstractRunnable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class PlayerLocationVerifierTask extends AbstractRunnable {
	
	private final LauncherCache launcherCache;
	
	public PlayerLocationVerifierTask(LaunchmaniaPlugin plugin, LauncherCache launcherCache) {
		super(plugin);
		this.launcherCache = launcherCache;
	}
	
	@Override
	public void tick() {
		Bukkit.getOnlinePlayers().forEach(player -> {
			final Block relativeBlock = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
			
			if (!launcherCache.contains(relativeBlock.getLocation())) return;
			if (plugin().getConfig().getBoolean("ignore-sneaking", true) && player.isSneaking()) return;
			
			final int jumpPower = plugin().getConfig().getInt("jump-power");
			final double jumpFront = plugin().getConfig().getDouble("jump-front");
			final double jumpSideways = plugin().getConfig().getDouble("jump-sideways");
			
			player.setVelocity(new Vector(jumpFront > 0 ? jumpFront : 0, jumpPower, jumpSideways > 0 ? jumpSideways : 0));
			player.setFallDistance(0);
			
			playSound(player);
		});
	}
	
	private void playSound(Player player) {
		if (!plugin().getConfig().getBoolean("jump-sound.enable")) return;
		
		final Location location = player.getLocation();
		final float volume = (float) plugin().getConfig().getDouble("jump-sound.volume");
		final float pitch = (float) plugin().getConfig().getDouble("jump-sound.pitch");
		
		XSound.of(plugin().getConfig().getString("jump-sound.type"))
		  .ifPresent(sound -> {
			  sound.play(location, volume, pitch);
		  });
	}
	
}
