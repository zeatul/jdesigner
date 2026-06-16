package glz.hawk.bookstore.pub.temp;


import glz.hawkframework.core.helper.MapHelper;
import glz.hawkframework.core.support.StateSupport;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

public enum BookCategory {
    /**
     * 生物书
     */
    BIOLOGY("biology"),
    /**
     * 化学书
     */
    CHEMISTRY("chemistry"),
    /**
     * 历史书
     */
    HISTORY("history");


    final public String value;

    BookCategory(String value) {
        this.value = value;
    }

    private final static Map<String, BookCategory> valueMap = MapHelper.<String, BookCategory>builder()
        .put(BIOLOGY.value, BIOLOGY)
        .put(CHEMISTRY.value, CHEMISTRY)
        .put(HISTORY.value, HISTORY)
        .buildHashMap();

    public final static Map<String, BookCategory> nameMap = MapHelper.<String, BookCategory>builder()
        .put(BIOLOGY.name(), BIOLOGY)
        .buildHashMap();

    public static @Nullable BookCategory fromValue(@Nullable String value) {
        if (value == null) return null;
        return valueMap.get(value);
    }

    public static @Nullable BookCategory fromName(@Nullable String name) {
        if (name == null) return null;
        return nameMap.get(name);
    }

    public static @Nonnull BookCategory parseByValue(@Nonnull String value) {
        return StateSupport.stateNotNull(fromValue(argNotNull(value)), () -> String.format("Found no BookCategory with value %s", value));
    }

    public static @Nonnull BookCategory parseByName(@Nonnull String name) {
        return StateSupport.stateNotNull(fromValue(argNotNull(name)), () -> String.format("Found no BookCategory named %s", name));
    }

}
