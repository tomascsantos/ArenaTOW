package io.github.TcFoxy.ArenaTOW.BattleArena.objects.scoreboard;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.joining.JoinResponseHandler;

public interface WaitingScoreboard extends JoinResponseHandler {

    ArenaScoreboard getScoreboard();

    void setRemainingSeconds(int seconds);
}
