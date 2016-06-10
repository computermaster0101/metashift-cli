package com.metashift.template

import org.rythmengine.RythmEngine
/**
 * Created by navid on 4/1/15.
 */
class RythmStringTemplateAdapter extends RythmTemplateAdapter{

    String template

    RythmStringTemplateAdapter(RythmEngine rythm,String template) {
        super(rythm)
        this.template = template
    }

    @Override
    Writer writeTo(Writer out) throws IOException {
        this.writeTo(out,null)
    }

    @Override
    Writer writeTo(Writer out, Map binding) throws IOException {
        rythm.render(out,template,binding)
        out
    }
}
