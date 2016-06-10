package crash.commands

import org.springframework.core.io.Resource

(scan.resources { 'file:../metashift-modules/resources/com/minds/metacommand/**' } | { Resource r ->

    if(r.file.isDirectory() && !r.file.isHidden()) {
        addCmdMnt r.file.canonicalPath
    }

} )()
