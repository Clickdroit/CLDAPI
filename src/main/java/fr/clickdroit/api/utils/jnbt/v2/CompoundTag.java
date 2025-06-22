package fr.clickdroit.api.utils.jnbt.v2;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompoundTag extends Tag {
    private final Map<String, Tag> value;

    public CompoundTag(Map<String, Tag> value) {
        this.value = Collections.unmodifiableMap(value);
    }

    public boolean containsKey(String key) {
        return this.value.containsKey(key);
    }

    public Map<String, Tag> getValue() {
        return this.value;
    }

    public CompoundTag setValue(Map<String, Tag> value) {
        return new CompoundTag(value);
    }

    public CompoundTagBuilder createBuilder() {
        return new CompoundTagBuilder(new HashMap<>(this.value));
    }

    public byte[] getByteArray(String key) {
        Tag tag = this.value.get(key);
        if (tag instanceof ByteArrayTag)
            return ((ByteArrayTag)tag).getValue();
        return new byte[0];
    }

    public byte getByte(String key) {
        Tag tag = this.value.get(key);
        if (tag instanceof ByteTag)
            return ((ByteTag)tag).getValue().byteValue();
        return 0;
    }

    public double getDouble(String key) {
        Tag tag = this.value.get(key);
        if (tag instanceof DoubleTag)
            return ((DoubleTag)tag).getValue().doubleValue();
        return 0.0D;
    }

    public double asDouble(String key) {
        Tag tag = this.value.get(key);
        if (tag instanceof ByteTag)
            return ((ByteTag)tag).getValue().byteValue();
        if (tag instanceof ShortTag)
            return ((ShortTag)tag).getValue().shortValue();
        if (tag instanceof IntTag)
            return ((IntTag)tag).getValue().intValue();
        if (tag instanceof LongTag)
            return ((LongTag)tag).getValue().longValue();
        if (tag instanceof FloatTag)
            return ((FloatTag)tag).getValue().floatValue();
        if (tag instanceof DoubleTag)
            return ((DoubleTag)tag).getValue().doubleValue();
        return 0.0D;
    }

    public float getFloat(String key) {
        Tag tag = this.value.get(key);
        if (tag instanceof FloatTag)
            return ((FloatTag)tag).getValue().floatValue();
        return 0.0F;
    }

    public int[] getIntArray(String key) {
        Tag tag = this.value.get(key);
        if (tag instanceof IntArrayTag)
            return ((IntArrayTag)tag).getValue();
        return new int[0];
    }

    public int getInt(String key) {
        Tag tag = this.value.get(key);
        if (tag instanceof IntTag)
            return ((IntTag)tag).getValue().intValue();
        return 0;
    }

    public int asInt(String key) {
        Tag tag = this.value.get(key);
        if (tag instanceof ByteTag)
            return ((ByteTag)tag).getValue().byteValue();
        if (tag instanceof ShortTag)
            return ((ShortTag)tag).getValue().shortValue();
        if (tag instanceof IntTag)
            return ((IntTag)tag).getValue().intValue();
        if (tag instanceof LongTag)
            return ((LongTag)tag).getValue().intValue();
        if (tag instanceof FloatTag)
            return ((FloatTag)tag).getValue().intValue();
        if (tag instanceof DoubleTag)
            return ((DoubleTag)tag).getValue().intValue();
        return 0;
    }

    public List<Tag> getList(String key) {
        Tag tag = this.value.get(key);
        if (tag instanceof ListTag)
            return ((ListTag)tag).getValue();
        return Collections.emptyList();
    }

    public ListTag getListTag(String key) {
        Tag tag = this.value.get(key);
        if (tag instanceof ListTag)
            return (ListTag)tag;
        return new ListTag((Class)StringTag.class, Collections.emptyList());
    }

    public <T extends Tag> List<T> getList(String key, Class<T> listType) {
        Tag tag = this.value.get(key);
        if (!(tag instanceof ListTag))
            return Collections.emptyList();
        ListTag listTag = (ListTag)tag;
        if (listTag.getType().equals(listType))
            return (List)listTag.getValue();
        return Collections.emptyList();
    }

    public long getLong(String key) {
        Tag tag = this.value.get(key);
        if (tag instanceof LongTag)
            return ((LongTag)tag).getValue().longValue();
        return 0L;
    }

    public long asLong(String key) {
        Tag tag = this.value.get(key);
        if (tag instanceof ByteTag)
            return ((ByteTag)tag).getValue().byteValue();
        if (tag instanceof ShortTag)
            return ((ShortTag)tag).getValue().shortValue();
        if (tag instanceof IntTag)
            return ((IntTag)tag).getValue().intValue();
        if (tag instanceof LongTag)
            return ((LongTag)tag).getValue().longValue();
        if (tag instanceof FloatTag)
            return ((FloatTag)tag).getValue().longValue();
        if (tag instanceof DoubleTag)
            return ((DoubleTag)tag).getValue().longValue();
        return 0L;
    }

    public short getShort(String key) {
        Tag tag = this.value.get(key);
        if (tag instanceof ShortTag)
            return ((ShortTag)tag).getValue().shortValue();
        return 0;
    }

    public String getString(String key) {
        Tag tag = this.value.get(key);
        if (tag instanceof StringTag)
            return ((StringTag)tag).getValue();
        return "";
    }

    public String toString() {
        StringBuilder bldr = new StringBuilder();
        bldr.append("TAG_Compound").append(": ").append(this.value.size()).append(" entries\r\n{\r\n");
        for (Map.Entry<String, Tag> entry : this.value.entrySet())
            bldr.append("   ").append(((Tag)entry.getValue()).toString().replaceAll("\r\n", "\r\n   ")).append("\r\n");
        bldr.append("}");
        return bldr.toString();
    }
}
