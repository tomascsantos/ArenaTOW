package io.github.TcFoxy.ArenaTOW.BattleArena.controllers;

import io.github.TcFoxy.ArenaTOW.BattleArena.MyBattleArena;
import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.MyMatch;
import io.github.TcFoxy.ArenaTOW.BattleArena.matches.MyMatchFinishedEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.matches.MyMatchOpenEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.MyArena;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.joining.MyArenaMatchQueue;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.joining.MyJoinOptions;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.joining.MyTeamJoinObject;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.joining.MyWaitingObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import mc.alk.arena.Defaults;
import mc.alk.arena.bukkit.BukkitInterface;
import mc.alk.arena.controllers.RoomController;
import mc.alk.arena.controllers.containers.GameManager;
import mc.alk.arena.controllers.containers.RoomContainer;
import mc.alk.arena.controllers.joining.AbstractJoinHandler;
import mc.alk.arena.events.matches.MatchFinishedEvent;
import mc.alk.arena.listeners.SignUpdateListener;
import mc.alk.arena.listeners.custom.MethodController;
import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.ContainerState;
import mc.alk.arena.objects.MatchParams;
import mc.alk.arena.objects.MatchState;
import mc.alk.arena.objects.arenas.Arena;
import mc.alk.arena.objects.arenas.ArenaControllerInterface;
import mc.alk.arena.objects.arenas.ArenaListener;
import mc.alk.arena.objects.arenas.ArenaType;
import mc.alk.arena.objects.events.ArenaEventHandler;
import mc.alk.arena.objects.exceptions.MatchCreationException;
import mc.alk.arena.objects.exceptions.NeverWouldJoinException;
import mc.alk.arena.objects.joining.MatchTeamQObject;
import mc.alk.arena.objects.options.EventOpenOptions;
import mc.alk.arena.objects.options.EventOpenOptions.EventOpenOption;
import mc.alk.arena.objects.options.TransitionOption;
import mc.alk.arena.objects.pairs.JoinResult;
import mc.alk.arena.objects.pairs.JoinResult.JoinStatus;
import mc.alk.arena.objects.teams.ArenaTeam;
import mc.alk.arena.util.Log;
import mc.alk.arena.util.PlayerUtil;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class MyBattleArenaController implements ArenaListener, Listener{

    private boolean stop = false;

    final private Set<MyMatch> running_matches = Collections.synchronizedSet(new CopyOnWriteArraySet<MyMatch>());
    final private Map<ArenaType,List<MyMatch>> unfilled_matches =new HashMap<ArenaType,List<MyMatch>>();
    private Map<String, MyArena> allarenas = new ConcurrentHashMap<String, MyArena>();
    final private Map<ArenaType,OldLobbyState> oldLobbyState = new HashMap<ArenaType,OldLobbyState>();
    private final MyArenaMatchQueue amq = new MyArenaMatchQueue();
    final SignUpdateListener signUpdateListener;

    final private Map<ArenaType, MyArena> fixedArenas = new HashMap<ArenaType, MyArena>();


    public class OldLobbyState{
        ContainerState pcs;
        Set<MyMatch> running = new HashSet<MyMatch>();
        public boolean isEmpty() {return running.isEmpty();}
        public void add(MyMatch am){running.add(am);}
        public boolean remove(MyMatch am) {return running.remove(am);}
    }


    public MyBattleArenaController(SignUpdateListener signUpdateListener){
        MethodController methodController = new MethodController("BAC");
        methodController.addAllEvents(this);
        try{Bukkit.getPluginManager().registerEvents(this, MyBattleArena.getSelf());}catch(Exception e){/* keep on truckin'*/}
        this.signUpdateListener = signUpdateListener;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMatchOpenEvent(MyMatchOpenEvent event) {
        MyMatch match = event.getMatch();
        match.addArenaListener(this);
        synchronized (running_matches) {
            running_matches.add(match);
        }
        if (match.isJoinablePostCreate()){
            List<MyMatch> matches = unfilled_matches.get(match.getParams().getType());
            if (matches == null) {
                matches = new CopyOnWriteArrayList<MyMatch>();
                unfilled_matches.put(match.getParams().getType(), matches);
            }
            matches.add(0, match);
        } else {
            removeFixedReservedArena(match.getArena());
        }
    }

    private void setFixedReservedArena(MyArena arena) {
        fixedArenas.put(arena.getArenaType(), arena);
    }

    private void removeFixedReservedArena(MyArena arena) {
        MyArena a = fixedArenas.get(arena.getArenaType());
        if (a != null && a.matches(arena)) {
            fixedArenas.remove(arena.getArenaType());
        }
    }

    public MyMatch createAndAutoMatch(MyArena arena, EventOpenOptions eoo)
            throws NeverWouldJoinException, IllegalStateException, MatchCreationException {
        MatchParams mp = eoo.getParams();
        MatchParams oldArenaParams = arena.getParams();

        mp.setForceStartTime(eoo.getSecTillStart());
        /// Since we want people to add this event, add this arena as the next
        setFixedReservedArena(arena);

        MyMatch m = amq.createMatch(arena, eoo);
        m.setOldArenaParams(oldArenaParams);
        saveStates(m,arena);
        arena.setAllContainerState(ContainerState.OPEN);
        m.setTimedStart(eoo.getSecTillStart(), eoo.getInterval());
        amq.incNumberOpenMatches(mp.getType());

        if (eoo.hasOption(EventOpenOption.FORCEJOIN)){
            addAllOnline(m.getParams(), arena);}

        return m;
    }

    private void addAllOnline(MatchParams mp, MyArena arena) {
        String cmd = mp.getCommand() +" add "+arena.getName();
        for (Player p: BukkitInterface.getOnlinePlayers()){
            PlayerUtil.doCommand(p, cmd);
        }
    }

    private void saveStates(MyMatch m, MyArena arena) {
        /// save the old states to put back after the match
        if (RoomController.hasLobby(arena.getArenaType())){
            RoomContainer pc = RoomController.getLobby(arena.getArenaType());
            OldLobbyState ols = oldLobbyState.get(arena.getArenaType());
            if (ols == null){
                ols = new OldLobbyState();
                ols.pcs = pc.getContainerState();
                oldLobbyState.put(arena.getArenaType(), ols);
            }
            ols.add(m);
        }
    }


    private void restoreStates(MyMatch am, MyArena arena){
        if (arena == null)
            arena = am.getArena();
        OldLobbyState ols = oldLobbyState.get(arena.getArenaType());
        if (ols != null){ /// we only put back the lobby state if its the last autoed match finishing
            if (ols.remove(am) && ols.isEmpty()){
                RoomController.getLobby(am.getArena().getArenaType()).setContainerState(ols.pcs);
            }
        }
    }

    public void startMatch(MyMatch arenaMatch) {
        /// arenaMatch run calls.... broadcastMessage ( which unfortunately is not thread safe)
        /// So we have to schedule a sync task... again
        Bukkit.getScheduler().scheduleSyncDelayedTask(MyBattleArena.getSelf(), arenaMatch);
    }


    @ArenaEventHandler
    public void matchFinished(MyMatchFinishedEvent event){
        if (Defaults.DEBUG ) Log.info("BattleArenaController::matchFinished=" + this + ":" );
        MyMatch am = event.getMatch();
        removeMatch(am); /// handles removing running match from the BArenaController

        final MyArena arena = allarenas.get(am.getArena().getName().toUpperCase());
        if (arena == null) { /// we have deleted this arena while a match was going on
            return;}

        /// put back old states if it was autoed
        restoreStates(am, arena);
        removeFixedReservedArena(arena);
        if (am.getParams().hasOptionAt(MatchState.ONCOMPLETE, TransitionOption.REJOIN)){
            MatchParams mp = am.getParams();
            List<ArenaPlayer> players = am.getNonLeftPlayers();
            String[] args = {};
            for (ArenaPlayer ap: players){
                MyBattleArena.getBAExecutor().join(ap, mp, args);
            }
        }
        /// isEnabled to check to see if we are shutting down
        if (MyBattleArena.getSelf().isEnabled()){
            Bukkit.getScheduler().scheduleSyncDelayedTask(MyBattleArena.getSelf(), new Runnable(){
                @Override
                public void run() {
                    amq.add(arena); /// add it back into the queue
                }
            }, am.getParams().getArenaCooldown()*20L);
        }
    }

    public void updateArena(MyArena arena) {
        allarenas.put(arena.getName().toUpperCase(), arena);
        if (amq.removeArena(arena) != null){ /// if its not being used
            amq.add(arena);}
    }

    public void addArena(MyArena arena) {
        allarenas.put(arena.getName().toUpperCase(), arena);
        amq.add(arena);
    }


    public Map<String, MyArena> getArenas(){return allarenas;}

    /**
     * Add the TeamQueueing object to the queue
     * @param tqo the TeamQueueing object to the queue
     * @return JoinResult
     */
    public JoinResult wantsToJoin(MyTeamJoinObject tqo) throws IllegalStateException {

        /// Can they add an existing Game
        JoinResult jr = joinExistingMatch(tqo);
        if (jr.status == JoinResult.JoinStatus.ADDED_TO_EXISTING_MATCH) {
            return jr;
        }

        /// Add a default arena if they havent specified
        if (!tqo.getJoinOptions().hasArena()) {
            tqo.getJoinOptions().setArena(getNextArena(tqo.getJoinOptions()));
        }

        /// We don't want them to add a queue if they can't fit
        if (tqo.getJoinOptions().getArena() != null &&
                tqo.getJoinOptions().getArena().getParams().hasOptionAt(MatchState.DEFAULTS,TransitionOption.ALWAYSOPEN)){
            if (this.getArenas(tqo.getMatchParams()).size()==1 &&
                    amq.getNumberOpenMatches(tqo.getMatchParams().getType()) >= 1)
                throw new IllegalStateException("&cThe arena " +
                        tqo.getJoinOptions().getArena().getDisplayName() + "&c is currently in use");
        }
        jr = amq.join(tqo);

        MatchParams mp = tqo.getMatchParams();
        if (jr.params == null)
            jr.params = mp;

        /// who is responsible for doing what
        if (Defaults.DEBUG)
            Log.info(" Join status = " + jr.status + "    " + tqo.getTeam() + "   " + tqo.getTeam().getId() + " --"
                    + ", haslobby=" + mp.needsLobby() + "  ,wr=" + (mp.hasOptionAt(MatchState.ONJOIN, TransitionOption.TELEPORTWAITROOM)) + "  " +
                    "   --- hasArena=" + tqo.getJoinOptions().hasArena());
        if (tqo.getJoinOptions().hasArena() && !(jr.status == JoinStatus.STARTED_NEW_GAME)) {
            MyArena a = tqo.getJoinOptions().getArena();
            if (!(a.getParams().hasOptionAt(MatchState.DEFAULTS, TransitionOption.ALWAYSOPEN) ||
                    a.getParams().hasOptionAt(MatchState.ONJOIN, TransitionOption.ALWAYSJOIN)) &&
                    mp.hasOptionAt(MatchState.ONJOIN, TransitionOption.TELEPORTIN) && MyBattleArena.getBAController().getMatch(a) != null) {
                throw new IllegalStateException("&cThe arena " + a.getDisplayName() + "&c is currently in use");
            }
        }
        switch (jr.status) {
            case ADDED_TO_ARENA_QUEUE:
            case ADDED_TO_QUEUE:
                break;
            case NONE:
                break;
            case ERROR:
            case ADDED_TO_EXISTING_MATCH:
            case STARTED_NEW_GAME:
                return jr;
            case NOTOPEN:
            case CANT_FIT:
                return jr;
            default:
                break;
        }
        if (mp.needsLobby()) {
            if (!RoomController.hasLobby(mp.getType())) {
                throw new IllegalStateException("&cLobby is not set for the " + mp.getName());
            }
            RoomController.getLobby(mp.getType()).teamJoining(tqo.getTeam());
        }
        if (tqo.getJoinOptions().hasArena()) {
            MyArena a = tqo.getJoinOptions().getArena();
            if (a.getParams().hasOptionAt(MatchState.ONJOIN, TransitionOption.TELEPORTWAITROOM)) {
                if (a.getWaitroom() == null) {
                    throw new IllegalStateException("&cWaitroom is not set for this arena");
                }
                a.getWaitroom().teamJoining(tqo.getTeam());
//            } else if (a.getParams().hasOptionAt(MatchState.ONJOIN, TransitionOption.TELEPORTSPECTATE)) {
//                if (a.getSpectatorRoom() == null) {
//                    throw new IllegalStateException("&cSpectate is not set for this arena");
//                }
//                a.getSpectatorRoom().teamJoining(tqo.getTeam());
            } else if (a.getParams().hasOptionAt(MatchState.ONJOIN, TransitionOption.TELEPORTIN)) {
                tqo.getJoinOptions().getArena().teamJoining(tqo.getTeam());
            }
        }
        for (ArenaTeam at : tqo.getTeams()) {
            for (ArenaPlayer ap : at.getPlayers()) {
                ap.getMetaData().setJoinOptions(tqo.getJoinOptions());
            }
        }
        return jr;
    }

    private JoinResult joinExistingMatch(MyTeamJoinObject tqo) {
        JoinResult jr = new JoinResult();
        jr.params = tqo.getMatchParams();
        if (unfilled_matches.isEmpty()) {
            return jr;
        }
        MatchParams params = tqo.getMatchParams();
        List<MyMatch> matches = unfilled_matches.get(params.getType());
        if (matches == null)
            return jr;
        for (MyMatch match : matches) {
            /// We dont want people joining in a non waitroom state
            if (!match.canStillJoin()) {
                continue;}
            if (!match.getParams().matches(tqo.getJoinOptions()) || !match.getArena().matches(tqo.getJoinOptions())) {
                continue;}
            AbstractJoinHandler tjh = match.getTeamJoinHandler();
            if (tjh == null)
                continue;
            AbstractJoinHandler.TeamJoinResult tjr = tjh.joiningTeam(tqo);
            switch (tjr.status) {
                case ADDED:
                case ADDED_TO_EXISTING:
                case ADDED_STILL_NEEDS_PLAYERS:
                    jr.status = JoinResult.JoinStatus.ADDED_TO_EXISTING_MATCH;
                    break;
                case CANT_FIT:
                    continue;
            }
            return jr;
        }
        return jr;
    }

    public boolean isInQue(ArenaPlayer p) {return amq.isInQue(p);}

    public void addMatchup(MatchTeamQObject m) {
        amq.join(m);
    }

    public MyArena reserveArena(MyArena arena) {return amq.reserveArena(arena);}
    public MyArena getArena(String arenaName) {return allarenas.get(arenaName.toUpperCase());}

    public MyArena removeArena(MyArena arena) {
        amq.removeArena(arena);
        allarenas.remove(arena.getName().toUpperCase());
        return arena;
    }

    public void deleteArena(MyArena arena) {
        removeArena(arena);
        ArenaControllerInterface ai = new ArenaControllerInterface(arena);
        ai.delete();
    }

    public void arenaChanged(MyArena arena){
        try{
            if (removeArena(arena) != null){
                addArena(arena);}
        } catch (Exception e){
            Log.printStackTrace(e);
        }
    }

    public MyArena getNextArena(MyJoinOptions myJoinOptions) {
        if (fixedArenas.containsKey(myJoinOptions.getMatchParams().getType()))
            return fixedArenas.get(myJoinOptions.getMatchParams().getType());
        return amq.getNextArena(myJoinOptions);
    }

    public MyArena getNextArena(MatchParams mp) {
        if (fixedArenas.containsKey(mp.getType()))
            return fixedArenas.get(mp.getType());
        return amq.getNextArena(mp);
    }

    public MyArena nextArenaByMatchParams(MatchParams mp){
        return amq.getNextArena(mp);
    }

    public MyArena getArenaByMatchParams(MatchParams jp) {
        for (Arena a : allarenas.values()){
            if (a.valid() && a.matches(jp)){
                return a;}
        }
        return null;
    }

    public MyArena getArenaByMatchParams(MyJoinOptions jp) {
        for (MyArena a : allarenas.values()){
            if (a.valid() && a.matches(jp)){
                return a;}
        }
        return null;
    }

    public List<MyArena> getArenas(MatchParams mp) {
        List<MyArena> arenas = new ArrayList<MyArena>();
        for (MyArena a : allarenas.values()){
            if (a.valid() && a.matches(mp)){
                arenas.add(a);}
        }
        return arenas;
    }

    public MyArena getArenaByNearbyMatchParams(MyJoinOptions jp) {
        MyArena possible = null;
        MatchParams mp = jp.getMatchParams();
        int sizeDif = Integer.MAX_VALUE;
        int m1 = mp.getMinTeamSize();
        for (MyArena a : allarenas.values()){
            if (a.getArenaType() != mp.getType() || !a.valid())
                continue;
            if (a.matches(jp)){
                return a;}
            int m2 = a.getParams().getMinTeamSize();
            if (m2 > m1 && m2 -m1 < sizeDif){
                sizeDif = m2 - m1;
                possible = a;
            }
        }
        return possible;
    }

    public Map<MyArena,List<String>> getNotMachingArenaReasons(MatchParams mp) {
        Map<MyArena,List<String>> reasons = new HashMap<MyArena, List<String>>();
        for (MyArena a : allarenas.values()){
            if (a.getArenaType() != mp.getType()){
                continue;
            }
            if (!a.valid()){
                reasons.put(a, a.getInvalidReasons());}
            if (!a.matches(mp)){
                List<String> rs = reasons.get(a);
                if (rs == null){
                    reasons.put(a, a.getInvalidMatchReasons(mp,null));
                } else {
                    rs.addAll(a.getInvalidMatchReasons(mp,null));
                }
            }
        }
        return reasons;
    }

    public Map<MyArena,List<String>> getNotMachingArenaReasons(MyJoinOptions jp) {
        Map<MyArena,List<String>> reasons = new HashMap<MyArena, List<String>>();
        MatchParams mp = jp.getMatchParams();
        MyArena wantedArena = jp.getArena();
        for (MyArena a : allarenas.values()){
            if (wantedArena !=null && !a.matches(wantedArena)) {
                continue;}
            if (a.getArenaType() != mp.getType()){
                continue;
            }
            if (!a.valid()){
                reasons.put(a, a.getInvalidReasons());}
            if (!a.matches(jp)){
                List<String> rs = reasons.get(a);
                if (rs == null){
                    reasons.put(a, a.getInvalidMatchReasons(mp, jp));
                } else {
                    rs.addAll(a.getInvalidMatchReasons(mp, jp));
                }
            }
        }
        return reasons;
    }

    public boolean hasArenaSize(int i) {return getArenaBySize(i) != null;}
    public MyArena getArenaBySize(int i) {
        for (MyArena a : allarenas.values()){
            if (a.getParams().matchesTeamSize(i)){
                return a;}
        }
        return null;
    }

    private void removeMatch(MyMatch am){
        synchronized(running_matches){
            running_matches.remove(am);
        }
        List<MyMatch> matches = unfilled_matches.get(am.getParams().getType());
        if (matches != null){
            matches.remove(am);
        }
    }

    public synchronized void stop() {
        if (stop)
            return;
        stop = true;
        amq.stop();
        amq.purgeQueue();
        synchronized(running_matches){
            for (MyMatch am: running_matches){
                cancelMatch(am);
                MyArena a = am.getArena();
                if (a != null){
                    MyArena arena = allarenas.get(a.getName().toUpperCase());
                    if (arena != null)
                        amq.add(arena);
                }
            }
            running_matches.clear();
        }
    }

    public void resume() {
        stop = false;
        amq.resume();
    }

    public boolean cancelMatch(MyArena arena) {
        synchronized(running_matches){
            for (MyMatch am: running_matches){
                if (am.getArena().getName().equals(arena.getName())){
                    cancelMatch(am);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean cancelMatch(ArenaPlayer p) {
        MyMatch am = getMatch(p);
        if (am==null)
            return false;
        cancelMatch(am);
        return true;
    }

    public boolean cancelMatch(ArenaTeam team) {
        Set<ArenaPlayer> ps = team.getPlayers();
        return !ps.isEmpty() && cancelMatch(ps.iterator().next());
    }

    public void cancelMatch(MyMatch am){
        am.cancelMatch();
        for (ArenaTeam t: am.getTeams()){
            t.sendMessage("&4!!!&e This arena match has been cancelled");
        }
    }

    public MyMatch getArenaMatch(MyArena a) {
        synchronized(running_matches){
            for (MyMatch am: running_matches){
                if (am.getArena().getName().equals(a.getName())){
                    return am;
                }
            }
        }
        return null;
    }

    public boolean insideArena(ArenaPlayer p) {
        synchronized(running_matches){
            for (MyMatch am: running_matches){
                if (am.isHandled(p)){
                    return true;
                }
            }
        }
        return false;
    }

    public MyMatch getMatch(ArenaPlayer p) {
        synchronized(running_matches){
            for (MyMatch am: running_matches){
                if (am.hasPlayer(p)){
                    return am;
                }
            }
        }
        return null;
    }

    public MyMatch getMatch(MyArena arena) {
        synchronized(running_matches){
            for (MyMatch am: running_matches){
                if (am.getArena().equals(arena)){
                    return am;
                }
            }
        }
        return null;
    }

    @Override
    public String toString(){
        return "[BAC]";
    }

    public String toStringQueuesAndMatches(){
        StringBuilder sb = new StringBuilder();
        sb.append("------ running  matches -------\n");
        synchronized(running_matches){
            for (MyMatch am : running_matches)
                sb.append(am).append("\n");
        }
        return sb.toString();
    }

    public String toStringArenas(){
        StringBuilder sb = new StringBuilder();
        sb.append(amq.toStringArenas());
        sb.append("------ arenas -------\n");
        for (MyArena a : allarenas.values()){
            sb.append(a).append("\n");
        }
        return sb.toString();
    }


    public void removeAllArenas() {
        synchronized(running_matches){
            for (MyMatch am: running_matches){
                am.cancelMatch();
            }
        }

        amq.stop();
        amq.removeAllArenas();
        allarenas.clear();
        amq.resume();
    }

    public void removeAllArenas(ArenaType arenaType) {
        synchronized(running_matches){
            for (MyMatch am: running_matches){
                if (am.getArena().getArenaType() == arenaType)
                    am.cancelMatch();
            }
        }

        amq.stop();
        amq.removeAllArenas(arenaType);
        Iterator<MyArena> iter = allarenas.values().iterator();
        while (iter.hasNext()){
            MyArena a = iter.next();
            if (a != null && a.getArenaType() == arenaType){
                iter.remove();}
        }
        amq.resume();
    }

    public void cancelAllArenas() {
        amq.stop();
        amq.clearTeamQueues();
        synchronized(running_matches){
            for (MyMatch am: running_matches){
                am.cancelMatch();
            }
        }
        GameManager.cancelAll();
        amq.resume();
    }


    public Collection<ArenaTeam> purgeQueue() {
        return amq.purgeQueue();
    }

    public boolean hasRunningMatches() {
        return !running_matches.isEmpty();
    }

    public MyWaitingObject getQueueObject(ArenaPlayer player) {
        return amq.getQueueObject(player);
    }

    public List<String> getInvalidQueueReasons(MyWaitingObject qo) {
        return amq.invalidReason(qo);
    }

    public boolean forceStart(MatchParams mp, boolean respectMinimumPlayers) {
        boolean started = false;
        synchronized (unfilled_matches) {
            List<MyMatch> matches = unfilled_matches.get(mp.getType());
            if (matches != null){
                for (MyMatch match : matches) {
                    if (match.isTimedStart()){
                        match.setTimedStart(0,0); /// start now! :)
                        started = true;
                    }
                }
            }
        }
        return amq.forceStart(mp, respectMinimumPlayers) || started;
    }

    public Collection<ArenaPlayer> getPlayersInAllQueues(){
        return amq.getPlayersInAllQueues();
    }
    public Collection<ArenaPlayer> getPlayersInQueue(MatchParams params){
        return amq.getPlayersInQueue(params);
    }

    public String queuesToString() {
        return amq.queuesToString();
    }

    public boolean isQueueEmpty() {
        Collection<ArenaPlayer> col = getPlayersInAllQueues();
        return col == null || col.isEmpty();
    }

    public MyArenaMatchQueue getArenaMatchQueue() {
        return amq;
    }


    public List<MyMatch> getRunningMatches(MatchParams params){
        List<MyMatch> list = new ArrayList<MyMatch>();
        synchronized(running_matches){
            for (MyMatch m: running_matches){
                if (m.getParams().getType() == params.getType()){
                    list.add(m);
                }
            }
            return list;
        }
    }

    public MyMatch getSingleMatch(MatchParams params) {
        MyMatch match = null;
        synchronized(running_matches){
            for (MyMatch m: running_matches){
                if (m.getParams().getType() == params.getType()){
                    if (match != null)
                        return null;
                    match = m;
                }
            }
        }
        return match;
    }

    public void openAll(MatchParams mp) {
        for (MyArena arena : getArenas(mp)) {
            arena.setAllContainerState(ContainerState.OPEN);
        }
    }

}
