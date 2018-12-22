package io.github.TcFoxy.ArenaTOW.v1_11_R1;

import io.github.TcFoxy.ArenaTOW.API.Events.CustomZombieReachTargetEvent;
import net.minecraft.server.v1_11_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftEntity;

import javax.annotation.Nullable;

public class MyPathfindingGoalWalk extends PathfinderGoal {
    private final EntityCreature a;
    private final MyEntityZombie zombie;
    private double b;
    private double c;
    private double d;
    private final double e;
    private Location loc;
    CraftEntity bukkitEntity;
    int distance;

    public MyPathfindingGoalWalk(final MyEntityZombie a, final double speed, Location loc, int distance) {
        this.a = a;
        this.zombie = a;
        this.e = speed;
        this.loc = loc;
        this.a(1);
        this.bukkitEntity = this.a.getBukkitEntity();
        this.distance = distance;
    }

    /*
     * a() is for "Should he walk?"
     */
    @Override
    public boolean a() {
        final Vec3D f = this.f();
        if (f == null) {
            return false;
        }
        this.b = f.x;
        this.c = f.y;
        this.d = f.z;

        //If within the required distance, then update the coordinates.
        if (this.bukkitEntity.getLocation().distance(
                new Location (this.bukkitEntity.getLocation().getWorld(), f.x, f.y, f.z)) < this.distance) {
            CustomZombieReachTargetEvent event = new CustomZombieReachTargetEvent(zombie);
            Bukkit.getPluginManager().callEvent(event);
        }
        return true;
    }

    /*
     * idk
     */
    @Override
    public boolean b() {
        return !this.a.getNavigation().n();
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
    private Vec3D f() {
        if (loc == null) {
            return null;
        }
        return new Vec3D(loc.getX(), loc.getY(), loc.getZ());
    }
}
