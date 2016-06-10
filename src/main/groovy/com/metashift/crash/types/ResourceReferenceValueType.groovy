package com.metashift.crash.types
import com.metashift.context.ResourceReference
import com.metashift.crash.completers.ResourceReferenceCompleter
import org.crsh.cli.type.ValueType
/**
 * Created by navid on 2/18/15.
 */
class ResourceReferenceValueType extends ValueType<ResourceReference>{

    public ResourceReferenceValueType() {
        super(ResourceReference.class, ResourceReferenceCompleter.class);
    }

    @Override
    public <S extends ResourceReference> S parse(Class<S> type, String s) throws Exception {
        return type.cast(new ResourceReference(s))
    }

}
