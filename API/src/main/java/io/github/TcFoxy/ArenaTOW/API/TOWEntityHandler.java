package io.github.TcFoxy.ArenaTOW.API;

import org.bukkit.World;

import org.bukkit.Color;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public interface TOWEntityHandler {

    /**
     * retrieve the set of mobs in the arena
     * @return mobs
     */
    Collection<TOWEntity> getMobs();

    default void killMobs() {
        Collection<TOWEntity> entities = getMobs();
        for (TOWEntity e : entities) {
            if (e.getMobType() != MobType.PLAYER) {
                e.setHealth(0);
            }
            entities.remove(e);
        }
    }

    /**
     * Takes in an object and returns a tow entity if possible
     * otherwise returns null
     * @param o
     * @return
     */
    TOWEntity getTowEntity(Object o);

    void addEntity(TOWEntity e);

    TOWEntity spawnMob(TOWEntityHandler handler, MobType mobType, Color teamColor, World world, double x, double y, double z);

    default boolean areSameTeam(TOWEntity e1, TOWEntity e2) {
        if (!e1.getTeam().equals(e2.getTeam())) {
            System.out.printf("Team1: %-20s | Team2: %-20s\n", e1.getTeam(), e2.getTeam());
            System.out.printf("Class1: %s | Class2: %s | %s", e1.getClass(), e2.getClass(), e1.getTeam().equals(e2.getTeam()));
        }
        return e1.getTeam().equals(e2.getTeam());
    }

    default boolean areSameTeam(Object o1, Object o2) {
        if (getTowEntity(o1) == null || getTowEntity(o2) == null) return false;
        return areSameTeam(getTowEntity(o1), getTowEntity(o2));
    }

    default boolean areSameTeam(TOWEntity t1, Object o1) {
        if (getTowEntity(o1) == null) return false;
        return areSameTeam(t1, getTowEntity(o1));
    }

}
