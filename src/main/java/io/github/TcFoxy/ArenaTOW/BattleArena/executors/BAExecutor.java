package io.github.TcFoxy.ArenaTOW.BattleArena.executors;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import io.github.TcFoxy.ArenaTOW.BattleArena.BattleArena;
import io.github.TcFoxy.ArenaTOW.BattleArena.competition.Competition;
import io.github.TcFoxy.ArenaTOW.BattleArena.competition.events.Event;
import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.Match;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.ArenaAlterController;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.ArenaAlterController.ArenaOptionPair;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.ArenaAlterController.ChangeType;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.ArenaClassController;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.BAEventController;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.CompetitionController;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.EventController;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.MoneyController;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.ParamAlterController;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.ParamController;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.PlayerController;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.RoomController;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.TeamController;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.WatchController;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.containers.LobbyContainer;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.containers.RoomContainer;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.joining.AbstractJoinHandler;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.messaging.MessageHandler;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.plugins.EssentialsController;
import io.github.TcFoxy.ArenaTOW.BattleArena.events.arenas.ArenaCreateEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.events.arenas.ArenaDeleteEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.events.players.ArenaPlayerJoinEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.events.players.ArenaPlayerLeaveEvent;
import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.PlayerHolder;
import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.competition.InArenaListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaClass;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaLocation;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaPlayer;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaSize;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.CompetitionSize;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.CompetitionState;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ContainerState;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.LocationType;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MatchParams;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MatchState;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.PlayerSave;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.StateGraph;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.Arena;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.ArenaControllerInterface;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.ArenaType;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.exceptions.InvalidOptionException;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.joining.TeamJoinObject;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.messaging.AnnouncementOptions;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.messaging.Channel;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.options.AlterParamOption;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.options.EventOpenOptions;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.options.JoinOptions;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.options.StateOptions;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.options.TransitionOption;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.pairs.JoinResult;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.pairs.ParamAlterOptionPair;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.pairs.TransitionOptionTuple;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.spawns.FixedLocation;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.ArenaTeam;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.FormingTeam;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.teams.TeamIndex;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.InventoryUtil;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.InventoryUtil.PInv;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.Log;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.MessageUtil;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.PermissionsUtil;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.ServerUtil;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.TeamUtil;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.TimeUtil;


/**
 *
 * @author alkarin
 *
 */
public class BAExecutor extends CustomCommandExecutor {

    Set<String> disabled = new HashSet<String>();

    final TeamController teamc;
    final EventController ec;
    final WatchController watchController;

    public BAExecutor() {
        super();
        this.ec = BattleArena.getEventController();
        this.teamc = BattleArena.getTeamController();
        this.watchController = BattleArena.getSelf().getWatchController();
    }

    @MCCommand(cmds = {"enable"}, admin = true, perm = "arena.enable", usage = "enable")
    public boolean arenaEnable(CommandSender sender, MatchParams mp,
            String[] args) {
        if (args.length > 1 && args[1].equalsIgnoreCase("all")) {
            Set<String> set = new HashSet<String>(); // / Since some commands
            // have aliases.. we
            // just want the
            // original name
            for (MatchParams param : ParamController.getAllParams()) {
                disabled.remove(param.getName());
                set.add(param.getName());
            }
            for (String s : set) {
                sendSystemMessage(sender, "type_enabled", s);
            }

            return true;
        }
        disabled.remove(mp.getName());
        return sendSystemMessage(sender, "type_enabled", mp.getName());
    }

    @MCCommand(cmds = {"disable"}, admin = true, perm = "arena.enable", usage = "disable")
    public boolean arenaDisable(CommandSender sender, MatchParams mp,
            String[] args) {
        if (args.length > 1 && args[1].equalsIgnoreCase("all")) {
            Set<String> set = new HashSet<String>(); // / Since some commands
            // have aliases.. we
            // just want the
            // original name
            for (MatchParams param : ParamController.getAllParams()) {
                disabled.add(param.getName());
                set.add(param.getName());
            }
            for (String s : set) {
                sendSystemMessage(sender, "type_disabled", s);
            }
            return true;
        }
        disabled.add(mp.getName());
        return sendSystemMessage(sender, "type_disabled", mp.getName());
    }

    public static boolean sendSystemMessage(CommandSender sender, String node, Object... args) {
        return sendMessage(sender, MessageHandler.getSystemMessage(node, args));
    }

    public static boolean sendSystemMessage(ArenaTeam team, String node, Object... args) {
        team.sendMessage(MessageHandler.getSystemMessage(node, args));
        return true;
    }

    public static boolean sendSystemMessage(ArenaPlayer sender, String node, Object... args) {
        return sendMessage(sender, MessageHandler.getSystemMessage(node, args));
    }

    @MCCommand(cmds = {"enabled"}, admin = true)
    public boolean arenaCheckArenaTypes(CommandSender sender) {
        String types = ArenaType.getValidList();
        sendMessage(sender, "&e valid types are = &6" + types);
        return sendMessage(sender, "&5Enabled types = &6 " + ParamController.getAvaibleTypes(disabled));
    }

    @MCCommand(cmds = {"join", "j"}, usage = "add [options]", helpOrder = 1)
    public boolean join(ArenaPlayer player, MatchParams mp, String args[]) {
        return join(player, mp, args, false);
    }

    public boolean join(ArenaPlayer player, final MatchParams omp, String args[], boolean adminJoin) {
        JoinOptions jp;
        try {
            jp = JoinOptions.parseOptions(omp, player, Arrays.copyOfRange(args, 1, args.length));
        } catch (InvalidOptionException e) {
            return sendMessage(player, e.getMessage());
        } catch (Exception e) {
            Log.printStackTrace(e);
            return sendMessage(player, e.getMessage());
        }
        return join(player, omp, jp, adminJoin);
    }

