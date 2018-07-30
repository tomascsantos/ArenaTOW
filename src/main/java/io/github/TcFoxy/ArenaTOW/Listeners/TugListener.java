package io.github.TcFoxy.ArenaTOW.Listeners;

import java.util.Collection;

import mc.alk.arena.BattleArena;
import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.teams.ArenaTeam;
import net.minecraft.server.v1_13_R1.EntityLiving;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftEntity;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.TcFoxy.ArenaTOW.TugArena;
import io.github.TcFoxy.ArenaTOW.Utils;
import io.github.TcFoxy.ArenaTOW.Serializable.PersistInfo;
import io.github.TcFoxy.ArenaTOW.nms.v1_13_R1.MyEntityGolem;
import io.github.TcFoxy.ArenaTOW.nms.v1_13_R1.MyFireball;
import io.github.TcFoxy.ArenaTOW.nms.v1_13_R1.interfaces.NMSConstants;

public class TugListener implements Listener{
	

	TugArena tug;
	
	
	public TugListener(TugArena tug){
		this.tug = tug;
	}
	
	
	
	/*
	 * used to make sure that entities 
	 * of the same team will not pathfind towards friendly targets
	 * or targets that are invisible.
	 */	
	@EventHandler
	private void sameTeamTarget(EntityTargetEvent event){
		if (event.getTarget() == null) return;		
		
		
		if(event.getEntity().getClass().getName() == NMSConstants.spigotZombie ||
				event.getEntity().getClass().getName() == NMSConstants.spigotGolem ||
				event.getEntity().getClass().getName() == NMSConstants.spigotGuardian ){
			if(event.getTarget() instanceof Player){
				Player p = (Player) event.getTarget();
				ArenaPlayer ap = BattleArena.toArenaPlayer(p);
				ArenaTeam team = ap.getTeam();
				if (team == null) return;
				
				EntityLiving el = (EntityLiving) ((CraftEntity) event.getEntity()).getHandle();
				String teamname = team.getDisplayName();
				String entityclass = el.getClass().getName();
				switch(entityclass){
				case NMSConstants.MyRedZombie:
					if(teamname.equals(Utils.toSimpleColor(Color.RED))) event.setCancelled(true);
					break;
				case NMSConstants.MyRedGolem:
					if(teamname.equals(Utils.toSimpleColor(Color.RED))) event.setCancelled(true);
					break;
				case NMSConstants.MyRedGuardian:
					if(teamname.equals(Utils.toSimpleColor(Color.RED))) event.setCancelled(true);
					break;
				case NMSConstants.MyBlueZombie:
					if(teamname.equals(Utils.toSimpleColor(Color.BLUE))) event.setCancelled(true);
					break;
				case NMSConstants.MyBlueGolem:
					if(teamname.equals(Utils.toSimpleColor(Color.BLUE))) event.setCancelled(true);
					break;
				case NMSConstants.MyBlueGuardian:
					if(teamname.equals(Utils.toSimpleColor(Color.BLUE))) event.setCancelled(true);
					break;
				default:
					return;
				}

				//if the event isnt cancelled but the player is invisible:
				if(!event.isCancelled()){
					Collection<PotionEffect> potions = p.getActivePotionEffects();
					if(potions.contains(PotionEffectType.INVISIBILITY)){
						event.setCancelled(true);
					}
				}
			}
		}else{
			return;
		}
	}
	
	/*
	 * when a player kills a minion, tower, or player,
	 * their team level increase, and they get money.
	 */
//	@EventHandler 
//	public void minionDeath(EntityDeathEvent event){
//		if(event.getEntity() instanceof IronGolem || event.getEntity() instanceof Zombie){
//			EntityLiving el = (EntityLiving) ((CraftEntity) event.getEntity().getLastDamageCause().getEntity()).getHandle();
//			String entityclass = el.getClass().getName();
//			ArenaTeam team;
//			Integer q = 0;
//			switch(entityclass){
//			case NMSConstants.customRedZombie:
//				team = tug.blueTeam;
//				q = 1;
//				break;
//			case NMSConstants.customRedGolem:
//				team = tug.blueTeam;
//				q = 50;
//				break;
//			case NMSConstants.customBlueZombie:
//				team = tug.redTeam;
//				q = 1;
//				break;
//			case NMSConstants.customBlueGolem:
//				team = tug.redTeam;
//				q = 50;
//				break;
//			default:
//				return;
//			}
//			if(team == null){
//				return;
//			}
//			Set<Player> players = team.getBukkitPlayers();
//			for(Player p: players){
//				ArenaEcon.addCash(p, q);
//				if(tug.sh == null){
//					Bukkit.broadcastMessage("sh == null");
//				}
//				tug.sh.refreshScore(p);
//			}
//			tug.teamLevel.addTeamPoint(q, team);
//			
//		}
//				
//	}
	
