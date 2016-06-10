package com.metashift.context

import org.springframework.stereotype.Component
/**
 * Tracks the current state of the users Fileable interaction while metashell is executing.
 * The context remembers changes to the currently selected filesystem directory.
 *
 * This context interacts directly with commands such as cd or ls
 * Created by navid on 2/19/15.
 */
@Component
class FileableContext {

    private Fileable currentDirectory = new Fileable(".");

    public void currentDirectory(Fileable path){
        if(!path?.exists()){
            throw new IllegalArgumentException('Current directory path must exist')
        }

        if(!path?.isDirectory()){
            throw new IllegalArgumentException('Current directory path must be a path to a directory')
        }

        currentDirectory = path;
    }

    public Fileable currentDirectory(){
        currentDirectory
    }

    public Fileable withCurrent(Fileable path=null){
        File ret = path?.f
        if(!(path?.path?.startsWith(File.separator))){
            if(path) {
                ret = new File(currentDirectory().f, path.path)
            }else{
                ret = currentDirectory().f
            }
        }
        new Fileable(ret)
    }

}
