package me.syncwrld.minecraft.launchmania.command;

import lombok.Data;
import lombok.experimental.Accessors;
import me.syncwrld.minecraft.launchmania.LaunchmaniaPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Accessors(fluent = true)
public abstract class AbstractCommand {
	private final List<String> aliases = new ArrayList<>();
	private final LaunchmaniaPlugin plugin;
	private String name;
	private String permission;
	private boolean onlyPlayers;
	
	public abstract void execute(CommandSender sender, String[] arguments);
	
	public void aliases(String... aliases) {
		this.aliases.addAll(Arrays.asList(aliases));
	}
	
	public AbstractCommand name(String name) {
		this.name = name;
		return this;
	}
	
	public AbstractCommand permission(String permission) {
		this.permission = permission;
		return this;
	}
	
	public AbstractCommand onlyPlayers(boolean onlyPlayers) {
		this.onlyPlayers = onlyPlayers;
		return this;
	}
	
	public List<String> aliases() {
		return aliases;
	}
	
	public void register() {
		Commands.registerCommand(plugin, this);
	}
	
	public void configureCommand(String name, String permission, boolean onlyPlayers, String... aliases) {
		this.name = name;
		this.permission = permission;
		this.onlyPlayers = onlyPlayers;
		this.aliases.addAll(Arrays.asList(aliases));
	}
	
	public LaunchmaniaPlugin plugin() {
		return plugin;
	}
	
	public String senderName(CommandSender sender) {
		return sender instanceof ConsoleCommandSender ? "CONSOLE" : sender.getName();
	}
	
	public void reply(CommandSender sender, String message) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}
	
	public void reply(CommandSender sender, String... messages) {
		final String[] result = new String[messages.length];
		for (int i = 0; i < messages.length; i++) {
			result[i] = ChatColor.translateAlternateColorCodes('&', messages[i]);
		}
		sender.sendMessage(result);
	}
	
}
