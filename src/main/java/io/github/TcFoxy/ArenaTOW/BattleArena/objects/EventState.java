package io.github.TcFoxy.ArenaTOW.BattleArena.objects;

import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.StateController;

public enum EventState implements CompetitionState{
	CLOSED,OPEN,RUNNING, FINISHED;
    int globalOrdinal;

    EventState() {
        globalOrdinal = StateController.register(this);
    }

    @Override
    public int globalOrdinal() {
        return globalOrdinal;
    }
}
