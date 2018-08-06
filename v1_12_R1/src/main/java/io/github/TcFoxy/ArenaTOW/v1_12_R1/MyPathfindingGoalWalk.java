package io.github.TcFoxy.ArenaTOW.v1_12_R1;

import net.minecraft.server.v1_12_R1.EntityCreature;
import net.minecraft.server.v1_12_R1.PathfinderGoal;
import net.minecraft.server.v1_12_R1.Vec3D;
import org.bukkit.Location;

import javax.annotation.Nullable;

class MyPathfindingGoalWalk extends PathfinderGoal
{
	private final EntityCreature a;
	private boolean hasTarget;
	private double x;
	private double y;
	private double z;
	private final double speed;
	private Location loc;

	public MyPathfindingGoalWalk(final EntityCreature a, final double speed, Location loc) {
		this.a = a;
		this.speed = speed;
		this.loc = loc;
		this.hasTarget = true;
		this.a(1);
	}

	/*
	 * a() is for "Should he walk?"
	 */
	@Override
	public boolean a() {
		if (!this.hasTarget) {
			return false;
		}
		final Vec3D f = this.getTarget();
		if (f == null) {
			return false;
		}
		this.x = f.x;
		this.y = f.y;
		this.z = f.z;
		return true;
	}

	/*
	 * idk what this is doing.
	 */
	@Override
	public boolean b() {
		return !this.a.getNavigation().o();
	}

	/*
	 * do the walk
	 */
	@Override
	public void c() {
		this.a.getNavigation().a(this.x, this.y, this.z, this.speed);
	}


	/**
	 * Deletes the target I believe.
	 * called internally somewhere.
	 */
	@Override
	public void d() {
		this.hasTarget = false; //TODO delete if this breaks things
	}

	/**
	 * deobfuscated version of d();
	 */
	public void clearTarget() {
		this.d();
	}

	/*
	 * get the location
	 */
	@Nullable
	private Vec3D getTarget() {
		if(loc == null){
			return null;
		}
		return new Vec3D(loc.getX(), loc.getY(), loc.getZ());
	}
}
