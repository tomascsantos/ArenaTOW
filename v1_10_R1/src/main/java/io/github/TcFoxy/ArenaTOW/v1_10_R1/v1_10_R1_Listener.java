package io.github.TcFoxy.ArenaTOW.v1_10_R1;

import net.minecraft.server.v1_10_R1.*;
import org.bukkit.Chunk;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.io.File;
import java.io.FilenameFilter;

public class v1_10_R1_Listener implements Listener {

    File dir;
    v1_10_R1_EntityRegistrar registrar;

    public v1_10_R1_Listener(v1_10_R1_EntityRegistrar registrar, File dir) {
        this.registrar = registrar;
        this.dir = dir;
    }

    @EventHandler
    public void onChunkLoad(final ChunkLoadEvent evt) {
        if (!registrar.chunkHasCustomEntities(evt.getChunk())) return;
        final Chunk c = evt.getChunk();
        final File cDir = new File(dir + System.getProperty("file.separator") + c.getWorld().getName() + System.getProperty("file.separator") + c.getX() + System.getProperty("file.separator") + c.getZ());

        for (File f : cDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".tbp");
            }
        }))
            registrar.loadEntity(f);
    }

    @EventHandler
    public void onChunkUnload(final ChunkUnloadEvent evt) {
        final Chunk c = evt.getChunk();
        for (org.bukkit.entity.Entity e : c.getEntities()) {
            Entity nmsEntity = ((CraftEntity) e).getHandle();
            if (nmsEntity instanceof EntityInsentient)
                if (registrar.registryEntries.containsKey(EntityTypes.b(nmsEntity)))
                    registrar.saveEntity((EntityInsentient) nmsEntity);
        }
    }
}
