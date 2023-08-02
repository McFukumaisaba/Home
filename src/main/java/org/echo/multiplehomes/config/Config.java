package org.echo.multiplehomes.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.echo.multiplehomes.MultipleHomes;

import java.io.File;
import java.util.List;

public class Config {

    private MultipleHomes main;
    private YamlConfiguration yaml;

    private File file;

    // Constructeur de la classe
    public Config(MultipleHomes main) {
        this.main = main;
        this.yaml = loadConfig("config.yml");
    }

    // Méthode pour charger la configuration depuis le fichier de configuration
    private YamlConfiguration loadConfig(String file_name) {

        if(!this.main.getDirectory().exists()) {
            this.main.getDirectory().mkdir();
        }

        this.file = new File(this.main.getDataFolder(), file_name);

        if (!this.file.exists()) {
            this.main.saveResource(file_name, false);
        }

        return YamlConfiguration.loadConfiguration(this.file);
    }

    private Object getValue(String key) {
        return yaml.get(key);
    }

    // GENERAL
    public int getNumberOfHomes() {
        return yaml.getInt("number_of_homes", 3);
    }
    public int getTeleportationDelay() {
        return yaml.getInt("teleportation_delay", 3);
    }

    // ECONOMY
    public boolean isEconomyEnable() {
        return yaml.getBoolean("economy_enable", false);

    }
    public double getHomePrice() {
        return yaml.getDouble("home_price", 1);
    }
    public double getHomePriceMultiplier() {
        return yaml.getDouble("home_price_multiplier", 0);
    }
    public double getTeleportationBlockDistancePerPrice() {
        return yaml.getDouble("teleportation_block_distance_per_price", 0);
    }
    public double getTeleportationPricePerBlockDistance() {
        return yaml.getDouble("teleportation_price_per_block_distance", 1);
    }

    // WORLD
    public List<String> getDisabledWorlds() {
        return yaml.getStringList("disabled_worlds");
    }
    public List<String> getWorldsDisableToPlace() {
        return yaml.getStringList("worlds_disable_to_place");
    }

    // BACK BUTTON
    public boolean isBackButtonEnabled() {
        return yaml.getBoolean("back_button", false);
    }
    public String getBackCommand(String playerName) {
        String command = yaml.getString("back_command");
        if (command != null) {
            return command.replace("{player}", playerName);
        }
        return "";
    }
    public boolean isBackCommandSentByConsole() {
        return yaml.getBoolean("back_send_by_console", false);
    }
}
