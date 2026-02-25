package fr.clickdroit.api.utils.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ItemCreator {
    private static final Logger LOGGER = Logger.getLogger(ItemCreator.class.getName());
    
    private ItemStack item;
    private Player possesseur;
    private String creator_name;
    private ArrayList<String> tag;
    private int slot;
    private ArrayList<Pattern> patterns;

    // Constructeurs
    public ItemCreator(Material material) {
        this.item = new ItemStack(material);
    }

    public ItemCreator(ItemStack item) {
        this.item = new ItemStack(item);
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName()) {
                setName(meta.getDisplayName());
            }
            if (meta.hasLore()) {
                setLores(meta.getLore());
            }
        }
    }

    public ItemCreator(ItemCreator itemcreator) {
        this.item = new ItemStack(itemcreator.getItem());
        this.possesseur = itemcreator.getPossesseur();
        this.creator_name = itemcreator.getCreator_name();
        if (itemcreator.getTag() != null) {
            this.tag = new ArrayList<>(itemcreator.getTag());
        }
    }

    public ItemCreator(String itemcreatorstring) {
        this.item = new ItemStack(Material.STONE);
        fromString(itemcreatorstring);
    }

    // Méthodes de base
    public ItemStack getItem() {
        return this.item;
    }

    public Material getMaterial() {
        return this.item.getType();
    }

    public ItemCreator setMaterial(Material material) {
        if (this.item == null) {
            this.item = new ItemStack(material);
        } else {
            this.item.setType(material);
        }
        return this;
    }

    public Integer getAmount() {
        return this.item.getAmount();
    }

    public ItemCreator setAmount(Integer amount) {
        this.item.setAmount(amount);
        return this;
    }

    public Short getDurability() {
        return this.item.getDurability();
    }

    public ItemCreator setDurability(Short durability) {
        this.item.setDurability(durability);
        return this;
    }

    public ItemCreator setDurability(Integer durability) {
        this.item.setDurability(durability.shortValue());
        return this;
    }

    public Integer getDurabilityInteger() {
        return (int) this.item.getDurability();
    }

    // Méthodes pour les métadonnées
    public ItemMeta getMeta() {
        return this.item.getItemMeta();
    }

    public String getName() {
        ItemMeta meta = this.item.getItemMeta();
        return meta != null ? meta.getDisplayName() : null;
    }

    public ItemCreator setName(String name) {
        ItemMeta meta = this.item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            this.item.setItemMeta(meta);
        }
        return this;
    }

    public List<String> getLores() {
        ItemMeta meta = this.item.getItemMeta();
        return meta != null ? meta.getLore() : null;
    }

    public ItemCreator setLores(List<String> lores) {
        ItemMeta meta = this.item.getItemMeta();
        if (meta != null) {
            meta.setLore(lores);
            this.item.setItemMeta(meta);
        }
        return this;
    }

    public ItemCreator setTableauLores(String[] lores) {
        return setLores(Arrays.asList(lores));
    }

    public ItemCreator addLore(String lore) {
        ItemMeta meta = this.item.getItemMeta();
        if (meta != null) {
            List<String> lores = meta.getLore();
            if (lores == null) {
                lores = new ArrayList<>();
            }
            lores.add(lore);
            meta.setLore(lores);
            this.item.setItemMeta(meta);
        }
        return this;
    }

    // Méthodes pour les enchantements
    public Map<Enchantment, Integer> getEnchantments() {
        return this.item.getEnchantments();
    }

    public ItemCreator setEnchantments(Map<Enchantment, Integer> enchantments) {
        this.item.getEnchantments().clear();
        this.item.addUnsafeEnchantments(enchantments);
        return this;
    }

    public ItemCreator addEnchantment(Enchantment enchantment, Integer level) {
        this.item.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    // Méthodes pour les ItemFlags
    public ItemCreator addItemFlags(ItemFlag... flags) {
        ItemMeta meta = this.item.getItemMeta();
        if (meta != null) {
            meta.addItemFlags(flags);
            this.item.setItemMeta(meta);
        }
        return this;
    }

    public List<ItemFlag> getItemFlags() {
        ItemMeta meta = this.item.getItemMeta();
        return meta != null ? new ArrayList<>(meta.getItemFlags()) : null;
    }

    public ItemCreator addallItemsflags() {
        ItemMeta meta = this.item.getItemMeta();
        if (meta != null) {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            this.item.setItemMeta(meta);
        }
        return this;
    }

    /**
     * Ajoute un effet de brillance (glow) à l'item.
     * Utilise un enchantement caché pour créer l'effet visuel.
     *
     * @return this pour le chaînage
     */
    public ItemCreator addGlowEffect() {
        ItemMeta meta = this.item.getItemMeta();
        if (meta != null) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            this.item.setItemMeta(meta);
        }
        return this;
    }

    // Méthodes pour les têtes de joueur
    public ItemCreator setSkull(String textureValue) {
        if (this.item.getType() == Material.SKULL_ITEM) {
            SkullMeta skullMeta = (SkullMeta) this.item.getItemMeta();
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "clickdroit_heads");
            gameProfile.getProperties().put("textures", new Property("textures", textureValue));

            try {
                Field profileField = skullMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(skullMeta, gameProfile);
                this.item.setItemMeta(skullMeta);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to set skull texture", e);
            }
        }
        return this;
    }

    /**
     * Crée une valeur de texture encodée en Base64 à partir d'une URL.
     *
     * @param url l'URL de la texture de la tête
     * @return la valeur de texture encodée en Base64
     */
    private String createTextureValue(String url) {
        String json = "{\"textures\":{\"SKIN\":{\"url\":\"" + url + "\"}}}";
        return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Définit la texture de la tête à partir d'une URL.
     *
     * @param url l'URL de la texture de la tête
     * @return this pour le chaînage
     */
    public ItemCreator setSkullURL(String url) {
        if (this.item.getType() == Material.SKULL_ITEM) {
            SkullMeta skullMeta = (SkullMeta) this.item.getItemMeta();
            String textureValue = createTextureValue(url);

            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "clickdroit_heads");
            gameProfile.getProperties().put("textures", new Property("textures", textureValue));

            try {
                Field profileField = skullMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(skullMeta, gameProfile);
                this.item.setItemMeta(skullMeta);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to set skull texture from URL", e);
            }
        }
        return this;
    }

    public String getOwner() {
        if (this.item.getType() == Material.SKULL_ITEM) {
            SkullMeta skullMeta = (SkullMeta) this.item.getItemMeta();
            return skullMeta != null ? skullMeta.getOwner() : null;
        }
        return null;
    }

    public ItemCreator setOwner(String owner) {
        if (this.item.getType() == Material.SKULL_ITEM) {
            SkullMeta skullMeta = (SkullMeta) this.item.getItemMeta();
            if (skullMeta != null) {
                skullMeta.setOwner(owner);
                this.item.setItemMeta(skullMeta);
            }
        }
        return this;
    }

    // Méthodes pour les bannières
    public DyeColor getBasecolor() {
        if (this.item.getType() == Material.BANNER) {
            BannerMeta bannerMeta = (BannerMeta) this.item.getItemMeta();
            return bannerMeta != null ? bannerMeta.getBaseColor() : null;
        }
        return null;
    }

    public ItemCreator setBasecolor(DyeColor color) {
        if (this.item.getType() == Material.BANNER) {
            BannerMeta bannerMeta = (BannerMeta) this.item.getItemMeta();
            if (bannerMeta != null) {
                bannerMeta.setBaseColor(color);
                this.item.setItemMeta(bannerMeta);
            }
        }
        return this;
    }

    // Méthodes pour les presets de bannière
    public ItemCreator addBannerPreset(BannerPreset preset, DyeColor color) {
        if (this.item.getType() == Material.BANNER) {
            Pattern[] patterns = preset.getPatterns(color);
            for (Pattern pattern : patterns) {
                addPattern(pattern);
            }
        }
        return this;
    }

    public ItemCreator addBannerPreset(Integer ID, DyeColor patterncolor) {
        switch (ID) {
            case 1:
                addBannerPreset(BannerPreset.barre, patterncolor);
                break;
            case 2:
                addBannerPreset(BannerPreset.precedent, patterncolor);
                break;
            case 3:
                addBannerPreset(BannerPreset.suivant, patterncolor);
                break;
            case 4:
                addBannerPreset(BannerPreset.coeur, patterncolor);
                break;
            case 5:
                addBannerPreset(BannerPreset.croix, patterncolor);
                break;
        }
        return this;
    }

    public List<Pattern> getPatterns() {
        if (this.item.getType() == Material.BANNER) {
            BannerMeta bannerMeta = (BannerMeta) this.item.getItemMeta();
            return bannerMeta != null ? bannerMeta.getPatterns() : null;
        }
        return null;
    }

    public ItemCreator addPattern(Pattern pattern) {
        if (this.item.getType() == Material.BANNER) {
            BannerMeta bannerMeta = (BannerMeta) this.item.getItemMeta();
            if (bannerMeta != null) {
                bannerMeta.addPattern(pattern);
                this.item.setItemMeta(bannerMeta);
            }
        }
        return this;
    }

    public ItemCreator setTableauPatterns(Pattern[] patterns) {
        if (this.item.getType() == Material.BANNER) {
            BannerMeta bannerMeta = (BannerMeta) this.item.getItemMeta();
            if (bannerMeta != null) {
                bannerMeta.setPatterns(Arrays.asList(patterns));
                this.item.setItemMeta(bannerMeta);
            }
        }
        return this;
    }

    // Méthodes pour les livres enchantés
    public HashMap<Enchantment, Integer> getStoredEnchantments() {
        if (this.item.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) this.item.getItemMeta();
            return meta != null ? new HashMap<>(meta.getStoredEnchants()) : null;
        }
        return null;
    }

    public ItemCreator setStoredEnchantments(HashMap<Enchantment, Integer> storedenchantments) {
        if (this.item.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) this.item.getItemMeta();
            if (meta != null) {
                // Nettoyer les enchantements existants
                for (Enchantment ench : new ArrayList<>(meta.getStoredEnchants().keySet())) {
                    meta.removeStoredEnchant(ench);
                }
                // Ajouter les nouveaux
                for (Map.Entry<Enchantment, Integer> entry : storedenchantments.entrySet()) {
                    meta.addStoredEnchant(entry.getKey(), entry.getValue(), true);
                }
                this.item.setItemMeta(meta);
            }
        }
        return this;
    }

    public ItemCreator addStoredEnchantment(Enchantment enchantment, Integer level) {
        if (this.item.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) this.item.getItemMeta();
            if (meta != null) {
                meta.addStoredEnchant(enchantment, level, true);
                this.item.setItemMeta(meta);
            }
        }
        return this;
    }

    // Méthodes utilitaires
    public ItemCreator setUnbreakable(Boolean unbreakable) {
        ItemMeta meta = this.item.getItemMeta();
        if (meta != null) {
            meta.spigot().setUnbreakable(unbreakable);
            this.item.setItemMeta(meta);
        }
        return this;
    }

    // Propriétés personnalisées
    public Player getPossesseur() {
        return this.possesseur;
    }

    public ItemCreator setPossesseur(Player possesseur) {
        this.possesseur = possesseur;
        return this;
    }

    public String getCreator_name() {
        return this.creator_name;
    }

    public ItemCreator setCreator_name(String creator_name) {
        this.creator_name = creator_name;
        return this;
    }

    public ArrayList<String> getTag() {
        return this.tag;
    }

    public ItemCreator setTag(ArrayList<String> tag) {
        this.tag = tag;
        return this;
    }

    public int getSlot() {
        return this.slot;
    }

    public ItemCreator setSlot(int slot) {
        this.slot = slot;
        return this;
    }

    // Méthodes de comparaison
    public enum ComparatorType {
        Material, Amount, Durability, Name, Lores, Enchantements,
        ItemsFlags, Owner, BaseColor, Patterns, StoredEnchantements,
        Possesseur, Creator_name, Tag
    }

    public Boolean comparate(ItemCreator item, ComparatorType type) {
        switch (type) {
            case Material:
                return getMaterial() == item.getMaterial();
            case Amount:
                return getAmount().equals(item.getAmount());
            case Durability:
                return getDurability().equals(item.getDurability());
            case Name:
                return (getName() == null && item.getName() == null) ||
                        (getName() != null && getName().equals(item.getName()));
            case Lores:
                return (getLores() == null && item.getLores() == null) ||
                        (getLores() != null && getLores().equals(item.getLores()));
            case Enchantements:
                return (getEnchantments() == null && item.getEnchantments() == null) ||
                        (getEnchantments() != null && getEnchantments().equals(item.getEnchantments()));
            case Owner:
                return (getOwner() == null && item.getOwner() == null) ||
                        (getOwner() != null && getOwner().equals(item.getOwner()));
            case Possesseur:
                return (getPossesseur() == null && item.getPossesseur() == null) ||
                        (getPossesseur() != null && getPossesseur().equals(item.getPossesseur()));
            default:
                return false;
        }
    }

    // Sérialisation
    @Override
    public String toString() {
        StringBuilder itemcreator = new StringBuilder();
        itemcreator.append("ItemCreator:{");
        itemcreator.append("Slot:{").append(this.slot).append("}");
        itemcreator.append(",Type:{").append(getMaterial().toString()).append("}");
        itemcreator.append(",Amount:{").append(getAmount()).append("}");
        itemcreator.append(",Durability:{").append(getDurability()).append("}");

        if (getName() != null) {
            itemcreator.append(",Name:{").append(getName()).append("}");
        }

        if (getLores() != null) {
            itemcreator.append(",Lores:{");
            for (String lore : getLores()) {
                itemcreator.append("['").append(lore).append("'],");
            }
            if (!getLores().isEmpty()) {
                itemcreator.setLength(itemcreator.length() - 1); // Enlever la dernière virgule
            }
            itemcreator.append("}");
        }

        if (getPossesseur() != null) {
            itemcreator.append(",Possesseur:{").append(getPossesseur().getUniqueId().toString()).append("}");
        }

        if (getCreator_name() != null) {
            itemcreator.append(",Creator_name:{").append(getCreator_name()).append("}");
        }

        if (getTag() != null) {
            itemcreator.append(",Tag:{");
            for (String tag : getTag()) {
                itemcreator.append("[").append(tag).append("],");
            }
            if (!getTag().isEmpty()) {
                itemcreator.setLength(itemcreator.length() - 1); // Enlever la dernière virgule
            }
            itemcreator.append("}");
        }

        itemcreator.append("}");
        return itemcreator.toString();
    }

    public ItemCreator fromString(String itemcreatorstring) {
        // Implémentation simplifiée de la désérialisation
        // Cette méthode nécessiterait une logique de parsing plus robuste
        // pour l'instant, on retourne this pour éviter les erreurs
        return this;
    }

    // Classe interne pour les comparaisons (si nécessaire)
    private class Comparaison {
        private ItemCreator parent;

        public Comparaison(ItemCreator parent) {
            this.parent = parent;
        }

        public Boolean islistequal(List<?> list1, List<?> list2) {
            if (list1 == null && list2 == null) return true;
            if (list1 == null || list2 == null) return false;
            return list1.equals(list2);
        }

        public Boolean ismapequal(Map<?, ?> map1, Map<?, ?> map2) {
            if (map1 == null && map2 == null) return true;
            if (map1 == null || map2 == null) return false;
            return map1.equals(map2);
        }
    }
}
