package org.echo.multiplehomes.gui;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.echo.multiplehomes.MultipleHomes;
import org.echo.multiplehomes.Utils;
import org.echo.multiplehomes.config.Home;
import org.echo.multiplehomes.teleport.Teleport;
import org.echo.multiplehomes.teleport.TeleportUtils;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.util.FormImage;

public class BedrockGuiMenu {
    private MultipleHomes main;
    private BedrockGuiListener listener;

    public BedrockGuiMenu(MultipleHomes main, Teleport teleport) {
        this.main = main;
        this.listener = new BedrockGuiListener(main, teleport);
    }

    public SimpleForm.Builder createHomeMenu(Player player, Boolean isTeleport) {
        SimpleForm.Builder form = SimpleForm.builder()
                .title(main.getMessages().getMessage("menu_name"));

        int numberOfHomes = main.getMyConfig().getNumberOfHomes();
        double price = main.getData().getPrice(player.getUniqueId());

        for (int i = 1; i <= numberOfHomes; i++) {
            Home home = main.getData().getHome(player.getUniqueId(), i);
            String homeName = main.getMessages().getHomeNameMessage(i);
            String description;
            String imgURL;

            if (home != null) {
                imgURL = "https://static.wikia.nocookie.net/minecraft_ja_gamepedia/images/f/f7/Lime_Bed.png/revision/latest?cb=20191108012252";

                String moneyMsg;
                if (isTeleport) {
                    double teleportationPrice = TeleportUtils.getTeleportationPrice(main, player.getLocation(), home.getLocation());
                    moneyMsg = main.getMessages().getTeleportationMessage(Double.toString(teleportationPrice));
                } else {
                    moneyMsg = main.getMessages().getReplaceMessage(Double.toString(price));
                }
                description = homeName + "\n" + formatLocationMessage(home.getLocation()) + "\n" + moneyMsg;
            }
            else if (player.hasPermission("multiplehomes.unlock." + i) || player.hasPermission("multiplehomes.unlock.*")) {
                if (isTeleport) {
                    imgURL = "https://static.wikia.nocookie.net/minecraft_ja_gamepedia/images/6/6e/Red_Bed.png/revision/latest?cb=20191108012457";
                    description = homeName + "（開放されていません）";
                } else {
                    imgURL = "https://static.wikia.nocookie.net/minecraft_ja_gamepedia/images/9/9a/Orange_Bed.png/revision/latest?cb=20191108012313";
                    description = homeName + "\n" + main.getMessages().getPlaceMessage(Double.toString(price));
                }
            }
            else {
                if (isTeleport) continue;

                imgURL = "https://static.wikia.nocookie.net/minecraft_ja_gamepedia/images/6/6e/Red_Bed.png/revision/latest?cb=20191108012457";
                description = homeName + "\n" + main.getMessages().getLockedMessage();
            }
            form.button(description, FormImage.Type.URL, imgURL);
        }
        if (isTeleport) {
            form.validResultHandler(response -> this.listener.teleportMenuListener(player, response));
        } else {
            form.validResultHandler(response -> this.listener.replaceMenuListener(player, response));
        }
        return form;
    }

    public SimpleForm.Builder createSelectModeMenu(Player player) {
        SimpleForm.Builder form = SimpleForm.builder()
                .title("モード選択")
                .validResultHandler(response -> listener.selectModeMenuListener(player, response));

        form.button("テレポート", FormImage.Type.URL, "https://static.wikia.nocookie.net/minecraft_ja_gamepedia/images/b/b3/Compass_JE3_BE3.gif/revision/latest");
        form.button("置き換え", FormImage.Type.URL, "https://static.wikia.nocookie.net/minecraft_ja_gamepedia/images/e/e9/Book_and_Quill_JE2_BE2.png/revision/latest/scale-to-width-down/150?cb=20201112121404");

        return form;
    }

    private String formatWorldMessage(World world) {
        String color = null;
        World.Environment env = world.getEnvironment();
        switch (env) {
            case NORMAL:
                color = "§b";
                break;
            case NETHER:
                color = "§4";
                break;
            case THE_END:
                color = "§d";
                break;
            case CUSTOM:
                color = "§f";
                break;
        }

        return color + world.getName();
    }

    private String formatLocationMessage(Location location) {
        String worldMsg = formatWorldMessage(location.getWorld());
        String locationMsg = Utils.formatLocation(location);

        return "§fワールド : " + worldMsg + "§f, 位置 : " + locationMsg;
    }
}
