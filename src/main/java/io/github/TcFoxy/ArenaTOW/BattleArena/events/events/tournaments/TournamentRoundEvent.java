package io.github.TcFoxy.ArenaTOW.BattleArena.events.events.tournaments;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.events.Event;
import io.github.TcFoxy.ArenaTOW.BattleArena.events.events.EventEvent;

/**
 * @author alkarin
 */
public class TournamentRoundEvent extends EventEvent {

    final int round;

    public TournamentRoundEvent(Event event, int round){
        super(event);
        this.round = round;
    }

    public int getRound() {
        return round;
    }

}
