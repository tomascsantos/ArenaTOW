package io.github.TcFoxy.ArenaTOW.playerModifiers.shop;

import java.util.HashMap;
import java.util.UUID;

import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.arenas.Arena;

import org.bukkit.entity.Player;

public class ArenaEcon {

	static HashMap<UUID, Integer> playerscash = new HashMap<UUID, Integer>();
	

	public static void onStart(Arena arena) {
		playerscash.clear();
		for(ArenaPlayer ap: arena.getMatch().getPlayers()){
			Player p = ap.getPlayer();
			if(!playerscash.containsKey(p.getUniqueId())){
				playerscash.put(p.getUniqueId(), 0);
			}
		}
	}
	
	public static Integer getCash(Player p){	
		return playerscash.get(p.getUniqueId());
	}
	
	public static void addCash(Player p, Integer amount){
		Integer cash = playerscash.get(p.getUniqueId());
		cash += amount;
		playerscash.put(p.getUniqueId(), cash);
	}
	
	public static boolean subtractCash(Player p, Integer amount){
		Integer cash = playerscash.get(p.getUniqueId());
		cash -= amount;
		//if less than zero deny transaction;
		if(cash < 0){
			return false;
		}else{
			playerscash.put(p.getUniqueId(), cash);
			return true;
		}
		
		
	}
}
