package io.github.TcFoxy.ArenaTOW.BattleArena.objects.victoryconditions.interfaces;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.scoreboard.ArenaScoreboard;

public interface ScoreTracker extends DefinesLeaderRanking {
	public void setScoreBoard(ArenaScoreboard scoreboard);

	public void setDisplayTeams(boolean display);

}
