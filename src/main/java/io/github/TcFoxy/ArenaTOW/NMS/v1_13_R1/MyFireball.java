package io.github.TcFoxy.ArenaTOW.nms.v1_13_R1;

import net.minecraft.server.v1_13_R1.EntityLiving;
import net.minecraft.server.v1_13_R1.EntitySmallFireball;
import net.minecraft.server.v1_13_R1.World;

class MyFireball extends EntitySmallFireball{

	private MyEntityGolem golem;
	
	public MyFireball(World world, EntityLiving entityliving, double d0, double d1, double d2) {
		super(world, entityliving, d0, d1, d2);
		this.golem = (MyEntityGolem) entityliving;
	}
	
	public MyEntityGolem getGolem(){
		return this.golem;
	}

}
