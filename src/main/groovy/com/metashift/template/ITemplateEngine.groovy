package com.metashift.template
import groovy.text.Template
import org.codehaus.groovy.control.CompilationFailedException
/**
 * Created by navid on 4/1/15.
 */
interface ITemplateEngine {

    public Template createTemplate(String templateText) throws CompilationFailedException, ClassNotFoundException, IOException;

    public Template createTemplate(File file) throws CompilationFailedException, ClassNotFoundException, IOException;

    public Template createTemplate(URL url) throws CompilationFailedException, ClassNotFoundException, IOException;

}
