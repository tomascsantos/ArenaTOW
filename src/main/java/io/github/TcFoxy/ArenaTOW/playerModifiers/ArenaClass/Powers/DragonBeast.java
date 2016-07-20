package io.github.TcFoxy.ArenaTOW.playerModifiers.ArenaClass.Powers;

import io.github.TcFoxy.ArenaTOW.ArenaTOW;
import io.github.TcFoxy.ArenaTOW.Utils;
import io.github.TcFoxy.ArenaTOW.playerModifiers.ArenaClass.ArenaClassController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import mc.alk.arena.BattleArena;
import mc.alk.arena.objects.ArenaPlayer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class DragonBeast implements Listener{
	
	ArenaClassController ACC;
	
	public DragonBeast(ArenaClassController controller){
		ACC = controller;
	}

	public static ArrayList<UUID> lavaplayer = new ArrayList<UUID>();


	public void mainEffect(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(ACC.checkCooldown(p,15)){
			for(int i =2;i<5;i++){
				final Set<Location> locs = Utils.circle(p.getLocation(), i, false);
				setLava(i, locs, p);
			}
			ArenaPlayer ap = BattleArena.toArenaPlayer(p);
			for(Player pa: ap.getTeam().getBukkitPlayers()){
				lavaplayer.add(pa.getUniqueId());
			}
		}
	}

	public void setLava(final Integer i, final Set<Location> locs, final Player p){
		Bukkit.getScheduler().runTaskLater(ArenaTOW.getSelf(), new Runnable() {
			@Override
			public void run() {
				for(Location location : (locs)){
					if(location.getBlock().getType() == Material.AIR){
						location.getBlock().setType(Material.STATIONARY_LAVA);
						cleanLava(locs, i, p);
					}
				}
				List<Entity> entities= p.getNearbyEntities(i, i, i);
				for(Entity e:entities){
					if(ACC.sameMobTeam(p, e)){
						lavaplayer.add(e.getUniqueId());
					}else{
						Vector initial = e.getLocation().getDirection();
						e.setVelocity(initial.multiply(-1).multiply(.5));
					}

				}

			}

		}, (i*2));
	}

	public void cleanLava(final Set<Location> locs, Integer i, final Player p){
		Bukkit.getScheduler().runTaskLater(ArenaTOW.getSelf(), new Runnable() {
			@Override
			public void run() {
				for(Location location : (locs)){
					if((location.getBlock().getType() == Material.STATIONARY_LAVA) 
							|| (location.getBlock().getType() == Material.LAVA)){
						location.getBlock().setType(Material.AIR);
					}
				}
			}
		}, (i*2)+3);
	}

	public void allowFireDamage(final Entity e){
		Bukkit.getScheduler().runTaskLater(ArenaTOW.getSelf(), new Runnable() {
			@Override
			public void run() {
				lavaplayer.remove(e.getUniqueId());
			}

		}, 5*Utils.TPS);
	}

	@EventHandler
	public void noLavaDamage(EntityDamageEvent event){
		if((event.getCause() == DamageCause.LAVA) || (event.getCause() == DamageCause.FIRE) || (event.getCause() == DamageCause.FIRE_TICK)){
			Entity e = event.getEntity();
			if(event.getCause()==DamageCause.LAVA){
				event.setDamage(7);
			}
			if(lavaplayer.contains(e.getUniqueId())){
				e.setFireTicks(0);
				event.setCancelled(true);
				allowFireDamage(e);
			}
		}
	}
}