    public boolean join(ArenaPlayer player, final MatchParams omp, JoinOptions jp, boolean adminJoin) {
        /// Make sure we have Match Options
        final StateGraph ops = omp.getStateGraph();
        if (ops == null) {
            return sendMessage(player, "&cThis match type has no valid options, contact an admin to fix");
        }

        /// Check if this match type is disabled
        if (isDisabled(player.getPlayer(), omp) && !PermissionsUtil.isAdmin(player.getPlayer())) {
            return true;
        }

        /// Check Perms
        if (!adminJoin && !PermissionsUtil.hasMatchPerm(player.getPlayer(), omp, "join")) {
            return sendSystemMessage(player, "no_join_perms", omp.getCommand());
        }
        /// Can the player add this match/event at this moment?
        if (!canJoin(player)) {
            return true;
        }
        /// Call the joining event
        ArenaPlayerJoinEvent event = new ArenaPlayerJoinEvent(player);
        event.callEvent();
        if (event.isCancelled()) {
            //noinspection SimplifiableIfStatement
            if (event.getMessage() != null && !event.getMessage().isEmpty()) {
                return sendMessage(player, event.getMessage());
            }
            return true;
        }

        /// Get or Make a team for the Player
        ArenaTeam t = teamc.getSelfFormedTeam(player);
        if (t == null) {
            t = TeamController.createTeam(omp, player);
        }

        if (!canJoin(t, true)) {
            sendSystemMessage(player, "teammate_cant_join", omp.getName());
            return sendMessage(player, "&6/team leave: &cto leave the team");
        }

        MatchParams mp = jp.getMatchParams();
        /// Check to make sure at least one arena can be joined at some time
        Arena arena = ac.getArenaByMatchParams(jp);
        if (arena == null) {
            if (!jp.hasWantedTeamSize()) {
                arena = ac.getArenaByNearbyMatchParams(jp);
                if (arena != null) {
                    mp.setMinTeamSize(arena.getParams().getMinTeamSize());
                    mp.setMaxTeamSize(arena.getParams().getMaxTeamSize());
                }
            }
            if (arena == null) {
                Map<Arena, List<String>> reasons = ac.getNotMachingArenaReasons(jp);
                if (!reasons.isEmpty()) {
                    for (Arena a : reasons.keySet()) {
                        List<String> rs = reasons.get(a);
                        if (!rs.isEmpty()) {
                            return sendMessage(player, "&c" + rs.get(0));
                        }
                    }
                }
                return sendSystemMessage(player, "valid_arena_not_built", mp.getName());
            }
        }

        if (!arena.isJoinable(mp)) {
            return MessageUtil.sendMessage(player,
                    "&c" + arena.getName() + " can't be joined at this time.\n"
                    + arena.getNotJoinableReasons(mp));
        }

        // BTInterface bti = BTInterface.getInterface(mp);
        // if (bti.isValid()){
        // bti.updateRanking(t);
        // }
        /// Check for lobbies
        if (ops.hasAnyOption(TransitionOption.TELEPORTLOBBY)
                && !RoomController.hasLobby(mp.getType())) {
            return sendMessage(player,
                    "&cThis match has no lobby and needs one! contact an admin to fix");
        }

        /// Check if the team is ready
        if (!ops.teamReady(t, null)) {
            t.sendMessage(ops.getRequiredString(MessageHandler
                    .getSystemMessage("need_the_following") + "\n"));
            return true;
        }

        /// Check entrance fee
        if (!checkAndRemoveFee(mp, t)) {
            return true;
        }

        TeamJoinObject tqo = new TeamJoinObject(t, mp, jp);
        JoinResult jr;
        try {
            /// Add them to the queue
            jr = ac.wantsToJoin(tqo);
        } catch (IllegalStateException e) {
            return sendMessage(player, "&c" + e.getMessage());
        }
        AnnouncementOptions ao = mp.getAnnouncementOptions();

        /// switch over to the joined params
        mp = jr.params;
        /// Annouce to the server if they have the option set

        Channel channel = ao != null ? ao.getChannel(true, MatchState.ONENTERQUEUE)
                : AnnouncementOptions.getDefaultChannel(true,
                        MatchState.ONENTERQUEUE);
        String neededPlayers = jr.maxPlayers == CompetitionSize.MAX ? "inf" : jr.maxPlayers + "";
        List<Object> vars = new ArrayList<Object>();
        vars.add(mp);
        vars.add(t);
        channel.broadcast(MessageHandler.getSystemMessage(
                vars, "server_joined_the_queue", mp.getPrefix(),
                player.getDisplayName(), jr.playersInQueue, neededPlayers));
        String sysmsg;
        switch (jr.status) {
            case ADDED_TO_EXISTING_MATCH:
                if (t.size() == 1) {
                    t.sendMessage(MessageHandler.getSystemMessage(
                            "you_joined_event", mp.getName()));
                } else {
                    t.sendMessage(MessageHandler
                            .getSystemMessage("you_added_to_team"));
                }
                break;
            case ADDED_TO_QUEUE:
                sysmsg = MessageHandler.getSystemMessage("joined_the_queue",
                        mp.getName(), jr.pos, neededPlayers);

                StringBuilder msg = new StringBuilder(sysmsg != null ? sysmsg
                        : "&eYou joined the &6" + mp.getName() + "&e queue");
                if (jr.maxPlayers != CompetitionSize.MAX) {
                    String posmsg = MessageHandler.getSystemMessage(
                            "position_in_queue", jr.pos, neededPlayers);
                    msg.append(posmsg != null ? posmsg : "");
                }
                if (jr.time != null) {
                    Long time = jr.time - System.currentTimeMillis();
                    msg.append(constructMessage(jr.params, time, jr.playersInQueue, jr.pos));
                }

                t.sendMessage(msg.toString());
                break;
            default:
                break;
        }
        return true;
    }

