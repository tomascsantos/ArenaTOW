package io.github.TcFoxy.ArenaTOW.BattleArena.events.events;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.events.Event;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.CompetitionResult;

public class EventResultEvent extends EventEvent {
	final CompetitionResult result;
	public EventResultEvent(Event event, CompetitionResult result) {
		super(event);
		this.result = result;
	}

	public CompetitionResult getResult(){
		return result;
	}
}
