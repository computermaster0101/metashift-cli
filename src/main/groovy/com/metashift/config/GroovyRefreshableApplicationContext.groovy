package com.metashift.config

import org.crsh.cli.impl.lang.Instance
import org.crsh.plugin.AutowireCapable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.BeanWrapper
import org.springframework.beans.BeanWrapperImpl
import org.springframework.beans.BeansException
import org.springframework.beans.factory.BeanNameAware
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader
import org.springframework.beans.factory.support.DefaultListableBeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.support.AbstractRefreshableApplicationContext
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.util.Assert
import org.springframework.util.StringUtils
/**
 * Created by navid on 2/25/15.
 */
class GroovyRefreshableApplicationContext extends AbstractRefreshableApplicationContext implements AutowireCapable, BeanNameAware, InitializingBean {

    private Logger logger = LoggerFactory.getLogger(GroovyRefreshableApplicationContext.class);

    private final BeanWrapper contextWrapper = new BeanWrapperImpl(this);

    private HashSet<Resource> configResources = new HashSet<>()

    private HashSet<String> resourcePattern = new HashSet<>()

    private HashSet<String> configLocations = new HashSet<>()

    private boolean setIdCalled = false;
    /**
     * Create a new GroovyRefreshableApplicationContext that needs to be
     * {@link #load loaded} and then manually {@link #refresh refreshed}.
     */
    public GroovyRefreshableApplicationContext() {
    }

    /**
     * Create a new GroovyRefreshableApplicationContext, loading bean definitions
     * from the given resources and automatically refreshing the context.
     * @param resources the resources to load from
     */
    public GroovyRefreshableApplicationContext(Resource... resources) {
        load(resources);
        refresh();
    }

    /**
     * Create a new GroovyRefreshableApplicationContext, loading bean definitions
     * from the given resource locations and automatically refreshing the context.
     * @param resourceLocations the resources to load from
     */
    public GroovyRefreshableApplicationContext(String... resourceLocations) {
        load(resourceLocations);
        refresh();
    }

    /**
     * Create a new GroovyRefreshableApplicationContext, loading bean definitions
     * from the given resource locations and automatically refreshing the context.
     * @param relativeClass class whose package will be used as a prefix when
     * loading each specified resource name
     * @param resourceNames relatively-qualified names of resources to load
     */
    public GroovyRefreshableApplicationContext(Class<?> relativeClass, String... resourceNames) {
        load(relativeClass, resourceNames);
        refresh();
    }

    public GroovyRefreshableApplicationContext(ApplicationContext context){
        super(context)
    }

    /**
     * Delegates the given environment to underlying {@link GroovyBeanDefinitionReader}.
     * Should be called before any call to {@code #load}.
     */
    @Override
    public void setEnvironment(ConfigurableEnvironment environment) {
        super.setEnvironment(environment);
    }

    /**
     * Load bean definitions from the given Groovy scripts or XML files.
     * <p>Note that ".xml" files will be parsed as XML content; all other kinds
     * of resources will be parsed as Groovy scripts.
     * @param resources one or more resources to load from
     */
    public void load(Resource... resources) {
        configResources.addAll(resources)
    }

    /**
     * Load bean definitions from the given Groovy scripts or XML files.
     * <p>Note that ".xml" files will be parsed as XML content; all other kinds
     * of resources will be parsed as Groovy scripts.
     * @param resourceLocations one or more resource locations to load from
     */
    public void load(String... resourceLocations) {
        resourcePattern.addAll(resourceLocations)
    }

    /**
     * Load bean definitions from the given Groovy scripts or XML files.
     * <p>Note that ".xml" files will be parsed as XML content; all other kinds
     * of resources will be parsed as Groovy scripts.
     * @param relativeClass class whose package will be used as a prefix when
     * loading each specified resource name
     * @param resourceNames relatively-qualified names of resources to load
     */
    public void load(Class<?> relativeClass, String... resourceNames) {
        Resource[] resources = new Resource[resourceNames.length];
        for (int i = 0; i < resourceNames.length; i++) {
            resources[i] = new ClassPathResource(resourceNames[i], relativeClass);
        }
        load(resources);
    }


    // Implementation of the GroovyObject interface

    public void setProperty(String property, Object newValue) {
        if (newValue instanceof BeanDefinition) {
            ((DefaultListableBeanFactory)getBeanFactory()).registerBeanDefinition(property, (BeanDefinition) newValue)
        }
        else {
            this.metaClass.setProperty(this, property, newValue);
        }
    }

    public Object getProperty(String property) {
        if (containsBean(property)) {
            return getBean(property);
        }
        else if (this.contextWrapper.isReadableProperty(property)) {
            return this.contextWrapper.getPropertyValue(property);
        }
        throw new NoSuchBeanDefinitionException(property);
    }

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
        // Create a new XmlBeanDefinitionReader for the given BeanFactory.
        GroovyBeanDefinitionReader beanDefinitionReader = new GroovyBeanDefinitionReader(beanFactory);

        // Configure the bean definition reader with this context's
        // resource loading environment.
        Binding binding = new Binding()
        binding.setProperty('meta',this)
        beanDefinitionReader.setBinding(binding)
        beanDefinitionReader.setEnvironment(this.getEnvironment());
        beanDefinitionReader.setResourceLoader(this);

