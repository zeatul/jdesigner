package glz.hawk.jdesigner.translator.database;

import glz.hawk.jdesigner.spec.database.Table;
import glz.hawk.jdesigner.translator.Translator;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

public class TableToComment implements Translator<Table, Optional<String>> {
    @Override
    public @NonNull Optional<String> translate(@NonNull Table table) {
        return table.getComment();
    }
}
