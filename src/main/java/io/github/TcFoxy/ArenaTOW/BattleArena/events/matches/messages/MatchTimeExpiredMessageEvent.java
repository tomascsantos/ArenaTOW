package io.github.TcFoxy.ArenaTOW.BattleArena.events.matches.messages;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.Match;
import io.github.TcFoxy.ArenaTOW.BattleArena.matches.MatchMessageEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MatchState;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.messaging.Channel;

public class MatchTimeExpiredMessageEvent extends MatchMessageEvent{
	public MatchTimeExpiredMessageEvent(Match match, MatchState state, Channel serverChannel,
			String serverMessage, String matchMessage) {
		super(match, state, serverChannel, serverMessage, matchMessage);
	}
}
