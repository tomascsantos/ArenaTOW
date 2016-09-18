package io.github.TcFoxy.ArenaTOW.BattleArena.util.compat;

import org.bukkit.event.entity.EntityDamageEvent;

public interface IEventHelper {

	void setDamage(EntityDamageEvent event, double damage);

}
