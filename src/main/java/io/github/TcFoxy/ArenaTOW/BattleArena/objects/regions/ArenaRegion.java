package io.github.TcFoxy.ArenaTOW.BattleArena.objects.regions;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.YamlSerializable;

public interface ArenaRegion extends YamlSerializable{
	public boolean valid();

    public String getID();

    public String getWorldName();

}
