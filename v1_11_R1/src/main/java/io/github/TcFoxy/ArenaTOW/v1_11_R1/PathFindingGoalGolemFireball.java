package io.github.TcFoxy.ArenaTOW.v1_11_R1;

import net.minecraft.server.v1_11_R1.*;

class PathfinderGoalGolemFireball extends PathfinderGoal {
    private MyEntityGolem a;
    private int c;

    public PathfinderGoalGolemFireball(MyEntityGolem paramEntityGolem) {
        this.a = paramEntityGolem;

        a(3);
    }

    public boolean a() {
        EntityLiving localEntityLiving = this.a.getGoalTarget();
        if ((localEntityLiving == null) || (!localEntityLiving.isAlive())) {
            return false;
        }
        return true;
    }

    public void d() {
        this.a.a(false);
    }

    public void e() {
        EntityLiving localEntityLiving = this.a.getGoalTarget();



        this.c -= 1;

        // Check for line of sight
        if (!this.a.hasLineOfSight(localEntityLiving)) {
            this.a.setGoalTarget(null);
            return;
        }

        //If same team then cancel.
        if (a.isSameTeam(localEntityLiving)) {
            return;
        }

        //If we get this far then we are proceeding with the fireball.
        double d2 = localEntityLiving.locX - this.a.locX;
        double d3 = localEntityLiving.getBoundingBox().b + localEntityLiving.length / 2.0F - (this.a.locY + this.a.length / 2.0F);
        double d4 = localEntityLiving.locZ - this.a.locZ;

        if (this.c <= 0) {

            this.c = 10; //10 tick cooldown

            this.a.world.a(null, 1009, new BlockPosition((int) this.a.locX, (int) this.a.locY, (int) this.a.locZ), 0);
            //for (int i = 0; i < 3; i++) {
            MyFireball localEntitySmallFireball = new MyFireball(this.a.world, this.a, d2, d3, d4);
            localEntitySmallFireball.motX = localEntitySmallFireball.dirX * 15;
            localEntitySmallFireball.motY = localEntitySmallFireball.dirY * 15;
            localEntitySmallFireball.motZ = localEntitySmallFireball.dirZ * 15;
            localEntitySmallFireball.locY = (this.a.locY + this.a.length / 2.0F + 0.5D);
            this.a.world.addEntity(localEntitySmallFireball);

            //}
        }
        this.a.getControllerLook().a(localEntityLiving, 10.0F, 10.0F);

        super.e();
    }
}





/*
class PathfinderGoalBlazeFireball extends PathfinderGoal
{
    // This is your blaze (duh)
    private EntityBlaze a;
    // This seems to be the charge
    // Note that the following values are based upon the code
    // and do not necessarily reflect behaviour or original thought :P
    // - 0 means it's idle
    // - 1 means it's charging
    // - 2, 3 and 4 means it's capable of firing
    private int b;
    // This seems to be the cooldown. If it's <= 0, they can fire
    private int c;

    public PathfinderGoalBlazeFireball(EntityBlaze paramEntityBlaze)
    {
        this.a = paramEntityBlaze;

        a(3);
    }

    // Checks if the blaze has a target
    public boolean a()
    {
        EntityLiving localEntityLiving = this.a.getGoalTarget();
        if ((localEntityLiving == null) || (!localEntityLiving.isAlive())) {
            return false;
        }

        return true;
    }

    // Reset the charge
    public void c()
    {
        this.b = 0;
    }

    // Extinguish the blaze
    public void d()
    {
        this.a.a(false);
    }

    public void e()
    {

        // Decrease the cooldown
        this.c -= 1;

        EntityLiving localEntityLiving = this.a.getGoalTarget();

        // Get the distance
        double d1 = this.a.h(localEntityLiving);

        // If the target is nearby enough (2 blocks, as this is the distance squared)
        if (d1 < 4.0D)
        {
            // And if the cooldown is not active
            if (this.c <= 0) {
                // Reset it to 20 ticks
                this.c = 20;
                // And attack the target (apparently a physical attack O.o - never knew blazes could do that)
                this.a.r(localEntityLiving);
            }
            // Move towards the target
            this.a.getControllerMove().a(localEntityLiving.locX, localEntityLiving.locY, localEntityLiving.locZ, 1.0D);
        } else if (d1 < 256.0D) { // And if the entity is further away (16 blocks, once again distance squared)
            double d2 = localEntityLiving.locX - this.a.locX;
            double d3 = localEntityLiving.getBoundingBox().b + localEntityLiving.length / 2.0F - (this.a.locY + this.a.length / 2.0F);
            double d4 = localEntityLiving.locZ - this.a.locZ;

            // If it does not have a cooldown
            if (this.c <= 0) {
                // Increment charge
                this.b += 1;
                if (this.b == 1) {
                    // Set the cooldown (till next charge) to 60 ticks
                    this.c = 60;
                    // Set blaze on fire
                    this.a.a(true);
                } else if (this.b <= 4) { // For the 2nd, 3rd and 4th charge tick, wait 6 ticks
                    this.c = 6;
                } else {
                    // Else set the cooldown to 100 ticks
                    this.c = 100;
                    // Set the charged state to 0
                    this.b = 0;
                    // And extinguish the blaze
                    this.a.a(false);
                }

                // If it's charged
                if (this.b > 1) {
                    float f = MathHelper.c(MathHelper.sqrt(d1)) * 0.5F;

                    // Play the blaze shoot sound
                    this.a.world.a(null, 1009, new BlockPosition((int)this.a.locX, (int)this.a.locY, (int)this.a.locZ), 0);
                    // Loop 5 times
                    // This is where you adjust the amount
                    for (int i = 0; i < 5; i++) {
                        // And shoot a fireball every iteration
                        // This is where you would adjust the direction
                        EntitySmallFireball localEntitySmallFireball = new EntitySmallFireball(this.a.world, this.a, d2 + this.a.bb().nextGaussian() * f, d3, d4 + this.a.bb().nextGaussian() * f);
                        // Set the Y to the blaze height + half its height + 0.5 blocks (should be around the height of its head)
                        localEntitySmallFireball.locY = (this.a.locY + this.a.length / 2.0F + 0.5D);
                        // Lastly add the entity to the world so it's being ticked and tracked
                        this.a.world.addEntity(localEntitySmallFireball);
                    }
                }
            }
            // Look at the entity
            this.a.getControllerLook().a(localEntityLiving, 10.0F, 10.0F);
        } else {
            // I assume this resets the path
            this.a.getNavigation().n();
            // And this would set a new path
            this.a.getControllerMove().a(localEntityLiving.locX, localEntityLiving.locY, localEntityLiving.locZ, 1.0D);
        }

        super.e();
    }
}
 */