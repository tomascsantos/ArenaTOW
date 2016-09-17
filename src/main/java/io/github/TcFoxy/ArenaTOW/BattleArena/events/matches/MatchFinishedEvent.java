package io.github.TcFoxy.ArenaTOW.BattleArena.events.matches;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.Match;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MatchState;

public class MatchFinishedEvent extends MatchEvent {
	final MatchState state;

	public MatchFinishedEvent(Match match){
		super(match);
		this.state = match.getState();
	}
	public MatchState getState() {
		return state;
	}
}