    public static String constructMessage(MatchParams mp, long millisRemaining, int playersInQ, Integer position) {
        StringBuilder msg = new StringBuilder();
        if (millisRemaining <= 0) {
            String max = mp.getMaxPlayers() == ArenaSize.MAX ? "\u221E" : mp.getMaxPlayers() + "";
            msg.append("\n").append(MessageHandler.getSystemMessage("match_starts_immediately",
                    mp.getMinPlayers() - playersInQ, playersInQ, max));
        } else {
            if (mp.getMaxPlayers() == CompetitionSize.MAX) {
                if (playersInQ < mp.getMinPlayers()) {
                    msg.append("\n").append(MessageHandler.getSystemMessage(
                            "match_starts_players_or_time2",
                            TimeUtil.convertMillisToString(millisRemaining),
                            mp.getMinPlayers() - playersInQ));
                } else if (playersInQ < mp.getMaxPlayers()) {
                    msg.append("\n").append(MessageHandler.getSystemMessage(
                            "match_starts_when_time",
                            TimeUtil.convertMillisToString(millisRemaining)));
                } else {
                    msg.append("\n").append(MessageHandler.getSystemMessage("you_start_when_free"));
                }
            } else if (mp.getMinPlayers().equals(mp.getMaxPlayers())) {
                if (playersInQ < mp.getMinPlayers()) {
                    msg.append("\n").append(MessageHandler.getSystemMessage(
                            "match_starts_immediately",
                            mp.getMaxPlayers() - playersInQ,
                            playersInQ,
                            mp.getMinPlayers()));
                } else {
                    msg.append("\n").append(MessageHandler.getSystemMessage("you_start_when_free"));
                }
            } else {
                if (playersInQ < mp.getMinPlayers()) {
                    msg.append("\n").append(MessageHandler.getSystemMessage(
                            "match_starts_players_or_time",
                            mp.getMaxPlayers() - playersInQ,
                            TimeUtil.convertMillisToString(millisRemaining),
                            mp.getMinPlayers()));
                } else if (playersInQ < mp.getMaxPlayers()) {
                    msg.append("\n").append(MessageHandler.getSystemMessage(
                            "match_starts_players_or_time3",
                            mp.getMaxPlayers() - playersInQ,
                            TimeUtil.convertMillisToString(millisRemaining)));
                } else {
                    if (position == null) {
                        msg.append("\n").append(MessageHandler.getSystemMessage("you_start_when_free"));
                    } else {
                        msg.append("\n").append(MessageHandler.getSystemMessage("you_start_when_free_pos", position));
                    }
                }
            }

        }

        return msg.toString();
    }

    protected boolean isDisabled(CommandSender sender, MatchParams mp) {
        if (disabled.contains(mp.getName())) {
            sendSystemMessage(sender, "match_disabled", mp.getName());
            final String enabled = ParamController.getAvaibleTypes(disabled);
            if (enabled.isEmpty()) {
                return sendSystemMessage(sender, "all_disabled");
            } else {
                return sendSystemMessage(sender, "currently_enabled", enabled);
            }
        }
        return false;
    }

    @MCCommand(cmds = {"leave", "l"}, usage = "leave", helpOrder = 2)
    public boolean leave(ArenaPlayer p, MatchParams mp) {
        return leave(p, mp, false);
    }

    @MCCommand(cmds = {"switch"}, perm = "arena.switch")
    public boolean switchTeam(ArenaPlayer p, MatchParams mp, String teamStr) {
        Integer index = TeamUtil.getFromHumanTeamIndex(teamStr);
        if (index == null) {
            return MessageUtil.sendMessage(p, "&cBad team index");
        }

        ArenaLocation loc = p.getCurLocation();
        PlayerHolder ph = loc.getPlayerHolder();
        Competition c = p.getCompetition();
        if (c == null && (ph == null || loc.getType() == LocationType.HOME)) {
            if (ac.isInQue(p)) {
                JoinOptions jo = p.getMetaData().getJoinOptions();
                if (jo != null) {
                    jo.setOption(JoinOptions.JoinOption.TEAM, index);
                    return MessageUtil.sendMessage(p, "&eSwitched to team &6" + index);
                }
            }
        } else if (c == null) {
            if (ph instanceof RoomContainer) {
                /// they are not in a match, but are in the waitroom beforehand

            } else {
                /// They aren't in anywhere
            }
        } else { /// they are in a competition
            if (c instanceof Match) {
                AbstractJoinHandler tjh = ((Match) c).getTeamJoinHandler();
                tjh.switchTeams(p, index, true);
            } else {
                /// Not a match (like a tournament), they can't switch
            }
        }
        return true;
    }

    public boolean leave(ArenaPlayer p, MatchParams mp, boolean adminLeave) {
        if (!adminLeave && !p.hasPermission("arena.leave")
                && !PermissionsUtil.hasMatchPerm(p.getPlayer(), mp, "leave")) {
            return true;
        }

        ArenaPlayerLeaveEvent event = new ArenaPlayerLeaveEvent(p, p.getTeam(),
                ArenaPlayerLeaveEvent.QuitReason.QUITCOMMAND);
        event.callEvent();
        if (event.getMessages() != null && !event.getMessages().isEmpty()) {
            MessageUtil.sendMessage(event.getPlayer(), event.getMessages());
        } else {
            sendSystemMessage(p, "you_not_in_queue");
        }
        return true;
    }

    // @MCCommand(cmds={"ready","r"}, inGame=true)
    // public boolean ready(ArenaPlayer player) {
    // boolean wasReady = player.isReady();
    // if (wasReady){
    // return sendMessage(player,"&cYou are already ready");
    // }
    // player.setReady(true);
    // sendMessage(player,"&2You are now ready");
    // new PlayerReadyEvent(player,player.isReady()).callEvent();
    // return true;
    // }
    @MCCommand(cmds = {"cancel"}, admin = true, usage = "cancel <arenaname or player>")
    public boolean arenaCancel(CommandSender sender, MatchParams params, String[] args) {
        if (args.length > 1 && args[1].equalsIgnoreCase("all")) {
            return cancelAll(sender);
        }
        List<Match> matches = ac.getRunningMatches(params);
        if (!matches.isEmpty()) {
            for (Match m : matches) {
                m.cancelMatch();
                if (m.getState() == MatchState.ONCANCEL) {
                    Arena arena = m.getArena();
                    ac.removeArena(arena);
                    ac.addArena(arena);
                }
            }
            return sendMessage(sender, "&2You have canceled the matches for &6"
                    + params.getType());
        }
        if (args.length < 2) {
            return sendMessage(sender, "cancel <arenaname or player>");
        }
        Player player = ServerUtil.findPlayer(args[1]);
        if (player != null) {
            ArenaPlayer ap = PlayerController.toArenaPlayer(player);
            if (ac.cancelMatch(ap)) {
                return sendMessage(
                        sender,
                        "&2You have canceled the match for &6"
                        + player.getName());
            } else {
                return sendMessage(sender, "&cMatch couldnt be found for &6"
                        + player.getName());
            }
        }
        String arenaName = args[1];
        Arena arena = ac.getArena(arenaName);
        if (arena == null) {
            return sendMessage(sender, "&cArena " + arenaName + " not found");
        }
        if (ac.cancelMatch(arena)) {
            return sendMessage(sender,
                    "&2You have canceled the match in arena &6" + arenaName);
        } else {
            return sendMessage(sender, "&cError cancelling arena match");
        }
    }

