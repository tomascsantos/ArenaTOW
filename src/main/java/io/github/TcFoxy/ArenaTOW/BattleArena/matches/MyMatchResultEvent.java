package io.github.TcFoxy.ArenaTOW.BattleArena.matches;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.MyMatch;
import mc.alk.arena.objects.CompetitionResult;

import org.bukkit.event.Cancellable;

public class MyMatchResultEvent extends MyMatchEvent implements Cancellable{
    CompetitionResult matchResult;
	boolean cancelled;
	final boolean matchEnding;

	public MyMatchResultEvent(MyMatch match, CompetitionResult matchResult) {
		super(match);
		this.matchResult = matchResult;
		matchEnding = !match.alwaysOpen();
	}

	public CompetitionResult getMatchResult() {
		return matchResult;
	}

	public void setMatchResult(CompetitionResult matchResult) {
		this.matchResult = matchResult;
	}

	@Override
    public boolean isCancelled() {
		return cancelled;
	}

	@Override
    public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public boolean isMatchEnding(){
		return matchEnding && !cancelled;
	}
}
