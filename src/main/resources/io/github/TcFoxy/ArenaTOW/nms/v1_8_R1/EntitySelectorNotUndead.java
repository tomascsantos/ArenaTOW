package io.github.TcFoxy.ArenaTOW.nms.v1_8_R1;

import net.minecraft.server.v1_8_R1.Entity;
import net.minecraft.server.v1_8_R1.EntityLiving;
import net.minecraft.server.v1_8_R1.EnumMonsterType;

import com.google.common.base.Predicate;

final class EntitySelectorNotUndead implements Predicate
{
	public boolean a(Entity paramEntity)
	{
		return ((paramEntity instanceof EntityLiving)) && (((EntityLiving)paramEntity).getMonsterType() != EnumMonsterType.UNDEAD);
	}

	@Override
	public boolean apply(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}
