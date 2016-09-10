package io.github.TcFoxy.ArenaTOW.BattleArena.matches;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.MyMatch;

public class MyMatchCompletedEvent extends MyMatchEvent {
	public MyMatchCompletedEvent(MyMatch match){
		super(match);
	}
}
