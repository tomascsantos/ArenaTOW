package io.github.TcFoxy.ArenaTOW.BattleArena.matches;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.MyMatch;
import mc.alk.arena.objects.MatchState;

public class MyMatchFinishedEvent extends MyMatchEvent {
	final MatchState state;

	public MyMatchFinishedEvent(MyMatch match){
		super(match);
		this.state = match.getState();
	}
	public MatchState getState() {
		return state;
	}
}
