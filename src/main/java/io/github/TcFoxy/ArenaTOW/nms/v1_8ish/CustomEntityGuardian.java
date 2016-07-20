package io.github.TcFoxy.ArenaTOW.nms.v1_8ish;

import net.minecraft.server.v1_10_R1.EntityGuardian;
import net.minecraft.server.v1_10_R1.World;

public class CustomEntityGuardian extends EntityGuardian{


	public CustomEntityGuardian(World world) {
		super(world);


	}

	@Override
	public boolean isElder() {
		return true;
	}

	@Override
	public void setElder(boolean paramBoolean) {
		super.setElder(true);
	}

	@Override
	public void move(double d0, double d1, double d2){
	}
}
