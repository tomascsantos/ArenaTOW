package io.github.TcFoxy.ArenaTOW.BattleArena.serializers;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;

import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.SignUpdateListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.signs.ArenaCommandSign;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.Log;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.MapOfTreeSet;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.SerializerUtil;

public class SignSerializer extends BaseConfig {
	public void loadAll(SignUpdateListener sc){
		Set<String> arenas = config.getKeys(false);

		for (String arenastr: arenas){
			ConfigurationSection maincs = config.getConfigurationSection(arenastr);
			if (maincs == null)
				continue;
			Set<String> signLocations = maincs.getKeys(false);
			if (signLocations == null || signLocations.isEmpty())
				continue;
			for (String strloc : signLocations){
				ConfigurationSection cs = maincs.getConfigurationSection(strloc);
				if (cs == null)
					continue;
				ArenaCommandSign acs = null;
				try {
					acs = ArenaCommandSign.deserialize(cs.getValues(true));
				} catch (IllegalArgumentException e){
					Log.err("[BattleArena] Sign not loaded: " + e.getMessage());
				}
				if (acs == null)
					continue;
				sc.addSign(acs);
			}
		}
	}

	public void saveAll(SignUpdateListener sc){
		MapOfTreeSet<String, ArenaCommandSign> statusSigns = sc.getStatusSigns();
		for (String matches: statusSigns.keySet()){
			Set<ArenaCommandSign> set = statusSigns.get(matches);
			if (set == null)
				continue;
			Map<String, Map<String,Object>> map =new HashMap<String,Map<String,Object>>();
			for (ArenaCommandSign acs: set){
				map.put(SerializerUtil.getBlockLocString(acs.getLocation()), acs.serialize());
			}
			config.createSection(matches, map);
		}
		save();

	}
}
