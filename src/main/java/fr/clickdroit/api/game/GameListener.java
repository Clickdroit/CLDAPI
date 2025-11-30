package fr.clickdroit.api.game;

import fr.clickdroit.api.API;
import fr.clickdroit.api.GamePlayer;
import fr.clickdroit.api.common.rules.Rules;
import fr.clickdroit.api.common.rules.items.DropItemRate;
import fr.clickdroit.api.common.rules.items.GeneralRules;
import fr.clickdroit.api.common.rules.items.UseItems;
import fr.clickdroit.api.config.GameConfig;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.game.team.Teams;
import fr.clickdroit.api.utils.ItemCreator;
import fr.clickdroit.api.utils.Title;
import fr.clickdroit.api.utils.UHCConstants;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.Random;

public class GameListener implements Listener {
    private final GameManager gameManager;

    private final GameConfig gameConfig;

    private final World world;

    private final Random random;

    public GameListener(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameConfig = gameManager.getGameConfig();
        this.world = gameManager.getWorldPopulator().getGameWorld();
        this.random = new Random();
    }

    @EventHandler
    private void onCreatureSpawnEvent(CreatureSpawnEvent event) {
        LivingEntity livingEntity = event.getEntity();
        CreatureSpawnEvent.SpawnReason spawnReason = event.getSpawnReason();
        if (spawnReason.equals(CreatureSpawnEvent.SpawnReason.NATURAL)) {
            if (livingEntity.getWorld().getName().equalsIgnoreCase(UHCConstants.LOBBY_WORLD_NAME)) {
                event.setCancelled(true);
                return;
            }
            if (livingEntity instanceof org.bukkit.entity.Enderman) {
                int next = this.random.nextInt(100);
                if (next > 50) {
                    event.setCancelled(true);
                    return;
                }
            }
            World entityWorld = event.getEntity().getWorld();
            if (entityWorld.getName().equalsIgnoreCase(UHCConstants.LOBBY_WORLD_NAME))
                event.setCancelled(true);
        }
    }

