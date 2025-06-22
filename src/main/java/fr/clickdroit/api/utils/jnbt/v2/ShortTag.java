package fr.clickdroit.api.utils.jnbt.v2;

public class ShortTag extends Tag {
    private final short value;

    public ShortTag(short value) {
        this.value = value;
    }

    public Short getValue() {
        return Short.valueOf(this.value);
    }

    public String toString() {
        return "TAG_Short(" + this.value + ")";
    }
}
