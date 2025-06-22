package fr.clickdroit.api.utils.item;

public enum ComparatorType {
    Material(0),
    Amount(1),
    Durability(2),
    Name(3),
    Lores(4),
    Enchantements(5),
    ItemsFlags(6),
    Owner(7),
    BaseColor(8),
    Patterns(9),
    StoredEnchantements(10),
    Possesseur(11),
    Creator_name(12),
    Tag(13);

    private final int id;

    ComparatorType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}