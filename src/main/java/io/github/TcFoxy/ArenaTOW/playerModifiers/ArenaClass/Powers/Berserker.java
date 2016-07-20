package io.github.TcFoxy.ArenaTOW.playerModifiers.ArenaClass.Powers;

import io.github.TcFoxy.ArenaTOW.ArenaTOW;
import io.github.TcFoxy.ArenaTOW.Utils;
import io.github.TcFoxy.ArenaTOW.playerModifiers.ArenaClass.ArenaClassController;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.player.PlayerInteractEvent;

public class Berserker {//No Listerner?
	
	ArenaClassController ACC;
	
	public Berserker(ArenaClassController controller){
		ACC = controller;
	}

	HashMap<Entity,Location> locs;
	List<Entity>entities;
	public void mainEffect(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(ACC.checkCooldown(p, 8)){
			entities = p.getNearbyEntities(3, 3, 3);
			if(entities==null){
				return;
			}
			locs = new HashMap<Entity, Location>();
			for(Entity e: entities){
				if((e instanceof Zombie) || (e instanceof IronGolem) || (e instanceof Guardian)){
					if(!ACC.sameMobTeam(p, e)){
						e.setCustomName("Stunned!");
						e.setCustomNameVisible(true);
						locs.put(e, e.getLocation());
					}
				}	
			}
			stunner(locs);
			cancelStunner(6);
		}
	}
	int stunnerId;
	public void stunner(final HashMap<Entity, Location> locs){
		stunnerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(ArenaTOW.getSelf(), new Runnable(){
			@Override
			public void run() {
				for(Entry<Entity, Location> e: locs.entrySet()){
					Entity ent = e.getKey(); 
					ent.teleport(e.getValue());
					ACC.nohit.put(ent.getUniqueId(), "You have been Stunned!");
				}
			}
		}, 0, 1);
	}
	
	private void cancelStunner(Integer time) {
		Bukkit.getScheduler().runTaskLater(ArenaTOW.getSelf(), new Runnable() {
			@Override
			public void run() {
				for(Entity e:entities){
					e.setCustomName(null);
					ACC.nohit.remove(e.getUniqueId());
				}
				entities.clear();
				Bukkit.getScheduler().cancelTask(stunnerId);
			}

		}, time*Utils.TPS);
	}
}
