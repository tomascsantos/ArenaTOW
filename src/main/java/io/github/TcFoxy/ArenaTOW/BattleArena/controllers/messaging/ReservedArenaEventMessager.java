package io.github.TcFoxy.ArenaTOW.BattleArena.controllers.messaging;


import java.util.Collection;
import java.util.Set;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.events.Event;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaPlayer;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MatchState;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.ArenaTeam;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.Log;


public class ReservedArenaEventMessager extends EventMessager{

	public ReservedArenaEventMessager(Event event){
		super(event);
	}

	@Override
	public void sendEventCancelledDueToLackOfPlayers(Set<ArenaPlayer> competingPlayers) {
		try{impl.sendEventCancelledDueToLackOfPlayers(getChannel(MatchState.ONCANCEL), competingPlayers);
		}catch(Exception e){Log.printStackTrace(e);}
	}

	@Override
	public void sendEventCancelled(Collection<ArenaTeam> teams) {
		try{impl.sendEventCancelled(getChannel(MatchState.ONCANCEL), teams);}catch(Exception e){Log.printStackTrace(e);}
	}

	public void sendTeamJoinedEvent(ArenaTeam t) {
        try{impl.sendTeamJoinedEvent(getChannel(MatchState.ONJOIN), t);}catch(Exception e){Log.printStackTrace(e);}
	}


	@Override
	public void sendEventDraw(Collection<ArenaTeam> drawers, Collection<ArenaTeam> losers) {

	}

}
