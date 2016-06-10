package com.metashift.crash.types

import com.metashift.context.MetaGraphReference
import com.metashift.crash.completers.MetaGraphReferenceCompleter
import org.crsh.cli.type.ValueType
/**
 * Created by navid on 2/27/15.
 */
class MetaGraphReferenceType extends ValueType<MetaGraphReference> {

    public MetaGraphReferenceType() {
        super(MetaGraphReference.class, MetaGraphReferenceCompleter.class);
    }

    @Override
    public <S extends MetaGraphReference> S parse(Class<S> type, String s) throws Exception {
        return type.cast(new MetaGraphReference(s))
    }

}

