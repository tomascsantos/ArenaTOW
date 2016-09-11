package io.github.TcFoxy.ArenaTOW.BattleArena.controllers;


import java.util.Random;

import org.bukkit.Location;

import io.github.TcFoxy.ArenaTOW.BattleArena.BattleArena;
import io.github.TcFoxy.ArenaTOW.BattleArena.Defaults;
import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.Match;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.containers.AbstractAreaContainer;
import io.github.TcFoxy.ArenaTOW.BattleArena.events.players.ArenaPlayerTeleportEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.PlayerHolder;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaLocation;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaLocation.LocationType;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaPlayer;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MatchParams;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.Arena;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.options.StateOptions;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.options.TransitionOption;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.spawns.SpawnLocation;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.ArenaTeam;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.Log;
import mc.alk.arena.objects.TeleportDirection;
import mc.alk.arena.util.Util;

public class TeleportLocationController {
	static Random rand = new Random();

	public static void teleport(PlayerHolder am, ArenaTeam team,
			ArenaPlayer player, StateOptions mo, int teamIndex) {
		player.markOldLocation();
		MatchParams mp = am.getParams();

		/// EnterWaitRoom is supposed to happen before the teleport in event, but it depends on the result of a teleport
		/// Since we cant really tell the eventual result.. do our best guess
		ArenaLocation dest = getArenaLocation(am,team,player,mo,teamIndex);
		ArenaLocation src = player.getCurLocation();
		src.setLocation(player.getLocation());
		if (Defaults.DEBUG_TRACE)Log.info(" ########### @@ " + player.getCurLocation()  +"  -->  " + am.getTeam(player) );

		TeleportDirection td = calcTeleportDirection(src,dest);
		ArenaPlayerTeleportEvent apte = new ArenaPlayerTeleportEvent(mp.getType(),player,team,src,dest,td);

		movePlayer(player, apte, mp);
	}

	public static void teleportOut(PlayerHolder am, ArenaTeam team,
			ArenaPlayer player, StateOptions mo) {
        MatchParams mp = am.getParams();
		Location loc;
		ArenaLocation src = player.getCurLocation();
		final LocationType type = LocationType.HOME;
		if (mo.hasOption(TransitionOption.TELEPORTTO)){
			loc = mo.getTeleportToLoc();
		} else {
			loc = player.getOldLocation().getLocation();
			/// TODO
			/// This is a bit of a kludge, sometimes we are "teleporting them out"
			/// when they are already out... so need to rethink how this can happen and should it
			if (loc == null && src.getType()==LocationType.HOME){
				loc = src.getLocation();
			}
		}
		if (loc == null){
			Log.err(BattleArena.getNameAndVersion()+" Teleporting to a null location!  teleportTo=" + mo.hasOption(TransitionOption.TELEPORTTO));
		}

		ArenaLocation dest = new ArenaLocation(AbstractAreaContainer.HOMECONTAINER, loc,type);
		ArenaPlayerTeleportEvent apte = new ArenaPlayerTeleportEvent(am.getParams().getType(),
				player,team,src,dest,TeleportDirection.OUT);
		if (movePlayer(player, apte,mp)){
            player.clearOldLocation();
        }
	}

