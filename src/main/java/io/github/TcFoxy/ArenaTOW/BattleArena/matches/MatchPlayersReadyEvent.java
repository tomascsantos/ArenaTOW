package io.github.TcFoxy.ArenaTOW.BattleArena.matches;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.Match;

public class MatchPlayersReadyEvent extends MatchEvent {
	public MatchPlayersReadyEvent(Match match){
		super(match);
	}
}
