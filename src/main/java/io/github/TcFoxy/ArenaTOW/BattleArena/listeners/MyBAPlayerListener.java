package io.github.TcFoxy.ArenaTOW.BattleArena.listeners;

import mc.alk.arena.Permissions;
import mc.alk.arena.controllers.PlayerController;
import mc.alk.arena.controllers.PlayerRestoreController;
import mc.alk.arena.events.players.ArenaPlayerLeaveEvent;
import mc.alk.arena.util.InventoryUtil.PInv;
import mc.alk.arena.util.MessageUtil;
import mc.alk.arena.util.PlayerUtil;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import io.github.TcFoxy.ArenaTOW.BattleArena.MyBattleArena;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.MyBattleArenaController;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MyArenaPlayer;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 *
 * @author alkarin
 *
 */
public class MyBAPlayerListener implements Listener  {
	static final HashMap<UUID,PlayerRestoreController> restore = new HashMap<UUID,PlayerRestoreController>();
	final MyBattleArenaController bac;

	public MyBAPlayerListener(MyBattleArenaController arenaController){
		this.bac = arenaController;
	}

	/**
	 * Why priority highest, some other plugins try to force respawn the player in spawn(or some other loc)
	 * problem is if they have come from the creative world, their game mode gets reset to creative
	 * but the other plugin force spawns them at spawn... so they now have creative in an area they shouldnt
	 * @param event PlayerRespawnEvent
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerRespawn(PlayerRespawnEvent event){
        UUID id = PlayerUtil.getID(event.getPlayer());
        if (restore.containsKey(id)){
			if (!restore.get(id).handle(event.getPlayer(),event)){
				restore.remove(id);
			}
		}
	}

	/**
	 * This method is just used to handle essentials and the /back command
	 * @param event PlayerDeathEvent
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerDeath(PlayerDeathEvent event){
		if (!EssentialsController.enabled() || !PlayerController.hasArenaPlayer(event.getEntity()))
			return;
        MyArenaPlayer ap = MyBattleArena.toArenaPlayer(event.getEntity());
        if (!restore.containsKey(ap.getID()))
                return;

        PlayerRestoreController prc = getOrCreateRestorer(ap);
		Location loc = prc.getBackLocation();
		if (loc != null){
			EssentialsController.setBackLocation(event.getEntity().getName(), loc);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerQuit(PlayerQuitEvent event){
		if (!PlayerController.hasArenaPlayer(event.getPlayer()))
			return;
		MyArenaPlayer p = PlayerController.getArenaPlayer(event.getPlayer());
		MyArenaPlayerLeaveEvent aple = new ArenaPlayerLeaveEvent(p, p.getTeam(),
				ArenaPlayerLeaveEvent.QuitReason.QUITMC);
		aple.callEvent();
	}

	private static PlayerRestoreController getOrCreateRestorer(MyArenaPlayer player) {
        final UUID id = player.getID();
        if (restore.containsKey(id)) {
            return restore.get(id);
        }
        PlayerRestoreController prc = new PlayerRestoreController(player);
        restore.put(id, prc);
        return prc;
    }

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerTeleport(PlayerTeleportEvent event){
		if (event.isCancelled() || !WorldGuardController.hasWorldGuard() ||
				WorldGuardController.regionCount() == 0 ||
				event.getPlayer().hasPermission(Permissions.TELEPORT_BYPASS_PERM))
			return;
		WorldGuardRegion region = WorldGuardController.getContainingRegion(event.getTo());
		if (region != null && !WorldGuardController.hasPlayer(event.getPlayer().getName(), region)){
			MessageUtil.sendMessage(event.getPlayer(), "&cYou can't enter the arena through teleports");
			event.setCancelled(true);
		}
	}

	public static void killOnReenter(MyArenaPlayer player) {
		getOrCreateRestorer(player).setKill(true);
	}

	public static void clearInventoryOnReenter(MyArenaPlayer player) {
		getOrCreateRestorer(player).setClearInventory(true);
	}

	public static void teleportOnReenter(MyArenaPlayer player, Location destloc, Location lastloc) {
		PlayerRestoreController prc = getOrCreateRestorer(player);
		prc.setTp(destloc);
		prc.setLastLocs(lastloc);
	}

	public static void addMessageOnReenter(MyArenaPlayer player, String message) {
		getOrCreateRestorer(player).setMessage(message);
	}

	public static void restoreExpOnReenter(MyArenaPlayer player, Integer val) {
		getOrCreateRestorer(player).addExp(val);
	}

	public static void restoreItemsOnReenter(MyArenaPlayer player, PInv pinv) {
		getOrCreateRestorer(player).setItem(pinv);
	}

	public static void restoreMatchItemsOnReenter(MyArenaPlayer player, PInv pinv) {
		getOrCreateRestorer(player).setMatchItems(pinv);
	}

	public static void removeMatchItems(MyArenaPlayer player) {
		getOrCreateRestorer(player).removeMatchItems();
	}

	public static void clearWoolOnReenter(MyArenaPlayer player, int color) {
		getOrCreateRestorer(player).setClearWool(color);
	}

	public static void restoreGameModeOnEnter(MyArenaPlayer player, GameMode gamemode) {
		getOrCreateRestorer(player).setGamemode(gamemode);
	}

	public static void removeItemOnEnter(MyArenaPlayer p, ItemStack is) {
		getOrCreateRestorer(p).addRemoveItem(is);
	}

	public static void removeItemsOnEnter(MyArenaPlayer p, List<ItemStack> itemsToRemove) {
		getOrCreateRestorer(p).addRemoveItem(itemsToRemove);
	}

	public static void restoreHealthOnReenter(MyArenaPlayer player, Double val) {
		getOrCreateRestorer(player).setHealth(val);
	}

	public static void restoreHungerOnReenter(MyArenaPlayer player, Integer val) {
		getOrCreateRestorer(player).setHunger(val);
	}

	public static void restoreMagicOnReenter(MyArenaPlayer player, Integer val) {
		getOrCreateRestorer(player).setMagic(val);
	}

	public static void deEnchantOnEnter(MyArenaPlayer player) {
		getOrCreateRestorer(player).deEnchant();
	}
    public static void restoreEffectsOnReenter(MyArenaPlayer player, Collection<PotionEffect> c) {
        getOrCreateRestorer(player).enchant(c);
    }

//    public static void killAllOnReenter(Set<String> keys) {
//		if (keys==null)
//			return;
//		for (String name: keys){
//			BAPlayerListener.killOnReenter(name);}
//	}

//	public static void clearInventoryOnReenter(Set<String> keys) {
//		if (keys==null)
//			return;
//		for (String name: keys){
//			BAPlayerListener.clearInventoryOnReenter(name);}
//	}

	public static Map<UUID, PlayerRestoreController> getPlayerRestores() {
		return restore;
	}

	public static void setBackLocation(MyArenaPlayer player, Location location) {
		getOrCreateRestorer(player).setBackLocation(location);
	}

	public static Location getBackLocation(Player player) {
        UUID id = PlayerUtil.getID(player);
        return restore.containsKey(id) ? restore.get(id).getBackLocation() : null;
    }


}
