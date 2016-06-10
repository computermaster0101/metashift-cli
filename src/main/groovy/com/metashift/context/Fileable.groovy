package com.metashift.context

import com.metashift.config.ServiceLocator

/**
 * This class extends File so commands can use auto completion that interacts directly with the FileSystemContext extension
 * Created by navid on 2/21/15.
 */
class Fileable {

    @Delegate File f

    Fileable(File f) {
        this.f = f
    }

    Fileable(String path){
        f = new File(path)
    }

    def asType(Class target) {
        if (File==target) {
            return new File(f.getCanonicalPath())
        }
    }

    Fileable withContext(){
        ServiceLocator.locateCurrent(FileableContext.class).withCurrent(this)
    }

}