	private static boolean movePlayer(ArenaPlayer player, ArenaPlayerTeleportEvent apte, MatchParams mp) {
		PlayerHolder src = apte.getSrcLocation().getPlayerHolder();
		PlayerHolder dest = apte.getDestLocation().getPlayerHolder();
		TeleportDirection td = apte.getDirection();
		if (Defaults.DEBUG_TRACE)Log.info(" ###########  " + player.getCurLocation()  +"  -->  " + dest.getLocationType() );
		if (Defaults.DEBUG_TRACE)Log.info(" ---- << -- " + player.getName() +"   src=" + src +"   dest="+dest +"    td=" + td);

		switch (td){
		case RESPAWN:
			break;
		case FIRSTIN:
			mp.getGameManager().onPreJoin(player,apte);
			dest.onPreJoin(player, apte);
            break;
		case IN:
			src.onPreLeave(player, apte);
			dest.onPreEnter(player, apte);
			break;
		case OUT:
			mp.getGameManager().onPreQuit(player,apte);
			src.onPreQuit(player, apte);
			dest.onPreJoin(player, apte);
			break;
		default:
			break;
		}
		dest.callEvent(apte);
        boolean success = TeleportController.teleport(player, apte.getDestLocation().getLocation(), true);
		if (!success && player.isOnline() && !player.isDead() && !Defaults.DEBUG_VIRTUAL){
			Log.err("[BA Warning] couldn't teleport "+player.getName()+" srcLoc="+apte.getSrcLocation() +" destLoc=" + apte.getDestLocation());
		}
		player.setCurLocation(apte.getDestLocation());
		switch (td){
		case RESPAWN:
			break;
		case FIRSTIN:
			mp.getGameManager().onPostJoin(player,apte);
			dest.onPostJoin(player, apte);
            break;
		case IN:
			src.onPostLeave(player, apte);
			dest.onPostEnter(player, apte);
			break;
		case OUT:
			mp.getGameManager().onPostQuit(player,apte);
			src.onPostQuit(player, apte);
			dest.onPostJoin(player, apte);
			break;
		default:
			break;
		}
        return success;
    }

	private static TeleportDirection calcTeleportDirection(ArenaLocation src, ArenaLocation dest) {
		if (src.getType() == LocationType.HOME){
			return TeleportDirection.FIRSTIN;
		} else if (src.getType() == dest.getType()){
			return TeleportDirection.RESPAWN;
		}
		return TeleportDirection.IN;
	}

	private static ArenaLocation getArenaLocation(PlayerHolder am, ArenaTeam team,
			ArenaPlayer player, StateOptions tops, int teamIndex){
		final MatchParams mp = am.getParams();
		final boolean randomRespawn = tops.hasOption(TransitionOption.RANDOMRESPAWN);
        SpawnLocation l;
		final LocationType type;
		final PlayerHolder ph;
		if (Defaults.DEBUG_TRACE)Log.info(" team=" + team+" teamindex = " + teamIndex +"  " + am.getClass().getSimpleName()  +"  " +am);
        if (teamIndex == -1){
            Log.err("Team index for " + am +" team="+team+" " + teamIndex+" was -1");
            Util.printStackTrace();
            teamIndex=0;
        }
        if (tops.shouldTeleportWaitRoom()){
			if (tops.hasOption(TransitionOption.TELEPORTMAINWAITROOM)){
				teamIndex = Defaults.MAIN_SPAWN;}
			ph = (am instanceof Match) ? ((Match)am).getArena().getWaitroom() : am;
			type = LocationType.WAITROOM;
			l = ph.getSpawn(teamIndex, randomRespawn);
		} else if (tops.shouldTeleportLobby()){
			if (tops.hasOption(TransitionOption.TELEPORTMAINLOBBY)){
				teamIndex = Defaults.MAIN_SPAWN;}
			ph = RoomController.getLobby(mp.getType());
			type = LocationType.LOBBY;
			l = RoomController.getLobbySpawn(teamIndex,mp.getType(),randomRespawn);
		} else if (tops.shouldTeleportSpectate()){
			ph = (am instanceof Match) ? ((Match)am).getArena().getSpectatorRoom() : am;
			type = LocationType.SPECTATE;
			l = ph.getSpawn(teamIndex, randomRespawn);
		} else { // They should teleportIn, aka to the Arena
			final Arena arena;
			if (am instanceof Arena){
				arena = (Arena) am;
			} else if (am instanceof Match){
				Match m = (Match) am;
				arena = m.getArena();
			} else {
				throw new IllegalStateException("[BA Error] Instance is " + am.getClass().getSimpleName());
			}
            boolean random = (player.getCurLocation().getType() == LocationType.HOME &&
                    tops.hasOption(TransitionOption.RANDOMSPAWN));
            ph = am;
			type = LocationType.ARENA;
			l = arena.getSpawn(teamIndex,random);
		}
		return new ArenaLocation(ph, l.getLocation(),type);
	}

}
