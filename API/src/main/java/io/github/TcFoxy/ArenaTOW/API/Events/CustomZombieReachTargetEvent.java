package io.github.TcFoxy.ArenaTOW.API.Events;

import io.github.TcFoxy.ArenaTOW.API.CustomZombie;
import io.github.TcFoxy.ArenaTOW.API.TOWEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Tomas Santos on 12/21/2018.
 */
public class CustomZombieReachTargetEvent extends Event{
    private static final HandlerList HANDLERS = new HandlerList();

    private final CustomZombie entityZombie;

    public CustomZombieReachTargetEvent(CustomZombie zombie) {
        this.entityZombie = zombie;
    }

    public CustomZombie getEntityZombie() {
        return this.entityZombie;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    //Spigot said we needed this?
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
