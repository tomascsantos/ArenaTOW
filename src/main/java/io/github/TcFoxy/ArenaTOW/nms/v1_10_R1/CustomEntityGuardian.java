package io.github.TcFoxy.ArenaTOW.nms.v1_10_R1;

import net.minecraft.server.v1_10_R1.DataWatcher;
import net.minecraft.server.v1_10_R1.DataWatcherObject;
import net.minecraft.server.v1_10_R1.DataWatcherRegistry;
import net.minecraft.server.v1_10_R1.EntityGuardian;
import net.minecraft.server.v1_10_R1.World;

public class CustomEntityGuardian extends EntityGuardian{

    private static final DataWatcherObject<Byte> elderWatcher = DataWatcher.a(CustomEntityGuardian.class, DataWatcherRegistry.a);
	
	public CustomEntityGuardian(World world) {
		super(world);
		
		
	}

	 @Override
	 public void initDatawatcher() {
	        super.initDatawatcher();
	        this.datawatcher.register(elderWatcher, (byte) 0); // elder
	 }
}
