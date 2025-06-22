package fr.clickdroit.api.utils.jnbt.v2;

import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;

public class ListTag extends Tag {
    private final Class<? extends Tag> type;

    private final List<Tag> value;

    public ListTag(Class<? extends Tag> type, List<? extends Tag> value) {
        Preconditions.checkNotNull(value);
        this.type = type;
        this.value = Collections.unmodifiableList(value);
    }

    public Class<? extends Tag> getType() {
        return this.type;
    }

    public List<Tag> getValue() {
        return this.value;
    }

    public ListTag setValue(List<Tag> list) {
        return new ListTag(getType(), list);
    }

    @Nullable
    public Tag getIfExists(int index) {
        try {
            return this.value.get(index);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public byte[] getByteArray(int index) {
        Tag tag = getIfExists(index);
        if (tag instanceof ByteArrayTag)
            return ((ByteArrayTag)tag).getValue();
        return new byte[0];
    }

    public byte getByte(int index) {
        Tag tag = getIfExists(index);
        if (tag instanceof ByteTag)
            return ((ByteTag)tag).getValue().byteValue();
        return 0;
    }

    public double getDouble(int index) {
        Tag tag = getIfExists(index);
        if (tag instanceof DoubleTag)
            return ((DoubleTag)tag).getValue().doubleValue();
        return 0.0D;
    }

    public double asDouble(int index) {
        Tag tag = getIfExists(index);
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

    public float getFloat(int index) {
        Tag tag = getIfExists(index);
        if (tag instanceof FloatTag)
            return ((FloatTag)tag).getValue().floatValue();
        return 0.0F;
    }

    public int[] getIntArray(int index) {
        Tag tag = getIfExists(index);
        if (tag instanceof IntArrayTag)
            return ((IntArrayTag)tag).getValue();
        return new int[0];
    }

    public int getInt(int index) {
        Tag tag = getIfExists(index);
        if (tag instanceof IntTag)
            return ((IntTag)tag).getValue().intValue();
        return 0;
    }

    public int asInt(int index) {
        Tag tag = getIfExists(index);
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

    public List<Tag> getList(int index) {
        Tag tag = getIfExists(index);
        if (tag instanceof ListTag)
            return ((ListTag)tag).getValue();
        return Collections.emptyList();
    }

    public ListTag getListTag(int index) {
        Tag tag = getIfExists(index);
        if (tag instanceof ListTag)
            return (ListTag)tag;
        return new ListTag((Class)StringTag.class, Collections.emptyList());
    }

    public <T extends Tag> List<T> getList(int index, Class<T> listType) {
        Tag tag = getIfExists(index);
        if (!(tag instanceof ListTag))
            return Collections.emptyList();
        ListTag listTag = (ListTag)tag;
        if (listTag.getType().equals(listType))
            return (List)listTag.getValue();
        return Collections.emptyList();
    }

    public long getLong(int index) {
        Tag tag = getIfExists(index);
        if (tag instanceof LongTag)
            return ((LongTag)tag).getValue().longValue();
        return 0L;
    }

    public long asLong(int index) {
        Tag tag = getIfExists(index);
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

    public short getShort(int index) {
        Tag tag = getIfExists(index);
        if (tag instanceof ShortTag)
            return ((ShortTag)tag).getValue().shortValue();
        return 0;
    }

    public String getString(int index) {
        Tag tag = getIfExists(index);
        if (tag instanceof StringTag)
            return ((StringTag)tag).getValue();
        return "";
    }

    public String toString() {
        StringBuilder bldr = new StringBuilder();
        bldr.append("TAG_List").append(": ").append(this.value.size()).append(" entries of type ").append(NBTUtils.getTypeName(this.type)).append("\r\n{\r\n");
        for (Tag t : this.value)
            bldr.append("   ").append(t.toString().replaceAll("\r\n", "\r\n   ")).append("\r\n");
        bldr.append("}");
        return bldr.toString();
    }
}
