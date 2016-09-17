package io.github.TcFoxy.ArenaTOW.BattleArena.events.events;

import java.util.Collection;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.events.Event;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.ArenaTeam;



public class EventStartEvent extends EventEvent {
	final Collection<ArenaTeam> teams;
	public EventStartEvent(Event event, Collection<ArenaTeam> teams) {
		super(event);
		this.teams = teams;
	}
	public Collection<ArenaTeam> getTeams() {
		return teams;
	}
}
