package io.github.TcFoxy.ArenaTOW.BattleArena.objects.victoryconditions;

import io.github.TcFoxy.ArenaTOW.BattleArena.BattleArena;
import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.Match;
import io.github.TcFoxy.ArenaTOW.BattleArena.matches.MatchFinishedEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.matches.MatchResultEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.matches.MatchStartEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.ArenaEventHandler;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.events.EventPriority;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.victoryconditions.interfaces.DefinesTimeLimit;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.Countdown;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.Countdown.CountdownCallback;

public class TimeLimit extends VictoryCondition implements DefinesTimeLimit, CountdownCallback {

    Countdown timer; /// Timer for when victory condition is time based
    int announceInterval;

    public TimeLimit(Match match) {
        super(match);
    }

    @SuppressWarnings("UnusedParameters")
    @ArenaEventHandler(priority=EventPriority.LOW)
    public void onStart(MatchStartEvent event){
        cancelTimers();
        announceInterval =match.getParams().getIntervalTime();
        timer = new Countdown(BattleArena.getSelf(),match.getParams().getMatchTime(), announceInterval, this);
    }

    @ArenaEventHandler(priority=EventPriority.LOW)
    public void onVictory(MatchResultEvent event){
        if (event.isMatchEnding())
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
        if (remaining <= 0){
            match.timeExpired();
        } else {
            if (remaining % announceInterval ==0)
                match.intervalTick(remaining);
        }
        return true;
    }

    @Override
    public int getTime() {
        return match.getParams().getMatchTime();
    }
}
