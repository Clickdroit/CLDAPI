package fr.clickdroit.api.utils.jnbt.v2;

public class ByteTag extends Tag {
    private final byte value;

    public ByteTag(byte value) {
        this.value = value;
    }

    public Byte getValue() {
        return Byte.valueOf(this.value);
    }

    public String toString() {
        return "TAG_Byte(" + this.value + ")";
    }
}
