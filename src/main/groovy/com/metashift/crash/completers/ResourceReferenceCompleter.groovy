package com.metashift.crash.completers
import com.metashift.config.ServiceLocator
import com.metashift.context.ResourceReferenceContext
import com.metashift.context.ResourceReference
import org.crsh.cli.descriptor.ParameterDescriptor
import org.crsh.cli.spi.Completer
import org.crsh.cli.spi.Completion
/**
 * Created by navid on 2/18/15.
 */
class ResourceReferenceCompleter implements Completer {

    private ResourceReferenceContext context;

    ResourceReferenceCompleter() {
        context = ServiceLocator.locateCurrent(ResourceReferenceContext.class)
    }

    @Override
    Completion complete(ParameterDescriptor parameter, String prefix) throws Exception {

        // if the current directory is still magic @ resource root then we must try to only work with its children
        if(context.isAbsolute(context.currentResourceReference())){

        }else{

        }

        return build(context.getChildren(context.withCurrent(new ResourceReference(prefix))),prefix)
    }

    private Completion build(Collection<ResourceReference> references,String prefix){
        Completion.Builder builder = Completion.builder(prefix);
        references.each {ResourceReference r ->
            builder.add(r.locationPattern,false)
        }
        builder.build()
    }
}
