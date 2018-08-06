package io.github.TcFoxy.ArenaTOW.API;


import java.awt.Color;

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
    //Mobtype getType();

    default boolean isSameTeam(TOWEntity other) {
        return this.getTeam().equals(other.getTeam());
    }

}
