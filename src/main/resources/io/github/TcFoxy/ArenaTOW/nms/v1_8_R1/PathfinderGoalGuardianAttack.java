package io.github.TcFoxy.ArenaTOW.nms.v1_8_R1;

import net.minecraft.server.v1_8_R1.DamageSource;
import net.minecraft.server.v1_8_R1.EntityGuardian;
import net.minecraft.server.v1_8_R1.EntityLiving;
import net.minecraft.server.v1_8_R1.EnumDifficulty;
import net.minecraft.server.v1_8_R1.GenericAttributes;
import net.minecraft.server.v1_8_R1.PathfinderGoal;

class PathfinderGoalGuardianAttack extends PathfinderGoal
{
  private CustomEntityGuardian a;
  private int b;

  public PathfinderGoalGuardianAttack(CustomEntityGuardian paramEntityGuardian)
  {
    this.a = paramEntityGuardian;

    a(3);
  }

  public boolean a()
  {
    EntityLiving localEntityLiving = this.a.getGoalTarget();
    if ((localEntityLiving == null) || (!localEntityLiving.isAlive())) {
      return false;
    }

    return true;
  }

  public boolean b()
  {
    return (super.b()) && ((this.a.cl()) || (this.a.h(this.a.getGoalTarget()) > 9.0D));
  }

  public void c()
  {
    this.b = -10;
    this.a.getNavigation().n();
    this.a.getControllerLook().a(this.a.getGoalTarget(), 90.0F, 90.0F);

    this.a.ai = true;
  }
  
  public void d()
  {
    CustomEntityGuardian.a(this.a, 0);
    this.a.setGoalTarget(null);

    //CustomEntityGuardian.a(this.a).f();
  }

  public void e()
  {
    EntityLiving localEntityLiving = this.a.getGoalTarget();

    this.a.getNavigation().n();
    this.a.getControllerLook().a(localEntityLiving, 90.0F, 90.0F);

    if (!this.a.hasLineOfSight(localEntityLiving)) {
      this.a.setGoalTarget(null);
      return;
    }

    this.b += 5;//This is how fast it will shoot baisically. (60/b = ticks between shot)
    if (this.b == 0)
    {
      CustomEntityGuardian.a(this.a, this.a.getGoalTarget().getId());
      this.a.world.broadcastEntityEffect(this.a, (byte)21);
    } else if (this.b >= this.a.ck()) {
      float f = 10.0F;
      localEntityLiving.damageEntity(DamageSource.b(this.a, this.a), f);
      localEntityLiving.damageEntity(DamageSource.mobAttack(this.a), (float)this.a.getAttributeInstance(GenericAttributes.e).getValue());
      this.a.setGoalTarget(null);
    } else if ((this.b < 60) || (this.b % 20 != 0));
    super.e();
  }
}