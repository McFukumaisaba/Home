package org.echo.multiplehomes.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.echo.multiplehomes.MultipleHomes;

public class Commands {

    private final MultipleHomes main;

    public Commands(MultipleHomes main) {
        this.main = main;
    }

    public void registerCommands() {
        new CommandAPICommand("multiplehomes")
                .withAliases("mh", "home", "homes")
                .withPermission("multiplehomes.use")
                .executesPlayer((sender, args) -> {
                    openHomesInventory(sender);
                })
                .withSubcommand(new CommandAPICommand("help")
                        .withPermission("multiplehomes.help")
                        .executes((sender, args) -> {
                            printHelp(sender);
                        }))
                .withSubcommand(new CommandAPICommand("open")
                        .withPermission("multiplehomes.open")
                        .withOptionalArguments(new PlayerArgument("target"))
                        .executesPlayer((sender, args) -> {
                            Player target = (Player) args.get("target");
                            if (target != null) {
                                openHomesInventory(target);
                            } else {
                                openHomesInventory(sender);
                            }

                        }))
                .withSubcommand(new CommandAPICommand("reload")
                        .withPermission("multiplehomes.reload")
                        .executes((sender, args) -> {
                            main.reloadPlugin();
                            sender.sendMessage("§a[MultipleHomes] plugin reloaded.");
                        }))
                .register();
    }

    private void printHelp(CommandSender sender) {
        sender.sendMessage("§6[MultipleHomes]");
        sender.sendMessage("§e/home help §7Print help of MultipleHomes plugin");
        sender.sendMessage("§e/home §7Open the homes inventory if you are a player§7");
        sender.sendMessage("§e/home open <player> §7Open the homes inventory for a targeted player (for console)");
        sender.sendMessage("§e/home reload §7Reload MultipleHomes plugin");
    }

    private void openHomesInventory(Player player) {
        if (main.getMyConfig().getDisabledWorlds().contains(player.getWorld().getName()))
            player.sendMessage(main.getMessages().getDisabledWorldsMessage());
        else
            main.getGuyMenu().openMenu(player);
    }
}
