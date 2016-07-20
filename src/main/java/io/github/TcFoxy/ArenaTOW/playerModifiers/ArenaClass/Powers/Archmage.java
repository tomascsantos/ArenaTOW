package io.github.TcFoxy.ArenaTOW.playerModifiers.ArenaClass.Powers;

import io.github.TcFoxy.ArenaTOW.Utils;
import io.github.TcFoxy.ArenaTOW.playerModifiers.ArenaClass.ArenaClassController;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Archmage implements Listener{
	
	private ArenaClassController ACC;

	public Archmage(ArenaClassController controller){
		ACC = controller;
	}

	public void mainEffect(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(ACC.checkCooldown(p, 10)){
			Block b = Utils.getTargetBlock(p, 30);
			Location loc = b.getLocation();
			for(int i = 0; i < 5 ; i++){
				LightningStrike strike = loc.getWorld().strikeLightning(loc);
				strike.setCustomName(p.getName());
			}
		}

	}	
	@EventHandler(priority = EventPriority.HIGHEST)
	private void lightningdamage(EntityDamageByEntityEvent event){
		if(event.getDamager() instanceof LightningStrike){
			LightningStrike strike = (LightningStrike) event.getDamager();
			String strikename = strike.getCustomName();
			if(strikename == null){return;}
			Player p = Bukkit.getPlayer(strikename);
			if(ACC.sameMobTeam(p, event.getEntity())){
				event.setCancelled(true);
			}else{
				event.setDamage(8);
			}


		}
	}
}
