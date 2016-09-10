package io.github.TcFoxy.ArenaTOW.BattleArena.matches;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.MyMatch;

public class MyMatchPlayersReadyEvent extends MyMatchEvent {
	public MyMatchPlayersReadyEvent(MyMatch match){
		super(match);
	}
}
