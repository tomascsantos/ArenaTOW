package io.github.TcFoxy.ArenaTOW.BattleArena.matches;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.Match;
import io.github.TcFoxy.ArenaTOW.BattleArena.events.CompetitionEvent;

public class MatchEvent extends CompetitionEvent {
	public MatchEvent(Match match) {
		super();
		setCompetition(match);
	}

	/**
	 * Returns the match for this event
	 * @return Match
	 */
	public Match getMatch() {
		return (Match) getCompetition();
	}
}
