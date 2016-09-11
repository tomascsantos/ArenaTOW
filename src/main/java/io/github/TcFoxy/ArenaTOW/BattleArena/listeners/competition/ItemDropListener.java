package io.github.TcFoxy.ArenaTOW.BattleArena.listeners.competition;


import org.bukkit.event.player.PlayerDropItemEvent;

import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.PlayerHolder;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.ArenaListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.ArenaEventHandler;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.EventPriority;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.options.TransitionOption;

public class ItemDropListener implements ArenaListener{
	PlayerHolder holder;

	public ItemDropListener(PlayerHolder holder){
		this.holder = holder;
	}

	@ArenaEventHandler(priority=EventPriority.HIGH)
	public void onPlayerDropItem(PlayerDropItemEvent event){
		if (holder.hasOption(TransitionOption.ITEMDROPOFF)){
			event.setCancelled(true);}
	}
}
