package org.echo.multiplehomes.commads;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.echo.multiplehomes.MultipleHomes;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.geysermc.geyser.api.GeyserApi;

public class Commands implements CommandExecutor {

    private MultipleHomes main;

    public Commands(MultipleHomes main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            // /multiplehomes command without any subcommand or arguments
            if (sender.hasPermission("multiplehomes.use")) {

                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    // Open the homes inventory for the player
                    openHomesInventory(player);
                } else {
                    sender.sendMessage("§cOnly players can use this command.");
                }
                return true;
            }
            else {
                sender.sendMessage(main.getMessages().getNoPermissionMessage());
            }
        } else if (args[0].equalsIgnoreCase("open")) {
            // /multiplehomes open <player>
            if (sender.hasPermission("multiplehomes.open")) {
                if (args.length > 1) {
                    // Get the target player
                    Player targetPlayer = main.getServer().getPlayer(args[1]);
                    if (targetPlayer != null) {
                        // Open the homes inventory for the target player
                        openHomesInventory(targetPlayer);
                    } else {
                        sender.sendMessage("§cPlayer not found: " + args[1]);
                    }
                } else {
                    sender.sendMessage("§fUsage: §e/home open <player>");
                }
            } else {
                sender.sendMessage(main.getMessages().getNoPermissionMessage());
            }
            return true;
        } else if (args[0].equalsIgnoreCase("reload")) {
            // /multiplehomes reload
            if (sender.hasPermission("multiplehomes.reload")) {
                // Reload the plugin
                main.reloadPlugin();
                sender.sendMessage("§a[MultipleHomes] plugin reloaded.");
            } else {
                sender.sendMessage(main.getMessages().getNoPermissionMessage());
            }
            return true;
        } else if (args[0].equalsIgnoreCase("help")) {
            // /MultipleHomes help
            if (sender.hasPermission("multiplehomes.help")) {
                // Print help message
                printHelp(sender);

                // Add additional help messages as needed
            } else {
                sender.sendMessage(main.getMessages().getNoPermissionMessage());
            }
            return true;
        }
        sender.sendMessage("§cUsage: §e/home help");
        return true;
    }

    private void printHelp(CommandSender sender) {
        sender.sendMessage("§6[MultipleHomes]");
        sender.sendMessage("§e/home help §7Print help of MultipleHomes plugin");
        sender.sendMessage("§e/home §7Open the homes inventory if you are a player§7");
        sender.sendMessage("§e/home open <player> §7Open the homes inventory for a targeted player (for console)");
        sender.sendMessage("§e/home reload §7Reload MultipleHomes plugin");
    }

    private void openHomesInventory(Player player) {
        if (main.getMyConfig().getDisabledWorlds().contains(player.getWorld().getName())) {
            player.sendMessage(main.getMessages().getDisabledWorldsMessage());
        }
        else {
            if (!player.getName().contains(".")) {
                main.getGuiMenu().openMenu(player);
            } else {
                FloodgatePlayer bePlayer = FloodgateApi.getInstance().getPlayer(player.getUniqueId());
                bePlayer.sendForm(main.getBedrockGuiMenu().createSelectModeMenu(player));
            }
        }
    }
}
