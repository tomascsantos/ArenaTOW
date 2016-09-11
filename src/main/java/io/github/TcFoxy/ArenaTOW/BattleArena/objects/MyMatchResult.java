package io.github.TcFoxy.ArenaTOW.BattleArena.objects;


import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.MyArenaTeam;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;

/**
 * @author alkarin
 */
public class MyMatchResult implements MyCompetitionResult {
	
	public enum WinLossDraw {
	    UNKNOWN, LOSS, DRAW, WIN
	}
	
    Set<MyArenaTeam> victors = new HashSet<MyArenaTeam>();
    Set<MyArenaTeam> losers = new HashSet<MyArenaTeam>();
    Set<MyArenaTeam> drawers = new HashSet<MyArenaTeam>();
    WinLossDraw wld = WinLossDraw.UNKNOWN;
    SortedMap<Integer, Collection<MyArenaTeam>> ranks;

    public MyMatchResult(){}
    public MyMatchResult(MyCompetitionResult r) {
        this.wld = r.getResult();
        victors.addAll(r.getVictors());
        losers.addAll(r.getLosers());
        drawers.addAll(r.getDrawers());
    }

    /**
     * Changes the outcome type of this match to the given type.
     * Example, adding winners to this match will not change the outcome,
     * unless this match is set to a WinLossDraw.WIN
     * @param wld The WinLossDraw type.
     */
    @Override
    public void setResult(WinLossDraw wld){
        this.wld = wld;
    }

    @Override
    public void setVictor(MyArenaTeam vic) {
        this.victors.clear();
        this.victors.add(vic);
        wld = WinLossDraw.WIN;
    }

    @Override
    public void setVictors(Collection<MyArenaTeam> victors) {
        this.victors.clear();
        this.victors.addAll(victors);
        wld = WinLossDraw.WIN;
    }

    @Override
    public void setDrawers(Collection<MyArenaTeam> drawers) {
        this.drawers.clear();
        this.drawers.addAll(drawers);
        wld = WinLossDraw.DRAW;
    }

    @Override
    public void setLosers(Collection<MyArenaTeam> losers) {
        this.losers.clear();
        this.losers.addAll(losers);
    }

    @Override
    public void addLosers(Collection<MyArenaTeam> losers) {
        this.losers.addAll(losers);
    }

    @Override
    public void addLoser(MyArenaTeam loser) {
        losers.add(loser);
    }

    @Override
    public Set<MyArenaTeam> getVictors() {
        return victors;
    }

    @Override
    public Set<MyArenaTeam> getLosers() {
        return losers;
    }

    @Override
    public void removeLosers(Collection<MyArenaTeam> teams){
        losers.removeAll(teams);
    }

    @Override
    public void removeDrawers(Collection<MyArenaTeam> teams){
        drawers.removeAll(teams);
    }
    @Override
    public void removeVictors(Collection<MyArenaTeam> teams){
        victors.removeAll(teams);
    }

    @Override
    public Set<MyArenaTeam> getDrawers(){
        return drawers;
    }

    @Override
    public String toString(){
        return "[" + wld + ",victor=" + victors + ",losers=" + losers + ",drawers=" + drawers + "]" + toPrettyString();
    }

    @Override
    public String toPrettyString() {
        if (victors.isEmpty()){
            return "&eThere are no victors yet";}
        StringBuilder sb = new StringBuilder();
        for (MyArenaTeam t: victors){
            sb.append(t.getTeamSummary()).append(" ");}
        sb.append(" &ewins vs ");
        for (MyArenaTeam t: losers){
            sb.append(t.getTeamSummary()).append(" ");}

        return sb.toString();
    }

    @Override
    public boolean isUnknown() {
        return wld == WinLossDraw.UNKNOWN;
    }
    @Override
    public boolean isDraw() {
        return wld == WinLossDraw.DRAW;
    }
    @Override
    public boolean isWon(){
        return hasVictor();
    }
    @Override
    public boolean isLost() {
        return wld == WinLossDraw.LOSS;
    }
    @Override
    public boolean isFinished(){
        return wld == WinLossDraw.WIN || wld == WinLossDraw.DRAW;
    }
    @Override
    public boolean hasVictor() {
        return wld == WinLossDraw.WIN;
    }
    @Override
    public WinLossDraw getResult(){
        return wld;
    }

    @Override
    public SortedMap<Integer, Collection<MyArenaTeam>> getRanking() {
        return ranks;
    }

    @Override
    public void setRanking(SortedMap<Integer, Collection<MyArenaTeam>> ranks) {
        this.ranks = ranks;
    }
}
