package io.github.TcFoxy.ArenaTOW;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle.EnumTitleAction;
import net.minecraft.server.v1_12_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_12_R1.PlayerConnection;

public class Utils {
	// Minecraft Ticks Per Second
	public static final int TPS = 20;

	public static ItemStack makeMobHelm(Color color) {
		ItemStack helm = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta meta = (LeatherArmorMeta) helm.getItemMeta();
		meta.setColor(color);
		helm.setItemMeta(meta);
		return helm;
	}

	/**
	 * 
	 * @param Color
	 *            col
	 * @return String color in simple english
	 */
	public static String toSimpleColor(Color col) {
		if (col == Color.BLUE) {
			return "Blue";
		} else if (col == Color.RED) {
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

	/*
	 * For nms fields:
	 */
	public static Object getPrivateField(String fieldName, Class<PathfinderGoalSelector> clazz, Object object) {
		Field field;
		Object o = null;
		try {
			field = clazz.getDeclaredField(fieldName);

			field.setAccessible(true);

			o = field.get(object);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return o;
	}

	public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title,
			String subtitle) {
		// TitleSendEvent titleSendEvent = new TitleSendEvent(player, title,
		// subtitle);
		// Bukkit.getPluginManager().callEvent(titleSendEvent);
		// if (titleSendEvent.isCancelled()) {
		// return;
		// }
		try {
			if (title != null) {
				title = ChatColor.translateAlternateColorCodes('&', title);
				title = title.replaceAll("%player%", player.getDisplayName());

				Object e = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);
				Object chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
						.getMethod("a", new Class[] { String.class })
						.invoke(null, new Object[] { "{\"text\":\"" + title + "\"}" });
				Constructor subtitleConstructor = getNMSClass("PacketPlayOutTitle")
						.getConstructor(new Class[] { getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
								getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE });
				Object titlePacket = subtitleConstructor
						.newInstance(new Object[] { e, chatTitle, fadeIn, stay, fadeOut });
				sendPacket(player, titlePacket);

				e = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
				chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
						.getMethod("a", new Class[] { String.class })
						.invoke(null, new Object[] { "{\"text\":\"" + title + "\"}" });
				subtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(new Class[] {
						getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent") });
				titlePacket = subtitleConstructor.newInstance(new Object[] { e, chatTitle });
				sendPacket(player, titlePacket);
			}
			if (subtitle != null) {
				subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
				subtitle = subtitle.replaceAll("%player%", player.getDisplayName());

				Object e = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);
				Object chatSubtitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
						.getMethod("a", new Class[] { String.class })
						.invoke(null, new Object[] { "{\"text\":\"" + title + "\"}" });
				Constructor subtitleConstructor = getNMSClass("PacketPlayOutTitle")
						.getConstructor(new Class[] { getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
								getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE });
				Object subtitlePacket = subtitleConstructor
						.newInstance(new Object[] { e, chatSubtitle, fadeIn, stay, fadeOut });
				sendPacket(player, subtitlePacket);

				e = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
				chatSubtitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
						.getMethod("a", new Class[] { String.class })
						.invoke(null, new Object[] { "{\"text\":\"" + subtitle + "\"}" });
				subtitleConstructor = getNMSClass("PacketPlayOutTitle")
						.getConstructor(new Class[] { getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
								getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE });
				subtitlePacket = subtitleConstructor
						.newInstance(new Object[] { e, chatSubtitle, fadeIn, stay, fadeOut });
				sendPacket(player, subtitlePacket);
			}
		} catch (Exception var11) {
			var11.printStackTrace();
		}
	}

	public static void sendPacket(Player player, Object packet) {
		try {
			Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
			Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", new Class[] { getNMSClass("Packet") })
					.invoke(playerConnection, new Object[] { packet });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Class<?> getNMSClass(String name) {
		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		try {
			return Class.forName("net.minecraft.server." + version + "." + name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void sendTabTitle(Player player, String header, String footer) {

	}

	public static Set<Location> circle(Location location, int radius, boolean hollow) {
		Set<Location> blocks = new HashSet<Location>();
		World world = location.getWorld();
		int X = location.getBlockX();
		int Y = location.getBlockY();
		int Z = location.getBlockZ();
		int radiusSquared = radius * radius;

		if (hollow) {
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

	private static Set<Location> makeHollow(Set<Location> blocks, boolean sphere) {
		Set<Location> edge = new HashSet<Location>();
		if (!sphere) {
			for (Location l : blocks) {
				World w = l.getWorld();
				int X = l.getBlockX();
				int Y = l.getBlockY();
				int Z = l.getBlockZ();
				Location front = new Location(w, X + 1, Y, Z);
				Location back = new Location(w, X - 1, Y, Z);
				Location left = new Location(w, X, Y, Z + 1);
				Location right = new Location(w, X, Y, Z - 1);
				if (!(blocks.contains(front) && blocks.contains(back) && blocks.contains(left)
						&& blocks.contains(right))) {
					edge.add(l);
				}
			}
			return edge;
		} else {
			for (Location l : blocks) {
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
				if (!(blocks.contains(front) && blocks.contains(back) && blocks.contains(left) && blocks.contains(right)
						&& blocks.contains(top) && blocks.contains(bottom))) {
					edge.add(l);
				}
			}
			return edge;
		}
	}
}
