package com.metashift.template
import com.metashift.context.MetaContext
import com.metashift.crash.BetterBootstrap
import com.metashift.core.util.WritableUtil
import groovy.text.StreamingTemplateEngine
import groovy.text.Template
/**
 * @Depricated use Tempalter and WritableUtil instead for same functionality
 * Created by navid on 3/20/15.
 */
@Deprecated
class GroovyTemplateUtil {

    private static StreamingTemplateEngine templateEngine = new StreamingTemplateEngine()

    public static String render(File templateFile,Map binding) {
        new Templater()
                .source(templateFile)
                .model(binding)
                .build()
    }


    public static void renderToFile(File templateFile,
                                    File destination,
                                    Map binding) {

        WritableUtil.write(destination, new Templater()
                                            .groovyStreaming()
                                            .source(templateFile)
                                            .model(binding)
                                            .build())
    }

    /**
     * Renders the template to a temporary file. Returns the new file created by the operation
     * @param templateFile
     * @param binding
     * @param tempDir
     * @return
     */
    public static File renderToTempFile(File templateFile,
                                        Map binding,
                                        File tempDir = null){

        WritableUtil.writeTemp("${templateFile.name}_",".groovy",tempDir,
                new Templater()
                    .groovyStreaming()
                    .source(templateFile)
                    .model(binding)
                    .build())
    }

    public static Template createTemplate(File templateFile){
        templateEngine.createTemplate(templateFile)
    }

    /**
     * Will load the default template parameters if there are any provided
     * The template defaults are expected to be in a file in the same directory with the name templateFile_defaults.groovy
     *
     * example:
     * templateFile = myTemplate.groovy.tpl
     * the default parameter File would be expected to be myTemplate_default.groovy
     *
     * @param templateFile
     * @return
     */
    public static Map loadDefaults(File templateFile){
        if (!templateFile.exists() || templateFile.isDirectory()){
            throw new IllegalArgumentException("templateFile must exist and be a valid file. This is not the case for ${templateFile.canonicalPath}")
        }
        Map ret = null
        String baseName = getBaseName(templateFile)
        File defaults = new File(templateFile.parentFile,"${baseName}_defaults.groovy")
        if (defaults.exists()){
            ret = MetaContext.getBindingFromScript(defaults)
        }
        ret
    }

    /**
     * Renders the template output to create a new command. This allows for immutable code with dynamic creation.. ha bitches..
     * Commands will be written to System.getProperty('user.home') + '/.metashift/liquid'
     * @param templateFile the template file to use
     * @param binding
     * @param commandName the name of the command to create. If not provided the command will have the name of the template.
     *        NOTE: this works by effectively creating 2 files per command. The template rendered file and the command file. Which serves as an alias.
     *
     * @return the file tha was created by rendering the template.
     */
    public static File renderToTempCmd(File templateFile,
                                       Map binding,
                                       String commandName=null){
        if (!templateFile.exists()){
            throw new IllegalArgumentException("Template File ${templateFile.path} cannot be found")
        }

        Template template = templateEngine.createTemplate(templateFile)

        if (!commandName) {
            commandName = getBaseName(templateFile)
        }

        // Create command with desired name that represents temp command
        File tempDir = new File(BetterBootstrap.TEMP_COMMAND_PATH)
        tempDir.mkdirs()

        File liquidCmd = new File(tempDir,"${commandName}.groovy")

        liquidCmd.withWriter {Writer writer ->
            Writable writable = template.make(binding)
            writable.writeTo(writer)
        }

        liquidCmd
    }


    /**
     * Renders the template output to create a new command. This allows for immutable code with dynamic creation.. ha bitches..
     * Commands will be written to System.getProperty('user.home') + '/.metashift/liquid'
     * @param templateFile the template file to use
     * @param binding
     * @param commandName the name of the command to create. If not provided the command will have the name of the template.
     *        NOTE: this works by effectively creating 2 files per command. The template rendered file and the command file. Which serves as an alias.
     *
     * @return the file tha was created by rendering the template.
     */
    public static File renderToTempCmd(String templateString,
                                       Map binding,
                                       String commandName){

        Template template = templateEngine.createTemplate(templateString)

        // Create command with desired name that represents temp command
        File tempDir = new File(BetterBootstrap.TEMP_COMMAND_PATH)
        tempDir.mkdirs()

        File liquidCmd = new File(tempDir,"${commandName}.groovy")

        liquidCmd.withWriter {Writer writer ->
            Writable writable = template.make(binding)
            writable.writeTo(writer)
        }

        liquidCmd
    }

    private static String getBaseName(File templateFile) {
        templateFile.name.indexOf('.').with {
            it != -1 ? templateFile.name[0..<it] : templateFile.name
        }
    }

}
