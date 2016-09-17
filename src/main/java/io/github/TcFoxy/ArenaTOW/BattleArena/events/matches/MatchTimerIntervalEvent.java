package io.github.TcFoxy.ArenaTOW.BattleArena.events.matches;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.Match;

public class MatchTimerIntervalEvent extends MatchEvent {
    final int secondsRemaining;
	public MatchTimerIntervalEvent(Match match, int remaining) {
		super(match);
		this.secondsRemaining = remaining;
	}

	public int getSecondsRemaining(){
		return secondsRemaining;
	}

}
