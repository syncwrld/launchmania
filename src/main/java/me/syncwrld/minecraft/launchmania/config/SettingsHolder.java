package me.syncwrld.minecraft.launchmania.config;

import com.cryptomorin.xseries.XMaterial;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

@Getter(AccessLevel.PUBLIC)
@Accessors(fluent = true)
public class SettingsHolder {
	private final Set<Material> blockWhitelist = new HashSet<>();
	private boolean blockWhitelistEnabled;
	
	public boolean allowed(Material type) {
		return !blockWhitelistEnabled || blockWhitelist.contains(type);
	}
	
	public void load(FileConfiguration configuration) {
		this.blockWhitelistEnabled = configuration.getBoolean("pad-block-whitelist.enable");
		
		if (blockWhitelistEnabled) {
			blockWhitelist.clear();
			configuration.getStringList("pad-block-whitelist.blocks").forEach(blockName -> {
				XMaterial.matchXMaterial(blockName).ifPresent(block -> {
					blockWhitelist.add(block.get());
				});
			});
		}
	}
	
	/*
	Paper servers makes an exception when any value of a velocity vector is greater than 4.0.
	TODO: Warn about this on console and make a prompt asking the user to disable this limitation.
	Written in 24/03/2025
	 */
	private boolean isPaperServer() {
		try {
			Class.forName("io.papermc.paper.PaperConfig");
			return true;
		} catch (ClassNotFoundException ignored) {
			return false;
		}
	}
	
}
