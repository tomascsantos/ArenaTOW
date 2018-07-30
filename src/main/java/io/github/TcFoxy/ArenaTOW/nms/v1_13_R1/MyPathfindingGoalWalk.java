package io.github.TcFoxy.ArenaTOW.nms.v1_13_R1;

import javax.annotation.Nullable;

import org.bukkit.Location;

import net.minecraft.server.v1_13_R1.EntityCreature;
import net.minecraft.server.v1_13_R1.PathfinderGoal;
import net.minecraft.server.v1_13_R1.Vec3D;

public class MyPathfindingGoalWalk extends PathfinderGoal
{
	private final EntityCreature a;
	private double b;
	private double c;
	private double d;
	private final double e;
	private Location loc;

	public MyPathfindingGoalWalk(final EntityCreature a, final double speed, Location loc) {
		this.a = a;
		this.e = speed;
		this.loc = loc;
		this.a(1);
	}

	/*
	 * a() is for "Should he walk?"
	 */
	@Override
	public boolean a() {
		final Vec3D f = this.g();
		if (f == null) {
			return false;
		}
		this.b = f.x;
		this.c = f.y;
		this.d = f.z;
		return true;
	}

	/*
	 * idk
	 */
	@Override
	public boolean b() {
		return !this.a.getNavigation().p();
	}

	/*
	 * do the walk
	 */
	@Override
	public void c() {
		this.a.getNavigation().a(this.b, this.c, this.d, this.e);
	}

	/*
	 * get the location
	 */
	@Nullable
	private Vec3D g() {
		if(loc == null){
			return null;
		}
		return new Vec3D(loc.getX(), loc.getY(), loc.getZ());
	}
}
