package io.github.TcFoxy.ArenaTOW.v1_10_R1;

import io.github.TcFoxy.ArenaTOW.API.*;
import net.minecraft.server.v1_10_R1.EntityLiving;
import net.minecraft.server.v1_10_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_10_R1.WorldServer;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import java.lang.reflect.Field;
import java.util.*;

/**
 * author @BigTeddy98
 * Used for tutorial purposes
 * https://forums.bukkit.org/threads/tutorial-register-your-custom-entities-nms-reflection.258542/
 */

class v1_10_R1_MobHandler implements TOWEntityHandler {

    private HashMap<UUID, TOWEntity> towEntities;

    public v1_10_R1_MobHandler() {
        towEntities = new HashMap<>();
    }

    @Override
    public Collection<TOWEntity> getMobs() {
        return towEntities.values();
    }

    @Override
    public void addEntity(TOWEntity e) {
        towEntities.put(e.getUID(), e);
    }


    @Override
    public TOWEntity getTowEntity(Object o) {
        if (o instanceof EntityLiving) {
            return towEntities.get(((EntityLiving) o).getUniqueID());
        }
        return null;
    }

    @Override
    public TOWEntity spawnMob(TOWEntityHandler handler, MobType mobType, Color teamColor, World world, double x, double y, double z) {
        TOWEntity entity = null;
        switch (mobType) {
            case ZOMBIE:
                entity = spawnTeamZombie(handler, world, x, y, z, teamColor);
                break;
            case NEXUS:
                entity = spawnTeamGuardian(handler, world, x, y, z, teamColor);
                break;
            case TOWER:
                entity = spawnTeamGolem(handler, world, x, y, z, teamColor);
        }
        if (entity != null) {
            addEntity(entity);
        }
        return entity;
    }

    private static MyEntityZombie spawnTeamZombie(TOWEntityHandler handler, World world, double x, double y, double z, Color col) {
        WorldServer nms = ((CraftWorld) world).getHandle();
        if (col.equals(Color.RED)) {
            MyRedZombie g = new MyRedZombie(nms, handler);
            g.setPosition(x, y, z);
            nms.addEntity(g, SpawnReason.CUSTOM);
            return g;
        } else if (col.equals(Color.BLUE)) {
            MyBlueZombie g = new MyBlueZombie(nms, handler);
            g.setPosition(x, y, z);
            nms.addEntity(g, SpawnReason.CUSTOM);
            return g;
        } else {
            Bukkit.broadcastMessage("ERROR, v1_10_R1_MobHandler spawnTeamZombie() invalid color");
            return null;
        }
    }

    private static MyEntityGolem spawnTeamGolem(TOWEntityHandler handler, World world, double x, double y, double z, Color col) {
        WorldServer nms = ((CraftWorld) world).getHandle();
        if (col.equals(Color.BLUE)) {
            MyBlueGolem g = new MyBlueGolem(nms, handler);
            g.setPosition(x, y, z);
            nms.addEntity(g, SpawnReason.CUSTOM);
            return g;
        } else if (col.equals(Color.RED)) {
            MyRedGolem g = new MyRedGolem(nms, handler);
            g.setPosition(x, y, z);
            nms.addEntity(g, SpawnReason.CUSTOM);
            return g;
        } else {
            Bukkit.broadcastMessage("ERROR, v1_10_R1_MobHandler spawnTeamGolem() invalid color");
            return null;
        }

    }

    private static MyEntityGuardian spawnTeamGuardian(TOWEntityHandler handler, World world, double x, double y, double z, Color col) {
        WorldServer nms = ((CraftWorld) world).getHandle();
        if (col.equals(Color.RED)) {
            MyRedGuardian g = new MyRedGuardian(nms, handler);
            g.setPosition(x, y, z);
            nms.addEntity(g, SpawnReason.CUSTOM);
            return g;
        } else if (col.equals(Color.BLUE)) {
            MyBlueGuardian g = new MyBlueGuardian(nms, handler);
            g.setPosition(x, y, z);
            nms.addEntity(g, SpawnReason.CUSTOM);
            return g;
        } else {
            Bukkit.broadcastMessage("ERROR, v1_10_R1_MobHandler spawnTeamGuardian() invalid color");
            return null;
        }

    }

    @SuppressWarnings("rawtypes")
    static void clearBehavior(PathfinderGoalSelector goalSelector, PathfinderGoalSelector targetSelector) {
        LinkedHashSet goalB = (LinkedHashSet) v1_10_R1_MobHandler.getPrivateField("b", PathfinderGoalSelector.class, goalSelector);
        goalB.clear();
        LinkedHashSet goalC = (LinkedHashSet) v1_10_R1_MobHandler.getPrivateField("c", PathfinderGoalSelector.class, goalSelector);
        goalC.clear();
        LinkedHashSet targetB = (LinkedHashSet) v1_10_R1_MobHandler.getPrivateField("b", PathfinderGoalSelector.class, targetSelector);
        targetB.clear();
        LinkedHashSet targetC = (LinkedHashSet) v1_10_R1_MobHandler.getPrivateField("c", PathfinderGoalSelector.class, targetSelector);
        targetC.clear();
    }

    private static Object getPrivateField(String fieldname, @SuppressWarnings("rawtypes") Class clazz, Object object) {
        Field field;
        Object o = null;

        try {
            field = clazz.getDeclaredField(fieldname);
            field.setAccessible(true);
            o = field.get(object);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return o;

    }


}