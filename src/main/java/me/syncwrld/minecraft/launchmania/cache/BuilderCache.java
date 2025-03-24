package me.syncwrld.minecraft.launchmania.cache;

import org.bukkit.entity.Player;

import java.util.HashSet;

public class BuilderCache {
	private final HashSet<String> builders = new HashSet<>();
	
	public void add(Player builderPlayer) {
		builders.add(builderPlayer.getName());
	}
	
	public boolean contains(Player builderPlayer) {
		return builders.contains(builderPlayer.getName());
	}
	
	public void remove(Player builderPlayer) {
		builders.remove(builderPlayer.getName());
	}
	
	public void setState(Player player, boolean state) {
		if (state) {
			add(player);
		} else {
			remove(player);
		}
	}
	
	public boolean state(Player player) {
		return contains(player);
	}
	
}
