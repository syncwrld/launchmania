package me.syncwrld.minecraft.launchmania.tool.logging;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

@RequiredArgsConstructor
public class BukkitLogger {
	private final Plugin plugin;
	
	private void log(String message) {
		final String prefix = "§6[" + plugin.getDescription().getName() + " v" + plugin.getDescription().getVersion() + "]§f ";
		Bukkit.getConsoleSender().sendMessage((prefix + message).replace("&", "§"));
	}
	
	public void info(String... messages) {
		for (String message : messages) {
			log("§9[INFO] §f" + message);
		}
	}
	
	public void warning(String... messages) {
		for (String message : messages) {
			log("§e[WARNING] §f" + message);
		}
	}
	
	public void error(String... messages) {
		for (String message : messages) {
			log("§c[ERROR] §f" + message);
		}
	}
	
	public void critical(String... messages) {
		for (String message : messages) {
			log("§4[CRITICAL] §f" + message);
		}
	}
	
	public void error(String message, Exception exception) {
		log("§c[ERROR] §f" + message);
		throw new RuntimeException(message, exception);
	}
}