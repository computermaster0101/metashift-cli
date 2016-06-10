package crash.commands.filesystem
import com.metashift.config.ServiceLocator
import com.metashift.crash.BetterBootstrap
import com.metashift.context.Fileable
import org.crsh.cli.Argument
import org.crsh.cli.Command
import org.crsh.cli.Usage
import org.crsh.command.BaseCommand
/**
 * Created by navid on 12/19/14.
 */
class addCmdMnt extends BaseCommand{

    @Usage("Adds a new directory to the list of command mounts")
    @Command
    void main(@Usage("file path to the directory you want to add. If no path provided the current directory will be used.")
              @Argument Fileable path) {

        BetterBootstrap bootstrap = ServiceLocator.locateCurrent(BetterBootstrap.class)

        bootstrap.addToCmdPath(path.withContext().absoluteFile)

        out << blue << "\tAdded ${path.withContext().canonicalPath}" << reset << "\n"
        out.flush()

    }


}
