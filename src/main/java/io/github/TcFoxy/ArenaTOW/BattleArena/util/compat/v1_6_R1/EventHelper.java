package io.github.TcFoxy.ArenaTOW.BattleArena.util.compat.v1_6_R1;

import org.bukkit.event.entity.EntityDamageEvent;

import io.github.TcFoxy.ArenaTOW.BattleArena.util.compat.IEventHelper;

public class EventHelper implements IEventHelper {

    @Override
    public void setDamage(EntityDamageEvent event, double damage) {
        event.setDamage(damage);
    }
}
