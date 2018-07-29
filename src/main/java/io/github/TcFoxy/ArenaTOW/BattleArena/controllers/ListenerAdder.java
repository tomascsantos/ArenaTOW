package io.github.TcFoxy.ArenaTOW.BattleArena.controllers;

import io.github.TcFoxy.ArenaTOW.BattleArena.Defaults;
import io.github.TcFoxy.ArenaTOW.BattleArena.competition.match.Match;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.plugins.TagAPIController;
import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.PlayerHolder;
import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.competition.BlockBreakListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.competition.BlockPlaceListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.competition.DamageListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.competition.HungerListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.competition.ItemDropListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.competition.ItemPickupListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.competition.PlayerMoveListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.competition.PlayerTeleportListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.competition.PotionListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.competition.PreClearInventoryListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.competition.TeamHeadListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MatchState;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.StateGraph;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.Arena;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.options.TransitionOption;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.regions.WorldGuardRegion;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.scoreboard.ScoreboardFactory;



public class ListenerAdder {

    public static void addListeners(PlayerHolder holder, StateGraph tops) {
        boolean needsDamageEvents = tops.hasAnyOption(TransitionOption.PVPOFF,TransitionOption.PVPON,TransitionOption.INVINCIBLE);
        boolean woolTeams = tops.hasAnyOption(TransitionOption.WOOLTEAMS) && holder.getParams().getMaxTeamSize() >1 ||
                tops.hasAnyOption(TransitionOption.ALWAYSWOOLTEAMS);
        if (woolTeams){
            holder.addArenaListener(new TeamHeadListener());}
        if (needsDamageEvents){
            holder.addArenaListener(new DamageListener(holder));}
        if (tops.hasAnyOption(TransitionOption.NOTELEPORT, TransitionOption.NOWORLDCHANGE, TransitionOption.WGNOENTER)){
            holder.addArenaListener(new PlayerTeleportListener(holder));}
        if (tops.hasAnyOption(TransitionOption.BLOCKBREAKON,TransitionOption.BLOCKBREAKOFF)){
            holder.addArenaListener(new BlockBreakListener(holder));}
        if (tops.hasAnyOption(TransitionOption.BLOCKPLACEON,TransitionOption.BLOCKPLACEOFF)){
            holder.addArenaListener(new BlockPlaceListener(holder));}
        if (tops.hasAnyOption(TransitionOption.ITEMDROPOFF)){
            holder.addArenaListener(new ItemDropListener(holder));}
        if (tops.hasAnyOption(TransitionOption.HUNGEROFF)){
            holder.addArenaListener(new HungerListener(holder));}
        if (tops.hasAnyOption(TransitionOption.ITEMPICKUPOFF)){
            holder.addArenaListener(new ItemPickupListener(holder));}
        if (tops.hasAnyOption(TransitionOption.POTIONDAMAGEON)){
            holder.addArenaListener(new PotionListener(holder));}
//        if (McMMOController.enabled() && McMMOController.hasDisabledSkills()){
//            holder.addArenaListener(McMMOController.createNewListener());}
        if (tops.hasAnyOption(TransitionOption.WGNOLEAVE)) {
            WorldGuardRegion region = null;
            if (holder instanceof Match) {
                region = ((Match) holder).getArena().getWorldGuardRegion();
            } else if (holder instanceof Arena) {
                region = ((Arena) holder).getWorldGuardRegion();
            }
            if (region != null && region.valid())
                holder.addArenaListener(new PlayerMoveListener(holder,region));
        }
        if (!ScoreboardFactory.hasBukkitScoreboard() &&
                TagAPIController.enabled() && !tops.hasAnyOption(TransitionOption.NOTEAMNAMECOLOR)){
            holder.addArenaListener(TagAPIController.getNewListener());}
        if (Defaults.PLUGIN_ANTILOOT && tops.hasOptionAt(MatchState.ONDEATH,TransitionOption.CLEARINVENTORY)){
            holder.addArenaListener(new PreClearInventoryListener());
        }
    }

}
