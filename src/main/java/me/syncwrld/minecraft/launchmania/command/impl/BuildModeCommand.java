package me.syncwrld.minecraft.launchmania.command.impl;

import me.syncwrld.minecraft.launchmania.LaunchmaniaPlugin;
import me.syncwrld.minecraft.launchmania.cache.BuilderCache;
import me.syncwrld.minecraft.launchmania.command.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildModeCommand extends AbstractCommand {
	
	private final BuilderCache builderCache;
	
	public BuildModeCommand(LaunchmaniaPlugin plugin, BuilderCache builderCache) {
		super(plugin);
		this.builderCache = builderCache;
		
		configureCommand(
		  "pad",
		  "launchmania.admin",
		  true,
		  "pads", "padmode", "launcher", "launchpad"
		);
	}
	
	@Override
	public void execute(CommandSender sender, String[] arguments) {
		final Player player = (Player) sender;
		final String message = builderCache.state(player) ?
		  "&cLauncher build mode has been disabled." :
		  "&aLauncher build mode has been enabled.";
		
		reply(sender, message);
		builderCache.setState(player, !builderCache.state(player));
	}
	
}
