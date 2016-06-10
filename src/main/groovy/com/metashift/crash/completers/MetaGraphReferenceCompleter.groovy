package com.metashift.crash.completers

import com.metashift.config.GroovyRefreshableApplicationContext
import com.metashift.config.ServiceLocator
import org.crsh.cli.descriptor.ParameterDescriptor
import org.crsh.cli.spi.Completer
import org.crsh.cli.spi.Completion
/**
 * Created by navid on 2/25/15.
 */
class MetaGraphReferenceCompleter implements Completer {

    private static GroovyRefreshableApplicationContext context = ServiceLocator.locateCurrent(GroovyRefreshableApplicationContext.class)

    @Override
    Completion complete(ParameterDescriptor parameter, String prefix) throws Exception {
        def suffixes = context.getBeanDefinitionNames().findAll {String s -> s.startsWith(prefix)}
                                                    .collectEntries {new MapEntry(it.substring(prefix.length()),false)}
        Completion ret
        if(suffixes.size() == 0){
            ret = Completion.create()
        }else if(suffixes.size() == 1){
            Map.Entry<String,Boolean> entry = suffixes.entrySet().getAt(0)
            ret = Completion.create(prefix,entry.key,true)
        }else{
            ret = Completion.create(prefix,suffixes)
        }
        ret
    }
}
