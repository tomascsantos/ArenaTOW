package io.github.TcFoxy.ArenaTOW.nms.v1_13_R1.interfaces;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_13_R1.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import io.github.TcFoxy.ArenaTOW.nms.v1_13_R1.MyBlueGolem;
import io.github.TcFoxy.ArenaTOW.nms.v1_13_R1.MyBlueGuardian;
import io.github.TcFoxy.ArenaTOW.nms.v1_13_R1.MyBlueZombie;
import io.github.TcFoxy.ArenaTOW.nms.v1_13_R1.MyEntityGolem;
import io.github.TcFoxy.ArenaTOW.nms.v1_13_R1.MyEntityGuardian;
import io.github.TcFoxy.ArenaTOW.nms.v1_13_R1.MyEntityZombie;
import io.github.TcFoxy.ArenaTOW.nms.v1_13_R1.MyRedGolem;
import io.github.TcFoxy.ArenaTOW.nms.v1_13_R1.MyRedGuardian;
import io.github.TcFoxy.ArenaTOW.nms.v1_13_R1.MyRedZombie;
import net.minecraft.server.v1_13_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_13_R1.WorldServer;
/**
 * author @BigTeddy98
 * Used for tutorial purposes
 * https://forums.bukkit.org/threads/tutorial-register-your-custom-entities-nms-reflection.258542/
 */

public class NMSUtils {

	public static MyEntityZombie spawnZombie(World world, double x, double y, double z) {
		WorldServer nms = ((CraftWorld) world).getHandle();
		MyEntityZombie g = new MyEntityZombie(nms);
		g.setPosition(x, y, z);
		nms.addEntity(g, SpawnReason.CUSTOM);
		return g;
	}
	public static MyEntityZombie spawnTeamZombie(org.bukkit.World world, double x, double y, double z, Color col) {
		WorldServer nms = ((CraftWorld) world).getHandle();
		if(col.equals(Color.RED)){
			MyRedZombie g = new MyRedZombie(nms);
			g.setPosition(x, y, z);
			nms.addEntity(g, SpawnReason.CUSTOM);
			return g;
		}else if (col.equals(Color.BLUE)){
			MyBlueZombie g = new MyBlueZombie(nms);
			g.setPosition(x, y, z);
			nms.addEntity(g, SpawnReason.CUSTOM);
			return g;
		}else{
			Bukkit.broadcastMessage("ERROR, NMSUtils spawnTeamZombie() invalid color");
			return null;
		}
	}

	public static MyEntityGolem spawnGolem(org.bukkit.World world, double x, double y, double z) {
		WorldServer nms = ((CraftWorld) world).getHandle();
		MyEntityGolem g = new MyEntityGolem(nms);
		g.setPosition(x, y, z);
		nms.addEntity(g, SpawnReason.CUSTOM);
		return g;
	}
	
	public static MyEntityGolem spawnTeamGolem(org.bukkit.World world, double x, double y, double z, Color col) {
		WorldServer nms = ((CraftWorld) world).getHandle();
		if(col.equals(Color.BLUE)){
			MyBlueGolem g = new MyBlueGolem(nms);
			g.setPosition(x, y, z);
			nms.addEntity(g, SpawnReason.CUSTOM);
			return g;
		}else if(col.equals(Color.RED)){
			MyRedGolem g = new MyRedGolem(nms);
			g.setPosition(x, y, z);
			nms.addEntity(g, SpawnReason.CUSTOM);
			return g;
		}else{
			Bukkit.broadcastMessage("ERROR, NMSUtils spawnTeamGolem() invalid color");
			return null;
		}
		
	}
	public static MyEntityGuardian spawnGuardian(org.bukkit.World world, double x, double y, double z) {
		WorldServer nms = ((CraftWorld) world).getHandle();
		MyEntityGuardian g = new MyEntityGuardian(nms);
		g.setPosition(x, y, z);
		nms.addEntity(g, SpawnReason.CUSTOM);
		return g;
	}

	public static MyEntityGuardian spawnTeamGuardian(org.bukkit.World world, double x, double y, double z, Color col) {
		WorldServer nms = ((CraftWorld) world).getHandle();
		if(col.equals(Color.RED)){
			MyRedGuardian g = new MyRedGuardian(nms);
			g.setPosition(x, y, z);
			nms.addEntity(g, SpawnReason.CUSTOM);
			return g;
		}else if (col.equals(Color.BLUE)){
			MyBlueGuardian g = new MyBlueGuardian(nms);
			g.setPosition(x, y, z);
			nms.addEntity(g, SpawnReason.CUSTOM);
			return g;
		}else{
			Bukkit.broadcastMessage("ERROR, NMSUtils spawnTeamGuardian() invalid color");
			return null;
		}
		
	}

	@SuppressWarnings("rawtypes")
	public static void clearBehavior(PathfinderGoalSelector goalSelector, PathfinderGoalSelector targetSelector){
		LinkedHashSet goalB = (LinkedHashSet)NMSUtils.getPrivateField("b", PathfinderGoalSelector.class, goalSelector); goalB.clear();
		LinkedHashSet goalC = (LinkedHashSet)NMSUtils.getPrivateField("c", PathfinderGoalSelector.class, goalSelector); goalC.clear();
		LinkedHashSet targetB = (LinkedHashSet)NMSUtils.getPrivateField("b", PathfinderGoalSelector.class, targetSelector); targetB.clear();
		LinkedHashSet targetC = (LinkedHashSet)NMSUtils.getPrivateField("c", PathfinderGoalSelector.class, targetSelector); targetC.clear();
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