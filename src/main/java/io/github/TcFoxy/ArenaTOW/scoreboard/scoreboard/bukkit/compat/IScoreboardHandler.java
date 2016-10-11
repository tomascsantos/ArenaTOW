package io.github.TcFoxy.ArenaTOW.scoreboard.scoreboard.bukkit.compat;

import org.bukkit.scoreboard.Objective;

import io.github.TcFoxy.ArenaTOW.scoreboard.scoreboard.api.STeam;

/**
 * @author alkarin
 */
public interface IScoreboardHandler {
    public static final IScoreboardHandler BLANK_HANDLER = new IScoreboardHandler(){

        @Override
        public void setDisplayName(Objective o, STeam team, String display) {
            /* do nothing */
        }
    };


    void setDisplayName(Objective o, STeam team, String display);
}
