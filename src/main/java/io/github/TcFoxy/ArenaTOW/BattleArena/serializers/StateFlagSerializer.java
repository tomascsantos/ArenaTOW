package io.github.TcFoxy.ArenaTOW.BattleArena.serializers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.ArenaAlterController.ChangeType;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.containers.LobbyContainer;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.containers.RoomContainer;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ContainerState;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.Arena;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.Log;


public class StateFlagSerializer extends BaseConfig{
	public List<String> loadEnabled(){
		ConfigurationSection cs = config.getConfigurationSection("enabled");
		List<String> disabled = new ArrayList<String>();
		if (cs != null){
			for (String name : cs.getKeys(false)){
				if (!cs.getBoolean(name)){
					disabled.add(name);}
			}
		}
		return disabled;
	}

	public void loadLobbyStates(Collection<LobbyContainer> lobbies) {
		ConfigurationSection cs = config.getConfigurationSection("closedLobbies");
		if (lobbies != null){
			for (RoomContainer rc : lobbies){
				String name = rc.getParams().getType().getName();
				if (name == null)
					continue;
				try{
					String s = cs.getString(name,null);
					if (s != null){
						ContainerState.AreaContainerState pst = ContainerState.AreaContainerState.valueOf(s);

						rc.setContainerState(ContainerState.toState(pst));
					}
				} catch (Exception e){
					Log.printStackTrace(e);
				}
			}
		}
	}

	public void loadContainerStates(Map<String, Arena> arenas){
		ConfigurationSection cs = config.getConfigurationSection("closedContainers");
		if (cs == null)
			return;
		for (Arena a: arenas.values()){
			ConfigurationSection cs2 = cs.getConfigurationSection(a.getName());
			if (cs2 == null)
				continue;
			try{
				String s = cs2.getString("arena", null);
				if (s != null){
					ContainerState.AreaContainerState pst = ContainerState.AreaContainerState.valueOf(s);
					a.setContainerState(ContainerState.toState(pst));
				}
				s = cs2.getString("waitroom", null);
				if (s != null){
					ContainerState.AreaContainerState pst = ContainerState.AreaContainerState.valueOf(s);
					a.setContainerState(ChangeType.WAITROOM, ContainerState.toState(pst));
				}
			} catch (Exception e){
				Log.printStackTrace(e);
			}
		}
	}

	public void save(Collection<String> disabled, Collection<LobbyContainer> collection,
			Map<String, Arena> arenaContainer){
		ConfigurationSection cs = config.createSection("enabled");
		if (disabled != null){
			for (String s: disabled){
				cs.set(s, false);}
		}
		cs = config.createSection("closedLobbies");
		if (collection != null){
			for (RoomContainer rc: collection){
				if (rc.isOpen() || rc.getParams().getType()==null)
					continue;
				cs.set(rc.getParams().getType().getName(), rc.getContainerState().getState().name());
			}
		}
		cs = config.createSection("closedContainers");
		if (arenaContainer != null){
			for (Arena a : arenaContainer.values()){
				ConfigurationSection cs2 = cs.createSection(a.getName());
				if (!a.isOpen()){
					cs2.set("arena", a.getContainerState().getState().name());}
				if (a.getWaitroom() != null && !a.getWaitroom().isOpen()){
					cs2.set("waitroom", a.getWaitroom().getContainerState().getState().name());}
			}
		}
		save();
	}


}
