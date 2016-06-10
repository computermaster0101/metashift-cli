package crash.commands.filesystem

import com.metashift.config.ServiceLocator
import com.metashift.context.FileableContext
import com.metashift.context.Fileable
import org.crsh.cli.Argument
import org.crsh.cli.Command
import org.crsh.cli.Usage
import org.crsh.command.BaseCommand
import org.crsh.command.ScriptException

/**
 * Created by navid on 12/19/14.
 */
class ls extends BaseCommand {

    @Usage("Lists the contents of the current directory or the directory provided")
    @Command
    List<File> main(@Usage("the directory to make current")
                @Argument Fileable path) {
        File dir = path?.f
        if(!(path?.path?.startsWith(File.separator))){
            FileableContext context = ServiceLocator.locateCurrent(FileableContext.class)
            if(path) {
                dir = new File(context.currentDirectory().f, path.path)
            }else{
                dir = context.currentDirectory().f
            }
        }
        if(!dir.isDirectory()){
            throw new ScriptException("The path provided must be a directory")
        }
        dir.listFiles()
    }


}
