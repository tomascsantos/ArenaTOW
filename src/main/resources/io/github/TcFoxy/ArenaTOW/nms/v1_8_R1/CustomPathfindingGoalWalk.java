package io.github.TcFoxy.ArenaTOW.nms.v1_8_R1;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import net.minecraft.server.v1_8_R1.EntityCreature;
import net.minecraft.server.v1_8_R1.PathfinderGoal;
import net.minecraft.server.v1_8_R1.RandomPositionGenerator;
import net.minecraft.server.v1_8_R1.Vec3D;

public class CustomPathfindingGoalWalk extends PathfinderGoal
{
	private EntityCreature a;
	private double b;
	private double c;
	private double d;
	private double e;
	private int f;
	private boolean g;
	private Location loc;

	public CustomPathfindingGoalWalk(EntityCreature paramEntityCreature, double paramDouble, Location location)
	{
		this(paramEntityCreature, paramDouble, 120);
		loc = location;
	}

	public CustomPathfindingGoalWalk(EntityCreature paramEntityCreature, double paramDouble, int paramInt) {
		this.a = paramEntityCreature;
		this.e = paramDouble;
		this.f = paramInt;
		a(1);
	}
	public boolean a()
	{
		/*
		if (!this.g) {
			if (this.a.bg() >= 100) {
				return false;
			}
			if (this.a.bb().nextInt(this.f) != 0) {
				return false;
			}
		}

		Vec3D localVec3D = RandomPositionGenerator.a(this.a, 10, 7);
		if (localVec3D == null) {
			return false;
		}
		this.b = localVec3D.a;
		this.c = localVec3D.b;
		this.d = localVec3D.c;
		this.g = false;
		*/
		return true;
	}

	public boolean b()
	{
		return !this.a.getNavigation().m();
	}

	public void c()
	{
		if(loc != null){
			Double x = loc.getX();
			Double y = loc.getY();
			Double z = loc.getZ();
			this.a.getNavigation().a(x, y, z, this.e);		
		}
	}

	public void f() {
		this.g = true;
	}

	public void b(int paramInt) {
		this.f = paramInt;
	}
}