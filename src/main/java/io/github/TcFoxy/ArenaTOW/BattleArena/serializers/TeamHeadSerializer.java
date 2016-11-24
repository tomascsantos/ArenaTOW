package io.github.TcFoxy.ArenaTOW.BattleArena.serializers;


import java.awt.Color;

import org.bukkit.inventory.ItemStack;

import io.github.TcFoxy.ArenaTOW.BattleArena.objects.exceptions.ConfigException;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.TeamAppearance;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.InventoryUtil;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.Log;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.MessageUtil;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.TeamUtil;


public class TeamHeadSerializer extends BaseConfig{

	private String[] keys = {"&cRed,wool:14,255,0,0", "&bBlue,wool:11,0,0,255"};
	
	public void loadAll(){
		for (String str: keys){
			try {
				addTeamHead(str);
			} catch (Exception e) {
				Log.err("Error parsing teamHead " + str);
				Log.printStackTrace(e);
				continue;
			}
		}
	}

	private static String addTeamHead(String str) throws Exception {
		String[] split = str.split(",");
		if (split.length != 5){
			throw new ConfigException("Team Colors must be in format 'Name,ItemStack,R,G,B'");
		}
		String name =MessageUtil.decolorChat(split[0]);
		if (name.isEmpty()){
			throw new ConfigException("Team Name must not be empty 'Name,ItemStack'");
		}
		ItemStack item = InventoryUtil.parseItem(split[1]);
		item.setAmount(1);
		Integer r = Integer.valueOf(split[2]);
		Integer g = Integer.valueOf(split[3]);
		Integer b = Integer.valueOf(split[4]);
		TeamAppearance th = new TeamAppearance(item,split[0], new Color(r, g, b));
		TeamUtil.addTeamHead(name,th);
		return name;
	}

}
