package io.github.TcFoxy.ArenaTOW.Plugin;

import io.github.TcFoxy.ArenaTOW.API.MobType;
import io.github.TcFoxy.ArenaTOW.API.TOWEntity;
import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.arenas.Arena;
import org.bukkit.Color;
import org.bukkit.Location;

/**
 * Created by Tomas Santos on 12/19/2018.
 */
public class TowPlayer implements TOWEntity{

    ArenaPlayer player;
    TugArena tugArena;

    public TowPlayer(ArenaPlayer player, TugArena tugArena) {
        this.player = player;
        this.tugArena = tugArena;
    }

    @Override
    public float getHealth() {
        return (float) player.getHealth();
    }

    @Override
    public Color getTeam() {
        return tugArena.playerTeamLookup.get(player);
    }

    @Override
    public boolean isAlive() {
        return !player.isDead();
    }

    @Override
    public void setHealth(float f) {
        player.setHealth((double) f);
    }

    @Override
    public MobType getMobType() {
        return MobType.PLAYER;
    }

    @Override
    public Location getLocation() {
        return player.getLocation();
    }
}
