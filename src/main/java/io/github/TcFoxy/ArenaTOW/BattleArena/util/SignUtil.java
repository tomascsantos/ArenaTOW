package io.github.TcFoxy.ArenaTOW.BattleArena.util;


import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import io.github.TcFoxy.ArenaTOW.BattleArena.Defaults;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.ArenaClassController;
import io.github.TcFoxy.ArenaTOW.BattleArena.controllers.ParamController;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.ArenaClass;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MatchParams;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.signs.ArenaCommandSign;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.signs.ArenaStatusSign;


public class SignUtil {

	public static ArenaCommandSign getArenaCommandSign(Sign sign, String[] lines) {
		if (lines.length < 2)
			return null;
		String param = MessageUtil.decolorChat(lines[0]).replaceAll("[\\[\\" + Defaults.SIGN_PREFIX + "\\]]", "").trim();
        MatchParams mp = ParamController.getMatchParams(param);
        if (mp == null){
			Collection<MatchParams> params = ParamController.getAllParams();
			for (MatchParams p: params){
				if (p.getName().equalsIgnoreCase(param) ||
                        p.getCommand().equalsIgnoreCase(param) ||
                        p.getSignDisplayName()!=null &&
                        MessageUtil.decolorChat(p.getSignDisplayName().replaceAll("[\\[\\"+Defaults.SIGN_PREFIX+"\\]]","").trim()).
                                equalsIgnoreCase(param)){
					mp = p;
					break;
				}
			}
			if (mp == null){
				return null;}
		}

		try {
            return ArenaCommandSign.create(sign.getLocation(),mp,lines);
        } catch (Exception e){
			return null;
		}
	}

	public static ArenaClass getArenaClassSign(String[] lines) {
        return ArenaClassController.getClass(
                MessageUtil.decolorChat(lines[0]).replaceAll("[\\[\\"+Defaults.SIGN_PREFIX+"\\]]", ""));
	}

	public static ArenaStatusSign getArenaStatusSign(String[] lines) {
		if (lines.length < 2)
			return null;
		String param = MessageUtil.decolorChat(lines[0]).replaceAll("\\"+Defaults.SIGN_PREFIX, "").trim();
		MatchParams mp = ParamController.getMatchParamCopy(param);
		if (mp == null)
			return null;
		if (lines[1].contains("status"))
			return new ArenaStatusSign(mp);

		return null;
	}

	public static Sign getSign(World w, int x, int y, int z) {
		Block b = w.getBlockAt(x, y, z);
		Material t = b.getType();
		return t == Material.SIGN || t == Material.SIGN_POST || t==Material.WALL_SIGN ? (Sign)b.getState(): null;
	}

	public static Sign getSign(Location l) {
		if (l == null)
			return null;
		Material t = l.getBlock().getType();
		return t == Material.SIGN || t == Material.SIGN_POST || t==Material.WALL_SIGN ? (Sign)l.getBlock().getState(): null;
	}

}
