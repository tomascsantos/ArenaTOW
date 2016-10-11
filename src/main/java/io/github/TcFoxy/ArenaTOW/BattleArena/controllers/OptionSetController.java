package io.github.TcFoxy.ArenaTOW.BattleArena.controllers;



import java.util.Map;
import java.util.TreeMap;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.options.StateOptions;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.CaseInsensitiveMap;

public class OptionSetController {
	static final Map<String,StateOptions> options = new CaseInsensitiveMap<StateOptions>();

	public static void addOptionSet(String key, StateOptions to) {
		options.put(key, to);
	}

	public static StateOptions getOptionSet(String key) {
		return options.get(key);
	}

	public static Map<String,StateOptions> getOptionSets() {
        return new TreeMap<String,StateOptions>(options);
	}

}
