package glz.hawk.jdesigner.translator.database;

import glz.hawk.jdesigner.spec.database.Column;
import glz.hawk.jdesigner.translator.Translator;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Locale;
import java.util.Optional;

import static glz.hawkframework.core.support.ArgumentSupport.argNotNull;

public class ColumnToComment implements Translator<Column, Optional<String>> {

    protected final Locale defaultLocale;

    public ColumnToComment(Locale defaultLocale) {
        this.defaultLocale = argNotNull(defaultLocale,"defaultLocale");
    }


    @Override
    public @NonNull Optional<String> translate(@NonNull Column column) {
        return column.getComment(defaultLocale);
    }
}
