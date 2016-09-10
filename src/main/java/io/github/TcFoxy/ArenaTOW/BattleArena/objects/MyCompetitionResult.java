package io.github.TcFoxy.ArenaTOW.BattleArena.objects;


import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MyMatchResult.WinLossDraw;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.MyArenaTeam;

import java.util.Collection;
import java.util.Set;
import java.util.SortedMap;


/**
 * Represents the result of a competition.
 * Modifying this object will modify the outcome of the match.
 */
public interface MyCompetitionResult{

    /**
	 * Changes the outcome type of this match to the given type.
	 * Example, adding winners to this match will not change the outcome,
	 * unless this match is set to a WinLossDraw.WIN
	 * @param wld The WinLossDraw type.
	 */
	public void setResult(WinLossDraw wld);

    public void setVictor(MyArenaTeam vic);

    public void setVictors(Collection<MyArenaTeam> victors);

    public void setDrawers(Collection<MyArenaTeam> drawers);

    public void setLosers(Collection<MyArenaTeam> losers);

    public void addLosers(Collection<MyArenaTeam> losers);

    public void addLoser(MyArenaTeam loser);

    public Set<MyArenaTeam> getVictors();

    public Set<MyArenaTeam> getLosers();

    public void removeLosers(Collection<MyArenaTeam> teams);

    public void removeDrawers(Collection<MyArenaTeam> teams);

    public void removeVictors(Collection<MyArenaTeam> teams);

    public Set<MyArenaTeam> getDrawers();

    public String toPrettyString();

    public boolean isUnknown();

    public boolean isDraw();

    public boolean isWon();

    public boolean isLost();

    public boolean isFinished();

    public boolean hasVictor();

    public WinLossDraw getResult();

    public SortedMap<Integer, Collection<MyArenaTeam>> getRanking();

    public void setRanking(SortedMap<Integer, Collection<MyArenaTeam>> ranks);
}
