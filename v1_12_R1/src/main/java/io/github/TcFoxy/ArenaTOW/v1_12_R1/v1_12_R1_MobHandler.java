package io.github.TcFoxy.ArenaTOW.v1_12_R1;

import io.github.TcFoxy.ArenaTOW.API.MobType;
import io.github.TcFoxy.ArenaTOW.API.TOWEntity;
import io.github.TcFoxy.ArenaTOW.API.TOWEntityHandler;
import net.minecraft.server.v1_12_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_12_R1.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * author @BigTeddy98
 * Used for tutorial purposes
 * https://forums.bukkit.org/threads/tutorial-register-your-custom-entities-nms-reflection.258542/
 */

class v1_12_R1_MobHandler implements TOWEntityHandler{

	private HashSet<TOWEntity> entities;
	private HashSet<TOWEntity> players;

	public v1_12_R1_MobHandler() {
		entities = new HashSet<>();
	}

	@Override
	public HashSet<TOWEntity> getMobs() {
		return entities;
	}

    @Override
    public HashSet<TOWEntity> getPlayers() {
        return players;
    }

    @Override
    public void addPlayer(TOWEntity e) {
        players.add(e);
    }

    @Override
	public TOWEntity spawnMob(MobType mobType, Color teamColor, World world, double x, double y, double z) {
	    TOWEntity entity = null;
		switch (mobType) {
            case ZOMBIE: entity = spawnTeamZombie(world, x, y, z, teamColor);
            break;
            case NEXUS: entity = spawnTeamGuardian(world, x, y, z, teamColor);
            break;
            case TOWER: entity = spawnTeamGolem(world, x, y, z, teamColor);
        }
        if (entity != null) {
            entities.add(entity);
        }
        return entity;
	}

    private static MyEntityZombie spawnTeamZombie(World world, double x, double y, double z, Color col) {
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
			Bukkit.broadcastMessage("ERROR, v1_12_R1_MobHandler spawnTeamZombie() invalid color");
			return null;
		}
	}

	private static MyEntityGolem spawnTeamGolem(World world, double x, double y, double z, Color col) {
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
			Bukkit.broadcastMessage("ERROR, v1_12_R1_MobHandler spawnTeamGolem() invalid color");
			return null;
		}

	}

	 private static MyEntityGuardian spawnTeamGuardian(World world, double x, double y, double z, Color col) {
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
			Bukkit.broadcastMessage("ERROR, v1_12_R1_MobHandler spawnTeamGuardian() invalid color");
			return null;
		}
		
	}

	@SuppressWarnings("rawtypes")
	static void clearBehavior(PathfinderGoalSelector goalSelector, PathfinderGoalSelector targetSelector){
		LinkedHashSet goalB = (LinkedHashSet) v1_12_R1_MobHandler.getPrivateField("b", PathfinderGoalSelector.class, goalSelector); goalB.clear();
		LinkedHashSet goalC = (LinkedHashSet) v1_12_R1_MobHandler.getPrivateField("c", PathfinderGoalSelector.class, goalSelector); goalC.clear();
		LinkedHashSet targetB = (LinkedHashSet) v1_12_R1_MobHandler.getPrivateField("b", PathfinderGoalSelector.class, targetSelector); targetB.clear();
        LinkedHashSet targetC = (LinkedHashSet) v1_12_R1_MobHandler.getPrivateField("c", PathfinderGoalSelector.class, targetSelector); targetC.clear();
	}

	private static Object getPrivateField(String fieldname, @SuppressWarnings("rawtypes") Class clazz, Object object){
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