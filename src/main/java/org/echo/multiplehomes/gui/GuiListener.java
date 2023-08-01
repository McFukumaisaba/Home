package org.echo.multiplehomes.gui;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.echo.multiplehomes.MultipleHomes;
import org.echo.multiplehomes.Utils;
import org.echo.multiplehomes.config.Home;
import org.echo.multiplehomes.teleport.Teleport;

import java.util.UUID;

public class GuiListener implements Listener {

    private MultipleHomes main;
    private Teleport teleport;

    public GuiListener(MultipleHomes main, Teleport teleport) {
        this.main = main;
        this.teleport = teleport;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();


        // Check if the clicked inventory is the GuyHomes menu
        if (inventory != null && event.getView().getTitle().equals(main.getMessages().getMessage("menu_name"))) {

            Player player = (Player) event.getWhoClicked();

            // Prevent players from picking up beds (or any other restricted items)
            event.setCancelled(true);
            player.closeInventory();
            player.updateInventory();

            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null) {

                String itemName = clickedItem.getItemMeta().getDisplayName();
                int itemPos = event.getSlot();

                if (clickedItem.getType() == Material.GREEN_BED) {
                    if (event.getClick().isLeftClick())
                        teleportAction(player, itemPos + 1);
                    else if (event.getClick().isRightClick())
                        placeAction(player, itemPos + 1);
                }
                else if (clickedItem.getType() == Material.ORANGE_BED) {
                    placeAction(player, itemPos + 1);
                }
                else if (clickedItem.getType() == Material.RED_BED) {
                    player.sendMessage(main.getMessages().getHomeLockedMessage());
                }
                else if (clickedItem.getType() == Material.BARRIER) {
                    returnAction(player);
                }
            }
        }
    }

    private void teleportAction(Player player, int homeId) {
        // Retrieve the player's UUID
        UUID playerUUID = player.getUniqueId();

        // Retrieve the location of the home from the data storage (e.g., Data class)
        Location homeLocation = main.getData().getHomeLocation(playerUUID, homeId);

        if (homeLocation != null) {
            // Check if the player is on cooldown for teleportation
            if (teleport.isTeleporting(player)) {
                player.sendMessage(main.getMessages().getMessage("teleportation_is_cooldown"));
                return;
            }
            // Teleport the player to the home location
            teleport.startTeleport(player, homeLocation, main.getMyConfig().getTeleportationDelay());
        } else {
            player.sendMessage(main.getMessages().getMessage("home_not_found"));
        }
    }

    private void placeAction(Player player, int homeId) {

        if (main.getMyConfig().getWorldsDisableToPlace().contains(player.getWorld().getName())) {
            player.sendMessage(main.getMessages().getWorldsDisableToPlaceMessage());
            return ;
        }

        double price = main.getData().getPrice(player.getUniqueId());

        Economy economy = main.getEconomy();
        if (economy != null) {
            if (economy.getBalance(player) > price) {
                economy.withdrawPlayer(player, price);
            }
            else {
                player.sendMessage(main.getMessages().getMessage("no_enough_money"));
                return ;
            }
        }
        Location playerLocation = player.getLocation();
        Home home = new Home(playerLocation.getWorld().getName(), playerLocation);
        main.getData().addPlayerHome(player.getUniqueId(), homeId, home);
        player.sendMessage(main.getMessages().getPlaceHomeMessage(main.getMessages().getHomeNameMessage(homeId), Utils.formatLocation(playerLocation), Double.toString(price)));
    }

    private void returnAction(Player player) {
        if (main.getMyConfig().isBackCommandSentByConsole()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), main.getMyConfig().getBackCommand(player.getName()));
        }
        else {
            player.performCommand(main.getMyConfig().getBackCommand(player.getName()));
        }

    }
}
