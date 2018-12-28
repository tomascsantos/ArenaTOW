package io.github.TcFoxy.ArenaTOW.v1_12_R1;

import io.github.TcFoxy.ArenaTOW.API.TOWEntity;
import io.github.TcFoxy.ArenaTOW.API.TOWEntityHandler;
import net.minecraft.server.v1_12_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_12_R1.World;
import org.bukkit.Color;

public class MyRedGolem extends MyEntityGolem implements TOWEntity {
    public MyRedGolem(World world, TOWEntityHandler handler) {
        super(world, handler);
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<MyBlueZombie>(this, MyBlueZombie.class, true));
        this.handler = handler;
    }

    @Override
    public Color getTeam() {
        return Color.RED;
    }
}
