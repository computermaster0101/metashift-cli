package com.metashift.modules.spawn.core.impl
import com.metashift.modules.spawn.ISpawn
import com.metashift.modules.spawn.core.IContextAware
import com.metashift.modules.spawn.core.IModelProvider
import com.metashift.modules.spawn.core.INamingStrategy
import com.metashift.template.Templater
import com.metashift.core.util.WritableUtil
import org.apache.commons.io.FilenameUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import rx.Observable
import rx.Observer
import rx.Subscriber
import rx.functions.Action0
import rx.functions.Action1
/**
 * A Spawn that produces one file from a template for each model provided.
 *
 * You must provide a {@link IModelProvider} and a {@link File} for the template.
 *
 * Created by navid on 4/7/15.
 */
class IndividualFileFromTemplateForEachModelSpawn extends AbstractSpawn implements ISpawn{

    /** Required **/
    IModelProvider modelProvider

    File template


    private Logger logger = LoggerFactory.getLogger(IndividualFileFromTemplateForEachModelSpawn.class);

    public static String MODEL = 'model'
    public static String OUTPUT_FILENAME = 'outputFilename'


    private Templater templater = new Templater()

    /**
     * This naming strategy can be used with a template file name to generate a name for the output file.
     *
     * It uses the source template name as the key to determine how to name the final file will be named.
     * This is done by using a modified form of BOON path expressions for the original templateFile name.
     * This expression will be replaced by the values found in the context.
     *
     * The File name will have the following replacements done
     *
     *    _ will be translated to .
     *    . or - will terminate expression.
     *
     * Ex:
     * model_shortName-Controller.java
     * model.shortName = context['model].shortName
     *
     * Ex:
     * model_shortName.java
     * model.shortName = context['model].shortName
     *
     */
    INamingStrategy outputFileNamingStrategy = new PathExpressionNamingStrategy()



    void run(){

        validate()

        if (modelProvider instanceof IContextAware){
            ((IContextAware)modelProvider).setContext(context)
        }

        Observable<?> observable = modelProvider.start()
        templater = new Templater()
        templater.source(template)

        boolean modelReceived = false
        observable.subscribe(new Action1<Object>() {
            @Override
            void call(Object model){
                context.put(MODEL,model)
                String fileName = getOutputFilename()
                File outputFile = new File(context.outputDir(),fileName)
                context.put(OUTPUT_FILENAME,fileName)

                templater.model(context)

                if (!outputFile.exists() || (outputFile.exists() && context.replaceFiles())){
                    modelReceived = true
                    WritableUtil.write(outputFile, templater.build())
                }else{
                    logger.warn("${outputFile.canonicalPath} already exists and will not be replaced")
                }
            }

        },new Action1<Throwable>() {
            @Override
            void call(Throwable throwable) {
                logger.error("Error Subscribing to provider ",throwable)
            }
        },new Action0() {
            @Override
            void call() {
                if (!modelReceived){
                    logger.warn("No Model data was found. Template will not be executed")
                }
            }
        })
    }


    private String getOutputFilename() {
        String templateFilename = template.getName()
        // We get the extension then remove extension to handle cases like .java.tpl
        String templateBasename = FilenameUtils.removeExtension(FilenameUtils.getBaseName(templateFilename))

        def sb = new StringBuilder()
        sb.with {
            append outputFileNamingStrategy.getName(context, fileNameToBoonExp(templateFilename))
            append(templateBasename.contains('-')
                    ? templateBasename.substring(templateBasename.indexOf('-') + 1, templateBasename.length())
                    : '')
            append '.'
            // We get the extension then remove extension to handle cases like .java.tpl
            def ext = FilenameUtils.getExtension(FilenameUtils.getBaseName(templateFilename))
            if (!ext){
                ext = FilenameUtils.getExtension(templateFilename)
            }
            append ext
        }
        sb.toString()
    }


    private void validate() {
        if (!template?.exists()){
            throw new IllegalArgumentException('Template must exist')
        }

        if (!outputFileNamingStrategy){
            throw new IllegalArgumentException('Template naming strategy must not be null')
        }

        if (!modelProvider){
            throw new IllegalArgumentException('Model provider must not be null')
        }
    }

    private static String fileNameToBoonExp(String fileName){
        // extract name
        def exp = FilenameUtils.getBaseName(fileName)
        // now extract path expression portion
        if (exp.contains('-')) {
            exp = exp.substring(0, exp.indexOf('-'))
        }
        exp.replaceAll('_','.')
    }

}