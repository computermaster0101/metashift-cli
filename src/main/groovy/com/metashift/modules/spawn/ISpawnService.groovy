package com.metashift.modules.spawn

/**
 * Created by navid on 3/30/15.
 */
interface ISpawnService {

    public static String ROOT_DIR = 'rootDirectory'

    /**
     * Executes all .seed files found in a given directory and subdirectories
     * When executing the .seed a ISpawnInitializationContext will be provided
     * @param rootDirectory the root directory to start scanning from. This will automatically be added to the binding
     * @param binding
     */
    void processDirectory(File rootDirectory, Map<String,Object> bindings)

}
