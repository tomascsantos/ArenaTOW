package io.github.TcFoxy.ArenaTOW.nms.v1_10_R1;

import java.lang.reflect.Field;

import net.minecraft.server.v1_10_R1.DamageSource;
import net.minecraft.server.v1_10_R1.DataWatcherObject;
import net.minecraft.server.v1_10_R1.EntityGuardian;
import net.minecraft.server.v1_10_R1.EntityHuman;
import net.minecraft.server.v1_10_R1.EntityLiving;
import net.minecraft.server.v1_10_R1.GenericAttributes;
import net.minecraft.server.v1_10_R1.PathfinderGoal;
import net.minecraft.server.v1_10_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_10_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_10_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_10_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_10_R1.World;

public class CustomEntityGuardian extends EntityGuardian{


	public CustomEntityGuardian(World world) {
		super(world);
		setElder(true);

	}

	@Override
	protected void r(){
	    this.goalRandomStroll = new PathfinderGoalRandomStroll(this, 1.0D, 80);
		
		this.targetSelector.a(5, new PathfinderGoalNearestAttackableTarget<EntityHuman>(this, EntityHuman.class, true));
		this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
	    this.goalSelector.a(4, new PathfinderGoalGuardianAttack(this, 16F));
	    this.goalSelector.a(7, this.goalRandomStroll);
	}

	@Override
	public void initAttributes()
	{
		super.initAttributes();
		getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(10.0D);
		getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(16.0D);
		getAttributeInstance(GenericAttributes.maxHealth).setValue(300.0D);
	}

	@Override
	public void move(double d0, double d1, double d2){
	}


	@SuppressWarnings("unchecked")
	public DataWatcherObject<Byte> getFieldA() throws Exception{
		Field f=EntityGuardian.class.getDeclaredField("a");
		f.setAccessible(true);
		DataWatcherObject<Byte> temp = (DataWatcherObject<Byte>) f.get((EntityGuardian)this);
		return temp;
	}
	
	@SuppressWarnings("unchecked")
	public DataWatcherObject<Integer> getFieldB() throws Exception{
		Field f=EntityGuardian.class.getDeclaredField("b");
		f.setAccessible(true);
		DataWatcherObject<Integer> temp = (DataWatcherObject<Integer>) f.get((EntityGuardian)this);
		return temp;
	}
	
	@SuppressWarnings("unused") //used by pathfinder
	private boolean a(final int n) {
		DataWatcherObject<Byte> a;
		try {
			a = getFieldA();
			return (((Byte)this.datawatcher.get(a)).byteValue() & n) != 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
    }
	
	private void b(final int n) {
		DataWatcherObject<Integer> b;
		try {
			b = getFieldB();
			this.datawatcher.set(b, n);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
    }


	static class PathfinderGoalGuardianAttack extends PathfinderGoal
	{
		private final CustomEntityGuardian a;
		private int b;
		private float damage;

		public PathfinderGoalGuardianAttack(final CustomEntityGuardian a, float damage) {
			this.a = a;
			this.a(3);
			this.damage = damage;
		}

		@Override
		public boolean a() {
			final EntityLiving goalTarget = this.a.getGoalTarget();
			return goalTarget != null && goalTarget.isAlive();
		}

		@Override
		public boolean b() {
			return super.b() && (this.a.isElder() || this.a.h(this.a.getGoalTarget()) > 9.0);
		}

		@Override
		public void c() {
			this.b = -10;
			this.a.getNavigation().o();
			this.a.getControllerLook().a(this.a.getGoalTarget(), 90.0f, 90.0f);
			this.a.impulse = true;
		}

		@Override
		public void d() {
			this.a.b(0);
			this.a.setGoalTarget(null);
			this.a.goalRandomStroll.f();
		}

		@Override
		public void e() {
			final EntityLiving goalTarget = this.a.getGoalTarget();
			this.a.getNavigation().o();
			this.a.getControllerLook().a(goalTarget, 90.0f, 90.0f);
			if (!this.a.hasLineOfSight(goalTarget)) {
				this.a.setGoalTarget(null);
				return;
			}
			++this.b;
			if (this.b == 0) {
				this.a.b(this.a.getGoalTarget().getId());
				this.a.world.broadcastEntityEffect(this.a, (byte)21);
			}
			else if (this.b >= 20) {//20 tick cooldown
				goalTarget.damageEntity(DamageSource.b(this.a, this.a), damage);
				goalTarget.damageEntity(DamageSource.mobAttack(this.a), (float)this.a.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).getValue());
				this.a.setGoalTarget(null);
			}
			super.e();
		}
	}
}
