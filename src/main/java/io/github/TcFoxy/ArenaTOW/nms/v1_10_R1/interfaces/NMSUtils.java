package io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.interfaces;

import io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.MyBlueGolem;
import io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.MyBlueGuardian;
import io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.MyBlueZombie;
import io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.MyEntityGuardian;
import io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.MyEntityIronGolem;
import io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.MyEntityZombie;
import io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.MyRedGolem;
import io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.MyRedGuardian;
import io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.MyRedZombie;

import java.lang.reflect.Field;

import net.minecraft.server.v1_10_R1.WorldServer;

import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
/**
 * author @BigTeddy98
 * Used for tutorial purposes
 * https://forums.bukkit.org/threads/tutorial-register-your-custom-entities-nms-reflection.258542/
 */

public class NMSUtils {

	public static MyEntityIronGolem spawnIronGolem(org.bukkit.World world, double x, double y, double z) {
		WorldServer nms = ((CraftWorld) world).getHandle();
		MyEntityIronGolem g = new MyEntityIronGolem(nms);
		g.setPosition(x, y, z);
		nms.addEntity(g, SpawnReason.CUSTOM);
		return g;
	}
	public static MyEntityZombie spawnZombie(org.bukkit.World world, double x, double y, double z) {
		WorldServer nms = ((CraftWorld) world).getHandle();
		MyEntityZombie g = new MyEntityZombie(nms);
		g.setPosition(x, y, z);
		nms.addEntity(g, SpawnReason.CUSTOM);
		return g;
	}
	public static MyBlueZombie spawnBlueZombie(org.bukkit.World world, double x, double y, double z) {
		WorldServer nms = ((CraftWorld) world).getHandle();
		MyBlueZombie g = new MyBlueZombie(nms);
		g.setPosition(x, y, z);
		nms.addEntity(g, SpawnReason.CUSTOM);
		return g;
	}
	public static MyRedZombie spawnRedZombie(org.bukkit.World world, double x, double y, double z) {
		WorldServer nms = ((CraftWorld) world).getHandle();
		MyRedZombie g = new MyRedZombie(nms);
		g.setPosition(x, y, z);
		nms.addEntity(g, SpawnReason.CUSTOM);
		return g;
	}
	public static MyRedGolem spawnRedGolem(org.bukkit.World world, double x, double y, double z) {
		WorldServer nms = ((CraftWorld) world).getHandle();
		MyRedGolem g = new MyRedGolem(nms);
		g.setPosition(x, y, z);
		nms.addEntity(g, SpawnReason.CUSTOM);
		return g;
	}
	public static MyBlueGolem spawnBlueGolem(org.bukkit.World world, double x, double y, double z) {
		WorldServer nms = ((CraftWorld) world).getHandle();
		MyBlueGolem g = new MyBlueGolem(nms);
		g.setPosition(x, y, z);
		nms.addEntity(g, SpawnReason.CUSTOM);
		return g;
	}
	public static MyEntityGuardian spawnGuardian(org.bukkit.World world, double x, double y, double z) {
		WorldServer nms = ((CraftWorld) world).getHandle();
		MyEntityGuardian g = new MyEntityGuardian(nms);
		g.setPosition(x, y, z);
		nms.addEntity(g, SpawnReason.CUSTOM);
		return g;
	}

	public static MyBlueGuardian spawnBlueGuardian(org.bukkit.World world, double x, double y, double z) {
		WorldServer nms = ((CraftWorld) world).getHandle();
		MyBlueGuardian g = new MyBlueGuardian(nms);
		g.setPosition(x, y, z);
		nms.addEntity(g, SpawnReason.CUSTOM);
		return g;
	}

	public static MyRedGuardian spawnRedGuardian(org.bukkit.World world, double x, double y, double z) {
		WorldServer nms = ((CraftWorld) world).getHandle();
		MyRedGuardian g = new MyRedGuardian(nms);
		g.setPosition(x, y, z);
		nms.addEntity(g, SpawnReason.CUSTOM);
		return g;
	}


	public static Object getPrivateField(String fieldname, @SuppressWarnings("rawtypes") Class clazz, Object object){
		Field field;
		Object o = null;

		try{
			field = clazz.getDeclaredField(fieldname);
			field.setAccessible(true);
			o = field.get(object);
		}
		catch(NoSuchFieldException e){
			e.printStackTrace();
		}
		catch(IllegalAccessException e){
			e.printStackTrace();
		}
		return o;

	}
}