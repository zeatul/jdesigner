package glz.hawk.jdesigner.translator.database;

import glz.hawk.jdesigner.spec.database.Column;
import glz.hawk.jdesigner.translator.Translator;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

public class ColumnToComment implements Translator<Column, Optional<String>> {

    @Override
    public @NonNull Optional<String> translate(@NonNull Column column) {
        return column.getComment();
    }
}
