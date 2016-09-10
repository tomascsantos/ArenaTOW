package io.github.TcFoxy.ArenaTOW.BattleArena.objects.joining;

import mc.alk.arena.controllers.joining.AbstractJoinHandler;
import mc.alk.arena.controllers.joining.TeamJoinFactory;
import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.MatchParams;
import mc.alk.arena.objects.MatchState;
import mc.alk.arena.objects.arenas.ArenaListener;
import mc.alk.arena.objects.exceptions.NeverWouldJoinException;
import mc.alk.arena.objects.joining.MatchTeamQObject;
import mc.alk.arena.objects.options.TransitionOption;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.MyArena;

import java.util.Collection;

public class MyWaitingObject {
    protected boolean joinable = true;
    protected final AbstractJoinHandler jh;
    protected final MatchParams params;
    protected final MyQueueObject originalQueuedObject;
    protected final MyArena arena;

    public MyWaitingObject(MyQueueObject qo) throws NeverWouldJoinException {
        this.params = qo.getMatchParams();
        this.originalQueuedObject = qo;
        this.arena = qo.getJoinOptions().getArena();
        if (qo instanceof MyMatchTeamQObject){
            this.jh = TeamJoinFactory.createTeamJoinHandler(qo.getMatchParams(), qo.getTeams());
            this.joinable = false;
        } else {
            this.jh = TeamJoinFactory.createTeamJoinHandler(qo.getMatchParams());
            this.joinable = true;
        }
    }

//    public boolean matches(QueueObject qo) {
//        return joinable &&
//                (arena != null ?
//                        arena.matches(qo.getMatchParams(), qo.getJoinOptions()) :
//                        params.matchesIgnoreNTeams(qo.getMatchParams()));
//    }
public boolean matches(MyQueueObject qo) {
        return joinable && (arena != null ?
                        arena.matches(qo.getJoinOptions()) :
                        params.matches(qo.getJoinOptions()));
    }
    public AbstractJoinHandler.TeamJoinResult join(MyTeamJoinObject qo) {
        return jh.joiningTeam(qo);
    }

    public boolean hasEnough() {
        return jh.hasEnough(params.getAllowedTeamSizeDifference());
    }

    public boolean isFull() {
        return jh.isFull();
    }

    public MatchParams getParams() {
        return params;
    }

    public MyArena getArena() {
        return arena;
    }

    public Collection<ArenaPlayer> getPlayers() {
        return jh.getPlayers();
    }

    public MyJoinOptions getJoinOptions() {
        return this.originalQueuedObject.getJoinOptions();
    }

    public Collection<ArenaListener> getArenaListeners(){
        return this.originalQueuedObject.getListeners();
    }

    public MyQueueObject getOriginalQueuedObject() {
        return originalQueuedObject;
    }

    public String toString() {
        return "[WO " + (arena != null ? arena.getName() : "") + " " + params.getDisplayName() + "]";
    }

    public boolean createsOnJoin() {
        MyArena a = originalQueuedObject.getArena();
        if (a != null) {
            return a.getParams().hasOptionAt(MatchState.ONJOIN, TransitionOption.TELEPORTIN) ||
                    params.hasOptionAt(MatchState.ONJOIN, TransitionOption.TELEPORTIN);
        }
        return params.hasOptionAt(MatchState.ONJOIN, TransitionOption.TELEPORTIN);
    }
}
