package com.metashift.crash.completers

import com.metashift.config.ServiceLocator
import com.metashift.context.FileableContext
import com.metashift.context.Fileable
import org.crsh.cli.completers.AbstractPathCompleter

/**
 * Provides completer for FileSystem objects.
 *
 * Created by navid on 2/18/15.
 */
class FileableCompleter extends AbstractPathCompleter<Fileable>  {

    @Override
    protected String getCurrentPath() throws Exception {
        FileableContext context = ServiceLocator.locateCurrent(FileableContext.class)
        return context.currentDirectory().canonicalPath
    }

    @Override
    protected Fileable getPath(String path) {
        return new Fileable(path)
    }

    @Override
    protected boolean exists(Fileable path) {
        return path.exists()
    }

    @Override
    protected boolean isDirectory(Fileable path) {
        return path.isDirectory()
    }

    @Override
    protected boolean isFile(Fileable path) {
        return path.isFile()
    }

    @Override
    protected Collection<Fileable> getChilren(Fileable path) {
        path.listFiles().collect { File f -> new Fileable(f) }
    }

    @Override
    protected String getName(Fileable path) {
        return path.getName()
    }

}
