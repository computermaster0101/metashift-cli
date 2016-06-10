package com.metashift.modules.spawn

/**
 * Created by navid on 4/8/15.
 */
interface ISpawnContext extends Map<String,Object>{

    /**
     * Keys used internally for convenience getters below
     */
    public static String OUTPUT_DIR = "outputDirectory"
    public static String STAGING_DIR = "stagingDirectory"
    public static String OUTPUT_PACKAGE = "outputPackage"

    /**
     * Will be called before any further processing with .seed
     * @param seedFile
     */
    public void initialize(File seedFile)

    public File outputDir()

    public void outputDir(File value)

    public boolean replaceFiles()

    public void replaceFiles(boolean value)

    public File seed()

    public String outputPackage()
    public void outputPackage(String value)

    /**
     * @return directory for spawns to use to stage resources
     */
    public File stagingDirectory()
    public void stagingDirectory(File value)
}