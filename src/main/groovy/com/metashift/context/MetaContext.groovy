package com.metashift.context
import com.metashift.config.GroovyRefreshableApplicationContext
import com.metashift.config.ServiceLocator
import org.crsh.command.ScriptException
import org.springframework.aop.target.HotSwappableTargetSource
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.core.io.Resource

/**
 * Created by navid on 3/17/15.
 */
class MetaContext {

    private static GroovyRefreshableApplicationContext context = ServiceLocator.locateCurrent(GroovyRefreshableApplicationContext.class)

    /**
     * Puts a new object into the meta context. Will return the old object if this is replacing an object or null if this is the first time the object was added.
     * @param element
     * @param key
     * @return
     */
    public static Object put(String key, Object element, boolean makeHotSwappable = true) {
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory()
        Object old = null
        if (beanFactory.containsLocalBean(key)) {
            Object bean = beanFactory.getBean(key)
            if (!(bean instanceof HotSwappableTargetSource)) {
                throw new ScriptException("object is part of meta context and is not HotSwappable remove first. Found ${bean}")
            }
            old = ((HotSwappableTargetSource) bean).swap(element)
        } else {

            beanFactory.autowireBean(element)
            if (makeHotSwappable) {
                HotSwappableTargetSource swappable = new HotSwappableTargetSource(element)
                beanFactory.registerSingleton(key, swappable)
            }else{
                beanFactory.registerSingleton(key,element)
            }
        }
        old
    }

    public static void load(Resource... resources) {
        try {
            context.load(resources)
            context.refresh()
            context.start()
        }catch(Exception e) {
            context.refresh()
            context.start()
            throw e
        }
    }

    public static void load(String... resourceLocations) {
        try {
            context.load(resourceLocations)
            context.refresh()
            context.start()
        }catch(Exception e) {
            context.refresh()
            context.start()
            throw e
        }
    }

    public static contains(String key){
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory()
        beanFactory.containsLocalBean(key)
    }

    public static remove(String key){
        context.getBeanFactory().destroyScopedBean(key)
    }

    public static remove(MetaGraphReference reference){
        context.getBeanFactory().destroyScopedBean(reference.name())
    }


    public static Object pull(String key){
        Object bean = context.getBean(key)
        if (bean instanceof HotSwappableTargetSource) {
            bean = ((HotSwappableTargetSource) bean).target
        }
        bean
    }

    public static <T> T pull(Class<T> type){
        T bean = context.getBean(type)
        if (bean instanceof HotSwappableTargetSource) {
            bean = (T)((HotSwappableTargetSource) bean).target
        }
        bean
    }

    public static <T> Map<String, T> pullAll(Class<T> type){
        context.getBeansOfType(type)
    }

    public static Object pull(MetaGraphReference reference) {
        pull(reference.name())
    }

    /**
     * Reset the context essentially discards all beans that have been configured.
     * @return
     */
    public static reset(){
        context.reset()
    }

    /**
     * Creates a Map from the provided script file.
     * The file should have a binding call that provides the map binding values
     *
     * <code>
     *     binding([
     *              key:value,
     *              key:value
     *            ])
     * <code>
     *
     * @param scriptFile
     * @return
     */
    public static Map getBindingFromScript(File scriptFile){
        Map ret = null
        def binding = new Binding()
        binding.binding = { Map map ->
            ret = map
        }

        def shell = new GroovyShell(binding)
        shell.evaluate scriptFile
        ret
    }

}
