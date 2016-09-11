package io.github.TcFoxy.ArenaTOW.BattleArena.matches;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.MyMatch;

public class MyMatchCancelledEvent extends MyMatchEvent {
	public MyMatchCancelledEvent(MyMatch match){
		super(match);
	}
}
