package org.echo.multiplehomes.teleport;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.echo.multiplehomes.MultipleHomes;

public class TeleportListener implements Listener {

    private Teleport teleport;

    private MultipleHomes main;

    public TeleportListener(MultipleHomes main, Teleport teleport) {
        this.teleport = teleport;
        this.main = main;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        if (teleport.isTeleporting(event.getPlayer())) {
            Location from = event.getFrom();
            Location to = event.getTo();

            if (from.distance(to) == 0) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(main.getMessages().getTeleportationCancelledMessage());
                teleport.cancelTeleportation(event.getPlayer());
            }
        }
    }
}
