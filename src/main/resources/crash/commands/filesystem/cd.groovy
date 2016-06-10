package crash.commands.filesystem
import com.metashift.config.ServiceLocator
import com.metashift.context.FileableContext
import com.metashift.context.Fileable
import org.crsh.cli.Argument
import org.crsh.cli.Command
import org.crsh.cli.Required
import org.crsh.cli.Usage
import org.crsh.command.BaseCommand
/**
 * Created by navid on 12/19/14.
 */
class cd extends BaseCommand{

    @Usage("Changes the current shell context directory to the given directory")
    @Command
    void main(@Usage("the directory to make current")
              @Required
              @Argument Fileable path) {
        FileableContext context = ServiceLocator.locateCurrent(FileableContext.class)
        context.currentDirectory(context.withCurrent(path))

    }


}
