package io.github.TcFoxy.ArenaTOW;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_10_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_10_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_10_R1.PacketPlayOutTitle.EnumTitleAction;
import net.minecraft.server.v1_10_R1.PlayerConnection;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.Vector;

public class Utils {
	//Minecraft Ticks Per Second
	public static final int TPS = 20;
	
	
	public static net.minecraft.server.v1_10_R1.ItemStack makeMobHelm(String color){
		ItemStack helm = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta meta = (LeatherArmorMeta) helm.getItemMeta();
		if(color.equals("Blue")){
			meta.setColor(Color.BLUE);
		}else if (color.equals("Red")){
			meta.setColor(Color.RED);
		}else if (color.equals("Black")){
			meta.setColor(Color.BLACK);
		}
		helm.setItemMeta(meta);
		net.minecraft.server.v1_10_R1.ItemStack nms = org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack.asNMSCopy(helm);
		return nms;
	}
	
	
	public static Block getTargetBlock(Player player, int range) {
	    Location loc = player.getEyeLocation();
	    Vector dir = loc.getDirection().normalize();
	 
	    Block b = null;
	 
	    for (int i = 0; i <= range; i++) {
	        b = loc.add(dir).getBlock();
	        if(b.getType() != Material.AIR){
	        	return b;
	        }
	    }
	 
	    return b;
	}
	
	public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
	    PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;

	    PacketPlayOutTitle packetPlayOutTimes = new PacketPlayOutTitle(EnumTitleAction.TIMES, null, fadeIn.intValue(), stay.intValue(), fadeOut.intValue());
	    connection.sendPacket(packetPlayOutTimes);

	    if (subtitle != null) {
	      subtitle = subtitle.replaceAll("%player%", player.getDisplayName());
	      subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
	      IChatBaseComponent titleSub = ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
	      PacketPlayOutTitle packetPlayOutSubTitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, titleSub);
	      connection.sendPacket(packetPlayOutSubTitle);
	    }

	    if (title != null) {
	      title = title.replaceAll("%player%", player.getDisplayName());
	      title = ChatColor.translateAlternateColorCodes('&', title);
	      IChatBaseComponent titleMain = ChatSerializer.a("{\"text\": \"" + title + "\"}");
	      PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(EnumTitleAction.TITLE, titleMain);
	      connection.sendPacket(packetPlayOutTitle);
	    }
	  }

	  public static void sendTabTitle(Player player, String header, String footer) {
	    if (header == null) header = "";
	    header = ChatColor.translateAlternateColorCodes('&', header);

	    if (footer == null) footer = "";
	    footer = ChatColor.translateAlternateColorCodes('&', footer);

	    header = header.replaceAll("%player%", player.getDisplayName());
	    footer = footer.replaceAll("%player%", player.getDisplayName());

	    PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
	    IChatBaseComponent tabTitle = ChatSerializer.a("{\"text\": \"" + header + "\"}");
	    IChatBaseComponent tabFoot = ChatSerializer.a("{\"text\": \"" + footer + "\"}");
	    PacketPlayOutPlayerListHeaderFooter headerPacket = new PacketPlayOutPlayerListHeaderFooter(tabTitle);
	    try
	    {
	      Field field = headerPacket.getClass().getDeclaredField("b");
	      field.setAccessible(true);
	      field.set(headerPacket, tabFoot);
	    } catch (Exception e) {
	      e.printStackTrace();
	    } finally {
	      connection.sendPacket(headerPacket);
	    }
	  }
	  
	  public static Set<Location> circle(Location location, int radius, boolean hollow){
	        Set<Location> blocks = new HashSet<Location>();
	        World world = location.getWorld();
	        int X = location.getBlockX();
	        int Y = location.getBlockY();
	        int Z = location.getBlockZ();
	        int radiusSquared = radius * radius;
	 
	        if(hollow){
	            for (int x = X - radius; x <= X + radius; x++) {
	                for (int z = Z - radius; z <= Z + radius; z++) {
	                    if ((X - x) * (X - x) + (Z - z) * (Z - z) <= radiusSquared) {
	                        Location block = new Location(world, x, Y, z);
	                        blocks.add(block);
	                    }
	                }
	            }
	            return makeHollow(blocks, false);
	        } else {
	            for (int x = X - radius; x <= X + radius; x++) {
	                for (int z = Z - radius; z <= Z + radius; z++) {
	                    if ((X - x) * (X - x) + (Z - z) * (Z - z) <= radiusSquared) {
	                        Location block = new Location(world, x, Y, z);
	                        blocks.add(block);
	                    }
	                }
	            }
	            return blocks;
	        }
	    }
	  
	  private static Set<Location> makeHollow(Set<Location> blocks, boolean sphere){
	        Set<Location> edge = new HashSet<Location>();
	        if(!sphere){
	            for(Location l : blocks){
	                World w = l.getWorld();
	                int X = l.getBlockX();
	                int Y = l.getBlockY();
	                int Z = l.getBlockZ();
	                Location front = new Location(w, X + 1, Y, Z);
	                Location back = new Location(w, X - 1, Y, Z);
	                Location left = new Location(w, X, Y, Z + 1);
	                Location right = new Location(w, X, Y, Z - 1);
	                if(!(blocks.contains(front) && blocks.contains(back) && blocks.contains(left) && blocks.contains(right))){
	                    edge.add(l);
	                }
	            }
	            return edge;
	        } else {
	            for(Location l : blocks){
	                World w = l.getWorld();
	                int X = l.getBlockX();
	                int Y = l.getBlockY();
	                int Z = l.getBlockZ();
	                Location front = new Location(w, X + 1, Y, Z);
	                Location back = new Location(w, X - 1, Y, Z);
	                Location left = new Location(w, X, Y, Z + 1);
	                Location right = new Location(w, X, Y, Z - 1);
	                Location top = new Location(w, X, Y + 1, Z);
	                Location bottom = new Location(w, X, Y - 1, Z);
	                if(!(blocks.contains(front) && blocks.contains(back) && blocks.contains(left) && blocks.contains(right) && blocks.contains(top) && blocks.contains(bottom))){
	                    edge.add(l);
	                }
	            }
	            return edge;
	        }
	    }
}
