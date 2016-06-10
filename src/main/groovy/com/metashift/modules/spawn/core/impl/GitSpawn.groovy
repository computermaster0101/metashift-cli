package com.metashift.modules.spawn.core.impl

//import org.ajoberstar.grgit.Grgit

/**
 * This spawn allows generating content from a git repository.
 *
 * We do our best to allow all upstream changes to be merged with any changes by user of spawned content.
 *
 *
 *
 * Created by navidmitchell on 11/20/15.
 */
class GitSpawn extends AbstractSpawn{

    String repositoryURI

    @Override
    void run() {
//        validate()
//        def grgit = null
//        // check if repo has been cloned already
//        // FIXME: Replace Files ??
//        if(new File(outputDirectory,'.git').exists() ){// && !context.replaceFiles()){
//            grgit = Grgit.open(dir: outputDirectory)
//            grgit.fetch()
//        }else{
//            //outputDirectory.deleteDir()
//            grgit = Grgit.clone(dir: outputDirectory, uri: repositoryURI)
//        }
    }


    private void validate() {
        if (!outputDirectory){
            throw new IllegalArgumentException('Staging Directory must not be null')
        }

        if (!repositoryURI){
            throw new IllegalArgumentException('gitRepositoryURL must not be null')
        }
    }



}
