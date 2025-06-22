package fr.clickdroit.api.utils.jnbt.v2;

public class IntTag extends Tag {
    private int value;

    public IntTag(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return Integer.valueOf(this.value);
    }

    public String toString() {
        return "TAG_Int(" + this.value + ")";
    }
}
