package com.metashift.crash.completers

import org.crsh.cli.descriptor.ParameterDescriptor
import org.crsh.cli.spi.Completer
import org.crsh.cli.spi.Completion
/**
 * Created by navid on 2/25/15.
 */
class MethodReferenceCompleter implements Completer {

    Class clazz

    MethodReferenceCompleter(Class referenceClass){
        this.clazz = referenceClass
    }

    /**
     * Invokes the object referenced with the arguments given.
     * The object to invoke the method on
     * @param arguments
     * @return
     */
    public Object invoke(String methodName, Object object, Object[] arguments){
        clazz.metaClass.invokeMethod(object,methodName,arguments)
    }

    public Object invoke(String methodName, Object object){
        clazz.metaClass.invokeMethod(object,methodName)
    }

    /**
     * Invokes the object referenced with the arguments given.
     * The object to invoke the method on
     * @param arguments
     * @return
     */
    public Object invoke(String methodName,Object object, Object arguments){
        clazz.metaClass.invokeMethod(object,methodName,arguments)
    }

    /**
     * Performs the completion with a different target than is set by default
     * @param target
     * @param parameter
     * @param prefix
     * @return
     * @throws Exception
     */
    Completion complete(Class target,ParameterDescriptor parameter, String prefix) throws Exception {
        Map<String,Boolean> suffixes = target.metaClass.methods.findAll { MetaMethod meth ->
            prefix == "" ? true : meth.name.startsWith(prefix)
        }*.name.collectEntries {
            new MapEntry(it.substring(prefix.length()),false)
        }

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


    @Override
    Completion complete(ParameterDescriptor parameter, String prefix) throws Exception {
        complete(clazz,parameter,prefix)
    }
}
