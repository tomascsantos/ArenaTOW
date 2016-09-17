package io.github.TcFoxy.ArenaTOW.BattleArena.objects.signs;


import org.bukkit.Location;

import io.github.TcFoxy.ArenaTOW.BattleArena.BattleArena;
import io.github.TcFoxy.ArenaTOW.BattleArena.Defaults;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaPlayer;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MatchParams;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.exceptions.InvalidOptionException;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.options.JoinOptions;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.Log;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.MessageUtil;

/**
 * @author alkarin
 */
class ArenaJoinSign extends ArenaCommandSign{
    final JoinOptions joinOptions;

    ArenaJoinSign(Location location, MatchParams mp, String[] op1, String[] op2) throws InvalidOptionException {
        super(location, mp, op1, op2);
        joinOptions = JoinOptions.parseOptions(mp, null, op1);
    }

    @Override
    public void performAction(ArenaPlayer player) {
        JoinOptions jops = joinOptions.clone();
        try {
            jops.setPlayer(player);
            BattleArena.getBAExecutor().join(player, mp, joinOptions, !Defaults.USE_SIGN_PERMS);
        } catch (InvalidOptionException e) {
            MessageUtil.sendMessage(player, e.getMessage());
        } catch (Exception e) {
            Log.printStackTrace(e);
            MessageUtil.sendMessage(player, e.getMessage());
        }
    }

    @Override
    public String getCommand() {
        return "Join";
    }
}
