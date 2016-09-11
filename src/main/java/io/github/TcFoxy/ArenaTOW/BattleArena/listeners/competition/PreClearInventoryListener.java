package io.github.TcFoxy.ArenaTOW.BattleArena.listeners.competition;


import org.bukkit.event.entity.PlayerDeathEvent;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.ArenaListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.ArenaEventHandler;

public class PreClearInventoryListener implements ArenaListener{

	@ArenaEventHandler(bukkitPriority = org.bukkit.event.EventPriority.LOWEST)
	public void onPlayerDeath(PlayerDeathEvent event) {
        event.getDrops().clear();
    }
}
