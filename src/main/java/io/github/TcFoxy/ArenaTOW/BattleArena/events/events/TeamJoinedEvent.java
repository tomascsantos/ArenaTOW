package io.github.TcFoxy.ArenaTOW.BattleArena.events.events;


import org.bukkit.event.Cancellable;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.events.Event;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.ArenaTeam;

public class TeamJoinedEvent extends EventEvent implements Cancellable {
	final ArenaTeam team;
	/// Cancel status
	boolean cancelled = false;

	public TeamJoinedEvent(Event event,ArenaTeam team) {
		super(event);
		this.team = team;
	}

	public ArenaTeam getTeam() {
		return team;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
