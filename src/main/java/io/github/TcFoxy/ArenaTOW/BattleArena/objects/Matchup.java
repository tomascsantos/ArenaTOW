package io.github.TcFoxy.ArenaTOW.BattleArena.objects;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.Match;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.ParamController;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.Arena;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.ArenaListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.options.JoinOptions;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.ArenaTeam;


public class Matchup {
	static int count = 0;
	final int id = count++; /// id

	public CompetitionResult result = new MatchResult();
	public List<ArenaTeam> teams = new ArrayList<ArenaTeam>();

	List<ArenaListener> listeners = new ArrayList<ArenaListener>();

	MatchParams params = null;
	Match match = null;
	final JoinOptions joinOptions;

	public Matchup(MatchParams params, ArenaTeam team, ArenaTeam team2, JoinOptions joinOptions) {
		this.params = params;
		teams.add(team);
		teams.add(team2);
		this.joinOptions = joinOptions;
	}

	public Matchup(MatchParams params, Collection<ArenaTeam> teams, JoinOptions joinOptions) {
		this.teams = new ArrayList<ArenaTeam>(teams);
		this.params = ParamController.copyParams(params);
		this.joinOptions = joinOptions;
	}

	public MatchParams getMatchParams() {
		return params;
	}

	public void setResult(CompetitionResult result) {
		this.result = result;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for (ArenaTeam t: teams){
			sb.append("t=").append(t).append(",");
		}
		return sb.toString() + " result=" + result;
	}

	public List<ArenaTeam> getTeams() {return teams;}
	public ArenaTeam getTeam(int i) {
		return teams.get(i);
	}
	public CompetitionResult getResult() {
		return result;
	}

	@Override
	public boolean equals(Object other) {
        return this == other ||
                other instanceof Matchup &&
                        this.hashCode() == other.hashCode();
    }

	@Override
	public int hashCode() { return id;}

	public void addArenaListener(ArenaListener transitionListener) {
		listeners.add(transitionListener);
	}

	public List<ArenaListener> getArenaListeners() {
		return listeners;
	}

	public void addMatch(Match match) {
		this.match = match;
	}

	public Match getMatch(){
		return match;
	}
	public Integer getPriority() {
		Integer priority = Integer.MAX_VALUE;
		for (ArenaTeam t: teams){
			if (t.getPriority() < priority){
				priority = t.getPriority();}
		}
		return priority;
	}
	public boolean hasMember(ArenaPlayer p) {
		for (ArenaTeam t: teams){
			if (t.hasMember(p))
				return true;
		}
		return false;
	}
	public ArenaTeam getTeam(ArenaPlayer p) {
		for (ArenaTeam t: teams){
			if (t.hasMember(p))
				return t;
		}
		return null;
	}
	public int size() {
		int size = 0;
		for (ArenaTeam t: teams){
			size += t.size();
		}
		return size;
	}

	public JoinOptions getJoinOptions() {
		return joinOptions;
	}

	public Arena getArena() {return joinOptions.getArena();}
	public void setArena(Arena arena) {joinOptions.setArena(arena);}

}
