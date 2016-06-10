package com.metashift.modules.spawn.util

/**
 * Created by navid on 4/9/15.
 */
class HttpMappingModelUtil {

    public Map<String,Map<String,String> > getRequestMappingTypes(String uri, Map<String,Class> types, Map<String,Class> globalTypes){
        Map<String,String> pathParameters = new HashMap<>()
        Map<String,String> queryParameters = new HashMap<>()

        String[] parts = uri.split('\\?')

        def getType = { String segment, Map<String,String> paramMap  ->
            if (segment.contains('{')) {
                String param = null
                segment.with{
                    param = substring(indexOf('{')+1,indexOf('}'))
                }
                if (param) {
                    String type = types?.get(param)?.getName()
                    if (!type) type = globalTypes?.get(param)?.getName()
                    if (!type) type = String.class.getName()
                    paramMap.put(param, type)
                }
            }
        }

        String[] pathParamStrings = parts[0]?.split('/')
        pathParamStrings?.each{
            getType(it,pathParameters)
        }

        String[] queryParamString = parts[1]?.split('&')
        queryParamString?.each{
            getType(it,queryParameters)
        }

        [pathParameters:pathParameters,queryParameters:queryParameters]
    }
}
