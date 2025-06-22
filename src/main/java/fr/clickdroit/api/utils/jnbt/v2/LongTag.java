package fr.clickdroit.api.utils.jnbt.v2;


public class LongTag extends Tag {
    private final long value;

    public LongTag(long value) {
        this.value = value;
    }

    public Long getValue() {
        return Long.valueOf(this.value);
    }

    public String toString() {
        return "TAG_Long(" + this.value + ")";
    }
}
