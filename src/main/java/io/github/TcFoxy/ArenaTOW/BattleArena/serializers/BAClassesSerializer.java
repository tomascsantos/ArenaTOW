package io.github.TcFoxy.ArenaTOW.BattleArena.serializers;


import java.util.List;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import io.github.TcFoxy.ArenaTOW.BattleArena.BattleArena;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.ArenaClassController;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaClass;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.CommandLineString;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.exceptions.InvalidOptionException;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.spawns.SpawnInstance;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.InventoryUtil;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.Log;


public class BAClassesSerializer extends BaseConfig{

	public void loadAll(){
		try {config.load(file);} catch (Exception e){Log.printStackTrace(e);}
		loadClasses(config.getConfigurationSection("classes"));
	}

	public void loadClasses(ConfigurationSection cs) {
		if (cs == null){
			Log.info(BattleArena.getPluginName() +" has no classes");
			return;}
		StringBuilder sb = new StringBuilder();
		Set<String> keys = cs.getKeys(false);
		boolean first = true;
		for (String className : keys){
			try{
				ArenaClass ac = parseArenaClass(cs.getConfigurationSection(className));
				if (ac == null)
					continue;
				if (first) first = false;
				else sb.append(", ");
				sb.append(ac.getName());
				ArenaClassController.addClass(ac);
			} catch (Exception e){
				Log.err("There was an error loading the class " + className +". It will be disabled");
				Log.printStackTrace(e);
			}
		}
		if (first){
			Log.info(BattleArena.getPluginName() +" no predefined classes found. inside of " + cs.getCurrentPath());
		} else {
			Log.info(BattleArena.getPluginName()+" registering classes: " +sb.toString());
		}
	}

	public ArenaClass parseArenaClass(ConfigurationSection cs) {
		List<ItemStack> items = null;
		List<PotionEffect> effects = null;
		List<SpawnInstance> mobs = null;
		List<CommandLineString> commands = null;

		if (cs.contains("items")){ items = InventoryUtil.getItemList(cs,"items");}
		if (cs.contains("enchants")){ effects = ConfigSerializer.getEffectList(cs,"enchants");}
		if (cs.contains("mobs")){ mobs = SpawnSerializer.getSpawnList(cs.getConfigurationSection("mobs"));}
		if (cs.contains("doCommands")){
			try {
				commands = ConfigSerializer.getDoCommands(cs.getStringList("doCommands"));
			} catch (InvalidOptionException e) {
				Log.printStackTrace(e);
			}
		}
		String displayName = cs.getString("displayName", null);
        displayName = displayName == null || displayName.isEmpty() ? cs.getName() : displayName;
        ArenaClass ac = new ArenaClass(cs.getName(),displayName, items,effects, null); //TODO replace null with permissions
		if (mobs != null && !mobs.isEmpty())
			ac.setMobs(mobs);
		if (cs.contains("disguise")){ ac.setDisguiseName(cs.getString("disguise"));}
		if (commands != null && !commands.isEmpty())
			ac.setDoCommands(commands);
		return ac;
	}

}
