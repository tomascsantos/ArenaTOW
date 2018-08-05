package io.github.TcFoxy.ArenaTOW.API;

import org.bukkit.World;

import java.awt.*;
import java.util.HashSet;
import java.util.TreeSet;

public interface TOWEntityHandler {

    /**
     * retrieve the set of mobs in the arena
     * @return mobs
     */
    HashSet<TOWEntity> getMobs();

    /**
     * retrieve the set of arenaplayers
     */
    HashSet<TOWEntity> getPlayers();

    default void killMobs() {
        HashSet<TOWEntity> entities = getMobs();
        for (TOWEntity e : entities) {
            e.setHealth(0);
            entities.remove(e);
        }
    }

    default boolean areSameTeam(TOWEntity e1, TOWEntity e2) {
        return e1.getTeam().equals(e2.getTeam());
    }

    TOWEntity spawnMob(MobType mobType, Color teamColor, World world, double x, double y, double z);

    /**
     * add arena players to the set of players
     */
    void addPlayer(TOWEntity e);

}
