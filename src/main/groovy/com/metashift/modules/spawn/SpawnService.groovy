package com.metashift.modules.spawn
import groovy.io.FileType
import org.springframework.stereotype.Service
/**
 *
 * Created by navid on 3/30/15.
 */
@Service
class SpawnService implements ISpawnService {

    private String seedFileExtension = '.seed'

    @Override
    void processDirectory(File rootDirectory, Map<String,Object> bindings) {
        if (!rootDirectory.isDirectory()){
            throw new IllegalArgumentException('The File provided must be a directory')
        }

        bindings.put(ROOT_DIR,rootDirectory)

        List<ISpawnInitializationContext> seedContexts = new ArrayList<>()

        rootDirectory.eachFileRecurse(FileType.FILES,{ File seedFile ->
            if (seedFile.name.endsWith(seedFileExtension)){
                ISpawnInitializationContext context = new SpawnInitializationContext(bindings)

                context.initialize(seedFile)

                context.outputDir().mkdirs()
                context.stagingDirectory().mkdirs()

                // TODO: fix output package
               // context.outputPackage ClassUtils.convertResourcePathToClassName(context.outputDir().name)

                seedContexts.add(context as ISpawnInitializationContext)
            }
        });

        seedContexts.each { ISpawnInitializationContext context ->
            evaluateSeed(context)
        }

        seedContexts.each { ISpawnInitializationContext context ->

            context.getSpawns().each { ISpawn spawn ->
                spawn.setContext(context)
                spawn.run()
            }

        }
    }


    private ISpawnInitializationContext evaluateSeed(ISpawnInitializationContext context){
        def binding = new Binding()
        binding.directory = context.seed().parentFile
        binding.context = context

        def shell = new GroovyShell(binding)

        shell.evaluate context.seed()

        context
    }




}
