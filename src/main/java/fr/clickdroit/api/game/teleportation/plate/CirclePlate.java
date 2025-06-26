package fr.clickdroit.api.game.teleportation.plate;

import fr.clickdroit.api.utils.Cuboid;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

/**
 * Plateforme circulaire Solo Leveling - Portail plat avec profondeur
 * Design avec de la profondeur vers le bas, principalement en verre
 */
public class CirclePlate implements Plate {
    private final Location center;
    private final Location teleportLocation;
    private final int radius;
    private final Material material;
    private final byte data;

    // Liste des blocs du portail
    private final List<BlockData> allBlocks;

    private static class BlockData {
        final Block block;
        final Material material;
        final byte data;

        BlockData(Block block, Material material, byte data) {
            this.block = block;
            this.material = material;
            this.data = data;
        }
    }

    public CirclePlate(Location center, int radius, Material material, int data) {
        this.center = center.clone();
        this.teleportLocation = center.clone().add(0.0D, 1.0D, 0.0D); // Le joueur marche au niveau du sol
        this.radius = Math.max(3, radius);
        this.material = material;
        this.data = (byte) data;

        this.allBlocks = createFlatPortalWithDepth();
    }

    /**
     * Crée un portail plat avec de la profondeur vers le bas
     */
    private List<BlockData> createFlatPortalWithDepth() {
        List<BlockData> blocks = new ArrayList<>();
        World world = center.getWorld();
        int centerX = center.getBlockX();
        int centerZ = center.getBlockZ();
        int centerY = center.getBlockY();

        // === SURFACE (Y0) - Le niveau où marche le joueur ===
        // REMPLIR TOUTE LA PLATEFORME SANS TROUS

        // D'abord remplir tout le cercle avec du verre de base
        addFilledCircle(blocks, world, centerX, centerZ, centerY, radius, Material.STAINED_GLASS, (byte) 9);

        // Puis ajouter les couches par-dessus pour le design
        // Bordure extérieure - Verre bleu foncé (plus large pour être bien ronde)
        addCircleRing(blocks, world, centerX, centerZ, centerY, radius, radius - 1, Material.STAINED_GLASS, (byte) 11);

        // Cercle intermédiaire - Verre bleu moyen
        addCircleRing(blocks, world, centerX, centerZ, centerY, radius - 1, radius - 2.5, Material.STAINED_GLASS, (byte) 9);

        // Cercle intérieur - Verre bleu clair
        addCircleRing(blocks, world, centerX, centerZ, centerY, radius - 2.5, radius - 3.5, Material.STAINED_GLASS, (byte) 3);

        // Centre - Verre blanc
        addCircleRing(blocks, world, centerX, centerZ, centerY, radius - 3.5, 1, Material.GLASS, (byte) 0);

        // Centre exact - Beacon
        blocks.add(new BlockData(world.getBlockAt(centerX, centerY, centerZ), Material.BEACON, (byte) 0));

        // === PROFONDEUR NIVEAU -1 ===
        // REMPLIR COMPLÈTEMENT SANS TROUS

        // Remplir tout d'abord
        addFilledCircle(blocks, world, centerX, centerZ, centerY - 1, radius, Material.STAINED_GLASS, (byte) 11);

        // Bordure plus foncée
        addCircleRing(blocks, world, centerX, centerZ, centerY - 1, radius, radius - 1, Material.STAINED_GLASS, (byte) 15);

        // === PROFONDEUR NIVEAU -2 ===
        // REMPLIR COMPLÈTEMENT

        addFilledCircle(blocks, world, centerX, centerZ, centerY - 2, radius, Material.STAINED_GLASS, (byte) 15);

        // === PROFONDEUR NIVEAU -3 ===
        // FOND EN VERRE GRIS/NOIR AU LIEU D'OBSIDIENNE

        addFilledCircle(blocks, world, centerX, centerZ, centerY - 3, radius, Material.STAINED_GLASS, (byte) 7); // Gris

        // Accents en verre noir au lieu de glowstone
        for (int angle = 0; angle < 360; angle += 60) {
            double rad = Math.toRadians(angle);
            int x = (int) (Math.cos(rad) * (radius - 2));
            int z = (int) (Math.sin(rad) * (radius - 2));

            blocks.add(new BlockData(
                    world.getBlockAt(centerX + x, centerY - 3, centerZ + z),
                    Material.STAINED_GLASS, (byte) 15 // Noir
            ));
        }

        // === BLOCS EXTÉRIEURS POUR RENDRE LA PLATEFORME VRAIMENT RONDE ===
        // Ajouter des blocs autour pour éviter l'effet carré

        for (int x = -(radius + 2); x <= radius + 2; x++) {
            for (int z = -(radius + 2); z <= radius + 2; z++) {
                double distance = Math.sqrt(x * x + z * z);

                // Blocs de transition pour arrondir (juste à l'extérieur du cercle principal)
                if (distance > radius && distance <= radius + 1.5) {
                    // Surface
                    blocks.add(new BlockData(
                            world.getBlockAt(centerX + x, centerY, centerZ + z),
                            Material.STAINED_GLASS, (byte) 15 // Noir pour la transition
                    ));

                    // Profondeur -1
                    blocks.add(new BlockData(
                            world.getBlockAt(centerX + x, centerY - 1, centerZ + z),
                            Material.STAINED_GLASS, (byte) 15
                    ));

                    // Profondeur -2
                    blocks.add(new BlockData(
                            world.getBlockAt(centerX + x, centerY - 2, centerZ + z),
                            Material.STAINED_GLASS, (byte) 15
                    ));

                    // Fond en verre gris au lieu d'obsidienne
                    blocks.add(new BlockData(
                            world.getBlockAt(centerX + x, centerY - 3, centerZ + z),
                            Material.STAINED_GLASS, (byte) 7 // Gris
                    ));
                }
            }
        }

        // === MURS INVISIBLES POUR EMPÊCHER DE SORTIR ===

        // Murs tout autour jusqu'en haut (plus large pour couvrir la zone étendue)
        for (int x = -(radius + 3); x <= radius + 3; x++) {
            for (int z = -(radius + 3); z <= radius + 3; z++) {
                double distance = Math.sqrt(x * x + z * z);

                if (distance <= radius + 2.5 && distance >= radius + 1.5) {
                    // Murs de la surface jusqu'en haut (5 blocs)
                    for (int y = 1; y <= 5; y++) {
                        blocks.add(new BlockData(
                                world.getBlockAt(centerX + x, centerY + y, centerZ + z),
                                Material.BARRIER, (byte) 0
                        ));
                    }

                    // Murs dans la profondeur aussi
                    for (int y = -1; y >= -3; y--) {
                        blocks.add(new BlockData(
                                world.getBlockAt(centerX + x, centerY + y, centerZ + z),
                                Material.BARRIER, (byte) 0
                        ));
                    }
                }
            }
        }

        return blocks;
    }

