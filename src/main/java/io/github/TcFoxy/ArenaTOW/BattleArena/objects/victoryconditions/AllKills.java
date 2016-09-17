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


public class AllKills extends VictoryCondition implements ScoreTracker {
    final ArenaObjective kills;
    final TrackerController sc;
    final ConfigurationSection section;

    public AllKills(Match match, ConfigurationSection section) {
        super(match);
        this.section = section;
        String displayName = section.getString("displayName", "All Kills");
        String criteria = section.getString("criteria", "Kill Mobs/players");
        kills = new ArenaObjective(getClass().getSimpleName(),displayName, criteria,
                SAPIDisplaySlot.SIDEBAR, 60);
        boolean isRated = match.getParams().isRated();
        boolean soloRating = !match.getParams().isTeamRating();
        sc = (isRated && soloRating) ? new TrackerController(match.getParams()): null;
    }

    @ArenaEventHandler(priority=EventPriority.LOW)
    public void playerKillEvent(ArenaPlayerKillEvent event) {
        int points = section.getInt("points.player", 1);
        kills.addPoints(event.getPlayer(), points);
        kills.addPoints(event.getTeam(), points);
        if (sc != null)
            sc.addRecord(event.getPlayer(), event.getTarget(), WinLossDraw.WIN);
    }

    @ArenaEventHandler(priority = EventPriority.LOW)
    public void onFindCurrentLeader(MatchFindCurrentLeaderEvent event) {
        event.setResult(kills.getMatchResult(match));
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
        kills.setDisplayTeams(display);
    }

}
