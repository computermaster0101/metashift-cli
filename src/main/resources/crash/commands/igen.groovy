package crash.commands

import org.springframework.core.io.Resource

(scan.resources { 'file:../metashift-spawn/resources/*' } | { Resource r ->

    if(r.file.isDirectory() && !r.file.isHidden()) {
        addCmdMnt r.file.canonicalPath
    }

} )()
