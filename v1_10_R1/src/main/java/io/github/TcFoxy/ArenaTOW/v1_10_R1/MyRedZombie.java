package io.github.TcFoxy.ArenaTOW.v1_10_R1;

import io.github.TcFoxy.ArenaTOW.API.TOWEntity;
import net.minecraft.server.v1_10_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_10_R1.World;
import org.bukkit.Color;
import org.bukkit.Location;

public class MyRedZombie extends MyEntityZombie implements TOWEntity {

    private String zombieteam = "Red";

    public MyRedZombie(World world) {
        super(world);
    }

    @Override
    protected void r() {
        super.r();
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<MyBlueZombie>(this, MyBlueZombie.class, true));
        this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<MyBlueGolem>(this, MyBlueGolem.class, true));
        this.targetSelector.a(4, new PathfinderGoalNearestAttackableTarget<MyBlueGuardian>(this, MyBlueGuardian.class, true));
    }

    @Override
    public void whereTo(Location directions) {
        super.whereTo(directions);
    }

    @Override
    public Color getTeam() {
        return Color.RED;
    }
}
