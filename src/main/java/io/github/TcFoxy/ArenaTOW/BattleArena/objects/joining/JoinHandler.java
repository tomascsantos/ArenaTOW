package io.github.TcFoxy.ArenaTOW.BattleArena.objects.joining;



import java.util.Collection;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaPlayer;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.ArenaTeam;

public interface JoinHandler {

    /**
     * Add a team to this object
     * @param team ArenaTeam
     * @return true if the team was added, false if not
     */
    public boolean addTeam(ArenaTeam team);

    /**
     * Remove a team from the object
     * @param team ArenaTeam
     * @return whether or not the team was removed
     */
    public boolean removeTeam(ArenaTeam team);

    /**
     * Add the players to this team
     * @param team ArenaTeam
     * @param players ArenaPlayers
     */
    public void addToTeam(ArenaTeam team, Collection<ArenaPlayer> players);

    /**
     * Add the player to this team
     * @param team ArenaTeam
     * @param player ArenaPlayer
     * @return true if the team was added, false if not
     */
    public boolean addToTeam(ArenaTeam team, ArenaPlayer player);

    /**
     * Remove the players from this team
     * @param team ArenaTeam
     * @param players ArenaPlayers
     */
    public void removeFromTeam(ArenaTeam team, Collection<ArenaPlayer> players);

    /**
     * Remove the player from this team
     * @param team ArenaTeam
     * @param player ArenaPlayer
     * @return true if the team was removed, false if not
     */
    public boolean removeFromTeam(ArenaTeam team, ArenaPlayer player);

}
