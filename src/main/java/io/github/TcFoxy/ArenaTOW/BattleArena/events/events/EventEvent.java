 package io.github.TcFoxy.ArenaTOW.BattleArena.events.events;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.events.Event;
import io.github.TcFoxy.ArenaTOW.BattleArena.events.CompetitionEvent;

public class EventEvent extends CompetitionEvent{
	public EventEvent(Event event) {
		super();
		setCompetition(event);
	}

	/**
	 * Returns the match for this event
	 * @return Match
	 */
	public Event getEvent() {
		return (Event) getCompetition();
	}
}
