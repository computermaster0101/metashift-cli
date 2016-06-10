package com.metashift.context

import com.metashift.config.GroovyRefreshableApplicationContext
import com.metashift.config.ServiceLocator

/**
 * Allows a meta context object to be found for the given metaGraph object name
 * Created by navid on 2/27/15.
 */
class MetaGraphReference {

    private static GroovyRefreshableApplicationContext context = ServiceLocator.locateCurrent(GroovyRefreshableApplicationContext.class)

    private String metaGraphName

    MetaGraphReference(String metaGraphName) {
        this.metaGraphName = metaGraphName
    }

    public String name(){
        metaGraphName
    }

    /**
     * Resolves the reference to the actual metagraph
     * @return
     */
    public Object resolve(){
        context.getBean(metaGraphName)
    }

}