    private boolean cancelAll(CommandSender sender) {
        Collection<ArenaTeam> teams = ac.purgeQueue();
        for (ArenaTeam t : teams) {
            t.sendMessage("&cYou have been removed from the queue");
        }
        ac.cancelAllArenas();
        ec.cancelAll();
        RoomController.cancelAll();
        return sendMessage(sender,
                "&2You have cancelled all matches/events and cleared the queue");
    }

    @MCCommand(cmds = {"status"}, admin = true, min = 2, usage = "status <arena or player>")
    public boolean arenaStatus(CommandSender sender, String[] args) {
        Match am;
        String pormatch = args[1];
        Arena a = ac.getArena(pormatch);
        Player player;
        if (a == null) {
            player = ServerUtil.findPlayer(pormatch);
            if (player == null) {
                return sendMessage(sender, "&eCouldnt find arena or player="
                        + pormatch);
            }
            ArenaPlayer ap = PlayerController.toArenaPlayer(player);
            am = ac.getMatch(ap);
            if (am == null) {
                return sendMessage(sender, "&ePlayer " + pormatch
                        + " is not in a match");
            }
        } else {
            am = ac.getArenaMatch(a);
            if (am == null) {
                return sendMessage(sender, "&earena " + pormatch
                        + " is not being used in a match");
            }
        }
        return sendMessage(sender, am.getMatchInfo());
    }

    @MCCommand(cmds = {"winner"}, admin = true, min = 2, usage = "winner <player>")
    public boolean arenaSetVictor(CommandSender sender, ArenaPlayer ap) {
        Match am = ac.getMatch(ap);
        if (am == null) {
            return sendMessage(sender, "&ePlayer " + ap.getName()
                    + " is not in a match");
        }
        am.setVictor(ap);
        return sendMessage(sender, "&6" + ap.getName()
                + " has now won the match!");
    }


    @MCCommand(cmds = {"auto"}, admin = true, perm = "arena.auto")
    public boolean arenaAuto(CommandSender sender, MatchParams params, String args[]) {
        try {
            EventOpenOptions eoo = EventOpenOptions.parseOptions(args, null, params);

            Arena arena = eoo.getArena(params);
            if (arena == null) {
                return sendMessage(sender, "[BattleArena] auto args=" + Arrays.toString(args)
                        + " can't be started. Arena  is not there or in use");
            }

            ac.createAndAutoMatch(arena, eoo);
            final int max = arena.getParams().getMaxPlayers();
            final String maxPlayers = max == ArenaSize.MAX ? "&6any&2 number of players"
                    : max + "&2 players";
            sendMessage(sender,
                    "&2You have " + args[0] + "ed a &6" + params.getName()
                    + "&2 inside &6" + arena.getName()
                    + " &2TeamSize=&6"
                    + arena.getParams().getTeamSize()
                    + "&2 #Teams=&6"
                    + arena.getParams().getNTeams()
                    + "&2 supporting " + maxPlayers + "&2 at &5"
                    + arena.getName());
        } catch (InvalidOptionException e) {
            sendMessage(sender, e.getMessage());
        } catch (Exception e) {
            sendMessage(sender, e.getMessage());
            Log.printStackTrace(e);
        }
        return true;
    }

    @MCCommand(cmds = {"open"}, admin = true, exact = 2, perm = "arena.open")
    public boolean arenaOpen(CommandSender sender, MatchParams mp,
            String arenaName) {
        if (arenaName.equalsIgnoreCase("all")) {
            ac.openAll(mp);
            return sendMessage(sender, "&6Arenas for " + mp.getName()
                    + ChatColor.YELLOW + " are now &2open");
        } else if (arenaName.equalsIgnoreCase("lobby")) {
            LobbyContainer lc = RoomController.getLobby(mp.getType());
            if (lc == null) {
                return sendMessage(sender, "&cYou need to set a lobby for "
                        + mp.getName());
            }
            lc.setContainerState(ContainerState.OPEN);
            return sendMessage(sender, "&6 Lobby for " + mp.getName()
                    + ChatColor.YELLOW + " is now &2open");
        } else {
            Arena arena = ac.getArena(arenaName);
            if (arena == null) {
                return sendMessage(sender, "&cArena " + arenaName
                        + " could not be found");
            }

            arena.setAllContainerState(ContainerState.OPEN);
            return sendMessage(sender, "&6" + arena.getName()
                    + ChatColor.YELLOW + " is now &2open");
        }
    }

    @MCCommand(cmds = {"open"}, admin = true, perm = "arena.open")
    public boolean arenaOpenContainer(CommandSender sender, Arena arena,
            ChangeType type) {
        try {
            if (type == ChangeType.LOBBY) {
                LobbyContainer lc = RoomController.getLobby(arena
                        .getArenaType());
                if (lc == null) {
                    return sendMessage(sender, "&cYou need to set a lobby for "
                            + arena.getArenaType().getName());
                }
                lc.setContainerState(ContainerState.OPEN);
            } else {
                arena.setContainerState(type, ContainerState.OPEN);
            }
        } catch (IllegalStateException e) {
            return sendMessage(sender, "&c" + e.getMessage());
        }
        return sendMessage(sender, "&6" + arena.getName() + ChatColor.YELLOW
                + " is now &2open");
    }

