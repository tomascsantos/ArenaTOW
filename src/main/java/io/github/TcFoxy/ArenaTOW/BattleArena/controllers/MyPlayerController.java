package io.github.TcFoxy.ArenaTOW.BattleArena.controllers;

import mc.alk.arena.util.PlayerUtil;
import mc.alk.arena.util.ServerUtil;

import org.bukkit.entity.Player;

import io.github.TcFoxy.ArenaTOW.BattleArena.MyDefaults;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MyArenaPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class MyPlayerController {
	private static HashMap<UUID,MyArenaPlayer> players = new HashMap<UUID,MyArenaPlayer>();

	/**
	 * wrap a player into an MyArenaPlayer
	 * @param player Bukkit player
	 * @return MyArenaPlayer
	 */
	public static MyArenaPlayer toArenaPlayer(Player player){
		MyArenaPlayer ap = players.get(PlayerUtil.getID(player));
		if (MyDefaults.DEBUG_VIRTUAL) {
			Player p2 = ServerUtil.findPlayerExact(player.getName());
			if (p2 != null)
				player = p2;
		}
		if (ap == null){
			ap = new MyArenaPlayer(player);
			players.put(ap.getID(), ap);
		} else{
                    if (player != null) {
                        ap.setPlayer(player);
                    }
		}
		return ap;
	}
    public static MyArenaPlayer toArenaPlayer(UUID id){
        MyArenaPlayer ap = players.get(id);
        Player player = ServerUtil.findPlayer(id);
        if (MyDefaults.DEBUG_VIRTUAL && player == null) {
            Player p2 = ServerUtil.findPlayer(id);
            if (p2 != null)
                player = p2;
        }
        if (ap == null){
            ap = player == null ? new MyArenaPlayer(id) : new MyArenaPlayer(player);
            players.put(ap.getID(), ap);
        } else if (player != null) {
            ap.setPlayer(player);
        }
        return ap;
    }
//
//    public static MyArenaPlayer getArenaPlayer(String playerName) {
//        return players.get(playerName);
//    }

	/**
	 * Returns the ArenaPlayer for the given player
	 * @param player Bukkit player
	 * @return player if found, null otherwise
	 */
	public static MyArenaPlayer getArenaPlayer(Player player){
		return players.get(PlayerUtil.getID(player));
	}

	public static boolean hasArenaPlayer(Player player){
		return players.containsKey(PlayerUtil.getID(player));
	}

	public static List<MyArenaPlayer> toArenaPlayerList(Collection<Player> players){
		List<MyArenaPlayer> aplayers = new ArrayList<MyArenaPlayer>(players.size());
		for (Player p: players)
			aplayers.add(toArenaPlayer(p));
		return aplayers;
	}

	public static Set<MyArenaPlayer> toArenaPlayerSet(Collection<Player> players){
		Set<MyArenaPlayer> aplayers = new HashSet<MyArenaPlayer>(players.size());
		for (Player p: players)
			aplayers.add(toArenaPlayer(p));
		return aplayers;
	}

	public static Set<Player> toPlayerSet(Collection<MyArenaPlayer> arenaPlayers) {
		Set<Player> players = new HashSet<Player>(arenaPlayers.size());
		for (MyArenaPlayer ap: arenaPlayers)
			players.add(ap.getPlayer());
		return players;
	}

	public static List<Player> toPlayerList(Collection<MyArenaPlayer> arenaPlayers) {
		List<Player> players = new ArrayList<Player>(arenaPlayers.size());
		for (MyArenaPlayer ap: arenaPlayers)
			players.add(ap.getPlayer());
		return players;
	}

    public static List<Player> UUIDToPlayerList(Collection<UUID> uuids) {
        List<Player> players = new ArrayList<Player>(uuids.size());
        for (UUID id : uuids)
            players.add(ServerUtil.findPlayer(id));
        return players;
    }

    public static void clearArenaPlayers(){
		players.clear();
	}
}
