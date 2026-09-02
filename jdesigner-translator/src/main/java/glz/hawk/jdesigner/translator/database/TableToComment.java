package glz.hawk.jdesigner.translator.database;

import glz.hawk.jdesigner.spec.database.Table;
import glz.hawk.jdesigner.translator.Translator;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Locale;
import java.util.Optional;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

public class TableToComment implements Translator<Table, Optional<String>> {

    protected final Locale defaultLocale;

    public TableToComment(Locale defaultLocale) {
        this.defaultLocale = argNotNull(defaultLocale,"defaultLocale");
    }

    @Override
    public @NonNull Optional<String> translate(@NonNull Table table) {
        return table.getComment(defaultLocale);
    }
}
