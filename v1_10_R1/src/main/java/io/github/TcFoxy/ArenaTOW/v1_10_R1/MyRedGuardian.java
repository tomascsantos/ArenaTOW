package io.github.TcFoxy.ArenaTOW.v1_10_R1;

import io.github.TcFoxy.ArenaTOW.API.TOWEntity;
import net.minecraft.server.v1_10_R1.World;
import org.bukkit.Color;

public class MyRedGuardian extends MyEntityGuardian implements TOWEntity {

    public MyRedGuardian(World paramWorld) {
        super(paramWorld);
        //this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityBlueZombie.class, true));
    }

    @Override
    public Color getTeam() {
        return Color.RED;
    }
}
