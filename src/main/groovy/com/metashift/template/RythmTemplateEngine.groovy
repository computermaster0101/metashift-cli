package com.metashift.template
import com.metashift.config.ServiceLocator
import groovy.text.Template
import org.codehaus.groovy.control.CompilationFailedException
import org.rythmengine.RythmEngine

/**
 * Created by navid on 3/27/15.
 */
class RythmTemplateEngine implements ITemplateEngine{

    private RythmEngine rythmEngine = null

    private RythmEngine getEngine(){
        if (!rythmEngine){
            rythmEngine =  ServiceLocator.locateCurrent(RythmEngine.class)
        }
        rythmEngine
    }

    @Override
    Template createTemplate(String templateText) throws CompilationFailedException, ClassNotFoundException, IOException {
        new RythmStringTemplateAdapter(engine,templateText)
    }

    @Override
    Template createTemplate(File file) throws CompilationFailedException, ClassNotFoundException, IOException {
        new RythmFileTemplateAdapter(engine,file)
    }

    @Override
    Template createTemplate(URL url) throws CompilationFailedException, ClassNotFoundException, IOException {
        new RythmURLTemplateAdapter(engine,url)
    }
}
