package io.github.TcFoxy.ArenaTOW.BattleArena.controllers.messaging;



import java.util.Collection;
import java.util.Set;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.events.Event;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaPlayer;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MatchState;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.messaging.AnnouncementOptions;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.messaging.Channel;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.messaging.Channels;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.messaging.EventMessageHandler;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.ArenaTeam;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.Log;


public class EventMessager {
	EventMessageHandler impl;
	final AnnouncementOptions bos;
	boolean silent = false;

	public EventMessager(Event event){
		this.impl = new EventMessageImpl(event);
		this.bos = event.getParams().getAnnouncementOptions();
	}

	protected Channel getChannel(MatchState state) {
		if (silent) return Channels.NullChannel;
		return bos != null && bos.hasOption(false,state) ? bos.getChannel(false,state) :
			AnnouncementOptions.getDefaultChannel(false,state);
	}

	public void setMessageHandler(EventMessageHandler handler) {
		this.impl = handler;
	}

	public EventMessageHandler getMessageHandler() {
		return impl;
	}

	public void sendCountdownTillEvent(int seconds) {
		try{impl.sendCountdownTillEvent(getChannel(MatchState.ONCOUNTDOWNTOEVENT), seconds);}
		catch(Exception e){Log.printStackTrace(e);}
	}

	public void sendEventStarting(Collection<ArenaTeam> teams) {
		try{impl.sendEventStarting(getChannel(MatchState.ONSTART), teams);}catch(Exception e){Log.printStackTrace(e);}
	}

	public void sendEventOpenMsg() {
		try{impl.sendEventOpenMsg(getChannel(MatchState.ONOPEN));}catch(Exception e){Log.printStackTrace(e);}
	}

	public void sendEventCancelledDueToLackOfPlayers(Set<ArenaPlayer> competingPlayers) {
		try{impl.sendEventCancelledDueToLackOfPlayers(getChannel(MatchState.ONCANCEL), competingPlayers);
	}catch(Exception e){Log.printStackTrace(e);}
	}

//	public void sendTeamJoinedEvent(Team t) {
//		try{impl.sendTeamJoinedEvent(getChannel(MatchState.ONJOIN),t);}catch(Exception e){Log.printStackTrace(e);}
//	}

	public void sendEventCancelled(Collection<ArenaTeam> teams) {
		try{impl.sendEventCancelled(getChannel(MatchState.ONCANCEL), teams);}catch(Exception e){Log.printStackTrace(e);}
	}

	public void sendCantFitTeam(ArenaTeam t) {
		try{impl.sendCantFitTeam(t);}catch(Exception e){Log.printStackTrace(e);}
	}

	public void sendWaitingForMorePlayers(ArenaTeam t, int remaining) {
		try{ impl.sendWaitingForMorePlayers(t, remaining);}catch(Exception e){Log.printStackTrace(e);}
	}
	public void setSilent(boolean silent){
		this.silent = silent;
	}

	public void sendEventVictory(Collection<ArenaTeam> victors, Collection<ArenaTeam> losers) {
		try{impl.sendEventVictory(getChannel(MatchState.ONVICTORY), victors,losers);}catch(Exception e){Log.printStackTrace(e);}
	}

	public void sendEventDraw(Collection<ArenaTeam> drawers, Collection<ArenaTeam> losers) {
		try{impl.sendEventDraw(getChannel(MatchState.ONVICTORY), drawers, losers);}catch(Exception e){Log.printStackTrace(e);}
	}

	public void sendTeamJoined(ArenaTeam team) {
		try{impl.sendTeamJoinedEvent(getChannel(MatchState.ONJOIN),team);}catch(Exception e){Log.printStackTrace(e);}
	}


//	public void sendEventJoin(Team team, ArenaPlayer player) {
//		try{impl.sendEventJoin(getChannel(MatchState.ONJOIN), drawers, losers);}catch(Exception e){Log.printStackTrace(e);}
//	}

}
