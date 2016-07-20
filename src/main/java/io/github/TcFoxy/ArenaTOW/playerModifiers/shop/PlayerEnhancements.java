package io.github.TcFoxy.ArenaTOW.playerModifiers.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import mc.alk.arena.objects.arenas.Arena;
import mc.alk.arena.objects.teams.ArenaTeam;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class PlayerEnhancements implements Listener{

	public static HashMap<UUID, Double> damageMulti;
	public static HashMap<UUID, Double> jumpMulti;
	public static ArrayList<UUID> noJump;
	public static ArrayList<UUID> nofalldamage; // no falldamage taken

	Arena arena;
	
	public PlayerEnhancements(Arena arena){
		this.arena = arena;
		damageMulti = new HashMap<UUID, Double>();
		jumpMulti = new HashMap<UUID, Double>();
		noJump = new ArrayList<UUID>();
		nofalldamage = new ArrayList<UUID>();
		}
	

	public void resetdefaultStats(){
		List<ArenaTeam> teams = arena.getTeams();
		for(ArenaTeam t:teams){
			Set<Player> p = t.getBukkitPlayers();
			for(Player pa:p){
				pa.setMaxHealth(20);
				pa.setWalkSpeed(0.2F);
			}	
		}
		jumpMulti.clear();
		noJump.clear();
		damageMulti.clear();
	}
	
	public static void enhancePlayer(Integer enhancement, Player p){
		switch(enhancement){
		case 1:
			addDamage(p);
			break;
		case 2:
			addHealth(p);
			break;
		case 3:
			addSpeed(p);
			break;
		case 4:
			addJump(p);
			break;
		default:
			p.sendMessage(ChatColor.DARK_RED + "enhancement types include: damage, health, speed, jump");
		}
	}

	
	private static void addDamage(Player p){
		UUID pID = p.getUniqueId();
		Double multiplier = 1.0;
		if(damageMulti.containsKey(pID)){
			multiplier = damageMulti.get(pID);
		}
		multiplier+= .1;
		damageMulti.put(pID, multiplier);
	}
	
	public static Double getDamageMultiplier(Player p){
		if(damageMulti.containsKey(p.getUniqueId())){
			return damageMulti.get(p.getUniqueId());
		}else{
			return 1.0;
		}
	}
	
	private static void addHealth(Player p){
		p.setMaxHealth(p.getMaxHealth() +1);
		p.setHealth(p.getMaxHealth());
	}
	
	public static Double getHealthBonus(Player p){
		return 20/p.getMaxHealth();
	}
	
	private static void addSpeed(Player p){
		p.setWalkSpeed(p.getWalkSpeed()+0.1f);
	}
	
	public static Double getSpeedBonus(Player p){
		return (double) (0.2f/p.getWalkSpeed());
	}
	
	private static void addJump(Player p){
		Double multiplier = 1.0;
		if(jumpMulti.containsKey(p.getUniqueId())){
			multiplier = jumpMulti.get(p.getUniqueId());
		}
		jumpMulti.put(p.getUniqueId(), multiplier + (.3));
	}
	
	public static Double getJumpBonus(Player p){
		if(jumpMulti.containsKey(p.getUniqueId())){
			return jumpMulti.get(p.getUniqueId());
		}else{
			return 1.0;
		}
		
	}

	/* 
	 * is used to make player jump higher...
	 */
	@EventHandler
	public void jumpBonus(PlayerMoveEvent e){
		Player p = e.getPlayer();
		if(e.getTo().getBlockY() != e.getFrom().getBlockY() 
				&& jumpMulti.containsKey(p.getUniqueId()) 
				&& !noJump.contains(p.getUniqueId())){
			Vector vec = p.getVelocity().multiply(jumpMulti.get(p.getUniqueId()));
			p.setVelocity(vec);
			noJump.add(p.getUniqueId());
		}
	}
	
	@EventHandler
	public void resetNoJump(PlayerMoveEvent e){
		Player p = e.getPlayer();
		if(noJump.contains(p.getUniqueId()) && (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR)){
			noJump.remove(p.getUniqueId());
		}
	}
	
	@EventHandler
	public void falldamge(EntityDamageEvent e){
		if(nofalldamage == null){
			return;
		}
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getEntity();
		if (e.getCause() == DamageCause.FALL) {
			if(nofalldamage.contains(p.getName())){
				e.setDamage(0);
				nofalldamage.remove(p.getName());
			}
		}
	}
}

