	/*
	 * golem's fireballs shouldnt hurt same team
	 */
	
	@EventHandler
	private void noFireBallDmg(EntityDamageByEntityEvent event){
		if (event.getDamager().getClass().getName().toString() == NMSConstants.spigotFireball){
			CraftFireball frball = (CraftFireball) event.getDamager();
			EntityFireball nmsFireball = (EntityFireball) frball.getHandle();
			if(nmsFireball instanceof MyFireball){
				MyEntityGolem golem = ((MyFireball) nmsFireball).getGolem();
				if(NMSConstants.isSameTeam(golem, ((CraftEntity) event.getEntity()).getHandle())){
					event.setCancelled(true);
					event.getEntity().setFireTicks(0);
					//Eventually make the Fireball go through!
				}
			}
		}
	}

	
	/*
	 * used to prevent any annoying loot drop by entities.
	 */
	@EventHandler
	private void noLootDrop(EntityDeathEvent event){
		event.setDroppedExp(0);
		event.getDrops().clear();
	}
	
	/*
	 * no breaking blocks
	 */
	@EventHandler
	private void noBreakBlocks(BlockBreakEvent event){
		for(ArenaPlayer ap : tug.arena.getMatch().getAlivePlayers()){
			if(event.getPlayer() == ap.getPlayer()){
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.DARK_RED + "Spamy Spam Spam - That's what you get for breaking blocks in the Arena!");
			}
		}
	}
	
	/*
	 * used to stop incendiary fireballs from igniting blocks
	 */
	@EventHandler
	 private void noFireballFire(BlockIgniteEvent event){
		if(event.getCause().equals(IgniteCause.FIREBALL)) event.setCancelled(true);
	}

	
	/*
	 * used to set the victors if one of the nexi is destroyed
	 */
	@EventHandler
	private void nexusDeath(EntityDeathEvent event){
		if(event.getEntityType() != EntityType.GUARDIAN)
			return;
		for(PersistInfo b : tug.activeInfo.values()){
			if(b.hasMob() && ((EntityLiving) b.getMob()).getHealth() == 0){
				if(b.getMob().getClass().getName() == NMSConstants.MyBlueGuardian){
					tug.arena.getMatch().setVictor(tug.redTeam);
					return;
				}else if (b.getMob().getClass().getName() == NMSConstants.MyRedGuardian){
					tug.arena.getMatch().setVictor(tug.blueTeam);
					return;
				}
			}
		}
	}
	
	/*
	 * disable the explosions and fire from wizard fireballs
	 */
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
	    Entity ent = event.getEntity();
	   
	    if (ent instanceof Creeper || ent instanceof Fireball) {
	        event.setCancelled(true); //Removes block damage
	    }
	}
	
	@EventHandler
	public void onExplosionPrime(ExplosionPrimeEvent event) {
	    event.setFire(false); //Only really needed for fireballs
	   
	    Entity ent = event.getEntity();
	    if (ent instanceof Fireball)
	        event.setRadius(2); //Increased from default(1), since the fireball now doesn't cause fire
	}
	
	
	/*
	 * used to activate item upgrade chest
	 */
//	@EventHandler
//	public void itemUpgrades(PlayerInteractEvent event){
//		if(event.getClickedBlock() == null){
//			return;
//		}
//		if(event.getClickedBlock().getType() == Material.ANVIL){
//			event.setCancelled(true);
//			tug.uGUI.openGUI(event);
//		}
//	}
	
}