    @EventHandler
    private void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();
            if (player.getGameMode().equals(GameMode.SPECTATOR))
                event.setCancelled(true);
            if (GamePlayer.getPlayer(player.getUniqueId()).isInvincible())
                event.setCancelled(true);
            if (event.getCause().equals(EntityDamageEvent.DamageCause.VOID) && player.getLocation().getY() < 0.0D &&
                    !GameUtils.isGameStarted())
                player.teleport(this.gameManager.getApi().getLobbyPopulator().getLobbyLocation());
            if (GameUtils.isGameStarted()) {
                GamePlayer gamePlayer = GamePlayer.getPlayer(player.getUniqueId());
                if (gamePlayer == null)
                    return;
                if (gamePlayer.getInvincibilityCount() > 0)
                    event.setCancelled(true);
                if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL) && gamePlayer
                        .getInvincibilityNoFallCount() > 0)
                    event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        Player player = (Player)event.getEntity();
        if (event.getFoodLevel() < player.getFoodLevel()) {
            player.setSaturation(10.0F);
            player.setExhaustion(0.0F);
        }
    }

    @EventHandler
    private void onThunder(ThunderChangeEvent event) {
        if (event.toThunderState())
            event.setCancelled(true);
    }

    @EventHandler
    private void onRain(WeatherChangeEvent event) {
        if (event.toWeatherState())
            event.setCancelled(true);
    }

    @EventHandler
    private void onFood(FoodLevelChangeEvent event) {
        if (!GameUtils.isGameStarted() || Rules.noDamage.isActive())
            event.setCancelled(true);
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof org.bukkit.entity.Cow) {
            if (DropItemRate.LEATHER.getAmount() != 0) {
                int r = this.random.nextInt(100);
                if (r <= DropItemRate.LEATHER.getAmount())
                    this.world.dropItemNaturally(event.getEntity().getLocation(), new ItemStack(Material.LEATHER, 1));
            }
        } else if (event.getEntity() instanceof org.bukkit.entity.Enderman) {
            if (DropItemRate.ENDERPEARL.getAmount() != 0) {
                int r = this.random.nextInt(100);
                if (r <= DropItemRate.ENDERPEARL.getAmount())
                    this.world.dropItemNaturally(event.getEntity().getLocation(), new ItemStack(Material.ENDER_PEARL, 1));
            }
        } else if (event.getEntity() instanceof org.bukkit.entity.Skeleton && DropItemRate.ARROW
                .getAmount() != 0) {
            int r = this.random.nextInt(100);
            if (r <= DropItemRate.ARROW.getAmount())
                this.world.dropItemNaturally(event.getEntity().getLocation(), new ItemStack(Material.ARROW, 1));
        }
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

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        if (!GameUtils.isGameStarted() &&
                !player.getGameMode().equals(GameMode.CREATIVE))
            event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (!GameUtils.isGameStarted())
            event.setCancelled(true);
    }

    @EventHandler
    public void onPickUp(PlayerPickupItemEvent event) {
        if (!GameUtils.isGameStarted())
            event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player &&
                !GameUtils.isGameStarted())
            event.setCancelled(true);
    }

    @EventHandler
    private void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player victim = (Player)event.getEntity();
            if (event.getDamager() instanceof Player) {
                Player attacker = (Player)event.getDamager();
                if (this.gameManager.getInGamePlayers().contains(attacker.getUniqueId()) && this.gameManager.getInGamePlayers().contains(victim.getUniqueId()) && this.gameManager
                        .getTeamManager().getPlayerTeam().containsKey(victim.getUniqueId()) && this.gameManager.getTeamManager().getPlayerTeam().containsKey(attacker.getUniqueId()) && ((Teams)this.gameManager
                        .getTeamManager().getPlayerTeam().get(victim.getUniqueId())).equals(this.gameManager.getTeamManager().getPlayerTeam().get(attacker.getUniqueId())) &&
                        !this.gameManager.getGameConfig().isFriendlyfire())
                    event.setCancelled(true);
            } else if (event.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile)event.getDamager();
                if (projectile.getShooter() instanceof Player) {
                    Player attacker = (Player)projectile.getShooter();
                    if (this.gameManager.getInGamePlayers().contains(attacker.getUniqueId()) && this.gameManager.getInGamePlayers().contains(victim.getUniqueId()) && this.gameManager
                            .getTeamManager().getPlayerTeam().containsKey(victim.getUniqueId()) && this.gameManager.getTeamManager().getPlayerTeam().containsKey(attacker.getUniqueId()) && ((Teams)this.gameManager
                            .getTeamManager().getPlayerTeam().get(victim.getUniqueId())).equals(this.gameManager.getTeamManager().getPlayerTeam().get(attacker.getUniqueId())) &&
                            !this.gameManager.getGameConfig().isFriendlyfire())
                        event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    private void onPlayerPortalEvent(PlayerPortalEvent event) {
        if (!this.gameManager.getGameConfig().isNether()) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cLe Nether est désactivé.");
        }
    }

    @EventHandler
    public void onCraft(PrepareItemCraftEvent event) {
        ItemStack item = event.getInventory().getResult();
        if (item == null)
            return;
        for (UseItems items : UseItems.values()) {
            if (items.equals(UseItems.FISHINGROD) &&
                    !items.isEnabled() && item
                    .getType() == items.getMaterial())
                event.getInventory().setResult(new ItemStack(Material.AIR));
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

    @EventHandler
    private void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material material = block.getType();
        Location location = block.getLocation();
        GamePlayer gamePlayer = GamePlayer.getPlayer(player.getUniqueId());
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
                    if (data == 1 || data == 3 || data == 5) {
                        event.setCancelled(true);
                        event.getBlock().setType(Material.AIR);
                        int x = (int)block.getLocation().getX();
                        int y = (int)block.getLocation().getY();
                        int z = (int)block.getLocation().getZ();
                        this.world.dropItemNaturally((new Location(block.getWorld(), x, y, z)).add(0.5D, 0.0D, 0.5D), new ItemStack(Material.COBBLESTONE));
                        return;
                    }
                    break;
                case GRAVEL:
                    if (DropItemRate.FLINT.getAmount() != 0) {
                        int r = this.random.nextInt(100);
                        if (r <= DropItemRate.FLINT.getAmount())
                            this.world.dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.FLINT, 1));
                    }
                    break;
                case LEAVES:
                case LEAVES_2:
                    if (player.getInventory().getItemInHand() != null && player.getInventory().getItemInHand().getType() != Material.AIR && player.getInventory().getItemInHand().getType() == Material.SHEARS) {
                        if (UseItems.SHEAR.isEnabled()) {
                            event.setCancelled(true);
                            event.getBlock().setType(Material.AIR);
                            if (DropItemRate.APPLE.getAmount() == 0) {
                                int i = this.random.nextInt(100);
                                if (i <= 10)
                                    this.world.dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.APPLE, 1));
                                break;
                            }
                            int r = this.random.nextInt(100);
                            if (r <= DropItemRate.APPLE.getAmount())
                                this.world.dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.APPLE, 1));
                        }
                        break;
                    }
                    event.setCancelled(true);
                    event.getBlock().setType(Material.AIR);
                    if (DropItemRate.APPLE.getAmount() > 0) {
                        int r = this.random.nextInt(100);
                        if (r <= DropItemRate.APPLE.getAmount())
                            this.world.dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.APPLE, 1));
                    }
                    break;
            }
            if (!Scenario.VEINMINER.isEnabled()) {
                switch (material) {
                    case REDSTONE_ORE:
                    case GLOWING_REDSTONE_ORE:
                        if (oremultiplicator) {
                            event.setCancelled(true);
                            event.getBlock().setType(Material.AIR);
                            dropItemAndExp(player, location, event.getExpToDrop(), new ItemStack[] { (new ItemCreator(Material.REDSTONE)).setAmount(Integer.valueOf(getRandInt(5, 3) * oreMultiplicatorValue)).getItem() });
                        }
                        break;
                    case LAPIS_ORE:
                        if (oremultiplicator) {
                            event.setCancelled(true);
                            event.getBlock().setType(Material.AIR);
                            dropItemAndExp(player, location, event.getExpToDrop(), new ItemStack[] { (new ItemCreator(Material.INK_SACK)).setDurability(Integer.valueOf(4)).setAmount(Integer.valueOf(getRandInt(7, 3) * oreMultiplicatorValue)).getItem() });
                        }
                        break;
                    case IRON_ORE:
                        if (oremultiplicator || cutclean) {
                            event.setCancelled(true);
                            event.getBlock().setType(Material.AIR);
                            dropItemAndExp(player, location, cutclean ? 3 : event.getExpToDrop(), new ItemStack[] { (new ItemCreator(cutclean ? Material.IRON_INGOT : Material.IRON_ORE)).setAmount(Integer.valueOf(oremultiplicator ? oreMultiplicatorValue : 1)).getItem() });
                        }
                        break;
                    case DIAMOND_ORE:
                        foundDiamond(player, block);
                        if (this.gameConfig.getDiamondMax() > 0 && gamePlayer.getDiamonds() >= this.gameConfig.getDiamondMax()) {
                            event.setCancelled(true);
                            Title.sendActionBar(player, "§f[§b*§f] §bLimitede diamants: §c"+ gamePlayer.getDiamonds() +"§f/§f "+ API.getAPI().getGameManager().getGameConfig().getDiamondMax() + " §c(Remplacé par de l'§eor§c)");
                                    event.getBlock().setType(Material.AIR);
                            dropItemAndExp(player, location, cutclean ? 5 : event.getExpToDrop(), new ItemStack[] { (new ItemCreator(cutclean ? Material.GOLD_INGOT : Material.GOLD_ORE)).setAmount(Integer.valueOf(oremultiplicator ? oreMultiplicatorValue : 1)).getItem() });
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
                            dropItemAndExp(player, location, event.getExpToDrop(), new ItemStack[] { (new ItemCreator(Material.DIAMOND)).setAmount(Integer.valueOf(oreMultiplicatorValue)).getItem() });
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
                            dropItemAndExp(player, location, cutclean ? 5 : event.getExpToDrop(), new ItemStack[] { (new ItemCreator(cutclean ? Material.GOLD_INGOT : Material.GOLD_ORE)).setAmount(Integer.valueOf(oremultiplicator ? oreMultiplicatorValue : 1)).getItem() });
                        }
                        break;
                    case COAL_ORE:
                        if (oremultiplicator || cutclean) {
                            event.setCancelled(true);
                            event.getBlock().setType(Material.AIR);
                            dropItemAndExp(player, location, event.getExpToDrop(), new ItemStack[] { (new ItemCreator(cutclean ? Material.TORCH : Material.COAL)).setAmount(Integer.valueOf(cutclean ? (oremultiplicator ? (oreMultiplicatorValue * 2) : 2) : (oremultiplicator ? oreMultiplicatorValue : 1))).getItem() });
                        }
                        break;
                    case EMERALD_ORE:
                        if (oremultiplicator) {
                            event.setCancelled(true);
                            event.getBlock().setType(Material.AIR);
                            dropItemAndExp(player, location, event.getExpToDrop(), new ItemStack[] { (new ItemCreator(Material.EMERALD)).setAmount(Integer.valueOf(oreMultiplicatorValue)).getItem() });
                        }
                        break;
                    case QUARTZ_ORE:
                        if (oremultiplicator) {
                            event.setCancelled(true);
                            event.getBlock().setType(Material.AIR);
                            dropItemAndExp(player, location, event.getExpToDrop(), new ItemStack[] { (new ItemCreator(Material.QUARTZ)).setAmount(Integer.valueOf(oreMultiplicatorValue)).getItem() });
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
                            b.setMetadata("Diamond", (MetadataValue)new FixedMetadataValue((Plugin)this.gameManager.getApi(), Boolean.valueOf(true)));
                        }
                    }
                }
            }
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (GamePlayer.getPlayer(players.getUniqueId()).isAlerts())
                    players.sendMessage("§f[FD] §b" + player.getName() + " §ba trouvé " + diamonds + " diamant" + ((diamonds > 1) ? "s" : "") + ".");
            }
        }
    }

    private int removeOres(final Player player, Block block, final int blockMaxAmount, final int xp) {
        Material m = block.getType();
        final Location loc = block.getLocation();
        if (m == Material.COAL_ORE || m == Material.EMERALD_ORE || m == Material.REDSTONE_ORE || m == Material.GLOWING_REDSTONE_ORE || m == Material.IRON_ORE || m == Material.GOLD_ORE || m == Material.DIAMOND_ORE || m == Material.QUARTZ_ORE || m == Material.LAPIS_ORE) {
            block.getWorld().playSound(block.getLocation(), Sound.DIG_STONE, 1.0F, 1.0F);
            block.setType(Material.AIR);
            dropItemAndExp(player, block.getLocation(), xp, new ItemStack[] { getResult(m) });
            if (blockMaxAmount <= 0)
                return 0;
            (new BukkitRunnable() {
                public void run() {
                    int i = blockMaxAmount;
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            for (int z = -1; z <= 1; z++) {
                                if (x != 0 || y != 0 || z != 0)
                                    i = GameListener.this.removeOres(player, loc.clone().add(x, y, z).getBlock(), i, xp);
                            }
                        }
                    }
                }
            }).runTaskLater((Plugin)this.gameManager.getApi(), 2L);
            return blockMaxAmount - 1;
        }
        return blockMaxAmount;
    }

    private ItemStack getResult(Material material) {
        boolean cutclean = Scenario.CUTCLEAN.isEnabled();
        boolean oremultiplicator = Scenario.ORESMULTIPLICATOR.isEnabled();
        int oreMultiplicatorValue = Scenario.ORESMULTIPLICATOR.getValue();
        ItemCreator result = new ItemCreator(Material.COAL);
        switch (material) {
            case EMERALD_ORE:
                result.getItem().setType(Material.EMERALD);
                result.setAmount(Integer.valueOf(oremultiplicator ? oreMultiplicatorValue : 1));
                break;
            case REDSTONE_ORE:
            case GLOWING_REDSTONE_ORE:
                result.getItem().setType(Material.REDSTONE);
                result.setAmount(Integer.valueOf(getRandInt(5, 3) * (oremultiplicator ? oreMultiplicatorValue : 1)));
                break;
            case COAL_ORE:
                result.getItem().setType(cutclean ? Material.TORCH : Material.COAL);
                result.setAmount(Integer.valueOf(cutclean ? (oremultiplicator ? (oreMultiplicatorValue * 2) : 2) : (oremultiplicator ? oreMultiplicatorValue : 1)));
                break;
            case LAPIS_ORE:
                result.getItem().setType(Material.INK_SACK);
                result.setDurability(Integer.valueOf(4));
                result.setAmount(Integer.valueOf(getRandInt(7, 3) * oreMultiplicatorValue));
                break;
            case IRON_ORE:
                result.getItem().setType(cutclean ? Material.IRON_INGOT : Material.IRON_ORE);
                result.setAmount(Integer.valueOf(oremultiplicator ? oreMultiplicatorValue : 1));
                break;
            case GOLD_ORE:
                result.getItem().setType(cutclean ? Material.GOLD_INGOT : Material.GOLD_ORE);
                result.setAmount(Integer.valueOf(oremultiplicator ? oreMultiplicatorValue : 1));
                break;
            case DIAMOND_ORE:
                result.getItem().setType(Material.DIAMOND);
                result.setAmount(Integer.valueOf(oremultiplicator ? oreMultiplicatorValue : 1));
                break;
            case QUARTZ_ORE:
                result.getItem().setType(Material.QUARTZ);
                result.setAmount(Integer.valueOf(oremultiplicator ? oreMultiplicatorValue : 1));
                break;
        }
        return result.getItem();
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        if (event.getBucket().equals(Material.LAVA_BUCKET) && !Rules.pvp.isActive()) {
            event.getPlayer().sendMessage("§cLe PvP est désactivé, l'utilisation de sources de lave est interdite.");
            event.getPlayer().getWorld().getBlockAt(event.getBlockClicked().getLocation().add(event.getBlockFace().getModX(), event.getBlockFace().getModY(), event.getBlockFace().getModZ())).setType(Material.AIR);
            event.getPlayer().getInventory().getItemInHand().setType(Material.LAVA_BUCKET);
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSignChange(SignChangeEvent event) {
        for (int i = 0; i < 4; i++) {
            if (event.getLine(i).matches("^[a-zA-Z0-9ÀÁÂÄÇÈÉÊËÎÍÎÏÒÓÕÖÛÛÛÛàáàäçèéëäîïôöûü &]*$") && event.getLine(i).length() > 20)
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.EATING || event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED)
            event.setCancelled(true);
    }

    private int getRandInt(int max, int min) {
        return this.random.nextInt(max - min + 1) + min;
    }

    @EventHandler
    private void onCommand(PlayerCommandPreprocessEvent event) {
        String msgToLowerCase = event.getMessage().toLowerCase();
        if (!GeneralRules.PRIVATEMSG.isEnabled())
            switch (msgToLowerCase) {
                case "/msg":
                case "/m":
                case "/t":
                case "/tell":
                case "/w":
                case "/whisper":
                    event.setCancelled(true);
                    event.getPlayer().sendMessage("§cLes messages privés sont désactivés.");
                    break;
            }
    }

    @EventHandler
    public void PlayerTeleportEvent(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
            int damage = this.gameConfig.getEnderpearlDamage();
            event.setCancelled(true);
            player.teleport(event.getTo());
            if (damage > 0)
                player.damage(damage);
        }
    }
}
