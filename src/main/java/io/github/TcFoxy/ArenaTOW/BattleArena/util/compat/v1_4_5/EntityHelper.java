package io.github.TcFoxy.ArenaTOW.BattleArena.util.compat.v1_4_5;

import org.bukkit.DyeColor;
import org.bukkit.entity.Wolf;

import io.github.TcFoxy.ArenaTOW.BattleArena.util.compat.IEntityHelper;

/**
 * @author alkarin
 */
public class EntityHelper implements IEntityHelper{
    @Override
    public void setCollarColor(Wolf wolf, DyeColor color) {
        wolf.setCollarColor(color);
    }
}
