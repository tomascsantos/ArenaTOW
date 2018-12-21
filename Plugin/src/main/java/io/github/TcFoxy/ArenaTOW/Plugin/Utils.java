package io.github.TcFoxy.ArenaTOW.Plugin;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.Vector;

import static org.bukkit.Color.BLUE;
import static org.bukkit.Color.RED;

public class Utils {
    //Minecraft Ticks Per Second
    public static final int TPS = 20;


    public static ItemStack makeMobHelm(Color color) {
        ItemStack helm = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta meta = (LeatherArmorMeta) helm.getItemMeta();
        meta.setColor(color);
        helm.setItemMeta(meta);
        return helm;
    }

    /**
     * @param col
     * @return String color in simple english
     */
    public static String toSimpleColor(Color col) {
        if (col == Color.BLUE) {
            return "Blue";
        } else if (col == RED) {
            return "Red";
        } else {
            Bukkit.getLogger().severe("Invalid color toSimpleColorString");
            return null;
        }
    }


    public static Block getTargetBlock(Player player, int range) {
        Location loc = player.getEyeLocation();
        Vector dir = loc.getDirection().normalize();

        Block b = null;

        for (int i = 0; i <= range; i++) {
            b = loc.add(dir).getBlock();
            if (b.getType() != Material.AIR) {
                return b;
            }
        }

        return b;
    }

    static String redTeam = "Red", blueTeam = "Blue", noteam = "No Team";

    static String getTeamName(int i) throws IllegalArgumentException{
        switch (i) {
            case 0: return redTeam;
            case 1: return blueTeam;
            default: throw new IllegalArgumentException();
        }
    }

    //No switch statement because Color enum is mutable.
    static String getTeamName(Color col) throws IllegalArgumentException{
        if (col == Color.RED) {
            return redTeam;
        } else if (col == Color.BLUE) {
            return blueTeam;
        } else {
            throw new IllegalArgumentException();
        }
    }

    static Color getTeamColor(String string) throws IllegalArgumentException{
        if (string.equals(redTeam)) {
            return Color.RED;
        } else if (string.equals(blueTeam)) {
            return  Color.BLUE;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
