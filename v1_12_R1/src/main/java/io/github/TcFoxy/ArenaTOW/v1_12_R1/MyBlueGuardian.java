package io.github.TcFoxy.ArenaTOW.v1_12_R1;

import io.github.TcFoxy.ArenaTOW.API.TOWEntity;
import io.github.TcFoxy.ArenaTOW.API.TOWEntityHandler;
import net.minecraft.server.v1_12_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_12_R1.World;
import org.bukkit.Color;

public class MyBlueGuardian extends MyEntityGuardian implements TOWEntity {


    public MyBlueGuardian(World paramWorld, TOWEntityHandler handler) {
        super(paramWorld, handler);
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<MyRedZombie>(this, MyRedZombie.class, true));
        this.handler = handler;
    }

    @Override
    public Color getTeam() {
        return Color.BLUE;
    }
}
