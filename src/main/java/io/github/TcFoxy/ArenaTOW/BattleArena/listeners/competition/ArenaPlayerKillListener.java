package io.github.TcFoxy.ArenaTOW.BattleArena.listeners.competition;

import io.github.TcFoxy.ArenaTOW.BattleArena.events.players.ArenaPlayerKillEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MatchParams;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MatchResult.WinLossDraw;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.ArenaListener;
import mc.alk.arena.controllers.plugins.TrackerController;

public class ArenaPlayerKillListener implements ArenaListener{
	final TrackerController sc;
	public ArenaPlayerKillListener(MatchParams params){
		sc = new TrackerController(params);
	}

	public void onArenaPlayerKillEvent(ArenaPlayerKillEvent event){
		sc.addRecord(event.getPlayer(),event.getTarget(), WinLossDraw.WIN);
	}
}
