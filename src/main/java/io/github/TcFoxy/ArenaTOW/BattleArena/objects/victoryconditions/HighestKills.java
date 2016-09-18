package io.github.TcFoxy.ArenaTOW.BattleArena.objects.victoryconditions;

import org.bukkit.configuration.ConfigurationSection;

import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.Match;

@Deprecated
public class HighestKills extends PlayerKills {
	public HighestKills(Match match, ConfigurationSection section) {
		super(match, section);
	}
}
