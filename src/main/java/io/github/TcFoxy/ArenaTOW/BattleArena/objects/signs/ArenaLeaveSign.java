package io.github.TcFoxy.ArenaTOW.BattleArena.objects.signs;


import org.bukkit.Location;

import io.github.TcFoxy.ArenaTOW.BattleArena.BattleArena;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaPlayer;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MatchParams;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.exceptions.InvalidOptionException;

/**
 * @author alkarin
 */
class ArenaLeaveSign extends ArenaCommandSign{

    ArenaLeaveSign(Location location, MatchParams mp, String[] op1, String[] op2) throws InvalidOptionException {
        super(location, mp, op1, op2);
    }

    @Override
    public void performAction(ArenaPlayer player) {
        BattleArena.getBAExecutor().leave(player, mp);
    }


    @Override
    public String getCommand() {
        return "Leave";
    }
}
