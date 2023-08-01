package org.echo.multiplehomes.config;

import org.bukkit.Location;

public class Home {
    private String world;
    private Location location;

    public Home(String world, Location location) {
        this.world = world;
        this.location = location;
    }

    public String getWorld() {
        return world;
    }

    public Location getLocation() {
        return location;
    }
}