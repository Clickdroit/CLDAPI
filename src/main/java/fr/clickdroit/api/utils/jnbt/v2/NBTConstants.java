package fr.clickdroit.api.utils.jnbt.v2;

import java.nio.charset.Charset;

public class NBTConstants {
    public static final Charset CHARSET = Charset.forName("UTF-8");

    public static final int TYPE_END = 0;

    public static final int TYPE_BYTE = 1;

    public static final int TYPE_SHORT = 2;

    public static final int TYPE_INT = 3;

    public static final int TYPE_LONG = 4;

    public static final int TYPE_FLOAT = 5;

    public static final int TYPE_DOUBLE = 6;

    public static final int TYPE_BYTE_ARRAY = 7;

    public static final int TYPE_STRING = 8;

    public static final int TYPE_LIST = 9;

    public static final int TYPE_COMPOUND = 10;

    public static final int TYPE_INT_ARRAY = 11;

    public static Class<? extends Tag> getClassFromType(int id) {
        switch (id) {
            case 0:
                return (Class)EndTag.class;
            case 1:
                return (Class)ByteTag.class;
            case 2:
                return (Class)ShortTag.class;
            case 3:
                return (Class)IntTag.class;
            case 4:
                return (Class)LongTag.class;
            case 5:
                return (Class)FloatTag.class;
            case 6:
                return (Class)DoubleTag.class;
            case 7:
                return (Class)ByteArrayTag.class;
            case 8:
                return (Class)StringTag.class;
            case 9:
                return (Class)ListTag.class;
            case 10:
                return (Class)CompoundTag.class;
            case 11:
                return (Class)IntArrayTag.class;
        }
        throw new IllegalArgumentException("Unknown tag type ID of " + id);
    }
}