    @MCCommand(cmds = {"close"}, admin = true, exact = 2, perm = "arena.close")
    public boolean arenaClose(CommandSender sender, MatchParams mp,
            String arenaName) {
        if (arenaName.equals("all")) {
            for (Arena arena : ac.getArenas(mp)) {
                arena.setAllContainerState(ContainerState.CLOSED);
            }
            return sendMessage(sender, "&6Arenas for " + mp.getName()
                    + ChatColor.YELLOW + " are now &4closed");
        } else if (arenaName.equalsIgnoreCase("lobby")) {
            LobbyContainer lc = RoomController.getLobby(mp.getType());
            if (lc == null) {
                return sendMessage(sender, "&cYou need to set a lobby for "
                        + mp.getName());
            }
            lc.setContainerState(ContainerState.CLOSED);
            return sendMessage(sender, "&6 Lobby for " + mp.getName()
                    + ChatColor.YELLOW + " is now &4closed");
        } else {
            Arena arena = ac.getArena(arenaName);
            if (arena == null) {
                return sendMessage(sender, "&cArena " + arenaName
                        + " could not be found");
            }
            arena.setAllContainerState(ContainerState.CLOSED);
            return sendMessage(sender, "&6" + arena.getName()
                    + ChatColor.YELLOW + " is now &4closed");
        }
    }

    @MCCommand(cmds = {"close"}, admin = true, perm = "arena.close")
    public boolean arenaCloseContainer(CommandSender sender, Arena arena,
            ChangeType closeLocation) {
        try {
            arena.setContainerState(closeLocation, ContainerState.CLOSED);
        } catch (IllegalStateException e) {
            return sendMessage(sender, "&c" + e.getMessage());
        }
        return sendMessage(sender, "&6" + arena.getName() + ChatColor.YELLOW
                + " " + closeLocation + " is now &4closed");
    }

    @MCCommand(cmds = {"delete"}, admin = true, perm = "arena.delete")
    public boolean arenaDelete(CommandSender sender, Arena arena) {
        new ArenaDeleteEvent(arena).callEvent();
        ac.deleteArena(arena);
        BattleArena.saveArenas(arena.getArenaType().getPlugin());
        return sendMessage(sender, ChatColor.GREEN
                + "You have deleted the arena &6" + arena.getName());
    }

    @MCCommand(cmds = {"save"}, admin = true, perm = "arena.save")
    public boolean arenaSave(CommandSender sender) {
        BattleArena.saveArenas(true);
        return sendMessage(sender, "&eArenas saved");
    }

    @MCCommand(cmds = {"reload"}, admin = true, perm = "arena.reload")
    public boolean arenaReload(CommandSender sender, MatchParams mp) {
        Plugin plugin = mp.getType().getPlugin();
        BAEventController baec = BattleArena.getBAEventController();
        if (ac.hasRunningMatches() || !ac.isQueueEmpty() || baec.hasOpenEvent()) {
            sendMessage(
                    sender,
                    "&cYou can't reload the config while matches are running or people are waiting in the queue");
            return sendMessage(sender,
                    "&cYou can use &6/arena cancel all&c to cancel all matches and clear queues");
        }

        ac.stop();
        BattleArena.getSelf().reloadConfig();

        // / Get rid of any current players
        PlayerController.clearArenaPlayers();
        CompetitionController.reloadCompetition(plugin, mp);
        ac.resume();
        return sendMessage(sender, "&6" + plugin.getName()
                + "&e configuration reloaded");
    }

    @MCCommand(cmds = {"info"}, exact = 1, usage = "info")
    public boolean arenaInfo(CommandSender sender, MatchParams mp) {
        String info = StateOptions.getInfo(mp, mp.getName());
        return sendMessage(sender, info);
    }

    @MCCommand(cmds = {"info"}, admin = true, usage = "info <arenaname>", order = 1, helpOrder = 6)
    public boolean info(CommandSender sender, Arena arena) {
        sendMessage(sender, arena.toDetailedString());
        Match match = ac.getMatch(arena);
        if (match != null) {
            List<String> strs = new ArrayList<String>();
            for (ArenaTeam t : match.getTeams()) {
                strs.add("&5 -&e" + t.getDisplayName());
            }
            sendMessage(sender, "Teams: " + StringUtils.join(strs, ", "));
        }
        return true;
    }

    @MCCommand(cmds = {"watch"}, subCmds = {"leave"})
    public boolean watchLeave(ArenaPlayer sender) {
        if (!watchController.hasWatcher(sender)) {
            return sendMessage(sender, "&cYou aren't watching any arenas");
        }
        watchController.leave(sender);
        return sendMessage(sender, "&eYou stopped watching");
    }

    @MCCommand(cmds = {"watch"})
    public boolean watch(ArenaPlayer sender, MatchParams mp, ArenaPlayer player) {
        if (player.getCompetition() == null || !(player.getCompetition() instanceof Match)) {
            return sendMessage(sender, "&cThat player is not in a game");
        }
        return watch(sender, mp, ((Match) player.getCompetition()).getArena());
    }

    @MCCommand(cmds = {"watch"})
    public boolean watch(ArenaPlayer sender, MatchParams mp, Arena arena) {
        if (!PermissionsUtil.hasMatchPerm(sender.getPlayer(), mp, "watch")) {
            return sendMessage(sender,
                    "&cYou don't have permission to watch a &6" + mp.getCommand());
        }
        if (isDisabled(sender.getPlayer(), mp)) {
            return true;
        }
        if (!canJoin(sender)) {
            return true;
        }
        if (arena.getVisitorLocs() == null) {
            return sendMessage(sender, ChatColor.YELLOW + "That arena doesnt allow visitors!");
        }
        if (watchController.watch(sender, arena)) {
            return sendMessage(sender, ChatColor.YELLOW + "You are now watching "
                    + arena.getName() + " /watch leave : to leave");
        } else {
            return sendMessage(sender, ChatColor.RED + "You can't watch at this time");
        }
    }

