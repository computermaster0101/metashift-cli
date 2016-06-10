package com.metashift.template

import groovy.text.Template

/**
 * Created by navid on 4/11/15.
 */
class FreemarkerTemplateAdapter implements Template{

    private freemarker.template.Template freeMarkerTemplate;

    FreemarkerTemplateAdapter(freemarker.template.Template freeMarkerTemplate) {
        this.freeMarkerTemplate = freeMarkerTemplate
    }


    @Override
    Writable make() {
        new Writable(){
            @Override
            Writer writeTo(Writer out) throws IOException {
                freeMarkerTemplate.process(new HashMap(),out)
                out
            }
        }
    }

    @Override
    Writable make(Map binding) {
        new Writable(){
            @Override
            Writer writeTo(Writer out) throws IOException {
                freeMarkerTemplate.process(binding,out)
                out
            }
        }
    }
}
