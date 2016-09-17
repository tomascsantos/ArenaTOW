package io.github.TcFoxy.ArenaTOW.BattleArena.events.matches;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.Match;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MatchState;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.messaging.Channel;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.messaging.Channels;

public class MatchMessageEvent extends MatchEvent {
	final MatchState state;
	String serverMessage;
	String matchMessage;
	Channel serverChannel;

	public MatchMessageEvent(Match match, MatchState state, Channel serverChannel, String serverMessage, String matchMessage) {
		super(match);
		this.serverChannel = serverChannel;
		this.serverMessage = serverMessage;
		this.matchMessage = matchMessage;
		this.state = state;
	}

	public String getServerMessage() {
		return serverMessage;
	}

	public void setServerMessage(String serverMessage) {
		this.serverMessage = serverMessage;
	}

	public String getMatchMessage() {
		return matchMessage;
	}

	public void setMatchMessage(String matchMessage) {
		this.matchMessage = matchMessage;
	}

	public Channel getServerChannel() {
		return serverChannel == null ? Channels.NullChannel : serverChannel;
	}

	public void setServerChannel(Channel serverChannel) {
		this.serverChannel = serverChannel;
	}
	public MatchState getState(){
		return state;
	}
}