    @MCCommand(cmds = {"create"}, admin = true, perm = "arena.create", usage = "create <arena name>")
    public boolean arenaCreate(Player sender, MatchParams mp, String name) {
        if (ac.getArena(name) != null) {
            return sendMessage(sender, "&cThere is already an arena named &6" + name);
        }
        if (ParamController.getMatchParams(name) != null) {
            return sendMessage(sender, "&cYou can't choose an arena type as an arena name");
        }
        try {
            int i = Integer.valueOf(name);
            return sendMessage(sender, "&cYou can't choose a number as the arena name! Arena name was &e" + i);
        } catch (Exception e) {
            /* good, it's not an integer*/
        }
        if (ParamController.getMatchParams(name) != null) {
            return sendMessage(sender, "&cYou can't choose an arena type as an arena name");
        }

        MatchParams ap = new MatchParams(mp.getType());

        Arena arena = ArenaType.createArena(name, ap, false);
        if (arena == null) {
            return sendMessage(sender, "&cCouldn't create the arena " + name + " of type " + ap.getType());
        }

        arena.setSpawnLoc(0, 0, new FixedLocation(sender.getLocation()));
        ac.addArena(arena);
        ArenaControllerInterface aci = new ArenaControllerInterface(arena);
        aci.create();
        new ArenaCreateEvent(arena).callEvent();
        aci.init();

        sendMessage(sender, "&2You have created the arena &6" + arena);
        sendMessage(sender, "&2A spawn point has been created where you are standing");
        sendMessage(sender, "&2You can add/change spawn points using &6/arena alter "
                + arena.getName() + " <1,2,...,x : which spawn>");
        BattleArena.saveArenas(arena.getArenaType().getPlugin());
        return BattleArena.getSelf().getArenaEditorExecutor().arenaSelect(sender, arena);
    }

    @MCCommand(cmds = {"select", "sel"}, admin = true, perm = "arena.alter")
    public boolean arenaSelect(CommandSender sender, Arena arena) {
        try {
            ArenaEditorExecutor aee = BattleArena.getSelf().getArenaEditorExecutor();
            return aee.arenaSelect(sender, arena);
        } catch (IllegalStateException e) {
            return sendMessage(sender, "&c" + e.getMessage());
        }
    }

    @MCCommand(cmds = {"setArenaOption", "alter", "edit"}, admin = true, perm = "arena.alter")
    public boolean arenaSetOption(CommandSender sender, Arena arena, ArenaOptionPair aop) {
        return ArenaEditorExecutor.setArenaOption(sender, arena, aop);
    }

    @MCCommand(cmds = {"setArenaOption", "alter", "edit"}, admin = true, perm = "arena.alter")
    public boolean arenaSetOption(CommandSender sender, Arena arena, ParamAlterOptionPair gop) {
        return ArenaEditorExecutor.setArenaOption(sender, arena, gop);
    }

    @MCCommand(cmds = {"setArenaOption", "alter", "edit"}, admin = true, perm = "arena.alter")
    public boolean arenaSetOption(CommandSender sender, Arena arena, TransitionOptionTuple top) {
        return ArenaEditorExecutor.setArenaOption(sender, arena, top);
    }

    @MCCommand(cmds = {"setOption"}, admin = true, perm = "arena.alter")
    public boolean setGameOption(CommandSender sender, MatchParams params, ParamAlterOptionPair gop) {
        return _setGameOption(sender, params, null, gop.alterParamOption, gop.value);
    }

    @MCCommand(cmds = {"setOption"}, admin = true, perm = "arena.alter")
    public boolean setGameOption(CommandSender sender, MatchParams params, TeamIndex index, ParamAlterOptionPair gop) {
        return _setGameOption(sender, params, index.getInt(), gop.alterParamOption, gop.value);
    }

    public boolean _setGameOption(CommandSender sender, MatchParams params,
            Integer teamIndex, AlterParamOption option, Object value) {
        try {
            ParamAlterController.setGameOption(sender, params, teamIndex, option, value);
            if (value != null) {
                sendMessage(sender, "&2Game options &6" + option + "&2 changed to &6" + value);
            } else {
                sendMessage(sender, "&2Game options &6" + option + "&2 changed");
            }
        } catch (InvalidOptionException e) {
            sendMessage(sender, "&cCould not set game option " + option.name());
            sendMessage(sender, "&c" + e.getMessage());
        }
        return true;
    }

    @MCCommand(cmds = {"setOption"}, admin = true, perm = "arena.alter")
    public boolean setGameStateOption(CommandSender sender, MatchParams params, TransitionOptionTuple top) {
        return _setGameStateOption(sender, params, null, top.state, top.op, top.value);
    }

    @MCCommand(cmds = {"setOption"}, admin = true, perm = "arena.alter")
    public boolean setGameStateOption(CommandSender sender, MatchParams params, TeamIndex index, TransitionOptionTuple top) {
        return _setGameStateOption(sender, params, index.getInt(), top.state, top.op, top.value);
    }

    public boolean _setGameStateOption(CommandSender sender, MatchParams params, Integer teamIndex,
            CompetitionState state, TransitionOption to, Object value) {
        try {
            ParamAlterController.setGameOption(sender, params, teamIndex, state, to, value);
            if (value != null) {
                sendMessage(sender, "&2Game options &6" + state + "&2 added &6" + to + " " + value);
            } else {
                sendMessage(sender, "&2Game options &6" + state + "&2 added &6" + to);
            }
            StateGraph tops = params.getThisStateGraph();
            StateOptions ops = tops.getOptions(state);
            sendMessage(sender, "&2Options at &6" + state + "&2 now &6" + ops.toString());
        } catch (InvalidOptionException e) {
            sendMessage(sender, "&cCould not set game option " + state + " " + to);
            sendMessage(sender, "&c" + e.getMessage());
        }
        return true;
    }

    @MCCommand(cmds = {"showOptions"}, admin = true, perm = "arena.alter")
    public boolean showGameOptions(CommandSender sender, MatchParams params) {
        sendMessage(sender, "&2Options for &f" + params.getName() + "&2 : " + params.getDisplayName());
        sendMessage(sender, params.toSummaryString());
        return sendMessage(sender, params.getOptionsSummaryString());
    }

    @MCCommand(cmds = {"showOptions"}, admin = true, perm = "arena.alter")
    public boolean showGameOptions(CommandSender sender, Arena arena) {
        sendMessage(sender, "&2Options for arena &f" + arena.getName() + "&2 : " + arena.getDisplayName());
        sendMessage(sender, arena.getParams().toSummaryString());
        return sendMessage(sender, arena.getParams().getOptionsSummaryString());
    }

    @MCCommand(cmds = {"deleteOption"}, admin = true, perm = "arena.alter")
    public boolean deleteOption(CommandSender sender, MatchParams params, String[] args) {
        params = ParamController.getMatchParams(params);
        ParamAlterController pac = new ParamAlterController(params);
        pac.deleteOption(sender, args);
        return true;
    }

