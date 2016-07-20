package io.github.TcFoxy.ArenaTOW.playerModifiers.ArenaClass.Powers;

import io.github.TcFoxy.ArenaTOW.ArenaTOW;
import io.github.TcFoxy.ArenaTOW.Utils;
import io.github.TcFoxy.ArenaTOW.playerModifiers.ArenaClass.ArenaClassController;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class Titan {
	
	ArenaClassController ACC;
	
	public Titan(ArenaClassController controller){
		ACC = controller;
	}

	int superstun;
	List<Entity>entities;
	
	public void mainEffect(PlayerInteractEvent event){
		final Player p = event.getPlayer();
		if(ACC.checkCooldown(p, 10)){
			ACC.nofalldamage.add(p.getUniqueId());
			Vector v = p.getLocation().getDirection().multiply(new Vector(2.5,1,2.5));
			v.add(new Vector(0.0, .7, 0.0));
			p.setVelocity(v);

			superstun = Bukkit.getScheduler().scheduleSyncRepeatingTask(ArenaTOW.getSelf(), new Runnable(){
				@Override
				public void run() {
					if(p.isOnGround()){
						Utils.sendTitle(p, 5, 20, 3, "&5ME SMASH!!!", "");
						entities = p.getNearbyEntities(5, 5, 5);
						if(entities==null){
							return;
						}
						locs = new HashMap<Entity, Location>();
						for(Entity e: entities){
							e.setCustomName("Stunned!");
							e.setCustomNameVisible(true);
							locs.put(e, e.getLocation());
						}
						stunner(locs);
						cancelStunner(4);
						Bukkit.getScheduler().cancelTask(superstun);

					}
				}
			}, 5, 1);
		}
	}
	
	int stunnerId;
	HashMap<Entity,Location> locs;
	
	public void stunner(final HashMap<Entity, Location> locs){
		stunnerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(ArenaTOW.getSelf(), new Runnable(){
			@Override
			public void run() {
				for(Entry<Entity, Location> e: locs.entrySet()){
					Entity ent = e.getKey(); 
					ent.teleport(e.getValue());
					ACC.nohit.put(ent.getUniqueId(), ChatColor.DARK_RED + "You have been Stunned!");
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
