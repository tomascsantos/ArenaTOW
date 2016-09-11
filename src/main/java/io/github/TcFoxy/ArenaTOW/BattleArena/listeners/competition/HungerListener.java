package io.github.TcFoxy.ArenaTOW.BattleArena.listeners.competition;


import org.bukkit.event.entity.FoodLevelChangeEvent;

import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.PlayerHolder;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.ArenaListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.ArenaEventHandler;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.options.TransitionOption;

public class HungerListener implements ArenaListener{
	PlayerHolder holder;

	public HungerListener(PlayerHolder holder){
		this.holder = holder;
	}

	@ArenaEventHandler
	public void onFoodLevelChangeEvent(FoodLevelChangeEvent event){
		if (holder.hasOption(TransitionOption.HUNGEROFF)){
			event.setCancelled(true);
        }
	}
}
