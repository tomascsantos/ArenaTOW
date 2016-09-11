package io.github.TcFoxy.ArenaTOW.BattleArena.listeners.competition;


import org.bukkit.event.block.BlockBreakEvent;

import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.PlayerHolder;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.ArenaListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.ArenaEventHandler;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.EventPriority;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.options.TransitionOption;

public class BlockBreakListener implements ArenaListener{
	PlayerHolder holder;

	public BlockBreakListener(PlayerHolder holder){
		this.holder = holder;
	}

	@ArenaEventHandler(priority=EventPriority.HIGH)
	public void onPlayerBlockBreak(BlockBreakEvent event) {
        if (holder.hasOption(TransitionOption.BLOCKBREAKOFF)) {
            event.setCancelled(true);
        } else if (holder.hasOption(TransitionOption.BLOCKBREAKON)) {
            event.setCancelled(false);
        }
    }
}
