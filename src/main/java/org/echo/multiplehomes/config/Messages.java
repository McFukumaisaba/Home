package org.echo.multiplehomes.config;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.echo.multiplehomes.MultipleHomes;

import java.io.File;

public class Messages {
    private MultipleHomes main;

    private YamlConfiguration yaml;
    private File file;

    // Constructeur de la classe
    public Messages(MultipleHomes main) {
        this.main = main;
        this.yaml = loadConfig("messages.yml");
    }

    // Méthode pour charger les messages depuis le fichier de configuration
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

    // Méthode pour récupérer un message à partir de sa clé
    public String getMessage(String key) {
        String message = yaml.getString(key);
        if (message != null) {
            return ChatColor.translateAlternateColorCodes('&', message);
        }
        return "";
    }

    // Méthode pour récupérer un message avec des remplacements
    private String getFormattedMessage(String key, String... replacements) {
        String message = getMessage(key);
        for (int i = 0; i < replacements.length; i += 2) {
            String placeholder = replacements[i];
            String replacement = replacements[i + 1];
            message = message.replace(placeholder, replacement);
        }
        return message;
    }

    // Méthode pour récupérer le message "home_locked"
    public String getHomeLockedMessage() {
        return getMessage("home_locked");
    }

    // Méthode pour récupérer le message "no_enough_money"
    public String getNotEnoughMoneyMessage() {
        return getMessage("no_enough_money");
    }

    // Méthode pour récupérer le message "place_home" avec des remplacements
    public String getPlaceHomeMessage(String homeName, String loc, String price) {
        return getFormattedMessage("place_home", "{home}", homeName, "{loc}", loc, "{price}", price);
    }

    // Méthode pour récupérer le message "no_permission"
    public String getNoPermissionMessage() {
        return getMessage("no_permission");
    }

    // Méthode pour récupérer le message "disabled_worlds"
    public String getDisabledWorldsMessage() {
        return getMessage("disabled_worlds");
    }

    // Méthode pour récupérer le message "worlds_disable_to_place"
    public String getWorldsDisableToPlaceMessage() {
        return getMessage("worlds_disable_to_place");
    }

    public String getWorldsAliasClassic() {
        return getMessage("world_alias_classic");
    }

    public String getWorldsAliasNether() {
        return getMessage("world_alias_nether");
    }

    public String getWorldsAliasEnder() {
        return getMessage("world_alias_end");
    }

    public String getWorldsAlias(World world) {
        String worldName = world.getName();
        if (worldName.contains("nether"))
            return getWorldsAliasNether();
        else if (worldName.contains("end"))
            return getWorldsAliasEnder();
        else
            return getWorldsAliasClassic();
    }

    // Méthode pour récupérer le message "place"
    public String getPlaceMessage(String price) {
        return getFormattedMessage("place", "{price}", price);
    }

    // Méthode pour récupérer le message "teleportation"
    public String getTeleportationMessage(String price) {
        return getFormattedMessage("teleportation",  "{price}", price);
    }

    public String getTeleportationCancelledMessage() {
        return getMessage("teleportation_cancelled");
    }
    public String getTeleportationNotEnoughMoney() {
        return getMessage("teleportation_no_enough_money");
    }
    public String getTeleportationPaid(String price) {
        return getFormattedMessage("teleportation_paid", "{price}", price);
    }

    public String getHomeNameMessage(int homeId) {
        return getFormattedMessage("home_name", "{home_id}", Integer.toString(homeId));
    }

    public String getReplaceMessage(String price) {
        return getFormattedMessage("replace", "{price}", price);
    }

    public String getBackMessage() {
        return getMessage("back");
    }

    public String getLockedMessage() {
        return getMessage("locked");
    }

    public String getLocationMessage(String loc) {
        return getFormattedMessage("location", "{loc}", loc);
    }

    public String getWorldMessage(String world) {
        return getFormattedMessage("world", "{world}", world);
    }
}
