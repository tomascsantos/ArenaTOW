package io.github.TcFoxy.ArenaTOW.BattleArena.listeners.competition;



import org.bukkit.event.inventory.InventoryClickEvent;

import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.PlayerStoreController;
import io.github.TcFoxy.ArenaTOW.BattleArena.events.players.ArenaPlayerEnterMatchEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.events.players.ArenaPlayerLeaveMatchEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.ArenaListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.ArenaEventHandler;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.EventPriority;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.ArenaTeam;
import mc.alk.arena.util.TeamUtil;


public class TeamHeadListener implements ArenaListener{

	@ArenaEventHandler(priority=EventPriority.HIGH)
	public void onPlayerInventoryClick(InventoryClickEvent event) {
		if (event.getSlot() == 39/*Helm Slot*/)
			event.setCancelled(true);
	}

	@ArenaEventHandler
	public void onArenaPlayerEnterEvent(ArenaPlayerEnterMatchEvent event){
		ArenaTeam t = event.getTeam();
		if (t.getHeadItem() != null)
			TeamUtil.setTeamHead(t.getHeadItem(), event.getPlayer());
	}

	@ArenaEventHandler
	public void onArenaPlayerLeaveMatchEvent(ArenaPlayerLeaveMatchEvent event){
		ArenaTeam t = event.getTeam();
		if (t.getHeadItem() != null)
			PlayerStoreController.removeItem(event.getPlayer(), t.getHeadItem());
	}
}
