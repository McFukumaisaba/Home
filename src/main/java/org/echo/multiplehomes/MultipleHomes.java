package org.echo.multiplehomes;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.echo.multiplehomes.commads.Commands;
import org.echo.multiplehomes.config.Config;
import org.echo.multiplehomes.config.Data;
import org.echo.multiplehomes.config.Messages;
import org.echo.multiplehomes.gui.BedrockGuiMenu;
import org.echo.multiplehomes.gui.GuiListener;
import org.echo.multiplehomes.gui.GuiMenu;
import org.echo.multiplehomes.teleport.Teleport;
import org.echo.multiplehomes.teleport.TeleportListener;

import java.io.File;

public final class MultipleHomes extends JavaPlugin {

        private Messages messages;
        private Config config;
        private Data data;
        private GuiMenu guiMenu;
        private BedrockGuiMenu bedrockGuiMenu;
        private Teleport teleport;

        private Economy eco;

        @Override
        public void onEnable() {

            config = new Config(this);

            if (config.isEconomyEnable()) {
                if (!setupEconomy()) {
                    this.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[MultipleHomes] WARN: Payments are disabled because you must have Vault and an economy plugin like EssentialX");
                    this.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[MultipleHomes] For disable economy, set '§ceconomy_enable: false§e' in config.yml and  remove all '§c{price}§e' in message.yml");
                }
            }

            messages = new Messages(this);

            // Initialisation des données des homes des joueurs
            data = new Data(this);

            guiMenu = new GuiMenu(this);

            teleport = new Teleport(this);

            bedrockGuiMenu = new BedrockGuiMenu(this, teleport);

            getServer().getPluginManager().registerEvents(new GuiListener(this, teleport), this);
            getServer().getPluginManager().registerEvents(new TeleportListener(this, teleport), this);

            getCommand("mh").setExecutor(new Commands(this));
            getCommand("home").setExecutor(new Commands(this));
            getCommand("homes").setExecutor(new Commands(this));
        }

        @Override
        public void onDisable() {
            data.savePlayerHomes();
        }

        public void reloadPlugin() {
            data.savePlayerHomes();
            messages = new Messages(this);
            config = new Config(this);
            data = new Data(this);
            teleport = new Teleport(this);
        }

        private boolean setupEconomy() {
            if (getServer().getPluginManager().getPlugin("Vault") == null) {
                this.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[MultipleHomes] Vault plugin not found");
                return false;
            }
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp == null) {
                this.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[MultipleHomes] Economy plugin not found");
                return false;
            }
            eco = rsp.getProvider();
            return eco != null;
        }

        public File getDirectory() {
        return getDataFolder();
    }

        // Méthode pour récupérer l'instance de la classe Messages
        public Messages getMessages() {
            return messages;
        }

        // Méthode pour récupérer l'instance de la classe PluginConfig
        public Config getMyConfig() {
            return this.config;
        }

        // Méthode pour récupérer l'instance de la classe Data
        public Data getData() {
            return data;
        }

        public GuiMenu getGuiMenu() {
            return guiMenu;
        }

        public BedrockGuiMenu getBedrockGuiMenu() {
            return bedrockGuiMenu;
        }

        public Economy getEconomy() {
            return eco;
        }
    }