        loadBeanDefinitions(beanDefinitionReader);
    }

    protected void loadBeanDefinitions(GroovyBeanDefinitionReader reader) throws BeansException, IOException {
        List<Resource> removeConfigResources = new ArrayList<>()

        List<String> removeResourcePattern = new ArrayList<>()

        List<String> removeConfigLocations = new ArrayList<>()

        configResources?.each {
            try {
                reader.loadBeanDefinitions(it);
            } catch (Exception ignored) {
                logger.warn "Error Loading ${it}", ignored
                removeConfigResources.add(it)
            }
        }
        configResources.removeAll(removeConfigResources)

        configLocations?.each{
            try {
                reader.loadBeanDefinitions(it);
            } catch (Exception ignored) {
                logger.warn "Error Loading ${it}", ignored
                removeConfigLocations.add(it)
            }
        }
        configLocations.removeAll(removeConfigLocations)

        resourcePattern?.each{
            try {
                reader.loadBeanDefinitions(it);
            } catch (Exception ignored) {
                logger.warn "Error Loading ${it}", ignored
                removeResourcePattern.add(it)
            }
        }
        resourcePattern.removeAll(removeResourcePattern)

        if (removeResourcePattern.size() > 0 || removeConfigLocations.size() > 0 || removeConfigResources.size() > 0) {
            // TODO: use standard logging mechanism
            System.out.println('The following resource paths could not be loaded and were removed from this context.')
            removeConfigResources.each{
                logger.warn it as String
            }

            removeConfigLocations.each{
                logger.warn it as String
            }

            removeResourcePattern.each{
                logger.warn it as String
            }
        }
    }

    @Override
    void autowire(Object existingObject) throws Exception {
        // Silly crash stuff where command is delegator blah blah..
        try {
            if(existingObject instanceof Instance){
                existingObject = ((Instance)existingObject).get()
            }
            getAutowireCapableBeanFactory().autowireBean(existingObject)
        } catch (Exception e) {
            System.out.println('Error trying to auto wire command '+e.getMessage())
        }
    }

    /**
     * Set the config locations for this application context in init-param style,
     * i.e. with distinct locations separated by commas, semicolons or whitespace.
     * <p>If not set, the implementation may use a default as appropriate.
     */
    public void setConfigLocation(String location) {
        setConfigLocations(StringUtils.tokenizeToStringArray(location, CONFIG_LOCATION_DELIMITERS));
    }

    /**
     * Set the config locations for this application context.
     * <p>If not set, the implementation may use a default as appropriate.
     */
    public void setConfigLocations(String... locations) {
        if (locations != null) {
            Assert.noNullElements(locations, "Config locations must not be null");
            this.configLocations = new String[locations.length];
            for (int i = 0; i < locations.length; i++) {
                this.configLocations[i] = resolvePath(locations[i]).trim();
            }
        }
        else {
            this.configLocations = null;
        }
    }

    /**
     * Return an array of resource locations, referring to the XML bean definition
     * files that this context should be built with. Can also include location
     * patterns, which will get resolved via a ResourcePatternResolver.
     * <p>The default implementation returns {@code null}. Subclasses can override
     * this to provide a set of resource locations to load bean definitions from.
     * @return an array of resource locations, or {@code null} if none
     * @see #getResources
     * @see #getResourcePatternResolver
     */
    protected String[] getConfigLocations() {
        return (this.configLocations != null ? this.configLocations : getDefaultConfigLocations());
    }

    /**
     * Return the default config locations to use, for the case where no
     * explicit config locations have been specified.
     * <p>The default implementation returns {@code null},
     * requiring explicit config locations.
     * @return an array of default config locations, if any
     * @see #setConfigLocations
     */
    protected String[] getDefaultConfigLocations() {
        return null;
    }

    /**
     * Resolve the given path, replacing placeholders with corresponding
     * environment property values if necessary. Applied to config locations.
     * @param path the original file path
     * @return the resolved file path
     * @see org.springframework.core.env.Environment#resolveRequiredPlaceholders(String)
     */
    protected String resolvePath(String path) {
        return getEnvironment().resolveRequiredPlaceholders(path);
    }


    @Override
    public void setId(String id) {
        super.setId(id);
        this.setIdCalled = true;
    }

    /**
     * Sets the id of this context to the bean name by default,
     * for cases where the context instance is itself defined as a bean.
     */
    @Override
    public void setBeanName(String name) {
        if (!this.setIdCalled) {
            super.setId(name);
            setDisplayName("ApplicationContext '${name}'");
        }
    }

    /**
     * Triggers {@link #refresh()} if not refreshed in the concrete context's
     * constructor already.
     */
    @Override
    public void afterPropertiesSet() {
        if (!isActive()) {
            refresh();
        }
    }

    @Override
    public void refresh() throws BeansException, IllegalStateException {
        super.refreshBeanFactory()
        super.refresh()
    }

    public void reset(){
        configResources.clear()
        resourcePattern.clear()
        configLocations.clear()
        refresh()
    }

}
