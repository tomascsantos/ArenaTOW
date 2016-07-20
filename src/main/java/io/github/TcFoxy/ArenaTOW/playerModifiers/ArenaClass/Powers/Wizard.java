package io.github.TcFoxy.ArenaTOW.playerModifiers.ArenaClass.Powers;

import io.github.TcFoxy.ArenaTOW.playerModifiers.ArenaClass.ArenaClassController;

import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;

public class Wizard {
	

	ArenaClassController ACC;
	
	public Wizard(ArenaClassController controller){
		ACC = controller;
	}

	public void mainEffect(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(ACC.checkCooldown(p, 3)){
			Fireball f = p.launchProjectile(Fireball.class);
			f.setShooter(p);
			f.setVelocity(p.getLocation().getDirection().multiply(2));
			f.setIsIncendiary(true);
		}
	}

	@EventHandler
	public void fireballhit(EntityDamageByEntityEvent event){
		if(event.getCause() == DamageCause.PROJECTILE){
			if(event.getDamager() instanceof Fireball){
				Fireball fireball = (Fireball) event.getDamager();
				if(fireball.getShooter() instanceof Player){
					Player p = (Player) fireball.getShooter();
					if(ACC.sameMobTeam(p, event.getEntity())){
						event.setCancelled(true);
					}
				}
			}
		}
	}
}
