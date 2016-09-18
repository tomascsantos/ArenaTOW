package io.github.TcFoxy.ArenaTOW.BattleArena.objects.victoryconditions;

import io.github.TcFoxy.ArenaTOW.BattleArena.BattleArena;
import io.github.TcFoxy.ArenaTOW.BattleArena.Defaults;
import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.Match;
import io.github.TcFoxy.ArenaTOW.BattleArena.events.matches.MatchFinishedEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MatchResult;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.WinLossDraw;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.ArenaEventHandler;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.EventPriority;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.scoreboard.ArenaScoreboard;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.ArenaTeam;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.victoryconditions.interfaces.DefinesTimeLimit;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.Countdown;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.Countdown.CountdownCallback;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.MessageUtil;
import mc.alk.scoreboardapi.api.SObjective;
import mc.alk.scoreboardapi.api.STeam;
import mc.alk.scoreboardapi.scoreboard.SAPIDisplaySlot;
import mc.alk.scoreboardapi.scoreboard.bukkit.BObjective;

public class TeamTimeLimit extends VictoryCondition implements DefinesTimeLimit, CountdownCallback {

    Countdown timer; /// Timer for when victory condition is time based
    int announceInterval;
    final ArenaTeam team;

    public TeamTimeLimit(Match match, ArenaTeam team) {
        super(match);
        this.team = team;
    }
    public void startCountdown(){
        timer = new Countdown(BattleArena.getSelf(),match.getParams().getMatchTime(), 1, this);
    }

    public void stopCountdown() {
        cancelTimers();
    }

    @SuppressWarnings("UnusedParameters")
    @ArenaEventHandler(priority=EventPriority.LOW)
    public void onFinished(MatchFinishedEvent event){
        cancelTimers();
    }

    private void cancelTimers() {
        if (timer != null){
            timer.stop();
            timer =null;
        }
    }

    @Override
    public boolean intervalTick(int remaining){
        if (match.isEnding())
            return false;
        if (remaining <= 0) {
            MatchResult cr = new MatchResult();
            cr.setResult(WinLossDraw.LOSS);
            cr.addLoser(team);
            match.endMatchWithResult(cr);
        }
        if (!Defaults.USE_SCOREBOARD)
            return true;
        ArenaScoreboard as = match.getScoreboard();
        if (as == null) {
            return true;}
        STeam t = as.getTeam(team.getIDString());
        SObjective ao = as.getObjective(SAPIDisplaySlot.SIDEBAR);
        if (t!=null && ao != null && ao instanceof BObjective){
            ((BObjective)ao).setDisplayName(ao.getDisplayNamePrefix(), ao.getBaseDisplayName(),
                    MessageUtil.colorChat("&e(" + remaining + ")"), t);
        }

        return true;
    }


    @Override
    public int getTime() {
        return match.getParams().getMatchTime();
    }

}
