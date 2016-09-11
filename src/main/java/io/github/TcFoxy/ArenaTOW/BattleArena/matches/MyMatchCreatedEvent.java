package io.github.TcFoxy.ArenaTOW.BattleArena.matches;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.MyMatch;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.joining.MyWaitingObject;

public class MyMatchCreatedEvent extends MyMatchEvent {
    MyWaitingObject originalObject;

    public MyMatchCreatedEvent(MyMatch match, MyWaitingObject wo) {
        super(match);
        this.originalObject = wo;
    }

    public MyWaitingObject getOriginalObject() {
        return originalObject;
    }
}
