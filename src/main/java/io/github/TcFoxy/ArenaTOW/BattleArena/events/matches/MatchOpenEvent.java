package io.github.TcFoxy.ArenaTOW.BattleArena.events.matches;

import org.bukkit.event.Cancellable;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.Match;

public class MatchOpenEvent extends MatchEvent implements Cancellable {
	/// Cancel status
	boolean cancelled = false;

	public MatchOpenEvent(Match match){
		super(match);
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}