    @MCCommand(cmds = {"restoreDefaultArenaOptions"}, admin = true, perm = "arena.alter")
    public boolean restoreDefaultOptions(CommandSender sender, Arena arena) {
        try {
            ArenaAlterController.restoreDefaultArenaOptions(arena, true);
        } catch (IllegalStateException e) {
            return sendMessage(sender, "&c" + e.getMessage());
        }
        return sendMessage(sender, "&2Arena &6" + arena.getName() + "set back to default game options");
    }

    @MCCommand(cmds = {"restoreDefaultArenaOptions"}, admin = true, perm = "arena.alter")
    public boolean restoreDefaultOptions(CommandSender sender, MatchParams params) {
        try {
            ArenaAlterController.restoreDefaultArenaOptions(params);
        } catch (IllegalStateException e) {
            return sendMessage(sender, "&c" + e.getMessage());
        }
        return sendMessage(sender, "&2Game type &6" + params.getType() + " set back to default game options");
    }

    @MCCommand(cmds = {"start"}, admin = true, perm = "arena.start")
    public boolean arenaStart(CommandSender sender, MatchParams mp) {
        List<Match> matches = ac.getRunningMatches(mp);
        if (matches.isEmpty()) {
            return sendMessage(sender, "&cThere are no open &6" + mp.getType());
        } else if (matches.size() > 1) {
            return sendMessage(sender, "&cThere are multiple &6" + mp.getType()
                    + "&c open.  Specify which arena.\n&e/" + mp.getCommand()
                    + " cancel <arena>");
        }
        ac.startMatch(matches.get(0));
        return sendMessage(sender, "&2" + mp.getType() + " has been started");
    }

    @MCCommand(cmds = {"forceStart"}, admin = true, perm = "arena.forcestart")
    public boolean arenaForceStart(CommandSender sender, MatchParams mp) {
        if (ac.forceStart(mp, false)) {
            return sendMessage(sender, "&2" + mp.getType() + " has been started");
        } else {
            return sendMessage(sender, "&c" + mp.getType() + " could not be started");
        }
    }

    @MCCommand(cmds = {"choose", "class"})
    public boolean chooseClass(ArenaPlayer sender, String arenaClass) {
        ArenaClass ac = ArenaClassController.getClass(arenaClass);
        if (ac == null) {
            return sendMessage(sender, "&cThere is no class called &6" + arenaClass);
        }
        if (sender.getCurLocation().getType() == LocationType.HOME) {
            return sendMessage(sender, "&cYou aren't in a game&6");
        }
        ArenaClassController.changeClass(sender.getPlayer(), sender.getCompetition(), ac);
        return true;
    }

    @MCCommand(cmds = {"list"})
    public boolean arenaList(CommandSender sender, MatchParams mp, String[] args) {
        boolean all = args.length > 1 && (args[1]).equals("all");

        Collection<Arena> arenas = ac.getArenas().values();
        HashMap<ArenaType, Collection<Arena>> arenasbytype = new HashMap<ArenaType, Collection<Arena>>();
        for (Arena arena : arenas) {
            Collection<Arena> as = arenasbytype.get(arena.getArenaType());
            if (as == null) {
                as = new ArrayList<Arena>();
                arenasbytype.put(arena.getArenaType(), as);
            }
            as.add(arena);
        }
        if (arenasbytype.isEmpty()) {
            sendMessage(sender, "&cThere are no &6" + mp.getName()
                    + "&c arenas");
        }
        for (ArenaType at : arenasbytype.keySet()) {
            if (!all && !at.matches(mp.getType())) {
                continue;
            }
            Collection<Arena> as = arenasbytype.get(at);
            if (!as.isEmpty()) {
                sendMessage(sender, "&e------ Arenas for &6" + at.toString()
                        + "&e ------");
                for (Arena arena : as) {
                    sendMessage(sender, arena.toSummaryString());
                }
            }
        }
        if (!all) {
            sendMessage(sender, "&6/arena list all&e: to see all arenas");
        }
        return sendMessage(sender,
                "&6/arena info <arenaname>&e: for details on an arena");
    }

    public boolean canJoin(ArenaTeam t) {
        return canJoin(t, true);
    }

    public boolean canJoin(ArenaTeam t, boolean showMessages) {
        for (ArenaPlayer ap : t.getPlayers()) {
            if (!_canJoin(ap, showMessages, true)) {
                return false;
            }
        }
        return true;
    }

    public boolean canJoin(ArenaPlayer player) {
        return canJoin(player, true);
    }

    public boolean canJoin(ArenaPlayer player, boolean showMessages) {
        return _canJoin(player, showMessages, false);
    }

    private boolean _canJoin(ArenaPlayer player, boolean showMessages, boolean teammate) {
        /// Check for any competition
        if (player.getCompetition() != null) {
            if (showMessages) {
                sendMessage(player, "&cYou are still in the " + player.getCompetition().getName() + ". &6/arena leave");
            }
            return false;
        }
        /// Inside the queue waiting for a match?
        if (InArenaListener.inQueue(player.getID())) {
            sendMessage(player, "&eYou are in the queue.");
            if (showMessages) {
                sendMessage(player, "&eType &6/arena leave");
            }
            return false;
        }

        /// Inside MobArena?
//        if (MobArenaInterface.hasMobArena()
//                && MobArenaInterface.insideMobArena(player)) {
//            if (showMessages) {
//                sendMessage(player, "&cYou need to finish with MobArena first!");
//            }
//            return false;
//        }

        /// Check for player in combat
//        if (CombatTagUtil.isTagged(player.getPlayer())
//                || (HeroesController.enabled() && HeroesController.isInCombat(player.getPlayer()))) {
//            if (showMessages) {
//                sendMessage(player, "&cYou are in combat!");
//            }
//            return false;
//        }

        /// Inside an Event?
        Event ae = insideEvent(player);
        if (ae != null) {
            if (showMessages) {
                sendMessage(player, "&eYou need to leave the Event first. &6/" + ae.getCommand() + " leave");
            }
            return false;
        }

        // / Inside a match?
        Match am = ac.getMatch(player);
        if (am != null) {
            ArenaTeam t = am.getTeam(player);
            if (am.isHandled(player)
                    || (!t.hasLeft(player) && t.hasAliveMember(player))) {
                if (showMessages) {
                    sendMessage(player, "&eYou are already in a match.");
                }
                return false;
            } else {
                return true;
            }
        }
        if (!teammate) {
            // / Inside a forming team?
            if (teamc.inFormingTeam(player)) {
                FormingTeam ft = teamc.getFormingTeam(player);
                if (ft.isJoining(player)) {
                    if (showMessages) {
                        sendMessage(player, "&eYou have been invited to the team. " + ft.getDisplayName());
                    }
                    if (showMessages) {
                        sendMessage(player, "&eType &6/team add|decline");
                    }
                } else if (!ft.hasAllPlayers()) {
                    if (showMessages) {
                        sendMessage(player, "&eYour team is not yet formed. &6/team disband&e to leave");
                    }
                    if (showMessages) {
                        sendMessage(player, "&eYou are still missing " + MessageUtil.joinPlayers(
                                ft.getUnjoinedPlayers(), ", ") + " !!");
                    }
                }
                return false;
            }
            // / Make a team for the new Player
            ArenaTeam t = teamc.getSelfFormedTeam(player);
            if (t != null) {
                for (ArenaPlayer p : t.getPlayers()) {
                    if (p == player) {
                        continue;
                    }
                    if (!_canJoin(p, true, true)) {
                        sendSystemMessage(player, "teammate_cant_join");
                        sendMessage(player, "&6/team leave: &cto leave the team");
                        return false;
                    }
                }
            }
        }

        if (EssentialsController.enabled()
                && EssentialsController.inJail(player)) {
            if (showMessages) {
                sendMessage(player, "&cYou are still in jail!");
            }
            return false;
        }
        return true;
    }

