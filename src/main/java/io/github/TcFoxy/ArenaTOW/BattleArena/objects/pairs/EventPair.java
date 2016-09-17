package io.github.TcFoxy.ArenaTOW.BattleArena.objects.pairs;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MatchParams;

public class EventPair{
	MatchParams eventParams;
	String[] args;
	public EventPair(MatchParams event, String[] args) {
		this.eventParams = event;
		this.args = args;
	}

	public MatchParams getEventParams() {
		return eventParams;
	}
	public void setEventParams(MatchParams eventParams) {
		this.eventParams = eventParams;
	}
	public String[] getArgs() {
		return args;
	}
	public void setArgs(String[] args) {
		this.args = args;
	}
}
