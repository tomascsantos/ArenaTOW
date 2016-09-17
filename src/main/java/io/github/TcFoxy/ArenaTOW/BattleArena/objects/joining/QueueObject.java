package io.github.TcFoxy.ArenaTOW.BattleArena.objects.joining;


import java.util.List;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaPlayer;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MatchParams;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.Arena;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.ArenaListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.options.JoinOptions;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.ArenaTeam;

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
