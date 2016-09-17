package io.github.TcFoxy.ArenaTOW.BattleArena.objects.victoryconditions;


import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import org.bukkit.configuration.ConfigurationSection;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.Match;
import io.github.TcFoxy.ArenaTOW.BattleArena.events.players.ArenaPlayerKillEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.matches.MatchFindCurrentLeaderEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MatchResult.WinLossDraw;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.ArenaEventHandler;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.EventPriority;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.scoreboard.ArenaObjective;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.scoreboard.ArenaScoreboard;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.ArenaTeam;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.victoryconditions.interfaces.ScoreTracker;
import mc.alk.arena.controllers.plugins.TrackerController;
import mc.alk.scoreboardapi.scoreboard.SAPIDisplaySlot;


public class KillLimit extends VictoryCondition implements ScoreTracker{
    final ArenaObjective kills;
    final TrackerController sc;
    final int numKills;
    final int playerKillPoints;

    public KillLimit(Match match, ConfigurationSection section) {
        super(match);
        numKills = section.getInt("numKills", 50);
        playerKillPoints = section.getInt("points.player", 1);
        String displayName = section.getString("displayName","&4Kill Limit");
        String criteria = section.getString("criteria", "&eFirst to &4" + numKills);
        kills = new ArenaObjective(getClass().getSimpleName(),displayName, criteria,
                SAPIDisplaySlot.SIDEBAR, 60);
        boolean isRated = match.getParams().isRated();
        boolean soloRating = !match.getParams().isTeamRating();
        sc = (isRated && soloRating) ? new TrackerController(match.getParams()): null;
    }

    @ArenaEventHandler(priority=EventPriority.LOW)
    public void playerKillEvent(ArenaPlayerKillEvent event) {
        kills.addPoints(event.getPlayer(), playerKillPoints);
        Integer points = kills.addPoints(event.getTeam(), playerKillPoints);
        if (sc != null)
            sc.addRecord(event.getPlayer(),event.getTarget(), WinLossDraw.WIN);
        if (points >= numKills){
            this.match.setVictor(event.getTeam());
        }

    }

    @ArenaEventHandler(priority = EventPriority.LOW)
    public void onFindCurrentLeader(MatchFindCurrentLeaderEvent event) {
        if (event.isMatchEnding()){
            event.setResult(kills.getMatchResult(match));
        } else {
            Collection<ArenaTeam> leaders = kills.getLeaders();
            if (leaders.size() > 1){
                event.setCurrentDrawers(leaders);
            } else {
                event.setCurrentLeaders(leaders);
            }
        }
    }

    @Override
    public List<ArenaTeam> getLeaders() {
        return kills.getTeamLeaders();
    }

    @Override
    public TreeMap<Integer,Collection<ArenaTeam>> getRanks() {
        return kills.getTeamRanks();
    }

    @Override
    public void setScoreBoard(ArenaScoreboard scoreboard) {
        this.kills.setScoreBoard(scoreboard);
        scoreboard.addObjective(kills);
    }

    @Override
    public void setDisplayTeams(boolean display) {
        kills.setDisplayPlayers(display);
    }

}
