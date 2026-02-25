package fr.clickdroit.api.game.listeners;

import fr.clickdroit.api.API;
import fr.clickdroit.api.GamePlayer;
import fr.clickdroit.api.common.rules.items.DropItemRate;
import fr.clickdroit.api.common.rules.items.UseItems;
import fr.clickdroit.api.config.GameConfig;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.game.GameUtils;
import fr.clickdroit.api.utils.Title;
import fr.clickdroit.api.utils.item.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.Random;
import java.util.Queue;
import java.util.Set;
import java.util.LinkedList;
import java.util.HashSet;

public class BlockListener implements Listener {
    private final GameManager gameManager;
    private final GameConfig gameConfig;
    private final World world;
    private final Random random;

    public BlockListener(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameConfig = gameManager.getGameConfig();
        this.world = gameManager.getWorldPopulator().getGameWorld();
        this.random = new Random();
    }

    @EventHandler
    public void onLeafDecay(LeavesDecayEvent event) {
        if (DropItemRate.APPLE.getAmount() != 0) {
            int r = this.random.nextInt(100);
            if (r <= DropItemRate.APPLE.getAmount()) {
                event.setCancelled(true);
                event.getBlock().setType(Material.AIR);
                this.world.dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.APPLE, 1));
            }
        }
    }

    private void dropItemAndExp(Player player, Location location, int xp, ItemStack... itemStacks) {
        if (xp > 0)
            player.giveExp(xp);
        for (ItemStack itemStack : itemStacks) {
            Map<Integer, ItemStack> map = player.getInventory().addItem(new ItemStack[] { itemStack });
            if (!map.isEmpty())
                this.world.dropItemNaturally(location, itemStack);
        }
    }

    private int getRandInt(int max, int min) {
        return this.random.nextInt(max - min + 1) + min;
    }

    @EventHandler
    private void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material material = block.getType();
        Location location = block.getLocation();
        GamePlayer gamePlayer = GamePlayer.getPlayer(player.getUniqueId());

        // Scenario Variables
        boolean cutclean = Scenario.CUTCLEAN.isEnabled();
        boolean oremultiplicator = Scenario.ORESMULTIPLICATOR.isEnabled();
        int oreMultiplicatorValue = Scenario.ORESMULTIPLICATOR.getValue();

        if (gamePlayer.isEditing()) {
            event.setCancelled(true);
            return;
        }

        if (!GameUtils.isGameStarted()) {
            if (!player.getGameMode().equals(GameMode.CREATIVE)) {
                event.setCancelled(true);
            } else if (!player.isOp()) {
                event.setCancelled(true);
            }
        } else {
            byte data;
            switch (material) {
                case STONE:
                    data = block.getData();
                    if (data == 1 || data == 3 || data == 5) { // Diorite, Granite, Andesite
                        event.setCancelled(true);
                        event.getBlock().setType(Material.AIR);
                        int x = (int) block.getLocation().getX();
                        int y = (int) block.getLocation().getY();
                        int z = (int) block.getLocation().getZ();
                        this.world.dropItemNaturally((new Location(block.getWorld(), x, y, z)).add(0.5D, 0.0D, 0.5D),
                                new ItemStack(Material.COBBLESTONE));
                        return;
                    }
                    break;
                case GRAVEL:
                    if (DropItemRate.FLINT.getAmount() != 0) {
                        int r = this.random.nextInt(100);
                        if (r <= DropItemRate.FLINT.getAmount())
                            this.world.dropItemNaturally(event.getBlock().getLocation(),
                                    new ItemStack(Material.FLINT, 1));
                    }
                    break;
                case LEAVES:
                case LEAVES_2:
                    if (player.getInventory().getItemInHand() != null
                            && player.getInventory().getItemInHand().getType() != Material.AIR
                            && player.getInventory().getItemInHand().getType() == Material.SHEARS) {
                        if (UseItems.SHEAR.isEnabled()) {
                            event.setCancelled(true);
                            event.getBlock().setType(Material.AIR);
                            if (DropItemRate.APPLE.getAmount() == 0) {
                                int i = this.random.nextInt(100);
                                if (i <= 10)
                                    this.world.dropItemNaturally(event.getBlock().getLocation(),
                                            new ItemStack(Material.APPLE, 1));
                                break;
                            }
                            int r = this.random.nextInt(100);
                            if (r <= DropItemRate.APPLE.getAmount())
                                this.world.dropItemNaturally(event.getBlock().getLocation(),
                                        new ItemStack(Material.APPLE, 1));
                        }
                        break;
                    }
                    event.setCancelled(true);
                    event.getBlock().setType(Material.AIR);
                    if (DropItemRate.APPLE.getAmount() > 0) {
                        int r = this.random.nextInt(100);
                        if (r <= DropItemRate.APPLE.getAmount())
                            this.world.dropItemNaturally(event.getBlock().getLocation(),
                                    new ItemStack(Material.APPLE, 1));
                    }
                    break;
                default:
                    break;
            }
            if (!Scenario.VEINMINER.isEnabled()) {
                switch (material) {
                    case REDSTONE_ORE:
                    case GLOWING_REDSTONE_ORE:
                        if (oremultiplicator) {
                            event.setCancelled(true);
                            event.getBlock().setType(Material.AIR);
                            dropItemAndExp(player, location, event.getExpToDrop(),
                                    new ItemStack[] { (new ItemCreator(Material.REDSTONE))
                                            .setAmount(getRandInt(5, 3) * oreMultiplicatorValue)
                                            .getItem() });
                        }
                        break;
                    case LAPIS_ORE:
                        if (oremultiplicator) {
                            event.setCancelled(true);
                            event.getBlock().setType(Material.AIR);
                            dropItemAndExp(player, location, event.getExpToDrop(), new ItemStack[] {
                                    (new ItemCreator(Material.INK_SACK)).setDurability(4)
                                            .setAmount(getRandInt(7, 3) * oreMultiplicatorValue)
                                            .getItem() });
                        }
                        break;
                    case IRON_ORE:
                        if (oremultiplicator || cutclean) {
                            event.setCancelled(true);
                            event.getBlock().setType(Material.AIR);
                            dropItemAndExp(player, location, cutclean ? 3 : event.getExpToDrop(), new ItemStack[] {
                                    (new ItemCreator(cutclean ? Material.IRON_INGOT : Material.IRON_ORE))
                                            .setAmount(oremultiplicator ? oreMultiplicatorValue : 1)
                                            .getItem() });
                        }
                        break;
                    case DIAMOND_ORE:
                        foundDiamond(player, block);
                        if (this.gameConfig.getDiamondMax() > 0
                                && gamePlayer.getDiamonds() >= this.gameConfig.getDiamondMax()) {
                            event.setCancelled(true);
                            Title.sendActionBar(player,
                                    "§f[§b*§f] §bLimitede diamants: §c" + gamePlayer.getDiamonds() + "§f/§f "
                                            + API.getAPI().getGameManager().getGameConfig().getDiamondMax()
                                            + " §c(Remplacé par de l'§eor§c)");
                            event.getBlock().setType(Material.AIR);
                            dropItemAndExp(player, location, cutclean ? 5 : event.getExpToDrop(), new ItemStack[] {
                                    (new ItemCreator(cutclean ? Material.GOLD_INGOT : Material.GOLD_ORE))
                                            .setAmount(oremultiplicator ? oreMultiplicatorValue : 1)
                                            .getItem() });
                            return;
                        }
                        if (Scenario.BLOODDIAMOND.isEnabled())
                            player.damage((Scenario.BLOODDIAMOND.getValue() * 2));
                        if (Scenario.DIAMOND_LESS.isEnabled()) {
                            event.setCancelled(true);
                            block.setType(Material.AIR);
                            player.giveExp(event.getExpToDrop());
                            break;
                        }
                        gamePlayer.addDiamonds();
                        if (oremultiplicator) {
                            event.setCancelled(true);
                            event.getBlock().setType(Material.AIR);
                            dropItemAndExp(player, location, event.getExpToDrop(),
                                    new ItemStack[] { (new ItemCreator(Material.DIAMOND))
                                            .setAmount(oreMultiplicatorValue).getItem() });
                        }
                        break;
                    case GOLD_ORE:
                        if (this.gameConfig.getGoldMax() > 0 && gamePlayer.getGolds() >= this.gameConfig.getGoldMax()) {
                            event.setCancelled(true);
                            player.sendMessage("§cVous avez atteint la limite d'ors minable.");
                            return;
                        }
                        gamePlayer.addGolds();
                        if (oremultiplicator || cutclean) {
                            event.setCancelled(true);
                            event.getBlock().setType(Material.AIR);
                            dropItemAndExp(player, location, cutclean ? 5 : event.getExpToDrop(), new ItemStack[] {
                                    (new ItemCreator(cutclean ? Material.GOLD_INGOT : Material.GOLD_ORE))
                                            .setAmount(oremultiplicator ? oreMultiplicatorValue : 1)
                                            .getItem() });
                        }
                        break;
                    case COAL_ORE:
                        if (oremultiplicator || cutclean) {
                            event.setCancelled(true);
                            event.getBlock().setType(Material.AIR);
                            dropItemAndExp(player, location, event.getExpToDrop(),
                                    new ItemStack[] { (new ItemCreator(cutclean ? Material.TORCH : Material.COAL))
                                            .setAmount(cutclean ? (oremultiplicator ? (oreMultiplicatorValue * 2) : 2)
                                                    : (oremultiplicator ? oreMultiplicatorValue : 1))
                                            .getItem() });
                        }
                        break;
                    case EMERALD_ORE:
                        if (oremultiplicator) {
                            event.setCancelled(true);
                            event.getBlock().setType(Material.AIR);
                            dropItemAndExp(player, location, event.getExpToDrop(),
                                    new ItemStack[] { (new ItemCreator(Material.EMERALD))
                                            .setAmount(oreMultiplicatorValue).getItem() });
                        }
                        break;
                    case QUARTZ_ORE:
                        if (oremultiplicator) {
                            event.setCancelled(true);
                            event.getBlock().setType(Material.AIR);
                            dropItemAndExp(player, location, event.getExpToDrop(),
                                    new ItemStack[] { (new ItemCreator(Material.QUARTZ))
                                            .setAmount(oreMultiplicatorValue).getItem() });
                        }
                        break;
                }
            } else {
                switch (material) {
                    case REDSTONE_ORE:
                    case GLOWING_REDSTONE_ORE:
                    case LAPIS_ORE:
                    case DIAMOND_ORE:
                    case COAL_ORE:
                    case EMERALD_ORE:
                    case QUARTZ_ORE:
                        event.setCancelled(true);
                        removeOres(player, block, 20, event.getExpToDrop());
                        break;
                    case IRON_ORE:
                        event.setCancelled(true);
                        removeOres(player, block, 20, cutclean ? 4 : event.getExpToDrop());
                        break;
                    case GOLD_ORE:
                        event.setCancelled(true);
                        removeOres(player, block, 20, cutclean ? 5 : event.getExpToDrop());
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void foundDiamond(Player player, Block block) {
        if (block.getType().equals(Material.DIAMOND_ORE) && !block.hasMetadata("Diamond")) {
            int diamonds = 0;
            for (int x = -5; x < 5; x++) {
                for (int y = -5; y < 5; y++) {
                    for (int z = -5; z < 5; z++) {
                        Block b = block.getLocation().add(x, y, z).getBlock();
                        if (b.getType() == Material.DIAMOND_ORE) {
                            diamonds++;
                            b.setMetadata("Diamond",
                                    (MetadataValue) new FixedMetadataValue((Plugin) this.gameManager.getApi(),
                                            Boolean.valueOf(true)));
                        }
                    }
                }
            }
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (GamePlayer.getPlayer(players.getUniqueId()).isAlerts())
                    players.sendMessage("§f[FD] §b" + player.getName() + " §ba trouvé " + diamonds + " diamant"
                            + ((diamonds > 1) ? "s" : "") + ".");
            }
        }
    }

    private void removeOres(Player player, Block startBlock, int blockMaxAmount, int xp) {
        Material m = startBlock.getType();
        if (m != Material.COAL_ORE && m != Material.EMERALD_ORE && m != Material.REDSTONE_ORE &&
                m != Material.GLOWING_REDSTONE_ORE && m != Material.IRON_ORE && m != Material.GOLD_ORE &&
                m != Material.DIAMOND_ORE && m != Material.QUARTZ_ORE && m != Material.LAPIS_ORE) {
            return;
        }

        Queue<Block> queue = new LinkedList<>();
        Set<Block> visited = new HashSet<>();

        queue.add(startBlock);
        visited.add(startBlock);

        int broken = 0;

        while (!queue.isEmpty() && broken < blockMaxAmount) {
            Block block = queue.poll();

            if (block.getType() != m && block != startBlock)
                continue;

            block.getWorld().playSound(block.getLocation(), Sound.DIG_STONE, 1.0F, 1.0F);
            block.setType(Material.AIR);
            dropItemAndExp(player, block.getLocation(), xp, new ItemStack[] { getResult(m) });
            broken++;

            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        if (x == 0 && y == 0 && z == 0)
                            continue;
                        Block adjacent = block.getRelative(x, y, z);
                        if (adjacent.getType() == m && !visited.contains(adjacent)) {
                            visited.add(adjacent);
                            queue.add(adjacent);
                        }
                    }
                }
            }
        }
    }

    private ItemStack getResult(Material material) {
        boolean cutclean = Scenario.CUTCLEAN.isEnabled();
        boolean oremultiplicator = Scenario.ORESMULTIPLICATOR.isEnabled();
        int oreMultiplicatorValue = Scenario.ORESMULTIPLICATOR.getValue();
        ItemCreator result = new ItemCreator(Material.COAL);
        switch (material) {
            case EMERALD_ORE:
                result.getItem().setType(Material.EMERALD);
                result.setAmount(oremultiplicator ? oreMultiplicatorValue : 1);
                break;
            case REDSTONE_ORE:
            case GLOWING_REDSTONE_ORE:
                result.getItem().setType(Material.REDSTONE);
                result.setAmount(getRandInt(5, 3) * (oremultiplicator ? oreMultiplicatorValue : 1));
                break;
            case COAL_ORE:
                result.getItem().setType(cutclean ? Material.TORCH : Material.COAL);
                result.setAmount(cutclean ? (oremultiplicator ? (oreMultiplicatorValue * 2) : 2)
                        : (oremultiplicator ? oreMultiplicatorValue : 1));
                break;
            case LAPIS_ORE:
                result.getItem().setType(Material.INK_SACK);
                result.setDurability(4);
                result.setAmount(getRandInt(7, 3) * oreMultiplicatorValue);
                break;
            case IRON_ORE:
                result.getItem().setType(cutclean ? Material.IRON_INGOT : Material.IRON_ORE);
                result.setAmount(oremultiplicator ? oreMultiplicatorValue : 1);
                break;
            case GOLD_ORE:
                result.getItem().setType(cutclean ? Material.GOLD_INGOT : Material.GOLD_ORE);
                result.setAmount(oremultiplicator ? oreMultiplicatorValue : 1);
                break;
            case DIAMOND_ORE:
                result.getItem().setType(Material.DIAMOND);
                result.setAmount(oremultiplicator ? oreMultiplicatorValue : 1);
                break;
            case QUARTZ_ORE:
                result.getItem().setType(Material.QUARTZ);
                result.setAmount(oremultiplicator ? oreMultiplicatorValue : 1);
                break;
            default:
                break;
        }
        return result.getItem();
    }
}
