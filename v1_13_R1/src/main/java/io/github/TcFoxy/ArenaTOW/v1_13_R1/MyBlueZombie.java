package io.github.TcFoxy.ArenaTOW.v1_13_R1;


import io.github.TcFoxy.ArenaTOW.API.TOWEntity;
import net.minecraft.server.v1_13_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_13_R1.World;
import org.bukkit.Location;

import java.awt.*;

class MyBlueZombie extends MyEntityZombie implements TOWEntity {
	
	public MyBlueZombie(World world) {
		super(world);
	}
	
	@Override
	public void n(){
		super.n();
		this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<MyRedZombie>(this, MyRedZombie.class, true));
		this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<MyRedGolem>(this, MyRedGolem.class, true));
		this.targetSelector.a(4, new PathfinderGoalNearestAttackableTarget<MyRedGuardian>(this, MyRedGuardian.class, true));
	}
	
	@Override
	public void whereTo(Location directions) {
		super.whereTo(directions);
	}

	@Override
	public Color getTeam() {
	    return Color.blue;
	}

}
