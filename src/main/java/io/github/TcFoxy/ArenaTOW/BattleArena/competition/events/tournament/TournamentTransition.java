package io.github.TcFoxy.ArenaTOW.BattleArena.competition.events.tournament;

import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.StateController;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.CompetitionTransition;

/**
 * @author alkarin
 */
enum TournamentTransition implements CompetitionTransition{
    FIRSTPLACE ("firstPlace"), PARTICIPANTS("participants"),
    ;

    final String name;
    int globalOrdinal;
    TournamentTransition(String name){
        globalOrdinal = StateController.register(this);
        this.name = name;
    }

    @Override
    public int globalOrdinal() {
        return globalOrdinal;
    }
    @Override
    public String toString(){
        return name;
    }
}

