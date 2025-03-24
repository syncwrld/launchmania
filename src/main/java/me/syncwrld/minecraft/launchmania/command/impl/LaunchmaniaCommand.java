package me.syncwrld.minecraft.launchmania.command.impl;

import com.google.common.base.Stopwatch;
import me.syncwrld.minecraft.launchmania.LaunchmaniaPlugin;
import me.syncwrld.minecraft.launchmania.command.AbstractCommand;
import me.syncwrld.minecraft.launchmania.tool.cooldown.CooldownManager;
import org.bukkit.command.CommandSender;

import java.util.concurrent.TimeUnit;

public class LaunchmaniaCommand extends AbstractCommand {

    private final CooldownManager<String> cooldownManager = new CooldownManager<>();

    public LaunchmaniaCommand(LaunchmaniaPlugin plugin) {
        super(plugin);
        configureCommand(
                "launchmania",
                null,
                false,
                "lm", "lmania"
        );
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
        if (!sender.hasPermission("launchmania.admin")) {
            if (cooldownManager.hasCooldown(senderName(sender))) {
                reply(sender, "§cWait " + cooldownManager.getRemainingTimeFormatted(senderName(sender)) + " before using this command again.");
                return;
            }

            reply(
                    sender,
                    "",
                    "&aLaunchmania v" + plugin().getDescription().getVersion(),
                    " &8▸ &7www.syncwrld.me",
                    ""
            );
            cooldownManager.setCooldown(senderName(sender), 3, TimeUnit.SECONDS);
            return;
        }

        if (arguments.length == 0) {
            reply(sender, "§cYou must specify a subcommand. Available subcommands: reload.");
            return;
        }

        final String subcommand = arguments[0].toLowerCase();

        if ("reload".equals(subcommand)) {
            Stopwatch stopwatch = Stopwatch.createStarted();
            plugin().reload();
            reply(sender, "&6&lLAUNCHMANIA! &eReloaded configuration. §8(took " + stopwatch.stop() + ")");
            cooldownManager.setCooldown(senderName(sender), 3, TimeUnit.SECONDS);
            return;
        }

        reply(sender, "§cUnknown subcommand. Available subcommands: reload.");
    }

}
