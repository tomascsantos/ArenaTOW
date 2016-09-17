package io.github.TcFoxy.ArenaTOW.BattleArena.events.matches;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.Match;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.joining.WaitingObject;

public class MatchCreatedEvent extends MatchEvent {
    WaitingObject originalObject;

    public MatchCreatedEvent(Match match, WaitingObject originalObject) {
        super(match);
        this.originalObject = originalObject;
    }

    public WaitingObject getOriginalObject() {
        return originalObject;
    }
}
