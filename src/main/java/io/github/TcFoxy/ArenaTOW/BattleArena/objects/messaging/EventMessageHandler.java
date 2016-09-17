package io.github.TcFoxy.ArenaTOW.BattleArena.objects.messaging;

import java.util.Collection;
import java.util.Set;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaPlayer;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.ArenaTeam;



public interface EventMessageHandler {
	public void sendCountdownTillEvent(Channel serverChannel, int seconds);
	public void sendEventStarting(Channel serverChannel, Collection<ArenaTeam> teams);
	public void sendEventVictory(Channel serverChannel, Collection<ArenaTeam> victors, Collection<ArenaTeam> losers);
	public void sendEventOpenMsg(Channel serverChannel);
	public void sendEventCancelledDueToLackOfPlayers(Channel serverChannel, Set<ArenaPlayer> competingPlayers);
	public void sendTeamJoinedEvent(Channel serverChannel, ArenaTeam team);
	public void sendEventCancelled(Channel serverChannel, Collection<ArenaTeam> teams);
	public void sendCantFitTeam(ArenaTeam team);
	public void sendWaitingForMorePlayers(ArenaTeam team, int remaining);
	public void sendEventDraw(Channel serverChannel, Collection<ArenaTeam> participants, Collection<ArenaTeam> losers);
	public void sendAddedToTeam(ArenaTeam team, ArenaPlayer player);
}