    public Event insideEvent(ArenaPlayer p) {
        return EventController.insideEvent(p);
    }

    public boolean checkAndRemoveFee(MatchParams pi, ArenaTeam t) {
        boolean takesFee = pi.hasEntranceFee();
        boolean needsItems = pi.hasOptionAt(MatchState.PREREQS, TransitionOption.NEEDITEMS);
        boolean takesItems = pi.hasOptionAt(MatchState.PREREQS, TransitionOption.TAKEITEMS);
        if (takesFee) {
            Double fee = pi.getEntranceFee();
            if (fee != null) {

                boolean hasEnough = true;
                for (ArenaPlayer player : t.getPlayers()) {
                    boolean has = MoneyController.hasEnough(player.getName(), fee);
                    hasEnough &= has;
                    if (!has) {
                        sendMessage(player, "&eYou need &6" + fee + "&e to compete");
                    }
                }
                if (!hasEnough) {
                    if (t.size() > 1) {
                        t.sendMessage("&eYour team does not have enough money to compete");
                    }
                    return false;
                }
            }
        }
        if (needsItems) {
            List<ItemStack> fee = pi.getStateOptions().getNeedItems(MatchState.PREREQS);
            if (fee != null) {
                boolean hasEnough = true;

                for (ArenaPlayer player : t.getPlayers()) {
                    boolean has = InventoryUtil.hasAllItems(player.getPlayer(), fee);
                    hasEnough &= has;
                    if (!has) {
                        sendMessage(player, "&eYou don't have all the needed items to compete");
                        for (ItemStack is : fee) {
                            sendMessage(player, "&c- &e" + InventoryUtil.getItemString(is));
                        }
                    }
                }
                if (!hasEnough) {
                    if (t.size() > 1) {
                        t.sendMessage("&eYour team does not have all the items to compete");
                    }
                    return false;
                }
            }
        }
        if (takesItems) {
            List<ItemStack> fee = pi.getStateOptions().getTakeItems(MatchState.PREREQS);
            if (fee != null) {
                boolean hasEnough = true;

                for (ArenaPlayer player : t.getPlayers()) {
                    boolean has = InventoryUtil.hasAllItems(player.getPlayer(), fee);
                    hasEnough &= has;
                    if (!has) {
                        sendMessage(player, "&eYou don't have all the needed items to compete");
                        for (ItemStack is : fee) {
                            sendMessage(player, "&c- &e" + InventoryUtil.getItemString(is));
                        }
                    }
                }
                if (!hasEnough) {
                    if (t.size() > 1) {
                        t.sendMessage("&eYour team does not have all the items to compete");
                    }
                    return false;
                }
            }
        }
        /// Take the requirements

        if (takesFee) {
            Double fee = pi.getEntranceFee();
            if (fee != null) {
                for (ArenaPlayer player : t.getPlayers()) {
                    getOrCreateJoinReqs(player).setMoney(fee);
                    MoneyController.subtract(player.getName(), fee);
                    sendMessage(player, "&6" + fee + " has been subtracted from your account");
                }
            }
        }
        if (takesItems) {
            List<ItemStack> fee = pi.getStateOptions().getTakeItems(MatchState.PREREQS);
            if (fee != null) {
                for (ArenaPlayer player : t.getPlayers()) {
                    getOrCreateJoinReqs(player).setItems(new PInv(fee));
                    InventoryUtil.removeItems(player.getInventory(), fee);
                }
            }
        }
        return true;
    }

    private PlayerSave getOrCreateJoinReqs(ArenaPlayer player) {
        PlayerSave ps = player.getMetaData().getJoinRequirements();
        if (ps == null) {
            ps = new PlayerSave(player);
            player.getMetaData().setJoinRequirements(ps);
        }
        return ps;
    }

    public static boolean refundFee(MatchParams pi, ArenaTeam t) {
        final StateGraph tops = pi.getStateGraph();
        if (tops.hasEntranceFee()) {
            Double fee = tops.getEntranceFee();
            if (fee == null || fee <= 0) {
                return true;
            }
            for (ArenaPlayer player : t.getPlayers()) {
                MoneyController.add(player.getName(), fee);
                sendMessage(player,
                        "&eYou have been refunded the entrance fee of &6" + fee);
            }
        }
        return true;
    }

    protected Arena getArena(String name) {
        return ac.getArena(name);
    }

    public static boolean checkPlayer(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sendMessage(sender, "&cYou need to be online for this command!");
            return false;
        }
        return true;
    }

    public void setDisabled(List<String> disabled) {
        this.disabled.addAll(disabled);
    }

    public Collection<String> getDisabled() {
        return this.disabled;
    }
}
