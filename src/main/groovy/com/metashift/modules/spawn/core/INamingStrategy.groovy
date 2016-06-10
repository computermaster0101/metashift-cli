package com.metashift.modules.spawn.core

/**
 * Generic interface to provide names for some object based on a key and a context.
 * This can have different meanings to different clients of the api
 * Created by navid on 4/2/15.
 */
interface INamingStrategy {

    String getName(Map<String,Object> context,String key);

}