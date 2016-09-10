package io.github.TcFoxy.ArenaTOW.BattleArena.matches;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.MyMatch;

import org.bukkit.event.Cancellable;

public class MyMatchOpenEvent extends MyMatchEvent implements Cancellable {
	/// Cancel status
	boolean cancelled = false;

	public MyMatchOpenEvent(MyMatch match){
		super(match);
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}
