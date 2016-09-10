package io.github.TcFoxy.ArenaTOW.BattleArena.matches;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.MyMatch;


public class MyMatchTimerIntervalEvent extends MyMatchEvent {
    final int secondsRemaining;
	public MyMatchTimerIntervalEvent(MyMatch match, int remaining) {
		super(match);
		this.secondsRemaining = remaining;
	}

	public int getSecondsRemaining(){
		return secondsRemaining;
	}

}
