package io.github.TcFoxy.ArenaTOW.v1_11_R1;

import io.github.TcFoxy.ArenaTOW.API.TOWEntity;
import io.github.TcFoxy.ArenaTOW.API.TOWEntityHandler;
import net.minecraft.server.v1_11_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_11_R1.World;
import org.bukkit.Color;

public class MyRedZombie extends MyEntityZombie implements TOWEntity {

    private String zombieteam = "Red";

    public MyRedZombie(World world, TOWEntityHandler handler) {
        super(world, handler);
        this.handler = handler;
    }

    @Override
    protected void r() {
        super.r();
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, MyBlueZombie.class, true));
        this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(this, MyBlueGolem.class, true));
        this.targetSelector.a(4, new PathfinderGoalNearestAttackableTarget<>(this, MyBlueGuardian.class, true));
    }

    @Override
    public void whereTo() {
        super.whereTo();
    }

    @Override
    public Color getTeam() {
        return Color.RED;
    }
}
