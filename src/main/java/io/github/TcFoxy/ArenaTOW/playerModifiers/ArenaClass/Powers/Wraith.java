package io.github.TcFoxy.ArenaTOW.playerModifiers.ArenaClass.Powers;

import io.github.TcFoxy.ArenaTOW.ArenaTOW;
import io.github.TcFoxy.ArenaTOW.Utils;
import io.github.TcFoxy.ArenaTOW.playerModifiers.ArenaClass.ArenaClassController;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Wraith implements Listener {
	
	ArenaClassController ACC;
	
	public Wraith(ArenaClassController controller){
		ACC = controller;
		hasshot = new HashMap<UUID, Integer>();
	}
	

	int wraith;
	public HashMap<UUID, Integer> hasshot = new HashMap<UUID, Integer>();


	public void mainEffect(PlayerInteractEvent event){
		final Player p = event.getPlayer();
		if(checkFirstTime(p)){
			ACC.nofalldamage.add(p.getUniqueId());
			p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 8*Utils.TPS, 3));

			for(Player enemy: Bukkit.getOnlinePlayers()){
				enemy.hidePlayer(p);
			}
			Vector v = p.getLocation().getDirection().multiply(new Vector(3.5,1,3.5));
			v.add(new Vector(0.0, .7, 0.0));
			p.setVelocity(v);

			wraith = Bukkit.getScheduler().scheduleSyncRepeatingTask(ArenaTOW.getSelf(), new Runnable(){
				@Override
				public void run() {
					if(p.isOnGround()){
						if(p.hasPotionEffect(PotionEffectType.INVISIBILITY)){
							p.removePotionEffect(PotionEffectType.INVISIBILITY);
							for(Player enemy: Bukkit.getOnlinePlayers()){
								enemy.showPlayer(p);
							}
							Bukkit.getScheduler().cancelTask(wraith);
						}
					}
				}
			}, 10, 10);
		}

	}

	public boolean checkFirstTime(Player p){
		UUID pID = p.getUniqueId();
		if(!hasshot.containsKey(pID)){
			hasshot.put(pID, 1);
		}
		switch(hasshot.get(pID)){
		case 1:
			hasshot.put(pID, 2);
			return true;
		case 2:
			Boolean returned = ACC.checkCooldown(p, 15);
			hasshot.put(pID, 3);
			return returned;
		case 3:
			if(ACC.getCooldown(p, 15)<=0){
				hasshot.put(pID, 2);
				return true;
			}
		}
		return false;

	}
}
