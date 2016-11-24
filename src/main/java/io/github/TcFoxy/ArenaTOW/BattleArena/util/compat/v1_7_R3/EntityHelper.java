package io.github.TcFoxy.ArenaTOW.BattleArena.util.compat.v1_7_R3;

import org.bukkit.DyeColor;
import org.bukkit.entity.Wolf;

import io.github.TcFoxy.ArenaTOW.BattleArena.util.compat.IEntityHelper;

public class EntityHelper implements IEntityHelper{

	@Override
	public void setCollarColor(Wolf wolf, DyeColor color) {
		wolf.setCollarColor(color);
	}

}
