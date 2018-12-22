package io.github.TcFoxy.ArenaTOW.v1_11_R1;

import io.github.TcFoxy.ArenaTOW.API.MobType;
import io.github.TcFoxy.ArenaTOW.API.TOWEntity;
import io.github.TcFoxy.ArenaTOW.API.TOWEntityHandler;
import net.minecraft.server.v1_11_R1.*;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class MyFireball extends EntitySmallFireball implements TOWEntity {

    private MyEntityGolem golem;

    public MyFireball(World world, EntityLiving entityliving, double d0, double d1, double d2) {
        super(world, entityliving, d0, d1, d2);
        this.golem = (MyEntityGolem) entityliving;
    }

    public MyEntityGolem getGolem() {
        return this.golem;
    }

    @Override
    public Location getLocation() {
        return new Location(this.world.getWorld(), this.locX, this.locY, this.locZ);
    }

    @Override
    public UUID getUID() {
        return null;
    }

    @Override
    public TOWEntityHandler getHandler() {
        return golem.getHandler();
    }

    @Override
    public Entity getMob() {
        return this.getBukkitEntity();
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

    }

    @Override
    public MobType getMobType() {
        return MobType.FIREBALL;
    }

}
