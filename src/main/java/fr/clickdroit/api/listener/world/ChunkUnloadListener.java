package fr.clickdroit.api.listener.world;

import fr.clickdroit.api.API;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkUnloadListener implements Listener {
    public static List<Chunk> keepChunk = new ArrayList<>();

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        Chunk chunk = event.getChunk();
        int size = (int)API.getAPI().getGameManager().getBorder().getWorldBorder().getSize();
        size /= 2;
        if (keepChunk.contains(chunk) && (chunk
                .getX() > size || chunk.getZ() > size))
            event.setCancelled(true);
    }
}

