package io.github.TcFoxy.ArenaTOW.API;

import io.github.TcFoxy.ArenaTOW.Plugin.ArenaTOW;
import net.minecraft.server.v1_13_R1.EntityInsentient;
import net.minecraft.server.v1_13_R1.World;

import java.util.function.Function;

public class EntityRegistrer {

    /**
     * Register a new custom entity.<br>
     * If the entity is already registered, does nothing.<br><br>
     *
     * When an entity is registered, it will load every already spawned instance of this entity, so it may or may not create a little lag spike.
     */
    @SuppressWarnings("unchecked")
    public static final void register(CustomRegistryEntry entry) {
        if (ArenaTOW.nmsver.contains("1_13_R1")) {
            Class nmsClass = entry.getNMSClass();
            Class customClass = entry.getCustomClass();
            Function<? super World, ? extends EntityInsentient> func = entry.getFunction();
            if (!net.minecraft.server.v1_13_R1.EntityInsentient.class.isAssignableFrom(nmsClass)) throw new IllegalArgumentException("The provided NMS class is not inheritent from EntityInsentient, and therefore does not need to be registered.");
            if (!net.minecraft.server.v1_13_R1.EntityInsentient.class.isAssignableFrom(customClass)) throw new IllegalArgumentException("The provided custom entity class is not inheritent from EntityInsentient, and therefore does not need to be registered.");

            io.github.TcFoxy.ArenaTOW.nms.v1_13_R1.EntityRegistrar.register(entry.getName(), nmsClass, func);
        }
    }

    private void unregisterEntities() {
        if (ArenaTOW.nmsver.contains("1_13_R1")) {
            io.github.TcFoxy.ArenaTOW.nms.v1_13_R1.EntityRegistrar.unregister();
        }
    }
}
