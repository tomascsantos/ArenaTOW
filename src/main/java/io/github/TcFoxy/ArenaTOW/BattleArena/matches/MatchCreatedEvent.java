package io.github.TcFoxy.ArenaTOW.BattleArena.matches;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.Match;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.joining.WaitingObject;

public class MatchCreatedEvent extends MatchEvent {
    WaitingObject originalObject;

    public MatchCreatedEvent(Match match, WaitingObject wo) {
        super(match);
        this.originalObject = wo;
    }

    public WaitingObject getOriginalObject() {
        return originalObject;
    }
}
