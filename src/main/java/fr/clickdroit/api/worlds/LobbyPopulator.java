package fr.clickdroit.api.worlds;

import fr.clickdroit.api.API;
import fr.clickdroit.api.listener.world.ChunkUnloadListener;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

public class LobbyPopulator {
    private final API api;

    private final World mainWorld;

    private final World lobbyWorld;

    private Location lobbyLocation;

    private Location lobbyRulesRoom;

    private Location center;

    private final Logger logger;

    private final File file;

    public LobbyPopulator(API api) {
        this.api = api;
        this.logger = api.getLogger();
        this.file = new File(api.getDataFolder() + "/lobbytrain.schematic");
        this.lobbyWorld = (new WorldCreator("Lobby")).createWorld();
        this.mainWorld = this.api.getGameManager().getWorldPopulator().getGameWorld();
        this.lobbyWorld.setGameRuleValue("randomTickSpeed", "0");
        this.lobbyWorld.setGameRuleValue("doFireTick", "false");
        this.lobbyLocation = new Location(Bukkit.getWorld("Lobby"), 5.4D, 74.0D, -2.5D, 0.0F, 0.0F);
        this.lobbyRulesRoom = new Location(Bukkit.getWorld("Lobby"), 5.4D, 74.0D, -2.5D, 0.0F, 0.0F);
        this.center = new Location(this.mainWorld, 0.0D, 30.0D, 0.0D);
        org.bukkit.Chunk chunk = this.mainWorld.getChunkAt(0, 0);
        long chunkKey = (long) chunk.getX() << 32 | (chunk.getZ() & 0xFFFFFFFFL);
        ChunkUnloadListener.keepChunk.add(chunkKey);
    }

    public void loadCenter() {
        World world = this.api.getGameManager().getWorldPopulator().getGameWorld();
        Location center = this.center;
        this.center = world.getHighestBlockAt((int) center.getX(), (int) center.getZ()).getLocation();
    }

    private void replaceBlocks(Location location, int radiusX, int radiusY, int radiusZ) {
        ArrayList<Block> blocks = new ArrayList<>();
        double x;
        for (x = location.getX() - radiusX; x <= location.getX() + radiusX; x++) {
            double y;
            for (y = location.getY() - radiusY; y <= location.getY() + radiusY; y++) {
                double z;
                for (z = location.getZ() - radiusZ; z <= location.getZ() + radiusZ; z++) {
                    Location loc = new Location(location.getWorld(), x, y, z);
                    if ((loc.getBlock().getType() == Material.STAINED_GLASS_PANE
                            || loc.getBlock().getType() == Material.STAINED_GLASS) &&
                            !blocks.contains(loc.getBlock()))
                        blocks.add(loc.getBlock());
                }
            }
        }
        for (Block bloc : blocks)
            Bukkit.getScheduler().runTaskLater((Plugin) this.api, () -> bloc.setType(Material.AIR), 5L);
    }

    public Location getCenter() {
        return this.center;
    }

    public Location getLobbyLocation() {
        return this.lobbyLocation;
    }

    public void setLobbyLocation(Location lobbyLocation) {
        this.lobbyLocation = lobbyLocation;
    }

    public Location getLobbyRulesRoom() {
        return this.lobbyRulesRoom;
    }

    public void setLobbyRulesRoom(Location lobbyRulesRoom) {
        this.lobbyRulesRoom = lobbyRulesRoom;
    }
}
