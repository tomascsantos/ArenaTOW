package io.github.TcFoxy.ArenaTOW.BattleArena.listeners.competition;


import org.bukkit.event.player.PlayerPickupItemEvent;

import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.PlayerHolder;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.ArenaListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.ArenaEventHandler;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.EventPriority;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.options.TransitionOption;

public class ItemPickupListener implements ArenaListener{
    final PlayerHolder holder;

	public ItemPickupListener(PlayerHolder holder){
		this.holder = holder;
	}

	@ArenaEventHandler(priority=EventPriority.HIGH)
	public void onPlayerItemPickupItem(PlayerPickupItemEvent event){
		if (holder.hasOption(TransitionOption.ITEMPICKUPOFF)){
			event.setCancelled(true);}
	}
}
