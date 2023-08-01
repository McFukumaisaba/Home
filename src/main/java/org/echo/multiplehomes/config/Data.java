package org.echo.multiplehomes.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.echo.multiplehomes.MultipleHomes;
import org.echo.multiplehomes.Utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Data {

    private MultipleHomes main;
    private File dataFile;
    private YamlConfiguration yaml;
    private Map<UUID, Map<String, Home>> playerHomes;

    public Data(MultipleHomes main) {
        this.main = main;
        this.dataFile = new File(main.getDataFolder(), "data.yml");
        this.playerHomes = new HashMap<>();
        loadPlayerHomes();

        // Sauvegarde des playersHomes toutes les 5 min
        Bukkit.getScheduler().runTaskTimer(main, this::savePlayerHomes, 0L, 5 * 60 * 20);
    }

    private void loadPlayerHomes() {
        yaml = YamlConfiguration.loadConfiguration(dataFile);

        ConfigurationSection homesSection = yaml.getConfigurationSection("homes");
        if (homesSection != null) {
            for (String playerIdString : homesSection.getKeys(false)) {
                UUID playerId = UUID.fromString(playerIdString);
                Map<String, Home> homes = new HashMap<>();

                ConfigurationSection playerHomesSection = homesSection.getConfigurationSection(playerIdString);
                if (playerHomesSection != null) {
                    for (String homeKey : playerHomesSection.getKeys(false)) {
                        String world = playerHomesSection.getString(homeKey + ".world");
                        if (world != null) {
                            String locationString = playerHomesSection.getString(homeKey + ".location");
                            Location location = Utils.parseLocationFromString(Bukkit.getWorld(world), locationString);
                            Home home = new Home(world, location);
                            homes.put(homeKey, home);
                        }
                    }
                }

                playerHomes.put(playerId, homes);
            }
        }
    }

    public void savePlayerHomes() {

        for (UUID playerId : playerHomes.keySet()) {
            Map<String, Home> homes = playerHomes.get(playerId);

            for (Map.Entry<String, Home> entry : homes.entrySet()) {
                String homeKey = entry.getKey();
                Home home = entry.getValue();

                yaml.set("homes." + playerId.toString() + "." + homeKey + ".world", home.getWorld());
                // Enregistrer la location dans le fichier data.yml
                yaml.set("homes." + playerId.toString() + "." + homeKey + ".location", "X:" + home.getLocation().getX() + ", Y:" + home.getLocation().getY() + ", Z:" + home.getLocation().getZ());
            }
        }

        try {
            yaml.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPlayerHome(UUID playerId, int homeId, Home home) {
        if (!playerHomes.containsKey(playerId)) {
            playerHomes.put(playerId, new HashMap<>());
        }

        Map<String, Home> homes = playerHomes.get(playerId);
        homes.put(Integer.toString(homeId), home);
    }

    public void removePlayerHome(UUID playerId, String homeKey) {
        if (playerHomes.containsKey(playerId)) {
            Map<String, Home> homes = playerHomes.get(playerId);
            homes.remove(homeKey);
        }
    }

    public Map<String, Home> getPlayerHomes(UUID playerId) {
        return playerHomes.getOrDefault(playerId, new HashMap<>());
    }

    public Home getHome(UUID playerId, int homeId) {
        Map<String, Home> playerHomes = getPlayerHomes(playerId);
        if (playerHomes.isEmpty())
            return null;
        return playerHomes.get(Integer.toString(homeId));
    }

    public double getPrice(UUID playerId) {
        Map<String, Home> playerHomes = getPlayerHomes(playerId);
        if (playerHomes == null)
            return main.getMyConfig().getHomePrice();
        else {
            double price = main.getMyConfig().getHomePrice();
            for (int i = 0; i < playerHomes.size(); i++)
                price = price * main.getMyConfig().getHomePriceMultiplier();
            return price;
        }
    }

    public Location getHomeLocation(UUID playerId, int homeKey) {
        Map<String, Home> playerHomes = getPlayerHomes(playerId);
        if (playerHomes.isEmpty())
            return null;
        Home playerHome = playerHomes.get(Integer.toString(homeKey));
        if (playerHome == null)
            return null;
        return playerHome.getLocation();
    }
}
