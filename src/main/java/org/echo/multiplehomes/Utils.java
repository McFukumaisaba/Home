package org.echo.multiplehomes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;

public class Utils {

    public static int getInvetorySize(int numberOfItem) {
        int size = 9;
        while (numberOfItem > size) {
            size += 9;
        }
        return size;
    }

    public static Inventory createInventory(int size, String inventoryName) {
        return Bukkit.createInventory(null, size, inventoryName);
    }

    public static String formatLocation(Location location) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        return String.format("%d %d %d", x, y, z);
    }

    public static Location parseLocationFromString(World world, String locationString) {
        String[] parts = locationString.split(", ");

        String xString = parts[0].substring(2); // Extrait la valeur de X en supprimant le préfixe "X:"
        String yString = parts[1].substring(2); // Extrait la valeur de Y en supprimant le préfixe "Y:"
        String zString = parts[2].substring(2); // Extrait la valeur de Z en supprimant le préfixe "Z:"

        double x = Double.parseDouble(xString);
        double y = Double.parseDouble(yString);
        double z = Double.parseDouble(zString);

        // Vous pouvez également extraire le monde et d'autres paramètres si nécessaire

        // Retourne une nouvelle instance de Location
        return new Location(world, x, y, z);
    }

}
