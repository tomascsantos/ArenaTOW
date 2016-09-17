package io.github.TcFoxy.ArenaTOW.BattleArena.matches;

import org.bukkit.event.Cancellable;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.Match;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.CompetitionResult;

public class MatchResultEvent extends MatchEvent implements Cancellable{
    CompetitionResult matchResult;
	boolean cancelled;
	final boolean matchEnding;

	public MatchResultEvent(Match match, CompetitionResult matchResult) {
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
