package io.github.TcFoxy.ArenaTOW.BattleArena.listeners;


import java.util.Comparator;

import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import io.github.TcFoxy.ArenaTOW.BattleArena.events.players.ArenaPlayerEnterQueueEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.events.players.ArenaPlayerLeaveQueueEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.matches.MatchFinishedEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.matches.MatchStartEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaSize;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.Arena;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.signs.ArenaCommandSign;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.MapOfTreeSet;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.MessageUtil;

public class SignUpdateListener implements Listener{
    MapOfTreeSet<String,ArenaCommandSign> arenaSigns =
            new MapOfTreeSet<String, ArenaCommandSign>(ArenaCommandSign.class, new Comparator<ArenaCommandSign>(){
                @Override
                public int compare(ArenaCommandSign o1, ArenaCommandSign o2) {
                    return o1.hashCode() - o2.hashCode();
                }
            });


    private String getMatchState(String str){
        if (str != null && (str.startsWith("\\d") || str.indexOf(' ') > 0 )){
            int index = str.indexOf(' ');
            return index != -1 ? str.substring(0, index) : str;
        } else {
            return "Open";
        }
    }

    private String getQCount(String str){
        if (str != null && (str.startsWith("\\d") || str.indexOf(' ') > 0 )){
            int index = str.indexOf(' ');
            return index != -1 ? str.substring(index+1, str.length()) : str;
        } else {
            return "";
        }
    }

    private void setPeopleInQueue(Arena arena, int playersInQueue,
                                  int neededPlayers, int maxPlayers) {
        ArenaCommandSign[] signLocs = arenaSigns.getSafe(arena.getName());
        if (signLocs == null || signLocs.length == 0){
            return;
        }
        final String strcount;
        if (neededPlayers == maxPlayers){
            strcount = (neededPlayers == ArenaSize.MAX) ?
                    playersInQueue +"&6/\u221E" : playersInQueue+"&6/"+neededPlayers;
        } else {
            strcount =(maxPlayers == ArenaSize.MAX) ?
                    playersInQueue +"&6/"+neededPlayers+"/\u221E" : playersInQueue+"&6/"+neededPlayers+"/"+maxPlayers;
        }
        for (ArenaCommandSign l : signLocs){
            Sign s = l.getSign();
            if (s == null)
                continue;
            s.setLine(3, MessageUtil.colorChat(getMatchState(s.getLine(3))+" " + strcount));
            s.update();
        }
    }

    private void setMatchState(Arena arena, String state) {
        ArenaCommandSign[] signLocs = arenaSigns.getSafe(arena.getName());
        if (signLocs == null || signLocs.length==0){
            return;
        }
        for (ArenaCommandSign l : signLocs){
            Sign s = l.getSign();
            if (s == null)
                continue;
            s.setLine(3, MessageUtil.colorChat(state + " "+ getQCount(s.getLine(3))));
            s.update();
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onMatchStartEvent(MatchStartEvent event){
        setMatchState(event.getMatch().getArena(), "Active");
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onMatchFinishedEvent(MatchFinishedEvent event){
        setMatchState(event.getMatch().getArena(), "Open");
    }

    @EventHandler
    public void onArenaPlayerEnterQueueEvent(ArenaPlayerEnterQueueEvent event){
        if (event.getArena() == null) return;
        int size = event.getQueueResult().playersInQueue;
        setPeopleInQueue(event.getArena(), size,
                event.getQueueResult().params.getMinPlayers(),
                event.getQueueResult().maxPlayers);
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onArenaPlayerLeaveQueueEvent(ArenaPlayerLeaveQueueEvent event){
        if (event.getArena() == null) return;
        int size = event.getPlayersInArenaQueue(event.getArena());
        setPeopleInQueue(event.getArena(), size,
                event.getParams().getMinPlayers(),
                event.getParams().getMaxPlayers());
    }

    public void addSign(ArenaCommandSign acs) {
        if (acs.getSign() == null || acs.getOption1() == null){
            return;}
        Arena a = acs.getArena();
        if (a == null)
            return;
        arenaSigns.add(a.getName(), acs);
    }

    public void updateAllSigns() {
    }

    public MapOfTreeSet<String, ArenaCommandSign> getStatusSigns() {
        return arenaSigns;
    }

}
