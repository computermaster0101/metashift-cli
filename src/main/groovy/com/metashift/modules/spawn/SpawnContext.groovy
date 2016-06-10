package com.metashift.modules.spawn
/**
 * Map that is used by ISpawn instances during processing
 *
 * There are some keys that are reserved for internal use.
 * See the public static Strings defined for the context
 *
 * Created by navid on 3/30/15.
 */
class SpawnContext implements ISpawnContext {

    // FIXME: Wrap map with error trapping delegate for all values and keys..
    // Example of problem is using ClassMetadataModel when not pointing to a valid class file
    @Delegate
    Map<String,Object> properties = new HashMap<>()

    private File seed
    private boolean replaceFiles = false

    @Override
    void initialize(File seed){
        this.seed = seed
    }

    public String outputPackage(){
        (String) properties.get(OUTPUT_PACKAGE)
    }

    public void outputPackage(String value){
        properties.put(OUTPUT_PACKAGE,value)
    }

    @Override
    public File outputDir(){
        if(!properties.containsKey(STAGING_DIR)){
            stagingDirectory(new File(seed.parentFile.absolutePath))
        }
        (File) properties.get(OUTPUT_DIR)
    }

    @Override
    public void outputDir(File value){
        properties.put(OUTPUT_DIR,value)
    }

    @Override
    public File stagingDirectory(){
        if(!properties.containsKey(STAGING_DIR)){
            stagingDirectory(new File(seed.parentFile,'.staging'))
        }
        (File) properties.get(STAGING_DIR)
    }

    @Override
    public void stagingDirectory(File value){
        properties.put(STAGING_DIR,value)
    }

    @Override
    public File seed() {
        return seed
    }

    public void seed(File seed) {
        this.seed = seed
    }

    @Override
    public boolean replaceFiles() {
        return replaceFiles
    }

    @Override
    public void replaceFiles(boolean replaceFiles) {
        this.replaceFiles = replaceFiles
    }

}
