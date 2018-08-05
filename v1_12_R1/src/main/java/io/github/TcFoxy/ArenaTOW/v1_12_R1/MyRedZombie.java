package io.github.TcFoxy.ArenaTOW.v1_12_R1;


import io.github.TcFoxy.ArenaTOW.API.TOWEntity;
import net.minecraft.server.v1_12_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_12_R1.World;
import org.bukkit.Location;

import java.awt.*;

class MyRedZombie extends MyEntityZombie implements TOWEntity {
	
	public MyRedZombie(World world) {
		super(world);
	}
	
	@Override
	public void n(){
		super.n();
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
	    return Color.red;
	}

}
