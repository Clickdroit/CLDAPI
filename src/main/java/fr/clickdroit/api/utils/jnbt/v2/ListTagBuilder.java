package fr.clickdroit.api.utils.jnbt.v2;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ListTagBuilder {
    private final Class<? extends Tag> type;

    private final List<Tag> entries;

    ListTagBuilder(Class<? extends Tag> type) {
        Preconditions.checkNotNull(type);
        this.type = type;
        this.entries = new ArrayList<>();
    }

    public ListTagBuilder add(Tag value) {
        Preconditions.checkNotNull(value);
        if (!this.type.isInstance(value))
            throw new IllegalArgumentException(String.valueOf(value.getClass().getCanonicalName()) + " is not of expected type " + this.type.getCanonicalName());
        this.entries.add(value);
        return this;
    }

    public ListTagBuilder addAll(Collection<? extends Tag> value) {
        Preconditions.checkNotNull(value);
        for (Tag v : value)
            add(v);
        return this;
    }

    public ListTag build() {
        return new ListTag(this.type, new ArrayList<>(this.entries));
    }

    public static ListTagBuilder create(Class<? extends Tag> type) {
        return new ListTagBuilder(type);
    }

    public static <T extends Tag> ListTagBuilder createWith(T... entries) {
        Preconditions.checkNotNull(entries);
        if (entries.length == 0)
            throw new IllegalArgumentException("This method needs an array of at least one entry");
        Class<? extends Tag> type = (Class)entries[0].getClass();
        for (int i = 1; i < entries.length; i++) {
            if (!type.isInstance(entries[i]))
                throw new IllegalArgumentException("An array of different tag types was provided");
        }
        ListTagBuilder builder = new ListTagBuilder(type);
        builder.addAll(Arrays.asList((Tag[])entries));
        return builder;
    }
}
