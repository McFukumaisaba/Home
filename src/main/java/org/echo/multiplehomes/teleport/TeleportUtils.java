package org.echo.multiplehomes.teleport;

import org.bukkit.Location;
import org.echo.multiplehomes.MultipleHomes;

public class TeleportUtils {

    public static double getTeleportationPrice(MultipleHomes plugin, Location playerLoc, Location targetLoc) {

        double pricePerBlockDistance = plugin.getMyConfig().getTeleportationPricePerBlockDistance();
        double blockDistancePerPrice = plugin.getMyConfig().getTeleportationBlockDistancePerPrice();

        double distance = getDistanceBetweenLocations(playerLoc, targetLoc);

        double finalPrice;
        if (blockDistancePerPrice == 0)
            finalPrice = pricePerBlockDistance;
        else {
            finalPrice = pricePerBlockDistance * Math.round(distance / blockDistancePerPrice);
        }

        if (finalPrice < 0)
            return 0;

        finalPrice = Math.round(finalPrice * 100.0) / 100.0;
        return finalPrice;
    }

    public static int getDistanceBetweenLocations(Location loc1, Location loc2) {
        double x1 = loc1.getX();
        double y1 = loc1.getY();
        double z1 = loc1.getZ();

        double x2 = loc2.getX();
        double y2 = loc2.getY();
        double z2 = loc2.getZ();

        double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2));

        int intDistance = (int) distance;

        return intDistance;
    }
}
