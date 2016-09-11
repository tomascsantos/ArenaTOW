package io.github.TcFoxy.ArenaTOW.BattleArena.events.players;

import io.github.TcFoxy.ArenaTOW.BattleArena.events.CompetitionEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaPlayer;

public class ArenaPlayerEvent extends CompetitionEvent{
	final ArenaPlayer player;

	public ArenaPlayerEvent(ArenaPlayer arenaPlayer){
		this.player = arenaPlayer;
	}

	public ArenaPlayer getPlayer(){
		return player;
	}
}
