package io.github.TcFoxy.ArenaTOW.BattleArena.util.plugins;



import org.bukkit.entity.Player;

import io.github.TcFoxy.ArenaTOW.BattleArena.util.PlayerUtil;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.plugins.HeroesUtil;

public class Heroes_1_5_2 extends HeroesUtil{

	@Override
	public void setHeroPlayerHealth(Player player, double health) {
		PlayerUtil.setHealth(player, health, true);
	}

	@Override
	public double getHeroHealth(Player player) {
		return PlayerUtil.getHealth(player,true);
	}

	@Override
	public void setHeroHealthP(Player player, double health) {
		double val = player.getMaxHealth() * health/100.0;
		PlayerUtil.setHealth(player, val, true);
	}

}
