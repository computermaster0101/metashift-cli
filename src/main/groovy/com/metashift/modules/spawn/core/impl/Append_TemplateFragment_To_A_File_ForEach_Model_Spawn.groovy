package com.metashift.modules.spawn.core.impl

import com.metashift.core.util.WritableUtil
import com.metashift.modules.spawn.ISpawn
import com.metashift.modules.spawn.core.IContextAware
import com.metashift.modules.spawn.core.IModelProvider
import com.metashift.modules.spawn.core.INamingStrategy
import com.metashift.template.Templater
import org.apache.commons.io.FilenameUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import rx.Observable
import rx.Observer
import rx.Subscriber
import rx.functions.Action0
import rx.functions.Action1

/**
 * A Spawn that produces a single file appending data for each model provided.
 * Each model can specify a different template fragment that will be used to generate data for that model.
 *
 * You must provide
 *      {@link IModelProvider}.
 *      FileName that will be the name of the file created,
 *      templateFragmentKeyName to pass to the INamingStrategy
 *
 * Created by navid on 4/7/15.
 */
class Append_TemplateFragment_To_A_File_ForEach_Model_Spawn extends AbstractSpawn implements ISpawn{

    /** Required **/
    IModelProvider modelProvider

    String fileName

    /**
     * The key to provide to the naming strategy below when resolving the template name
     */
    String templateFragmentKeyName

    /**
     * The naming strategy to generate the name of the template needed per model
     */
    INamingStrategy fragmentTemplateNamingStrategy = new PathExpressionNamingStrategy()


    private Logger logger = LoggerFactory.getLogger(Append_TemplateFragment_To_A_File_ForEach_Model_Spawn.class);

    public static String MODEL = 'model'

    private Templater templater = new Templater()


    void run(){

        validate()

        File outputFile = new File(context.outputDir(),fileName)
        if (outputFile.exists() && !context.replaceFiles()){

            logger.warn("${outputFile.canonicalPath} already exists and will not be replaced")

        }else {

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile, true)))

            if (modelProvider instanceof IContextAware) {
                ((IContextAware) modelProvider).setContext(context)
            }

            Observable<?> observable = modelProvider.start()
            templater = new Templater()

            boolean modelReceived = false
            observable.subscribe(new Action1<Object>() {
                @Override
                void call(Object model) {

                    context.put(MODEL, model)
                    File template = new File(fragmentTemplateNamingStrategy.getName(context,templateFragmentKeyName))

                    templater.source(template)
                    templater.model(context)

                    templater.build().writeTo(bufferedWriter)
                    bufferedWriter.flush()

                    modelReceived = true
                }

            }, new Action1<Throwable>() {
                @Override
                void call(Throwable throwable) {
                    logger.error("Error Subscribing to provider ", throwable)
                }
            }, new Action0() {
                @Override
                void call() {

                    if (!modelReceived) {
                        logger.warn("No Model data was found. Template will not be executed")
                    }else{
                        bufferedWriter.close()
                    }
                }
            })
        }
    }

    private void validate() {
        if(!fileName){
            throw new IllegalArgumentException('fileName must not be null')
        }

        if(!templateFragmentKeyName){
            throw new IllegalArgumentException('templateFragmentKeyName must not be null')
        }

        if (!fragmentTemplateNamingStrategy){
            throw new IllegalArgumentException('fragmentTemplateNamingStrategy must not be null')
        }

        if (!modelProvider){
            throw new IllegalArgumentException('Model provider must not be null')
        }
    }


}