    /**
     * Ajoute un cercle plein de blocs
     */
    private void addFilledCircle(List<BlockData> blocks, World world, int centerX, int centerZ, int centerY,
                                 double radius, Material material, byte data) {
        for (int x = -(int)radius - 1; x <= radius + 1; x++) {
            for (int z = -(int)radius - 1; z <= radius + 1; z++) {
                double distance = Math.sqrt(x * x + z * z);

                if (distance <= radius) {
                    blocks.add(new BlockData(
                            world.getBlockAt(centerX + x, centerY, centerZ + z),
                            material, data
                    ));
                }
            }
        }
    }

    /**
     * Ajoute un anneau circulaire de blocs
     */
    private void addCircleRing(List<BlockData> blocks, World world, int centerX, int centerZ, int centerY,
                               double outerRadius, double innerRadius, Material material, byte data) {
        for (int x = -(int)outerRadius - 1; x <= outerRadius + 1; x++) {
            for (int z = -(int)outerRadius - 1; z <= outerRadius + 1; z++) {
                double distance = Math.sqrt(x * x + z * z);

                if (distance <= outerRadius && distance >= innerRadius) {
                    blocks.add(new BlockData(
                            world.getBlockAt(centerX + x, centerY, centerZ + z),
                            material, data
                    ));
                }
            }
        }
    }

    @Override
    public void build() {
        for (BlockData blockData : allBlocks) {
            try {
                blockData.block.setTypeIdAndData(blockData.material.getId(), blockData.data, true);
            } catch (Exception e) {
                blockData.block.setType(blockData.material);
            }
        }
    }

    @Override
    public void destroy() {
        for (BlockData blockData : allBlocks) {
            blockData.block.setType(Material.AIR);
        }
    }

    @Override
    public Location getTeleportLocation() {
        return teleportLocation.clone();
    }

    public boolean isInCircle(Location location) {
        if (!location.getWorld().equals(center.getWorld())) {
            return false;
        }

        double distance = Math.sqrt(
                Math.pow(location.getX() - center.getX(), 2) +
                        Math.pow(location.getZ() - center.getZ(), 2)
        );

        return distance <= radius;
    }

    // Getters
    public Location getCenter() { return center.clone(); }
    public int getRadius() { return radius; }
    public Material getMaterial() { return material; }
    public byte getData() { return data; }
    public int getTotalBlockCount() { return allBlocks.size(); }
}