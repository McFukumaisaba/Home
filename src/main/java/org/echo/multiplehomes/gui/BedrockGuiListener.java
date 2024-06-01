package org.echo.multiplehomes.gui;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.echo.multiplehomes.MultipleHomes;
import org.echo.multiplehomes.Utils;
import org.echo.multiplehomes.config.Home;
import org.echo.multiplehomes.teleport.Teleport;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.UUID;

public class BedrockGuiListener {
    private MultipleHomes main;
    private Teleport teleport;

    public BedrockGuiListener(MultipleHomes main, Teleport teleport) {
        this.main = main;
        this.teleport = teleport;
    }

    public void selectModeMenuListener(Player player, SimpleFormResponse response) {
        System.out.println("selectModeMenuListener発火");
        int clickId = response.clickedButtonId();

        SimpleForm.Builder form = null;

        switch (clickId) {
            case 0:
                form = this.main.getBedrockGuiMenu().createHomeMenu(player, true);
                break;
            case 1:
                form = this.main.getBedrockGuiMenu().createHomeMenu(player, false);
                break;
        }

        FloodgatePlayer bePlayer = FloodgateApi.getInstance().getPlayer(player.getUniqueId());
        bePlayer.sendForm(form);
        System.out.println("formを送信");
    }

    public void teleportMenuListener(Player player, SimpleFormResponse response) {
        UUID playerUUID = player.getUniqueId();
        int homeId = response.clickedButtonId() + 1;

        Location homeLocation = main.getData().getHomeLocation(playerUUID, homeId);

        if (homeLocation != null) {
            if (teleport.isTeleporting(player)) {
                player.sendMessage(main.getMessages().getMessage("teleportation_is_cooldown"));
                return;
            }

            teleport.startTeleport(player, homeLocation, main.getMyConfig().getTeleportationDelay());
        } else {
            player.sendMessage(main.getMessages().getMessage("home_not_found"));
        }
    }

    public void replaceMenuListener(Player player, SimpleFormResponse response) {
        if (main.getMyConfig().getWorldsDisableToPlace().contains(player.getWorld().getName())) {
            player.sendMessage(main.getMessages().getWorldsDisableToPlaceMessage());
            return;
        }

        int homeId = response.clickedButtonId() + 1;

        double price = main.getData().getPrice(player.getUniqueId());

        Economy economy = main.getEconomy();
        if (economy != null) {
            if (economy.getBalance(player) > price) {
                economy.withdrawPlayer(player, price);
            }
            else {
                player.sendMessage(main.getMessages().getMessage("no_enough_money"));
                return;
            }
        }
        Location playerLocation = player.getLocation();
        Home home = new Home(playerLocation.getWorld().getName(), playerLocation);
        main.getData().addPlayerHome(player.getUniqueId(), homeId, home);
        player.sendMessage(main.getMessages().getPlaceHomeMessage(main.getMessages().getHomeNameMessage(homeId), Utils.formatLocation(playerLocation), Double.toString(price)));
    }
}
