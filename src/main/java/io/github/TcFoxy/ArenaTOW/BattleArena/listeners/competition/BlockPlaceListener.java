package io.github.TcFoxy.ArenaTOW.BattleArena.listeners.competition;


import org.bukkit.event.block.BlockPlaceEvent;

import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.PlayerHolder;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.ArenaListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.ArenaEventHandler;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.EventPriority;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.options.TransitionOption;

public class BlockPlaceListener implements ArenaListener{
	PlayerHolder holder;

	public BlockPlaceListener(PlayerHolder holder){
		this.holder = holder;
	}

	@ArenaEventHandler(priority=EventPriority.HIGH)
	public void onPlayerBlockPlace(BlockPlaceEvent event){
		if (holder.hasOption(TransitionOption.BLOCKPLACEOFF)){
			event.setCancelled(true);
		} else if (holder.hasOption(TransitionOption.BLOCKPLACEON)){
			event.setCancelled(false);
		}
	}
}
