package io.github.TcFoxy.ArenaTOW.v1_11_R1;

import io.github.TcFoxy.ArenaTOW.API.TOWEntity;
import io.github.TcFoxy.ArenaTOW.API.TOWEntityHandler;
import net.minecraft.server.v1_11_R1.World;
import org.bukkit.Color;

public class MyRedGuardian extends MyEntityGuardian implements TOWEntity {

    public MyRedGuardian(World paramWorld, TOWEntityHandler handler) {
        super(paramWorld, handler);
        this.handler = handler;
        //this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityBlueZombie.class, true));
    }

    @Override
    public Color getTeam() {
        return Color.RED;
    }
}
