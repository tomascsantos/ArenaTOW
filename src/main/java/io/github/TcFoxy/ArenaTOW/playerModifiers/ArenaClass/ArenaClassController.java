package io.github.TcFoxy.ArenaTOW.playerModifiers.ArenaClass;

import io.github.TcFoxy.ArenaTOW.ArenaTOW;
import io.github.TcFoxy.ArenaTOW.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import mc.alk.arena.BattleArena;
import mc.alk.arena.competition.match.Match;
import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.spawns.SpawnLocation;
import mc.alk.arena.objects.teams.ArenaTeam;
import net.minecraft.server.v1_10_R1.EntityLiving;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ArenaClassController implements Listener {
	
	HashMap<String, Integer> cooldowns = new HashMap<String,Integer>();
	public static HashMap<UUID, String> fragile; //no moving or taking damage
	public static HashMap<UUID, String> nohit; //no dealing damage
	public static ArrayList<UUID> nofalldamage; // no falldamage taken

	Match match;
	
	
	public ArenaClassController(Match match, ArenaTOW tow){
		this.match = match;
		cooldowns = new HashMap<String,Integer>();
		cooldowns.clear();

		fragile = new HashMap<UUID, String>();
		nohit = new HashMap<UUID, String>();
		nofalldamage = new ArrayList<UUID>();
	}

	/////////////////////////////////////////////////////////
	//////////////////////COOLDOWN//////////////////////////
	///////////////////////////////////////////////////////
	
	public Boolean checkCooldown(Player p, Integer cooltime){
		if(!cooldowns.containsKey(p.getName())){
			cooldowns.put(p.getName(), (int) System.currentTimeMillis());
			p.setLevel(cooltime);
			return true;
		}
		int playertime = cooldowns.get(p.getName());
		int currenttime = (int) System.currentTimeMillis();
		
		if(currenttime - playertime >= cooltime*1000){
			cooldowns.put(p.getName(), (int) System.currentTimeMillis());
			p.setLevel(cooltime);
			return true;
		}else{
			return false;
		}

	}

	public Integer getCooldown(Player p, Integer cooltime){
		int cooldown = (int) (System.currentTimeMillis() - cooldowns.get(p.getName()));
		return cooltime-(cooldown/1000);
	}
	public int cooldownId;
	public void xpCooldown(){
		cooldownId = Bukkit.getScheduler().scheduleSyncRepeatingTask(ArenaTOW.getSelf(), new Runnable(){
			@Override
			public void run() {
				for(ArenaPlayer ap : match.getAlivePlayers()){
					Player p = ap.getPlayer();
					if(p.getLevel() != 0){
						p.setLevel(p.getLevel()-1);
					}
				}
			}
		}, 20, 20);
	}
	/////////////////////////////////////////////////////////
	/////////////////////Check Mob////////////////////////
	/////////////////////////////////////////////////////////
	
	public boolean sameMobTeam(Player p, Entity e){
		if((e instanceof Zombie) || (e instanceof IronGolem) || (e instanceof Guardian)){
			ArenaPlayer ap = BattleArena.toArenaPlayer(p);
			ArenaTeam team = ap.getTeam();
			String teamname = team.getDisplayName();
			EntityLiving el = (EntityLiving) ((CraftEntity) e).getHandle();
			String entityclass = el.getClass().getName();
			boolean buf = false;
			switch(entityclass){
				case "io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.EntityRedZombie":
					if(teamname == "Red") buf = true;
					break;
				case "io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.CustomRedGolem":
					if(teamname == "Red") buf = true;
					break;
				case "io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.CustomRedGuardian":
					if(teamname == "Red") buf = true;
					break;
				case "io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.EntityBlueZombie":
					if(teamname == "Blue") buf = true;
					break;
				case "io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.CustomBlueGolem":
					if(teamname == "Blue") buf = true;
					break;
				case "io.github.TcFoxy.ArenaTOW.nms.v1_10_R1.CustomBlueGuardian":
					if(teamname == "Blue") buf = true;
					break;
			}
			return buf;
		}
		return false;
		
	}
	
	
	/////////////////////////////////////////////////////////
	//////////////////////HearthStone////////////////////////
	/////////////////////////////////////////////////////////
	
	public void Hearthstone(PlayerInteractEvent event) {
		final Player p = event.getPlayer();
		p.sendMessage(ChatColor.DARK_PURPLE + "The HearthStone is warming up...");
		if(fragile.containsKey(p.getUniqueId())){
			return;
		}
		nohit.put(p.getUniqueId(),  ChatColor.DARK_RED + "You can't do damage! your hearthstone is charging!");
		fragile.put(p.getUniqueId(), ChatColor.DARK_RED + "HearthStone has been cancelled, you took damage or moved!");
		fragile(p);
	}

	private void fragile(final Player p){
		Bukkit.getScheduler().runTaskLater(ArenaTOW.getSelf(), new Runnable() {
			@Override
			public void run() {
				if(fragile.containsKey(p.getUniqueId())){
					ArenaPlayer ap = BattleArena.toArenaPlayer(p);
					SpawnLocation l = ap.getCompetition().getSpawn(ap.getTeam().getId()-1, false);
					p.teleport(l.getLocation());
					p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10*20, 3));
					p.sendMessage(ChatColor.DARK_PURPLE + "Welcome Home, you have been healed.");
					fragile.remove(p.getUniqueId());
					nohit.remove(p.getUniqueId());
				}
			}
		}, 5*Utils.TPS);
	}

	@EventHandler
	private void nodamage(EntityDamageByEntityEvent event){
		if(fragile == null){
			return;
		}
		if(event.getEntity().getClass().getName() == "org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer"){
			Player p = (Player) event.getEntity();
			if(fragile.containsKey(p.getUniqueId())){
				p.sendMessage(fragile.get(p.getUniqueId()) + "");
				fragile.remove(p.getUniqueId());
				nohit.remove(p.getUniqueId());
			}
		}
	}

	@EventHandler
	private void nomove(PlayerMoveEvent e){
		if(fragile == null){
			return;
		}
		Player p = e.getPlayer();
		if(fragile.containsKey(p.getUniqueId())){
			if(e.getFrom().getBlockX() != e.getTo().getBlockX() ||
					e.getFrom().getBlockZ() != e.getTo().getBlockZ()){
				e.getPlayer().sendMessage(fragile.get(p.getUniqueId()) + "");
				fragile.remove(p.getUniqueId());
				nohit.remove(p.getUniqueId());
				
			}
		}

	}
	

	@EventHandler
	private void noHit(EntityDamageByEntityEvent event){
		UUID id = event.getDamager().getUniqueId();
		if(nohit.containsKey(id)){
			event.setCancelled(true);
			if(Bukkit.getPlayer(id) != null){
				Bukkit.getPlayer(id).sendMessage(nohit.get(id) + "");
			}
			
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
			if(nofalldamage.contains(p.getUniqueId())){
				e.setDamage(0);
				nofalldamage.remove(p.getUniqueId());
			}
		}
	}
}
