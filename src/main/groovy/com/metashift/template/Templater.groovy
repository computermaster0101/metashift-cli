package com.metashift.template

import org.apache.commons.io.FilenameUtils
import org.springframework.core.io.Resource
/**
 * Created by navid on 3/27/15.
 */
class Templater {

    private Map<String,Object> model = new HashMap<>()

    private File templateFile = null

    private static FreemarkerTemplateEngine freemarkerTemplateEngine = new FreemarkerTemplateEngine()
    private static GroovyStreamingTemplateEngine groovyStreamingTemplateEngine = new GroovyStreamingTemplateEngine()
    private static RythmTemplateEngine rythmTemplateEngine = new RythmTemplateEngine()

    private ITemplateEngine engine = new FreemarkerTemplateEngine()

    /**
     * Named arguments can be passed to the model instance to create a root model object
     *
     * @param m a map of key / value pairs
     * @return this
     */
    public Templater model(Map<String,Object> m) {
        model.clear()
        model.putAll(m)
        this
    }

    public Templater put(String key,Object value){
        model.put(key,value)
        this
    }

    /**
     * Returns the current model object
     * @return the model object if any
     */
    public Map<String,Object> getModel(){
        model
    }


    public Templater source(Resource resource){
        templateFile = resource.file
        this
    }

    /**
     * Sets the template file to use.
     * This will automatically set the template engine if the template file ends in one of the following extensions
     * .ftl = freemarker
     * .gtl = GroovyStreamingTemplateEngine
     * .rythm = Rythm
     * @param file
     * @return
     */
    public Templater source(File file){
        if (!file.exists()){
            throw new IllegalArgumentException("Template File ${file.path} cannot be found")
        }

        templateFile = file

        String ext = FilenameUtils.getExtension(file.name)
        if (ext.endsWith('ftl')){
            freemarker()
        }else if(ext.endsWith('gtl')){
            groovyStreaming()
        }else if(ext.endsWith('rythm')){
            rythm()
        }
        this
    }

    /**
     * Sets the internal template engine to use the groovy template engine
     */
    public Templater groovyStreaming(){
        engine = groovyStreamingTemplateEngine
        this
    }

    public Templater rythm(){
        engine = rythmTemplateEngine
        this
    }

    public Templater freemarker(){
        engine = freemarkerTemplateEngine
        this
    }
    /**
     * Build a groovy Template
     * @return
     */
    public Writable build(){
        engine.createTemplate(templateFile).make(model)
    }

}
