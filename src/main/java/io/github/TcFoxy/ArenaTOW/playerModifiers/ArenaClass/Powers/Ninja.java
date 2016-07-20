package io.github.TcFoxy.ArenaTOW.playerModifiers.ArenaClass.Powers;

import io.github.TcFoxy.ArenaTOW.Utils;
import io.github.TcFoxy.ArenaTOW.playerModifiers.ArenaClass.ArenaClassController;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class Ninja implements Listener{
	
	ArenaClassController ACC;
	
	public Ninja(ArenaClassController controller){
		ACC = controller;
	}

	public void mainEffect(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(ACC.checkCooldown(p, 3)){
			Location l = p.getLocation();
			Location newloc = Utils.getTargetBlock(p, 15).getLocation().add(0, 1, 0);
			newloc.setPitch(l.getPitch());
			newloc.setYaw(l.getYaw());
			p.teleport(newloc);
			ACC.nofalldamage.add(p.getUniqueId());
			p.playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 2, 2);
		}
	}
}
