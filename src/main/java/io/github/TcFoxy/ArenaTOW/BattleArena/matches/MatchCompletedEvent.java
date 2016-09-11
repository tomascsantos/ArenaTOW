package io.github.TcFoxy.ArenaTOW.BattleArena.matches;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.Match;

public class MatchCompletedEvent extends MatchEvent {
	public MatchCompletedEvent(Match match){
		super(match);
	}
}
