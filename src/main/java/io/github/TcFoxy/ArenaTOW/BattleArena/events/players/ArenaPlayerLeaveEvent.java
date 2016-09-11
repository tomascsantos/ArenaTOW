package io.github.TcFoxy.ArenaTOW.BattleArena.events.players;

import java.util.ArrayList;
import java.util.List;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaPlayer;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.ArenaTeam;



/**
 * Signifies that the player has typed the command to leave the competition
 */
public class ArenaPlayerLeaveEvent extends ArenaPlayerEvent{
	public enum QuitReason{
		QUITCOMMAND, QUITMC, KICKED, OTHER
	}
	final ArenaTeam team;
	final QuitReason reason;
	boolean handledQuit = false;
	List<String> messages = null;

	public ArenaPlayerLeaveEvent(ArenaPlayer arenaPlayer, ArenaTeam team, QuitReason reason) {
		super(arenaPlayer);
		this.team = team;
		this.reason = reason;
	}

	public ArenaTeam getTeam() {
		return team;
	}

	public boolean isHandledQuit() {
		return handledQuit;
	}

	public void setHandledQuit(boolean handledQuit) {
		this.handledQuit = handledQuit;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void addMessage(String str) {
		if (messages == null){
			messages = new ArrayList<String>();}
		if (!messages.contains(str))
			messages.add(str);
	}
}
