package me.syncwrld.minecraft.launchmania.command;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;

public class Commands {

    private static CommandMap commandMap;

    static {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void registerCommand(Plugin plugin, AbstractCommand abstractCommand) {
        if (commandMap == null) {
            throw new IllegalStateException("Was not able to get the command map");
        }

        Command command= new Command(abstractCommand.name()) {
            @Override
            public boolean execute(CommandSender commandSender, String s, String[] strings) {
                if (!(commandSender instanceof Player) && abstractCommand.onlyPlayers()) {
                    commandSender.sendMessage("You can only use this command in-game.");
                    return true;
                }
                
                if (abstractCommand.permission() != null && !commandSender.hasPermission(abstractCommand.permission())) {
                    commandSender.sendMessage("You don't have permission to use this command.");
                    return true;
                }
                
                abstractCommand.execute(commandSender, strings);
                return true;
            }
        };
        
        command.setAliases(abstractCommand.aliases());
        commandMap.register(plugin.getDescription().getName(), command);
    }
}
