package io.github.TcFoxy.ArenaTOW.BattleArena.events.matches.messages;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.Match;
import io.github.TcFoxy.ArenaTOW.BattleArena.events.matches.MatchMessageEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MatchState;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.messaging.Channel;

public class MatchIntervalMessageEvent extends MatchMessageEvent{
	final int timeRemaining;

	public MatchIntervalMessageEvent(Match match, MatchState state, Channel serverChannel,
			String serverMessage, String matchMessage, int remainingTime) {
		super(match, state, serverChannel, serverMessage, matchMessage);
		this.timeRemaining = remainingTime;
	}
	public int getTimeRemaining(){
		return timeRemaining;
	}
}
