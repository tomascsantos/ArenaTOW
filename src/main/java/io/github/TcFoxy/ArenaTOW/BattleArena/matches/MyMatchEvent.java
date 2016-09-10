package io.github.TcFoxy.ArenaTOW.BattleArena.matches;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.MyMatch;
import mc.alk.arena.events.CompetitionEvent;

public class MyMatchEvent extends CompetitionEvent {
	public MyMatchEvent(MyMatch match) {
		super();
		setCompetition(match);
	}

	/**
	 * Returns the match for this event
	 * @return Match
	 */
	public MyMatch getMatch() {
		return (MyMatch) getCompetition();
	}
}
