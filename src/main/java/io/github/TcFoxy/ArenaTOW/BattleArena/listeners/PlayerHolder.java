package io.github.TcFoxy.ArenaTOW.BattleArena.listeners;

import org.bukkit.event.Listener;

import io.github.TcFoxy.ArenaTOW.BattleArena.events.BAEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.events.players.ArenaPlayerTeleportEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.LocationType;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaPlayer;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.CompetitionState;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MatchParams;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MatchState;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.StateOption;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.ArenaListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.options.StateOptions;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.spawns.SpawnLocation;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.ArenaTeam;



public interface PlayerHolder extends Listener, ArenaListener{
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

	public boolean isHandled(ArenaPlayer player);

	public boolean checkReady(ArenaPlayer player, ArenaTeam team, StateOptions mo, boolean b);

	public void callEvent(BAEvent event);

	public SpawnLocation getSpawn(int index, boolean random);

//	public SpawnLocation getSpawn(MyArenaPlayer player, boolean random);

	public LocationType getLocationType();

	public ArenaTeam getTeam(ArenaPlayer player);

	public void onPreJoin(ArenaPlayer player, ArenaPlayerTeleportEvent apte);

	public void onPostJoin(ArenaPlayer player, ArenaPlayerTeleportEvent apte);

	public void onPreQuit(ArenaPlayer player, ArenaPlayerTeleportEvent apte);

	public void onPostQuit(ArenaPlayer player, ArenaPlayerTeleportEvent apte);

	public void onPreEnter(ArenaPlayer player, ArenaPlayerTeleportEvent apte);

	public void onPostEnter(ArenaPlayer player, ArenaPlayerTeleportEvent apte);

	public void onPreLeave(ArenaPlayer player, ArenaPlayerTeleportEvent apte);

	public void onPostLeave(ArenaPlayer player, ArenaPlayerTeleportEvent apte);

    /**
     * Checks the current CompetitionState for the given option
     * @param option the option to check for
     * @return true if the current CompetitionState has the specified options or false otherwise
     */
    boolean hasOption(StateOption option);
}
