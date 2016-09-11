package io.github.TcFoxy.ArenaTOW.BattleArena.controllers;


import java.util.HashMap;
import java.util.Map;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaParams;

/**
 * @author alkarin
 */
public enum StatsController {
    INSTANCE;

    Map<ArenaParams, CompetitionStat> stats = new HashMap<ArenaParams, CompetitionStat>();

    class CompetitionStat{
        int nComps;
        int totalPlayers;
    }

    public static void addCompetition(ArenaParams params, int nPlayers){
        CompetitionStat stat = INSTANCE.getOrCreateStat(params);
        stat.nComps++;
        stat.totalPlayers += nPlayers;
    }

    private CompetitionStat getOrCreateStat(ArenaParams params) {
        CompetitionStat stat = stats.get(params);
        if (stat ==null) {
            stat = new CompetitionStat();
            stats.put(params, stat);
        }
        return stat;
    }


}
