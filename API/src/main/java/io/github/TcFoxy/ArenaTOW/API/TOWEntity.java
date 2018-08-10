package io.github.TcFoxy.ArenaTOW.API;


import org.bukkit.Color;
import org.bukkit.Location;

import java.util.Objects;

public interface TOWEntity {

    /**
     * @return returns the health of the mob
     */
    float getHealth();

    /**
     * @return returns the health of mob as Integer
     */
    default Integer getIntHealth() {
        return Math.round(getHealth());
    }

    /**
     * Teams can be red or blue
     * @return the team that the mob is associated with
     */
    Color getTeam();

    /**
     * @return true if the mob is alive
     */
    boolean isAlive();

    /**
     * Sets the mobs health
     * @param f the value to set the health to
     */
    void setHealth(float f);

    /**
     * The type of the mob
     * //TODO implement
     */
    MobType getMobType();

    /**
     * Returns false if the other object is not on the same team
     * can handle any object
     * @param o
     * @return
     */
    default boolean isSameTeam(Object o) {
        if (!(o instanceof TOWEntity)) return false;
        return this.getTeam().equals(((TOWEntity)o).getTeam());
    }

    /**
     * returns the location of the entity
     */
    Location getLocation();

}
