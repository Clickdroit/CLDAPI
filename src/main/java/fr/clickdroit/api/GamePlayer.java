package fr.clickdroit.api;

import fr.clickdroit.api.game.combatlog.CombatLogEntity;
import fr.clickdroit.api.game.team.Teams;
import fr.clickdroit.api.utils.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Représente un joueur dans une partie UHC.
 * <p>
 * Cette classe encapsule toutes les informations spécifiques à un joueur
 * pendant une partie UHC, incluant :
 * <ul>
 * <li>Son statut (vivant/mort)</li>
 * <li>Son équipe</li>
 * <li>Ses statistiques (kills, diamants, ors)</li>
 * <li>Son inventaire sauvegardé</li>
 * <li>Son entité de combat log</li>
 * </ul>
 * 
 * <p>
 * Les instances sont gérées via une liste statique accessible par
 * {@link #getGamePlayers()} et {@link #getPlayer(UUID)}.
 * </p>
 * 
 * @author Clickdroit
 * @version 1.0
 * @see Teams
 * @see CombatLogEntity
 */
public class GamePlayer {
    private final UUID uuid;

    private final String name;

    private boolean isAlive;

    private Teams teams;

    private int kills;

    private int diamonds;

    private int golds;

    private boolean editing;

    private boolean editingDeathInv;

    private boolean invincible;

    private boolean alerts;

    private long lastFight;

    private CombatLogEntity combatLogEntity;

    private Location lastLocation;

    private ItemStack[] playerInv;

    private ItemStack[] playerArmor;

    private int playerExp;

    private ArrayList<PotionEffect> potionEffects;

    private Player lastAnswer;

    private int invincibilityCount;

    private int invincibilityNoFallCount;

    private boolean op;

    private boolean headstaff;

    public GamePlayer(Player player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.isAlive = true;
        this.teams = null;
        this.kills = 0;
        this.diamonds = 0;
        this.golds = 0;
        this.editing = false;
        this.editingDeathInv = false;
        this.invincible = false;
        this.playerInv = new ItemStack[0];
        this.playerArmor = new ItemStack[0];
        this.playerExp = 0;
        this.lastLocation = player.getLocation();
        this.potionEffects = new ArrayList<>();
        this.alerts = false;
        this.lastFight = -1L;
        this.invincibilityCount = 0;
        this.invincibilityNoFallCount = 0;
        addPlayer(this);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public boolean isAlive() {
        return this.isAlive;
    }

    public void setAlive(boolean alive) {
        this.isAlive = alive;
    }

    public int getKills() {
        return this.kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void addKill() {
        this.kills++;
    }

    public int getDiamonds() {
        return this.diamonds;
    }

    public void addDiamonds() {
        this.diamonds++;
        Title.sendActionBar(getPlayer(), "§f[§b*§f] §bLimite de diamants: §f" + this.diamonds + "§f/§f"
                + API.getAPI().getGameManager().getGameConfig().getDiamondMax());
    }

    public int getGolds() {
        return this.golds;
    }

    public void addGolds() {
        this.golds++;
    }

    public boolean isEditing() {
        return this.editing;
    }

    public void setEditing(boolean editing) {
        this.editing = editing;
    }

    public boolean isEditingDeathInv() {
        return this.editingDeathInv;
    }

    public void setEditingDeathInv(boolean editingDeathInv) {
        this.editingDeathInv = editingDeathInv;
    }

    public boolean isInvincible() {
        return this.invincible;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    public ItemStack[] getPlayerInv() {
        return this.playerInv;
    }

    public void setPlayerInv(ItemStack[] playerInv) {
        this.playerInv = playerInv;
    }

    public ItemStack[] getPlayerArmor() {
        return this.playerArmor;
    }

    public void setPlayerArmor(ItemStack[] playerArmor) {
        this.playerArmor = playerArmor;
    }

    public int getPlayerExp() {
        return this.playerExp;
    }

    public void setPlayerExp(int playerExp) {
        this.playerExp = playerExp;
    }

    public Location getLastLocation() {
        return this.lastLocation;
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    public ArrayList<PotionEffect> getPotionEffects() {
        return this.potionEffects;
    }

    public Player getLastAnswer() {
        return this.lastAnswer;
    }

    public void setLastAnswer(Player lastAnswer) {
        this.lastAnswer = lastAnswer;
    }

    public boolean isAlerts() {
        return this.alerts;
    }

    public void setAlerts(boolean alerts) {
        this.alerts = alerts;
    }

    public Teams getTeams() {
        return this.teams;
    }

    public void setTeams(Teams teams) {
        this.teams = teams;
    }

    public long getLastFight() {
        return this.lastFight;
    }

    public void setLastFight(long lastFight) {
        this.lastFight = lastFight;
    }

    public int getInvincibilityCount() {
        return this.invincibilityCount;
    }

    public void setInvincibilityCount(int invincibilityCount) {
        this.invincibilityCount = invincibilityCount;
    }

    public void addInvincibilityCount(int count) {
        this.invincibilityCount += count;
    }

    public void removeInvincibilityCount() {
        this.invincibilityCount--;
        if (this.invincibilityCount < 0)
            this.invincibilityCount = 0;
    }

    public int getInvincibilityNoFallCount() {
        return this.invincibilityNoFallCount;
    }

    public void setInvincibilityNoFallCount(int invincibilityNoFallCount) {
        this.invincibilityNoFallCount = invincibilityNoFallCount;
    }

    public void addInvincibilityNoFallCount(int count) {
        this.invincibilityNoFallCount += count;
    }

    public void removeInvincibilityNoFallCount() {
        this.invincibilityNoFallCount--;
        if (this.invincibilityNoFallCount < 0)
            this.invincibilityNoFallCount = 0;
    }

    public CombatLogEntity getCombatLogEntity() {
        return this.combatLogEntity;
    }

    public void setCombatLogEntity(CombatLogEntity combatLogEntity) {
        this.combatLogEntity = combatLogEntity;
    }

    private static List<GamePlayer> gamePlayers = new ArrayList<>();

    public static List<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public static GamePlayer getPlayer(UUID uuid) {
        GamePlayer player = null;
        for (GamePlayer gamePlayer : gamePlayers) {
            if (gamePlayer.getUuid().equals(uuid))
                player = gamePlayer;
        }
        return player;
    }

    public static GamePlayer getPlayer(String name) {
        GamePlayer player = null;
        for (GamePlayer gamePlayer : gamePlayers) {
            if (gamePlayer.getName().equals(name))
                player = gamePlayer;
        }
        return player;
    }

    public static void addPlayer(GamePlayer up) {
        gamePlayers.add(up);
    }

    public static void removePlayer(UUID uuid) {
        gamePlayers.removeIf(p -> p.getUuid().equals(uuid));
    }

    public static boolean havePlayer(UUID uuid) {
        for (GamePlayer gamePlayer : gamePlayers) {
            if (gamePlayer.getUuid().equals(uuid))
                return true;
        }
        return false;
    }

    public boolean isOp() {
        return this.op;
    }

    public boolean isHeadStaff() {
        return this.headstaff;
    }
}
