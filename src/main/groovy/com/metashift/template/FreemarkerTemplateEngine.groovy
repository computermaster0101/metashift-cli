package com.metashift.template
import com.metashift.config.ServiceLocator
import freemarker.template.Configuration
import groovy.text.Template
import org.codehaus.groovy.control.CompilationFailedException
import org.codehaus.groovy.runtime.DefaultGroovyMethodsSupport

/**
 * Created by navid on 4/11/15.
 */
class FreemarkerTemplateEngine implements ITemplateEngine {

    Configuration configuration = ServiceLocator.locateCurrent(Configuration.class)

    @Override
    Template createTemplate(String templateText) throws CompilationFailedException, ClassNotFoundException, IOException {
        freemarker.template.Template t = new freemarker.template.Template("stringTemplate", templateText, configuration);
        new FreemarkerTemplateAdapter(t)
    }

    @Override
    Template createTemplate(File file) throws CompilationFailedException, ClassNotFoundException, IOException {
        Reader reader = new FileReader(file)
        try {
            freemarker.template.Template t = new freemarker.template.Template(file.getPath(), reader, configuration);
            new FreemarkerTemplateAdapter(t)
        }catch (Exception e){
            DefaultGroovyMethodsSupport.closeWithWarning(reader);
        }
    }

    @Override
    Template createTemplate(URL url) throws CompilationFailedException, ClassNotFoundException, IOException {
        Reader reader = new InputStreamReader(url.openStream());
        try {
            freemarker.template.Template t = new freemarker.template.Template(url.toString(), reader, configuration);
            new FreemarkerTemplateAdapter(t)
        } finally {
            DefaultGroovyMethodsSupport.closeWithWarning(reader);
        }

    }
}
