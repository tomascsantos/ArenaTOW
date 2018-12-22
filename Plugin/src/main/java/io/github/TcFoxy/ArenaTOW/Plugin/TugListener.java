package io.github.TcFoxy.ArenaTOW.Plugin;

import io.github.TcFoxy.ArenaTOW.API.Events.CustomEntityTakeDamageEvent;
import io.github.TcFoxy.ArenaTOW.API.Events.CustomZombieReachTargetEvent;
import io.github.TcFoxy.ArenaTOW.API.MobType;
import io.github.TcFoxy.ArenaTOW.API.TOWEntity;
import io.github.TcFoxy.ArenaTOW.Plugin.Serializable.PersistInfo;
import mc.alk.arena.BattleArena;
import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.teams.ArenaTeam;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.entity.*;

public class TugListener implements Listener {


    private TugArena tug;


    public TugListener(TugArena tug) {
        this.tug = tug;
    }


    @SuppressWarnings("unused")
	@EventHandler
    private void reachedDestination(CustomZombieReachTargetEvent event) {
	    event.getEntityZombie().whereTo();
    }

    @SuppressWarnings("unused")
    @EventHandler
    private void friendlyFire(CustomEntityTakeDamageEvent event) {
        if (event.getDamaged().isSameTeam(event.getAttacker())) {
            event.setCancelled(true);
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

    @SuppressWarnings("unused")
    @EventHandler
    private void noFireBallDmg(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof TOWEntity &&
                ((TOWEntity) event.getDamager()).getMobType() == MobType.FIREBALL) {

            TOWEntity fireball = (TOWEntity) event.getDamager();

            if (fireball.isSameTeam(event.getEntity())) {
                event.setCancelled(true);
            }
        }
    }


    /*
     * used to prevent any annoying loot drop by entities.
     */
    @SuppressWarnings("unused")
    @EventHandler
    private void noLootDrop(EntityDeathEvent event) {
        event.setDroppedExp(0);
        event.getDrops().clear();
    }

    /*
     * no breaking blocks
     */
    @SuppressWarnings("unused")
    @EventHandler
    private void noBreakBlocks(BlockBreakEvent event) {
        if (tug.arena.getAlivePlayers().contains(BattleArena.toArenaPlayer(event.getPlayer()))) {
            event.setCancelled(true);
        }
    }

    /*
     * used to stop incendiary fireballs from igniting blocks
     */
    @SuppressWarnings("unused")
    @EventHandler
    private void noFireballFire(BlockIgniteEvent event) {
        if (event.getCause().equals(IgniteCause.FIREBALL)) event.setCancelled(true);
    }


    /*
     * used to set the victors if one of the nexi is destroyed
     */
    @EventHandler
    private void nexusDeath(EntityDeathEvent event) {
        if (event.getEntityType().equals(EntityType.GUARDIAN) || event.getEntityType().equals(EntityType.ELDER_GUARDIAN)) {
            TOWEntity towEntity = tug.getEntityHandler().getTowEntity(event.getEntity());
            System.out.println("tow entity = " + towEntity);
            System.out.println("Towentity team: " + towEntity.getTeam());
            ArenaTeam team = tug.getOppisiteTeam(towEntity.getTeam());
            tug.arena.getMatch().setVictor(team);
        }
            return;
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
