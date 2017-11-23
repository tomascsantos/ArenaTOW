//package io.github.TcFoxy.ArenaTOW.BattleArena.listeners.competition;
//
//
//import org.bukkit.entity.Entity;
//import org.bukkit.entity.LivingEntity;
//import org.bukkit.entity.Player;
//import org.bukkit.event.entity.EntityDamageByEntityEvent;
//import org.bukkit.event.entity.EntityDamageEvent;
//
//import io.github.TcFoxy.ArenaTOW.BattleArena.BattleArena;
//import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.PlayerHolder;
//import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaPlayer;
//import io.github.TcFoxy.ArenaTOW.BattleArena.objects.PVPState;
//import io.github.TcFoxy.ArenaTOW.BattleArena.objects.StateGraph;
//import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.ArenaListener;
//import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.ArenaEventHandler;
//import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.EventPriority;
//import io.github.TcFoxy.ArenaTOW.BattleArena.objects.options.StateOptions;
//import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.ArenaTeam;
//import io.github.TcFoxy.ArenaTOW.BattleArena.util.DmgDeathUtil;
//import io.github.TcFoxy.ArenaTOW.BattleArena.util.Log;
//import io.github.TcFoxy.ArenaTOW.BattleArena.util.compat.IEventHelper;
//import io.github.TcFoxy.ArenaTOW.BattleArena.util.compat.v1_7_R3.EventHelper;
//
//
//
//public class DamageListener implements ArenaListener{
//	StateGraph transitionOptions;
//	PlayerHolder holder;
//	static IEventHelper handler;
//
//	static {
//		Class<?>[] args = {};
//		try {
//			final Class<?> clazz = EventHelper.class;
//			handler = (IEventHelper) clazz.getConstructor(args).newInstance((Object[])args);
//		} catch (Exception e) {
//			Log.printStackTrace(e);
//		}
//	}
//
//
//	//    static {
//	//        Class<?>[] args = {};
//	//        try {
//	//            Version version = Util.getCraftBukkitVersion();
//	//            if (version.compareTo("v1_6_R1") >= 0){
//	//                final Class<?> clazz = Class.forName("io.github.TcFoxy.ArenaTOW.BattleArena.util.compat.v1_6_R1.EventHelper");
//	//                handler = (IEventHelper) clazz.getConstructor(args).newInstance((Object[])args);
//	//            } else {
//	//                final Class<?> clazz = Class.forName("io.github.TcFoxy.ArenaTOW.BattleArena.util.compat.pre.EventHelper");
//	//                handler = (IEventHelper) clazz.getConstructor(args).newInstance((Object[])args);
//	//            }
//	//        } catch (Exception e) {
//	//            Log.printStackTrace(e);
//	//        }
//	//    }
//
//	public DamageListener(PlayerHolder holder){
//		this.transitionOptions = holder.getParams().getStateGraph();
//		this.holder = holder;
//	}
//
////	@ArenaEventHandler(suppressCastWarnings=true,priority=EventPriority.LOW)
////	public void onEntityDamageEvent(EntityDamageEvent event) {
////		ArenaPlayer damager = null;
////		final ArenaPlayer target = (event.getEntity() instanceof Player) ? BattleArena.toArenaPlayer((Player) event.getEntity()) : null;
////
////		if (target == null) {
////			return;
////		}
////
////		if (event instanceof EntityDamageByEntityEvent && event.getEntity() instanceof LivingEntity){		
////			final Entity damagerEntity = ((EntityDamageByEntityEvent)event).getDamager();		
////			damager = DmgDeathUtil.getPlayerCause(damagerEntity);				
////		}
////		final StateOptions to = transitionOptions.getOptions(holder.getState());
////		if (to == null) {
////			return;
////		}
////		final PVPState pvp = to.getPVP();
////		if (pvp == null) {
////			return;
////		}
////
////		switch(pvp){
////		case INVINCIBLE:
////			/// all damage is cancelled
////			target.setFireTicks(0);
////			handler.setDamage(event,0);
////			event.setCancelled(true);
////			return;
////		case ON:
////			ArenaTeam targetTeam = holder.getTeam(target);
////			if (targetTeam == null || !targetTeam.hasAliveMember(target)) /// We dont care about dead players
////				return;
////			if (damager == null){ /// damage from some source, its not pvp though. so we dont care
////				return;
////			}
////			ArenaTeam t = holder.getTeam(damager);
////			if (t != null && t.hasMember(target)){ /// attacker is on the same team
////				event.setCancelled(true);
////			} else {/// different teams... lets make sure they can actually hit
////				event.setCancelled(false);
////			}
////			break;
////		case OFF:
////			if (damager != null){ /// damage done from a player
////				handler.setDamage(event,0);
////				event.setCancelled(true);
////			}
////			break;
////		default:
////			break;
////		}
////	}
//}
