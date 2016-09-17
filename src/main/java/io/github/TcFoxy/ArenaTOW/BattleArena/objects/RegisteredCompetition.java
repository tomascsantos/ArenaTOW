package io.github.TcFoxy.ArenaTOW.BattleArena.objects;



import org.bukkit.plugin.Plugin;

import io.github.TcFoxy.ArenaTOW.BattleArena.BattleArena;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.BattleArenaController;
import io.github.TcFoxy.ArenaTOW.BattleArena.executors.CustomCommandExecutor;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.ArenaType;
import io.github.TcFoxy.ArenaTOW.BattleArena.serializers.ArenaSerializer;
import io.github.TcFoxy.ArenaTOW.BattleArena.serializers.ConfigSerializer;
import io.github.TcFoxy.ArenaTOW.BattleArena.serializers.MessageSerializer;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.Log;

public class RegisteredCompetition {
	final Plugin plugin;
	final String competitionName;
	ConfigSerializer configSerializer;
	ArenaSerializer arenaSerializer;
	CustomCommandExecutor customExecutor;

	public RegisteredCompetition(Plugin plugin, String competitionName){
		this.plugin = plugin;
		this.competitionName = competitionName;
	}

	public ConfigSerializer getConfigSerializer() {
		return configSerializer;
	}

	public void setConfigSerializer(ConfigSerializer serializer) {
		this.configSerializer = serializer;
	}

	public String getCompetitionName() {
		return competitionName;
	}

	public ArenaSerializer getArenaSerializer() {
		return arenaSerializer;
	}

	public void setArenaSerializer(ArenaSerializer arenaSerializer) {
		this.arenaSerializer = arenaSerializer;
	}

	public void reload(){
		reloadConfigType();
		reloadExecutors();
		reloadArenas();
		reloadMessages();
	}

	private void reloadMessages(){
		/// Reload messages
		MessageSerializer.reloadConfig(competitionName);
	}

	public MessageSerializer getMessageSerializer(){
		return MessageSerializer.getMessageSerializer(competitionName);
	}

	private void reloadExecutors(){
		/* TODO allow them to switch executors restart */
	}

	private void reloadArenas(){
		BattleArenaController ac = BattleArena.getBAController();
		for (ArenaType type : ArenaType.getTypes(plugin)){
			ac.removeAllArenas(type);
		}
		for (ArenaType type : ArenaType.getTypes(plugin)){
			ArenaSerializer.loadAllArenas(plugin,type);
		}
	}

	private void reloadConfigType() {
		configSerializer.reloadFile();
		try {
			/// The config serializer will also deal with MatchParams registration and aliases
			configSerializer.loadMatchParams();
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
		if (plugin != BattleArena.getSelf())
			plugin.reloadConfig();
	}

	public void saveParams(MatchParams params){
		configSerializer.save(params);
	}

	public Plugin getPlugin(){
		return plugin;
	}

	public CustomCommandExecutor getCustomExecutor() {
		return customExecutor;
	}
	public void setCustomExeuctor(CustomCommandExecutor customExecutor){
		this.customExecutor = customExecutor;
	}
}
