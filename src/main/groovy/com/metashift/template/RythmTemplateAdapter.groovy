package com.metashift.template

import groovy.text.Template
import org.rythmengine.RythmEngine

/**
 * Created by navid on 4/1/15.
 */
abstract class RythmTemplateAdapter implements Template{

    protected RythmEngine rythm

    RythmTemplateAdapter(RythmEngine rythm) {
        this.rythm = rythm
    }

    public abstract Writer writeTo(Writer out) throws IOException;

    public abstract Writer writeTo(Writer out,Map binding) throws IOException;

    @Override
    Writable make() {
        new Writable() {
            @Override
            Writer writeTo(Writer out) throws IOException {
                RythmTemplateAdapter.this.writeTo(out)
            }
        }
    }

    @Override
    Writable make(Map binding) {
        new Writable() {
            @Override
            Writer writeTo(Writer out) throws IOException {
                RythmTemplateAdapter.this.writeTo(out,binding)
            }
        }
    }


}