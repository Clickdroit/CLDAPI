package fr.clickdroit.api.listener.world;

import fr.clickdroit.api.API;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkUnloadListener implements Listener {
    public static List<Long> keepChunk = new ArrayList<>();

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        Chunk chunk = event.getChunk();
        int size = (int) API.getAPI().getGameManager().getBorder().getWorldBorder().getSize();
        size /= 2;

        long chunkKey = (long) chunk.getX() << 32 | (chunk.getZ() & 0xFFFFFFFFL);

        if (keepChunk.contains(chunkKey) && (chunk.getX() > size || chunk.getZ() > size)) {
            event.setCancelled(true);
        }
    }
}
