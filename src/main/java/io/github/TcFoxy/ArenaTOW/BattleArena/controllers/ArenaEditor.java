package io.github.TcFoxy.ArenaTOW.BattleArena.controllers;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.Arena;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.PlayerUtil;




public class ArenaEditor implements Listener{
    int nListening=0;
    Integer timerID;
    HashMap<UUID, CurrentSelection> selections = new HashMap<UUID,CurrentSelection>();

    public class CurrentSelection  {
        public long lastUsed;
        public Arena arena;
        public Long listeningIndex;

        CurrentSelection(long used, Arena arena){
            this.lastUsed = used; this.arena = arena;
        }
        public void updateCurrentSelection(){
            lastUsed = System.currentTimeMillis();
        }
        public long getLastUsed() {
            return lastUsed;
        }

        public void setLastUsed(long lastUsed) {
            this.lastUsed = lastUsed;
        }

        public Arena getArena() {
            return arena;
        }

        public void setArena(Arena arena) {
            this.arena = arena;
        }
    }



    public void setCurrentArena(CommandSender p, Arena arena) {
        UUID id = PlayerUtil.getID(p);
        if (selections.containsKey(id)) {
            CurrentSelection cs = selections.get(id);
            cs.setLastUsed(System.currentTimeMillis());
            cs.setArena(arena);
        } else {
            CurrentSelection cs = new CurrentSelection(System.currentTimeMillis(), arena);
            selections.put(id, cs);
        }
    }

    public Arena getArena(CommandSender p) {
        CurrentSelection cs = selections.get(PlayerUtil.getID(p));
        if (cs == null)
            return null;
        return cs.arena;
    }


    public CurrentSelection getCurrentSelection(CommandSender sender) {
        return selections.get(PlayerUtil.getID(sender));
    }
}
