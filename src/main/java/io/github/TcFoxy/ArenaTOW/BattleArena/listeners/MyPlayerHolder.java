package io.github.TcFoxy.ArenaTOW.BattleArena.listeners;

import io.github.TcFoxy.ArenaTOW.BattleArena.events.MyBAEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MyArenaLocation.LocationType;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MyArenaPlayer;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.MyArenaTeam;
import mc.alk.arena.events.players.ArenaPlayerTeleportEvent;
import mc.alk.arena.objects.CompetitionState;
import mc.alk.arena.objects.MatchParams;
import mc.alk.arena.objects.MatchState;
import mc.alk.arena.objects.StateOption;
import mc.alk.arena.objects.arenas.ArenaListener;
import mc.alk.arena.objects.options.StateOptions;
import mc.alk.arena.objects.spawns.SpawnLocation;

import org.bukkit.event.Listener;


public interface MyPlayerHolder extends Listener, ArenaListener{
	/**
	 * Add an arena listener
	 * @param arenaListener ArenaListener
	 */
	public void addArenaListener(ArenaListener arenaListener);

    /**
     * Remove an arena listener
     * @param arenaListener ArenaListener
     * @return boolean if found and removed
     */
    public boolean removeArenaListener(ArenaListener arenaListener);

	public MatchParams getParams();

	public CompetitionState getState();

    /**
     * Use getState instead
     * @return MatchState
     */
    @Deprecated
	public MatchState getMatchState();

	public boolean isHandled(MyArenaPlayer player);

	public boolean checkReady(MyArenaPlayer player, MyArenaTeam team, StateOptions mo, boolean b);

	public void callEvent(MyBAEvent event);

	public SpawnLocation getSpawn(int index, boolean random);

//	public SpawnLocation getSpawn(MyArenaPlayer player, boolean random);

	public LocationType getLocationType();

	public MyArenaTeam getTeam(MyArenaPlayer player);

	public void onPreJoin(MyArenaPlayer player, ArenaPlayerTeleportEvent apte);

	public void onPostJoin(MyArenaPlayer player, ArenaPlayerTeleportEvent apte);

	public void onPreQuit(MyArenaPlayer player, ArenaPlayerTeleportEvent apte);

	public void onPostQuit(MyArenaPlayer player, ArenaPlayerTeleportEvent apte);

	public void onPreEnter(MyArenaPlayer player, ArenaPlayerTeleportEvent apte);

	public void onPostEnter(MyArenaPlayer player, ArenaPlayerTeleportEvent apte);

	public void onPreLeave(MyArenaPlayer player, ArenaPlayerTeleportEvent apte);

	public void onPostLeave(MyArenaPlayer player, ArenaPlayerTeleportEvent apte);

    /**
     * Checks the current CompetitionState for the given option
     * @param option the option to check for
     * @return true if the current CompetitionState has the specified options or false otherwise
     */
    boolean hasOption(StateOption option);
}
