package io.github.TcFoxy.ArenaTOW.BattleArena.controllers.containers;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;

import io.github.TcFoxy.ArenaTOW.BattleArena.BattleArena;
import io.github.TcFoxy.ArenaTOW.BattleArena.Defaults;
import io.github.TcFoxy.ArenaTOW.BattleArena.competition.TransitionController;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.PlayerStoreController;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.plugins.EssentialsController;
import io.github.TcFoxy.ArenaTOW.BattleArena.events.BAEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.events.players.ArenaPlayerEnterMatchEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.events.players.ArenaPlayerLeaveEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.events.players.ArenaPlayerLeaveMatchEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.events.players.ArenaPlayerTeleportEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.BAPlayerListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.PlayerHolder;
import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.Custom.MethodController;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaLocation;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaPlayer;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.CompetitionState;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.LocationType;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MatchParams;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MatchState;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.StateOption;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.ArenaListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.ArenaType;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.ArenaEventHandler;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.EventPriority;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.options.StateOptions;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.spawns.SpawnLocation;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.ArenaTeam;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.Log;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.PlayerUtil;


public class GameManager implements PlayerHolder{
	static final HashMap<ArenaType, GameManager> map = new HashMap<ArenaType, GameManager>();

	final MatchParams params;
	final Set<ArenaPlayer> handled = new HashSet<ArenaPlayer>(); /// which players are now being handled
	MethodController methodController;

	public static GameManager getGameManager(MatchParams mp) {
		if (map.containsKey(mp.getType()))
			return map.get(mp.getType());
		GameManager gm = new GameManager(mp);
		map.put(mp.getType(), gm);
		return gm;
	}

	protected void updateBukkitEvents(MatchState matchState,ArenaPlayer player){
		methodController.updateEvents(matchState, player);
	}

	private GameManager(MatchParams params){
		this.params = params;
		methodController = new MethodController("GM "+params.getName());
		methodController.addAllEvents(this);
		if (Defaults.TESTSERVER) {Log.info("GameManager Testing"); return;}
		Bukkit.getPluginManager().registerEvents(this, BattleArena.getSelf());
	}

	@Override
	public void addArenaListener(ArenaListener arenaListener) {
        methodController.addListener(arenaListener);
    }

    @Override
    public boolean removeArenaListener(ArenaListener arenaListener) {
        return methodController.removeListener(arenaListener);
    }


    @ArenaEventHandler(priority=EventPriority.HIGHEST)
	public void onArenaPlayerLeaveEvent(ArenaPlayerLeaveEvent event){
		if (handled.contains(event.getPlayer()) && !event.isHandledQuit()){
			ArenaPlayer player = event.getPlayer();
			ArenaTeam t = getTeam(player);
			TransitionController.transition(this, MatchState.ONCANCEL, player, t, false);
		}
	}

	private void quitting(ArenaPlayer player){
		if (handled.remove(player)){
			TransitionController.transition(this, MatchState.ONLEAVE, player, null, false);
			updateBukkitEvents(MatchState.ONLEAVE, player);
			player.reset(); /// reset their isReady status, chosen class, etc.
		}
	}

	private void cancel() {
		List<ArenaPlayer> col = new ArrayList<ArenaPlayer>(handled);
		for (ArenaPlayer player: col){
			ArenaTeam t = getTeam(player);
			TransitionController.transition(this, MatchState.ONCANCEL, player, t, false);
		}
	}

	@Override
	public MatchParams getParams() {
		return params;
	}

	@Override
	public CompetitionState getState() {
		return MatchState.NONE;
	}

	@Override
	public MatchState getMatchState() {
		return MatchState.NONE;
	}

	@Override
	public boolean isHandled(ArenaPlayer player) {
		return handled.contains(player);
	}

	@Override
	public boolean checkReady(ArenaPlayer player, ArenaTeam team, StateOptions mo, boolean b) {
		return false;
	}

	@Override
	public void callEvent(BAEvent event) {
		methodController.callEvent(event);
	}

	@Override
	public SpawnLocation getSpawn(int index, boolean random) {
		return null;
	}

	@Override
	public LocationType getLocationType() {
		return null;
	}

	@Override
	public ArenaTeam getTeam(ArenaPlayer player) {
		return null;
	}

	@Override
	public void onPreJoin(ArenaPlayer player, ArenaPlayerTeleportEvent apte) {
		if (handled.add(player)){
            if (Defaults.DEBUG_TRACE) Log.trace(-1, player.getName() + "   &5GM !!!!&2onPreJoin  t=" + player.getTeam());
            PlayerStoreController.getPlayerStoreController().storeScoreboard(player);
			TransitionController.transition(this, MatchState.ONENTER, player, null, false);
			updateBukkitEvents(MatchState.ONENTER, player);
			if (EssentialsController.enabled())
				BAPlayerListener.setBackLocation(player,
						EssentialsController.getBackLocation(player.getName()));
			// When teleporting in for the first time defaults
			PlayerUtil.setGameMode(player.getPlayer(), GameMode.SURVIVAL);
			EssentialsController.setGod(player.getPlayer(), false);
            callEvent(new ArenaPlayerEnterMatchEvent(player, player.getTeam()));
		}
	}

	@Override
	public void onPostJoin(ArenaPlayer player, ArenaPlayerTeleportEvent apte) {
        if (Defaults.DEBUG_TRACE) Log.trace(-1, player.getName() + "   &5GM !!!!&2onPostJoin  t=" + player.getTeam());
		player.getMetaData().setJoining(false);
    }

	@Override
	public void onPreQuit(ArenaPlayer player, ArenaPlayerTeleportEvent apte) {
        if (Defaults.DEBUG_TRACE) Log.trace(-1, player.getName() + "   &5GM !!!!&4onPreQuit  t=" + player.getTeam());
	}

	@Override
	public void onPostQuit(ArenaPlayer player, ArenaPlayerTeleportEvent apte) {
		this.quitting(player);
        callEvent(new ArenaPlayerLeaveMatchEvent(player,player.getTeam()));
		if (EssentialsController.enabled())
			BAPlayerListener.setBackLocation(player, null);
        PlayerStoreController.getPlayerStoreController().restoreScoreboard(player);
        if (Defaults.DEBUG_TRACE) Log.trace(-1, player.getName() + "   &5GM !!!!&4onPostQuit  t=" + player.getTeam());
	}

	@Override
	public void onPreEnter(ArenaPlayer player, ArenaPlayerTeleportEvent apte) {
	}

	@Override
	public void onPostEnter(ArenaPlayer player, ArenaPlayerTeleportEvent apte) {
        if (Defaults.DEBUG_TRACE) Log.trace(-1, player.getName() + "   &5GM !!!!&fonPostEnter  t=" + player.getTeam());
	}

	@Override
	public void onPreLeave(ArenaPlayer player, ArenaPlayerTeleportEvent apte) {
	}

	@Override
	public void onPostLeave(ArenaPlayer player, ArenaPlayerTeleportEvent apte) {
        if (Defaults.DEBUG_TRACE) Log.trace(-1, player.getName() + "   &5GM !!!!&8onPostLeave  t=" + player.getTeam());
	}

    @Override
    public boolean hasOption(StateOption option) {
        return params.hasOptionAt(getState(), option);
    }

    public boolean hasPlayer(ArenaPlayer player) {
		return handled.contains(player);
	}

	public static void cancelAll() {
		synchronized(map){
			for (GameManager gm: map.values()){
				gm.cancel();
			}
		}
	}

    public void setTeleportTime(ArenaPlayer player, ArenaLocation location){

    }

}
