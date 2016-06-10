package com.metashift.core.util

import org.apache.commons.io.FilenameUtils

/**
 * Created by navid on 3/20/15.
 */
class FileUtil {

    /**
     * Parse the given file into an array of strings.
     * You can specify a regex pattern of lines to ignore if matched
     * NOTE: internally will use the File.readLines() so line separators are same
     * @param ignore regex to match for lines to ignore
     * @return
     */
   public static String[] readLines(File file,String ignore = null){
        if (!file || !file.exists() || !file.isFile()){
            throw new IllegalArgumentException("File must exist")
        }
        String[] ret = file.absoluteFile.readLines()

        if (ignore){
            ret = ret.findAll {String s -> s.size() > 0 && !(s ==~ ignore)}
        }
        ret
    }

    /**
     * Recursively walk up from the current child directory until we find a parent directory matching the given name.
     * @return the parent directory or null if no matching directory can be found
     */
    public static File getParent(File child,String parentName){
        File root = (File)child
        while(root && FilenameUtils.getBaseName(root.name) != parentName){
            root = root.parentFile
        }
        root
    }

}
