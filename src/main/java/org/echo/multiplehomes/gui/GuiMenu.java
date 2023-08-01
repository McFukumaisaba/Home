package org.echo.multiplehomes.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.echo.multiplehomes.MultipleHomes;
import org.echo.multiplehomes.Utils;
import org.echo.multiplehomes.config.Home;
import org.echo.multiplehomes.teleport.TeleportUtils;

import java.util.ArrayList;
import java.util.List;

public class GuiMenu {

    private MultipleHomes main;

    public GuiMenu(MultipleHomes main) {
        this.main = main;
    }

        public void openMenu(Player player) {

            int numberOfHomes = main.getMyConfig().getNumberOfHomes();

            Inventory inventory;

            if (main.getMyConfig().isBackButtonEnabled())
                inventory = Utils.createInventory(Utils.getInvetorySize(numberOfHomes + 1), main.getMessages().getMessage("menu_name"));
            else
                inventory = Utils.createInventory(Utils.getInvetorySize(numberOfHomes), main.getMessages().getMessage("menu_name"));

            double price = main.getData().getPrice(player.getUniqueId());

            // Add homes to the inventory
            for (int i = 1; i <= numberOfHomes; i++) {

                String homeName = main.getMessages().getHomeNameMessage(i);
                Home home = main.getData().getHome(player.getUniqueId(), i);
                ItemStack homeItem = createHomeItem(player, homeName, i, home, price);
                inventory.setItem(i - 1, homeItem);
            }

            // Add back button
            if (main.getMyConfig().isBackButtonEnabled()) {
                ItemStack backButton = createBackButton();
                inventory.setItem(Utils.getInvetorySize(numberOfHomes) - 1, backButton);
            }

            // Open the menu for the player
            player.openInventory(inventory);
        }

        private ItemStack createHomeItem(Player player, String homeName, int id, Home home, double price) {

            ItemStack itemStack;

            List<String> lore = new ArrayList<String>();

            if (home != null) {
                itemStack = new ItemStack(Material.GREEN_BED);
                double teleportationPrice = TeleportUtils.getTeleportationPrice(main, player.getLocation(), home.getLocation());

                lore.add(main.getMessages().getLocationMessage(Utils.formatLocation(home.getLocation())));
                lore.add(main.getMessages().getWorldMessage(main.getMessages().getWorldsAlias(home.getLocation().getWorld())));
                lore.add(main.getMessages().getReplaceMessage(Double.toString(price)));
                lore.add(main.getMessages().getTeleportationMessage(Double.toString(teleportationPrice)));
            }
            else if (player.hasPermission("multiplehomes.unlock." + id) || player.hasPermission("multiplehomes.unlock.*")) {
                itemStack = new ItemStack(Material.ORANGE_BED);
                lore.add(main.getMessages().getPlaceMessage(Double.toString(price)));
            }
            else {
                itemStack = new ItemStack(Material.RED_BED);
                lore.add(main.getMessages().getLockedMessage());
            }

            ItemMeta itemMeta = itemStack.getItemMeta();

            // Set item display name
            itemMeta.setDisplayName(homeName);

            // Set item lore
            itemMeta.setLore(lore);

            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }

        private ItemStack createBackButton() {

            ItemStack itemStack = new ItemStack(Material.BARRIER);
            ItemMeta itemMeta = itemStack.getItemMeta();

            // Set item display name
            itemMeta.setDisplayName(main.getMessages().getBackMessage());

            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
    }
