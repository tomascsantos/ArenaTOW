package io.github.TcFoxy.ArenaTOW.BattleArena.util.compat.pre;


import java.lang.reflect.Method;

import org.bukkit.event.entity.EntityDamageEvent;

import io.github.TcFoxy.ArenaTOW.BattleArena.util.Log;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.compat.IEventHelper;

public class EventHelper implements IEventHelper {
    Method ede_setDamage;

    public EventHelper(){
        try {
            ede_setDamage = EntityDamageEvent.class.getMethod("setDamage", int.class);
        } catch (Exception e) {
            Log.printStackTrace(e);
        }
    }

    @Override
    public void setDamage(EntityDamageEvent event, double damage) {
        try {
            ede_setDamage.invoke(event, (int) damage);
        } catch (Exception e) {
            Log.printStackTrace(e);
        }
    }
}
