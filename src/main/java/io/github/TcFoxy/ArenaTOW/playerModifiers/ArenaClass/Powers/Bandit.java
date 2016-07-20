package io.github.TcFoxy.ArenaTOW.playerModifiers.ArenaClass.Powers;

import io.github.TcFoxy.ArenaTOW.ArenaTOW;
import io.github.TcFoxy.ArenaTOW.Utils;
import io.github.TcFoxy.ArenaTOW.playerModifiers.ArenaClass.ArenaClassController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import mc.alk.arena.BattleArena;
import mc.alk.arena.objects.ArenaPlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Bandit implements Listener{
	
	ArenaClassController ACC;
	
	public Bandit(ArenaClassController controller){
		ACC = controller;
	}
	
	
	public static ArrayList<UUID> onhit = new ArrayList<UUID>();
	public static HashMap<UUID,Integer> invisibletime = new HashMap<UUID, Integer>();
	int invisibleID;

	public void mainEffect(PlayerInteractEvent event) {
		final Player p = event.getPlayer();
		if(ACC.checkCooldown(p, 20)){
			p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Utils.TPS*10, 10));
			onhit.add(p.getUniqueId());
			endMobTarget(p);
			invisibletime.put(p.getUniqueId(), 10);
			for(Player enemy: Bukkit.getOnlinePlayers()){
				enemy.hidePlayer(p);
			}
			invisibleID = Bukkit.getScheduler().scheduleSyncRepeatingTask(ArenaTOW.getSelf(), new Runnable(){
				@Override
				public void run() {
					doInvisibleMessage(p);
				}
			}, 0, 20);
		}
	}

	public void doInvisibleMessage(Player p){
		int currenttime = invisibletime.get(p.getUniqueId());
		if(currenttime == 0){
			Bukkit.getScheduler().cancelTask(invisibleID);
			invisibletime.remove(p.getUniqueId());
		}
		Utils.sendTitle(p, 5, 20, 3, "&5You are Invisible", currenttime + " seconds left!");
		invisibletime.remove(p.getUniqueId());
		invisibletime.put(p.getUniqueId(), currenttime-1);

	}

	public void endMobTarget(Player p){
		List<Entity> entities = p.getNearbyEntities(20, 7, 20);
		for(Entity e:entities){
			if((e instanceof Zombie) || (e instanceof IronGolem) || (e instanceof Guardian)){
				Creature creature = (Creature) e;
				if(creature.getTarget() == p){
					creature.setTarget(null);
				}
			}
		}
	}

	private void endInvisible(Integer time, final Player p) {
		Bukkit.getScheduler().runTaskLater(ArenaTOW.getSelf(), new Runnable() {
			@Override
			public void run() {
				if(onhit.contains(p.getUniqueId())){
					onhit.remove(p.getUniqueId());
					Bukkit.getScheduler().cancelTask(invisibleID);
					Utils.sendTitle(p, 5, 40, 3, "&5You are NOW VISIBLE!!!", "I would run...");
					for(Player enemy: Bukkit.getOnlinePlayers()){
						enemy.showPlayer(p);
					}
					if(p.hasPotionEffect(PotionEffectType.INVISIBILITY)){
						p.removePotionEffect(PotionEffectType.INVISIBILITY);
					}
				}

			}
		}, time*Utils.TPS);
	}

	@EventHandler
	public void onhitend(EntityDamageByEntityEvent event){
		if(onhit.contains(event.getDamager().getUniqueId())){
			Player p = (Player) event.getDamager();
			ArenaPlayer ap = BattleArena.toArenaPlayer(p);
			endInvisible(0, p);
		}

	}
}
