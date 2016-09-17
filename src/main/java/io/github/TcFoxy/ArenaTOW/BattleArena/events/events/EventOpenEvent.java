package io.github.TcFoxy.ArenaTOW.BattleArena.events.events;

import org.bukkit.event.Cancellable;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.events.Event;

public class EventOpenEvent extends EventEvent implements Cancellable {
	/// Cancel status
	boolean cancelled = false;

	public EventOpenEvent(Event event){
		super(event);
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
