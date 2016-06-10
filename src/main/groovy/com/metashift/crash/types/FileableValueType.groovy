package com.metashift.crash.types
import com.metashift.context.Fileable
import com.metashift.crash.completers.FileableCompleter
import org.crsh.cli.type.ValueType
/**
 * Created by navid on 2/18/15.
 */
class FileableValueType extends ValueType<Fileable>{

    public FileableValueType() {
        super(Fileable.class, FileableCompleter.class);
    }

    @Override
    public <S extends Fileable> S parse(Class<S> type, String s) throws Exception {
        return type.cast(new Fileable(s))
    }

}
