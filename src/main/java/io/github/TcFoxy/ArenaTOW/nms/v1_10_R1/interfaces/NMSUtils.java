package io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.interfaces;

import io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.CustomBlueGolem;
import io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.CustomBlueGuardian;
import io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.CustomEntityGuardian;
import io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.CustomEntityIronGolem;
import io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.CustomEntityZombie;
import io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.CustomRedGolem;
import io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.CustomRedGuardian;
import io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.EntityBlueZombie;
import io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.EntityRedZombie;

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
	
	public static CustomEntityIronGolem spawnIronGolem(org.bukkit.World world, double x, double y, double z) {
		WorldServer nms = ((CraftWorld) world).getHandle();
		CustomEntityIronGolem g = new CustomEntityIronGolem(nms);
		g.setPosition(x, y, z);
		nms.addEntity(g, SpawnReason.CUSTOM);
		return g;
		}
	public static CustomEntityZombie spawnZombie(org.bukkit.World world, double x, double y, double z) {
		WorldServer nms = ((CraftWorld) world).getHandle();
		CustomEntityZombie g = new CustomEntityZombie(nms);
		g.setPosition(x, y, z);
		nms.addEntity(g, SpawnReason.CUSTOM);
		return g;
		}
	public static EntityBlueZombie spawnBlueZombie(org.bukkit.World world, double x, double y, double z) {
		WorldServer nms = ((CraftWorld) world).getHandle();
		EntityBlueZombie g = new EntityBlueZombie(nms);
		g.setPosition(x, y, z);
		nms.addEntity(g, SpawnReason.CUSTOM);
		return g;
		}
	public static EntityRedZombie spawnRedZombie(org.bukkit.World world, double x, double y, double z) {
		WorldServer nms = ((CraftWorld) world).getHandle();
		EntityRedZombie g = new EntityRedZombie(nms);
		g.setPosition(x, y, z);
		nms.addEntity(g, SpawnReason.CUSTOM);
		return g;
		}
	public static CustomRedGolem spawnRedGolem(org.bukkit.World world, double x, double y, double z) {
		WorldServer nms = ((CraftWorld) world).getHandle();
		CustomRedGolem g = new CustomRedGolem(nms);
		g.setPosition(x, y, z);
		nms.addEntity(g, SpawnReason.CUSTOM);
		return g;
		}
	public static CustomBlueGolem spawnBlueGolem(org.bukkit.World world, double x, double y, double z) {
		WorldServer nms = ((CraftWorld) world).getHandle();
		CustomBlueGolem g = new CustomBlueGolem(nms);
		g.setPosition(x, y, z);
		nms.addEntity(g, SpawnReason.CUSTOM);
		return g;
		}
	public static CustomEntityGuardian spawnGuardian(org.bukkit.World world, double x, double y, double z) {
		WorldServer nms = ((CraftWorld) world).getHandle();
		CustomEntityGuardian g = new CustomEntityGuardian(nms);
		g.setPosition(x, y, z);
		nms.addEntity(g, SpawnReason.CUSTOM);
		return g;
		}
	
	public static CustomBlueGuardian spawnBlueGuardian(org.bukkit.World world, double x, double y, double z) {
		WorldServer nms = ((CraftWorld) world).getHandle();
		CustomBlueGuardian g = new CustomBlueGuardian(nms);
		g.setPosition(x, y, z);
		nms.addEntity(g, SpawnReason.CUSTOM);
		return g;
		}
	
	public static CustomRedGuardian spawnRedGuardian(org.bukkit.World world, double x, double y, double z) {
		WorldServer nms = ((CraftWorld) world).getHandle();
		CustomRedGuardian g = new CustomRedGuardian(nms);
		g.setPosition(x, y, z);
		nms.addEntity(g, SpawnReason.CUSTOM);
		return g;
		}


	public static Object getPrivateField(String fieldname, Class clazz, Object object){
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