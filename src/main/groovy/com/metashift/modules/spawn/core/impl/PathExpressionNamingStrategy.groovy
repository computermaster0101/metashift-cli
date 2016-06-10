package com.metashift.modules.spawn.core.impl
import com.metashift.modules.spawn.core.INamingStrategy
import org.boon.core.reflection.BeanUtils
/**
 * This naming strategy can be used with a template file name to generate a name for the template output.
 *
 * It uses the source template name as the key to determine how to name the final file will be named.
 * This is done by using BOON path expressions for the original templateFile name.
 * This expression will be replaced by the values found in the context.
 *
 *
 * Created by navid on 4/2/15.
 */
class PathExpressionNamingStrategy implements INamingStrategy{

    /**
     * Will get the template ouput name based on the value in key.
     * Key should be a valid path expression
     *
     *
     * @param context
     * @param key
     * @return
     */
    @Override
    String getName(Map<String, Object> context, String key) {
        BeanUtils.idx(context,key)
    }


}
