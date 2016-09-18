package io.github.TcFoxy.ArenaTOW.BattleArena.controllers.plugins;

import org.bukkit.entity.Player;

import io.github.TcFoxy.ArenaTOW.BattleArena.util.plugins.CombatTagUtil;

@Deprecated
public class CombatTagInterface {

    public static void untag(Player player) {
        CombatTagUtil.untag(player);
    }

    public static boolean isTagged(Player player) {
        return CombatTagUtil.isTagged(player);
    }

}
