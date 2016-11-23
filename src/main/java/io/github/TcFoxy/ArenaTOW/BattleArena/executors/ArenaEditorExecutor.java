package io.github.TcFoxy.ArenaTOW.BattleArena.executors;

import org.bukkit.command.CommandSender;

import io.github.TcFoxy.ArenaTOW.BattleArena.BattleArena;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.ArenaAlterController;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.ArenaAlterController.ArenaOptionPair;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.ArenaEditor;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.ArenaEditor.CurrentSelection;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.Arena;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.exceptions.InvalidOptionException;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.pairs.ParamAlterOptionPair;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.pairs.TransitionOptionTuple;
import io.github.TcFoxy.ArenaTOW.BattleArena.util.MessageUtil;

public class ArenaEditorExecutor extends CustomCommandExecutor {
    public static String idPrefix = "ar_";
    
    public ArenaEditorExecutor(){
        super();
    }

    @MCCommand(cmds={"select","sel"}, admin=true)
    public boolean arenaSelect(CommandSender sender, Arena arena) {
        ArenaEditor aac = BattleArena.getArenaEditor();
        aac.setCurrentArena(sender, arena);
        return MessageUtil.sendMessage(sender, "&2You have selected arena &6" + arena.getName());
    }

    @MCCommand(cmds = {}, admin = true, perm = "arena.alter")
    public boolean arenaGeneric(CommandSender sender, CurrentSelection cs, ArenaOptionPair aop) {
        return ArenaEditorExecutor.setArenaOption(sender, cs.getArena(), aop);
    }

    @MCCommand(cmds = {}, admin = true, perm = "arena.alter")
    public boolean arenaGeneric(CommandSender sender,CurrentSelection cs,  ParamAlterOptionPair gop) {
        return ArenaEditorExecutor.setArenaOption(sender, cs.getArena(), gop);
    }

    @MCCommand(cmds = {}, admin = true, perm = "arena.alter")
    public boolean arenaGeneric(CommandSender sender,CurrentSelection cs,  TransitionOptionTuple top) {
        return ArenaEditorExecutor.setArenaOption(sender, cs.getArena(), top);
    }

    public static boolean setArenaOption(CommandSender sender, Arena arena, TransitionOptionTuple top) {
        try {
            ArenaAlterController.setArenaOption(sender, arena, top.state, top.op, top.value);
            if (top.value != null) {
                sendMessage(sender, "&2Arena "+arena.getDisplayName()+" options &6" + top.op + "&2 changed to &6" + top.value);
            } else {
                sendMessage(sender, "&2Arena "+arena.getDisplayName()+" options &6" + top.op + "&2 changed");
            }
            return true;
        } catch (IllegalStateException e) {
            return sendMessage(sender, "&c" + e.getMessage());
        } catch (InvalidOptionException e) {
            return sendMessage(sender, "&c" + e.getMessage());
        }
    }

    public static boolean setArenaOption(CommandSender sender,Arena arena, ArenaOptionPair aop){
        try {
            /// all of the messages are handled inside of setArenaOption
            ArenaAlterController.setArenaOption(sender, arena, aop.ao, aop.value);
            return true;
        } catch (IllegalStateException e) {
            return sendMessage(sender, "&c" + e.getMessage());
        }
    }

    public static boolean setArenaOption(CommandSender sender,Arena arena,  ParamAlterOptionPair gop){
        try {
            ArenaAlterController.setArenaOption(sender, arena, gop.alterParamOption, gop.value);
            if (gop.value != null) {
                sendMessage(sender, "&2Arena "+arena.getDisplayName()+" options &6" + gop.alterParamOption.name() + "&2 changed to &6" + gop.value);
            } else {
                sendMessage(sender, "&2Arena "+arena.getDisplayName()+" options &6" + gop.alterParamOption.name() + "&2 changed");
            }
            return true;
        } catch (IllegalStateException e) {
            return sendMessage(sender, "&c" + e.getMessage());
        }
    }

}
