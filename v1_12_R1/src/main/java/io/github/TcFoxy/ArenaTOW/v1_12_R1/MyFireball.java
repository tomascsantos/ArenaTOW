package io.github.TcFoxy.ArenaTOW.v1_12_R1;

import io.github.TcFoxy.ArenaTOW.API.TOWEntity;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.EntitySmallFireball;
import net.minecraft.server.v1_12_R1.World;

import java.awt.*;

class MyFireball extends EntitySmallFireball implements TOWEntity {

	private MyEntityGolem golem;

	public MyFireball(World world, EntityLiving entityliving, double d0, double d1, double d2) {
		super(world, entityliving, d0, d1, d2);
		this.golem = (MyEntityGolem) entityliving;
	}
	
	public MyEntityGolem getGolem(){
		return this.golem;
	}

	@Override
	public float getHealth() {
	    return 0;
	}

	@Override
	public Color getTeam() {
		return ((TOWEntity) this.golem).getTeam();
	}

	@Override
	public void setHealth(float f) {
	    //dummy method from inheritance
	}
}
