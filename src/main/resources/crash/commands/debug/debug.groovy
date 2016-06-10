package crash.commands.debug

import com.metashift.metadata.model.IClassMetadata
import com.metashift.metadata.model.IMetadataReader
import com.metashift.config.ServiceLocator
import com.metashift.crash.BetterBootstrap
import com.metashift.crash.ObservablePipeBuilder
import org.crsh.cli.Command
import org.crsh.cli.Usage
import org.crsh.command.Pipe
import org.springframework.core.io.Resource
import rx.functions.Action1

/**
 * Created by navid on 2/22/15.
 */
class debug{
    
    @Usage('Lists all command mount points')
    @Command
    void listCmdMounts(){
        BetterBootstrap bootstrap = ServiceLocator.locateCurrent(BetterBootstrap.class)
        out << "Listing all mounts for commands:\n" << magenta
        for (String mount : bootstrap.getCmdMounts()) {
            out << mount << "\n"
        }
        out << reset << " "
        out.flush()
    }

    @Usage("Logs all objects from the pipe and sends back out the pipe unchanged")
    @Command
    public Pipe<Object,Object> tracePipe() {

        def builder = new ObservablePipeBuilder<Object,Object>()
        builder.forEach({ Object obj ->
            if (obj) {
                out << blue << obj.class.name << reset << "\n"
                out << green << "\t" << obj.toString() << reset << "\n"
            }else{
                out << red << "NULL Object detected in pipe" << reset << "\n"
            }
            out.flush()
            obj
        } as Action1)

        builder.build(true)
    }

    @Usage("Logs filename for Resource objects from the pipe and sends back out the pipe unchanged")
    @Command
    public Pipe<Resource,Resource> traceResource() {
        def builder = new ObservablePipeBuilder<>()
        builder.forEach({ Resource res ->
            out << res?.filename <<"\n"
            out.flush()
            res
        } as Action1)

        builder.build(true)
    }

    @Usage("Logs meta readers from pipe and sends them out unchanged")
    @Command
    public Pipe<IMetadataReader,IMetadataReader> traceMetaReader() {

        def builder = new ObservablePipeBuilder<IMetadataReader,IMetadataReader>()
        builder.forEach({ IMetadataReader obj ->
            try {
                IClassMetadata meta = obj?.getClassMetadata();
                out << (meta.isInterface() ? blue : green) << meta.getClazzName() << reset <<"\n"

            } catch (RuntimeException e) {
                out << red << "Exception Occurred " << e.getMessage() << reset << "\n"
            }
            out.flush()

            obj
        } as Action1)

        builder.build(true)
    }
    
}