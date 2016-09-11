package io.github.TcFoxy.ArenaTOW.BattleArena.objects.joining;

import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.MatchParams;
import mc.alk.arena.objects.arenas.ArenaListener;
import mc.alk.arena.objects.teams.ArenaTeam;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.Arena;

import java.util.List;

public abstract class QueueObject {

	protected Integer priority;

	final protected MatchParams matchParams;

	final protected JoinOptions jp;

	int numPlayers;

    public List<ArenaListener> listeners;


    public QueueObject(JoinOptions jp){
		this.jp = jp;
        matchParams = jp.getMatchParams();
    }

    public QueueObject(JoinOptions jp, MatchParams params){
        this.jp = jp;
        matchParams = params;
    }

    public abstract Integer getPriority();

	public abstract boolean hasMember(ArenaPlayer p);

	public abstract ArenaTeam getTeam(ArenaPlayer p);

	public abstract int size();

	public abstract List<ArenaTeam> getTeams();

	public abstract boolean hasTeam(ArenaTeam team);

	public long getJoinTime(){return jp.getJoinTime();}

	public MatchParams getMatchParams() {
		return matchParams;
	}

	public JoinOptions getJoinOptions() {
		return jp;
	}

	public int getNumPlayers() {
		return numPlayers;
	}

    public Arena getArena() {
        return jp.getArena();
    }

    public List<ArenaListener> getListeners() {
        return listeners;
    }
}
