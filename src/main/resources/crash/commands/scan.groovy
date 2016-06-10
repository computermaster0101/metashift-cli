package crash.commands
import com.metashift.metadata.MetadataReaderFactory
import com.metashift.metadata.model.IMetadataReader
import com.metashift.config.ServiceLocator
import com.metashift.context.ResourceReferenceContext
import com.metashift.crash.PipesRx
import com.metashift.context.ResourceReference
import org.crsh.cli.Argument
import org.crsh.cli.Command
import org.crsh.cli.Required
import org.crsh.cli.Usage
import org.crsh.command.BaseCommand
import org.crsh.command.Pipe
import org.springframework.core.io.Resource
import rx.Observable
/**
 * Created by navid on 12/19/14.
 */
@Usage("Scan various types of objects and produces metadata that will be sent to the stream")
class scan extends BaseCommand{

    private MetadataReaderFactory metaReader = new MetadataReaderFactory()


    /**
     * TODO: Add man doc with reference to spring resources style info
     * http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/core/io/support/PathMatchingResourcePatternResolver.html
     *
     * @param path
     * @param context
     */
    @Usage("Scan a resources location for all classes and return output a IMetedataReader for each one to the stream")
    @Command
    Pipe<Void, IMetadataReader> metaReaders(@Usage("Resolve the given location pattern into IMetadataReader objects.")
                                           @Required
                                           @Argument String location) {
        return PipesRx.subscriber(metaReader.metadataReaders(location))
    }

    /**
     * TODO: Add man doc with reference to spring resources style info
     * http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/core/io/support/PathMatchingResourcePatternResolver.html
     *
     * @param path
     * @param context
     */
    @Usage("Scan a resources location for all classes and return output a IMetedataReader for each one to the stream")
    @Command
    Pipe<Void, Resource> resources(@Usage("Resolve the given location pattern into Resource objects.")
                                   @Required
                                   @Argument ResourceReference resourceReference) {

        ResourceReferenceContext referenceContext = ServiceLocator.locateCurrent(ResourceReferenceContext.class)

        return PipesRx.subscriber(Observable.from(referenceContext.resources(resourceReference.locationPattern)))
    }



}
