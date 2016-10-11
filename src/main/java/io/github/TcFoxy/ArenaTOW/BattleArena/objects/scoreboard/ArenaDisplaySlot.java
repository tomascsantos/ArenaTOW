package io.github.TcFoxy.ArenaTOW.BattleArena.objects.scoreboard;

import io.github.TcFoxy.ArenaTOW.scoreboard.scoreboard.SAPIDisplaySlot;

public enum ArenaDisplaySlot {
	SIDEBAR, PLAYER_LIST, BELOW_NAME, NONE;

	public SAPIDisplaySlot toSAPI() {
		switch(this){
		case BELOW_NAME: return SAPIDisplaySlot.BELOW_NAME;
		case NONE:return SAPIDisplaySlot.NONE;
		case PLAYER_LIST:return SAPIDisplaySlot.PLAYER_LIST;
		case SIDEBAR:return SAPIDisplaySlot.SIDEBAR;
		default: return null;
		}
	}
}
