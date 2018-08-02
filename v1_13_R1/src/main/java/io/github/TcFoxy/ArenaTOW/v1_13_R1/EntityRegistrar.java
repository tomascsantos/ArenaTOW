package io.github.TcFoxy.ArenaTOW.v1_13_R1;

import io.github.TcFoxy.ArenaTOW.Plugin.ArenaTOW;
import net.minecraft.server.v1_13_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.craftbukkit.v1_13_R1.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.function.Function;

/**
 * This class holds the register function which is called by the EntityRegistrar class.
 * It also contains helpers for chunk loading the custom entities handled by v13_Listener.
 */
class EntityRegistrar {

    /**
     * Add custom entities to REGISTRY so they can all be registered or unregistered.
     * @param name
     * @param customClass
     * @param function
     */
    public static final void register(String name, Class<? extends EntityInsentient> customClass, Function<? super World, ? extends EntityInsentient> function) {
        CustomEntityList.REGISTRY.put(name, EntityTypes.a(name, EntityTypes.a.a(customClass, function).b()));
    }


    /**
     * Saves entities as they are about to be destroyed by unloading chunks
     * @param e The entity being saved.
     */
    static void saveEntity(EntityInsentient e) {
        try {
            Chunk c = e.getBukkitEntity().getLocation().getChunk();
            File cDir = new File(ArenaTOW.getSelf().getSaveDir() + System.getProperty("file.separator") + c.getWorld().getName() + System.getProperty("file.separator") + c.getX() + System.getProperty("file.separator") + c.getZ());
            cDir.mkdirs();
            NBTTagCompound nbt = new NBTTagCompound();
            e.save(nbt);
            nbt.setString("cid", getCustomEntityID(e));
            e.getBukkitEntity().remove();
            NBTCompressedStreamTools.a(nbt, new FileOutputStream(new File(cDir, String.valueOf(cDir.list().length + 1) + " - " + nbt.getString("cid") + ".tbp")));
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    static boolean loadEntity(NBTTagCompound nbt, org.bukkit.World w) {
        EntityInsentient e;
        block4 : {
            if (!CustomEntityList.REGISTRY.containsKey(nbt.getString("cid"))) {
                return false;
            }
            try {
                WorldServer nmsW = ((CraftWorld)w).getHandle();
                e = CustomEntityList.REGISTRY.get(nbt.getString("cid")).a(nmsW);
                NBTTagList pos = nbt.getList("Pos", 6);
                NBTTagList rot = nbt.getList("Rotation", 5);
                e.setPositionRotation(pos.k(0), pos.k(1), pos.k(2), rot.l(0), rot.l(1));
                if (nmsW.addEntity(e, CreatureSpawnEvent.SpawnReason.NATURAL)) break block4;
                return false;
            }
            catch (Exception e1) {
                e1.printStackTrace();
                return false;
            }
        }
        e.f(nbt);
        return true;
    }

    static void loadEntity(File entityFile) {
        try {
            String[] pathElements = entityFile.getAbsolutePath().split(System.getProperty("file.separator"));
            if (loadEntity(NBTCompressedStreamTools.a(new FileInputStream(entityFile)), Bukkit.getWorld(pathElements[pathElements.length - 4]))) {
                entityFile.delete();
            }
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    static boolean chunkHasCustomEntities(Chunk c) {
        File cDir = new File(ArenaTOW.getSelf().getSaveDir() + System.getProperty("file.separator") + c.getWorld().getName() + System.getProperty("file.separator") + c.getX() + System.getProperty("file.separator") + c.getZ());
        if (!cDir.exists()) {
            return false;
        }
        if (cDir.listFiles(new FilenameFilter(){

            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".tbp");
            }
        }).length <= 0) {
            return false;
        }
        return true;
    }

    protected static String getCustomEntityID(EntityInsentient e) {
        return CustomEntityList.REGISTRY.inverse().get(e.P());
    }

}
