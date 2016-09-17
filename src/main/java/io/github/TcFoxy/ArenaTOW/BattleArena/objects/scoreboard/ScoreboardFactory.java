package io.github.TcFoxy.ArenaTOW.BattleArena.objects.scoreboard;

import io.github.TcFoxy.ArenaTOW.BattleArena.Defaults;
import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.Match;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MatchParams;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.scoreboard.base.ArenaBukkitScoreboard;

public class ScoreboardFactory {
	private static boolean hasBukkitScoreboard = false;
	static{
		try {
			Class.forName("org.bukkit.scoreboard.Scoreboard");
			ScoreboardFactory.hasBukkitScoreboard = true;
		} catch (ClassNotFoundException e) {
			ScoreboardFactory.hasBukkitScoreboard = false;
		}
	}

    public static ArenaScoreboard createScoreboard(String scoreboardName, MatchParams params) {
        // Intellij warning suppression
        // noinspection PointlessBooleanExpression,ConstantConditions
        return (Defaults.USE_SCOREBOARD && hasBukkitScoreboard && !Defaults.TESTSERVER) ?
                new ArenaBukkitScoreboard(scoreboardName, params) : new ArenaScoreboard(scoreboardName);
    }

    public static ArenaScoreboard createScoreboard(Match match, MatchParams params) {
        return createScoreboard(match.getName(),params);
    }

	public static boolean hasBukkitScoreboard(){
		return hasBukkitScoreboard;
	}

}
