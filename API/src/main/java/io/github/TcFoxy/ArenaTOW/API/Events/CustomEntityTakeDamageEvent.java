package io.github.TcFoxy.ArenaTOW.API.Events;

import io.github.TcFoxy.ArenaTOW.API.TOWEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


/**
 * Created by Tomas Santos on 12/22/2018.
 */
public class CustomEntityTakeDamageEvent extends Event implements Cancellable{

    TOWEntity damaged;
    Entity attacker;
    private static final HandlerList HANDLERS = new HandlerList();

    public CustomEntityTakeDamageEvent(TOWEntity damaged, Entity attacker) {
        this.damaged = damaged;
        this.attacker = attacker;
    }

    public TOWEntity getDamaged() {
        return this.damaged;
    }

    public Entity getAttacker() {
        return this.attacker;
    }

    private boolean isCancelled;
    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.isCancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
