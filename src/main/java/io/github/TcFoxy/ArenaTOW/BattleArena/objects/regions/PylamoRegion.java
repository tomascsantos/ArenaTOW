package io.github.TcFoxy.ArenaTOW.BattleArena.objects.regions;


import java.util.Map;

import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.plugins.PylamoController;
import mc.alk.arena.objects.regions.ArenaRegion;

public class PylamoRegion implements ArenaRegion{
	String regionName;

	public PylamoRegion(){}

	public PylamoRegion(String regionName){
		this.regionName = regionName;
	}

	@Override
	public Object yamlToObject(Map<String,Object> map, String value) {
		regionName = value;
		return new PylamoRegion(regionName);
	}

	@Override
	public Object objectToYaml() {
		return regionName;
	}

	public void setID(String id) {
		regionName = id;
	}

	@Override
    public String getID() {
		return regionName;
	}

    @Override
    public String getWorldName() {
        return null;
    }

    @Override
    public boolean valid(){
		return regionName != null && PylamoController.enabled();
	}
}
