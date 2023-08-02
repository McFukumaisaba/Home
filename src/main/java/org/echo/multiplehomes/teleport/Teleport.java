package org.echo.multiplehomes.teleport;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.echo.multiplehomes.MultipleHomes;
import org.echo.multiplehomes.Utils;

import java.util.*;

public class Teleport {

    private MultipleHomes main;
    private Map<UUID, BukkitTask> cooldowns;
    private Map<UUID, Location> teleportingPlayers;

    public Teleport(MultipleHomes main) {
        this.main = main;
        this.cooldowns = new HashMap<>();
        this.teleportingPlayers = new HashMap<>();
    }

    public boolean isTeleporting(Player player) {
        return teleportingPlayers.containsKey(player.getUniqueId());
    }

    public void startTeleport(Player player, Location destination, int cooldownSeconds) {
        if (isTeleporting(player)) {
            return; // Already teleporting
        }

        // Store the player's current location for canceling teleportation if needed
        teleportingPlayers.put(player.getUniqueId(), player.getLocation());

        // Start the cooldown task
        BukkitTask cooldownTask = new BukkitRunnable() {
            int remainingSeconds = cooldownSeconds;

            @Override
            public void run() {
                if (remainingSeconds > 0) {
                    player.sendTitle("", "§e" + remainingSeconds, 0, 20, 0);
                    remainingSeconds--;
                } else {
                    teleportPlayer(player, destination);
                    cooldowns.remove(player.getUniqueId());
                    teleportingPlayers.remove(player.getUniqueId());
                    cancel(); // Cancel the task
                }
            }
        }.runTaskTimer(main, 0, 20);

        cooldowns.put(player.getUniqueId(), cooldownTask);
    }

    public void cancelTeleportation(Player player) {
        BukkitTask cooldownTask = cooldowns.get(player.getUniqueId());
        if (cooldownTask != null) {
            cooldownTask.cancel();
            cooldowns.remove(player.getUniqueId());
            teleportingPlayers.remove(player.getUniqueId());
            player.sendTitle(main.getMessages().getMessage("title_teleportation_cancelled"), "", 0, 40, 20);
        }
    }

    private void teleportPlayer(Player player, Location location) {

        if (main.getEconomy() != null) {

            double price = TeleportUtils.getTeleportationPrice(main, player.getLocation(), location);

            if (main.getEconomy().getBalance(player) < price) {
                player.sendMessage(main.getMessages().getTeleportationNotEnoughMoney());
                return ;
            }
            else {
                if (price > 0) {
                    player.sendMessage(main.getMessages().getTeleportationPaid(Double.toString(price)));
                    main.getEconomy().withdrawPlayer(player, price);
                }
            }
        }
        player.teleport(location);
        player.sendTitle(main.getMessages().getMessage("title_teleported"), "§e" + Utils.formatLocation(location), 0, 40, 20);
    }
}
