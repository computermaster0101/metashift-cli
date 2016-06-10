package com.metashift.core.util

/**
 * Created by navid on 4/1/15.
 */
class WritableUtil {


    public static String write(Writable writable){
        StringWriter writer = new StringWriter()
        writable.writeTo(writer)
        String ret = writer.toString()
        writer.close()
        ret
    }


    public static void write(File destination,Writable writable){
        destination.withWriter { Writer writer ->
            writable.writeTo(writer)
            writer.close()
        }
    }


    /**
     * Renders the file as a temp file created in the temp directory provided.
     * @param  prefix     The prefix string to be used in generating the file's
     *                    name; must be at least three characters long
     *
     * @param  suffix     The suffix string to be used in generating the file's
     *                    name; may be <code>null</code>, in which case the
     *                    suffix <code>".tmp"</code> will be used
     *
     * @param  directory  The directory in which the file is to be created, or
     *                    <code>null</code> if the default temporary-file
     *                    directory is to be used
     * @return
     */
    public static File writeTemp(String prefix,String suffix, File directory, Writable writable){

        File outFile
        if (directory){
            outFile = File.createTempFile(prefix,suffix,directory)
        }else{
            outFile = File.createTempFile(prefix,suffix)
        }

        write(outFile,writable)
    }


    public static File writeTemp(String prefix,File directory, Writable writable){
        writeTemp(prefix,null,directory,writable)
    }

    /**
     * Renders the file as a temp file created in the default temp directory.
     * @param  prefix     The prefix string to be used in generating the file's
     *                    name; must be at least three characters long
     *
     * @param  suffix     The suffix string to be used in generating the file's
     *                    name; may be <code>null</code>, in which case the
     *                    suffix <code>".tmp"</code> will be used
     *
     * @param  directory  The directory in which the file is to be created, or
     *                    <code>null</code> if the default temporary-file
     *                    directory is to be used
     * @return
     */
    public static File writeTemp(String prefix,String suffix, Writable writable){
        writeTemp(prefix,suffix,writable)
    }


    public static File writeTemp(String prefix, Writable writable){
        writeTemp(prefix,(String)null,writable)
    }


}
