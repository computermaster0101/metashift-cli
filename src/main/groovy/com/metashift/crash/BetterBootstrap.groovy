package com.metashift.crash

import com.metashift.config.GroovyRefreshableApplicationContext
import groovy.grape.Grape
import org.crsh.plugin.Embedded
import org.crsh.plugin.PluginDiscovery
import org.crsh.spring.SpringMap
import org.crsh.spring.SpringPluginDiscovery
import org.crsh.util.Utils
import org.crsh.vfs.FS
import org.crsh.vfs.Path
import org.crsh.vfs.spi.FSDriver
import org.crsh.vfs.spi.FSMountFactory
import org.crsh.vfs.spi.file.FileMountFactory
import org.crsh.vfs.spi.url.ClassPathMountFactory
import org.springframework.beans.BeansException
import org.springframework.beans.factory.BeanClassLoaderAware
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.ListableBeanFactory
import org.springframework.beans.factory.annotation.Autowired

import java.util.logging.Level
/**
 * Used in place of SpringBoostrap to have more control of command directories
 * Created by navid on 2/22/15.
 */
class BetterBootstrap extends Embedded implements
                BeanClassLoaderAware,
                BeanFactoryAware,
                InitializingBean,
                DisposableBean {

    public static String TEMP_COMMAND_PATH = System.getProperty('user.home') + '/.metashift/liquid'

    /** . */
    private ClassLoader loader;

    /** . */
    private BeanFactory factory;

    /** . */
    protected final HashMap<String, FSMountFactory<?>> drivers = new HashMap<String, FSMountFactory<?>>();

    /** . */
    private String cmdMountPointConfig;

    /** . */
    private String confMountPointConfig;

    /** The configuration file system. */
    private FS confFS;

    /** The command file system. */
    private FS cmdFS;

    private List<String> commandMounts = new ArrayList<>();

    @Autowired
    private GroovyRefreshableApplicationContext metaContext;

    public void setBeanClassLoader(ClassLoader loader) {
        this.loader = loader;
    }

    public void setBeanFactory(BeanFactory factory) throws BeansException {
        this.factory = factory;
    }

    public void afterPropertiesSet() throws Exception {

        // Initialise the registrable drivers
        try {
            drivers.put("classpath", new ClassPathMountFactory(loader));
            drivers.put("file", new FileMountFactory(Utils.getCurrentDirectory()));
        }
        catch (Exception e) {
            log.log(Level.SEVERE, "Coult not initialize classpath driver", e);
            return;
        }

        // List beans
        Map<String,Object> attributes = new HashMap<String, Object>();
        attributes.put("factory", factory);
        attributes.put("autowire",metaContext);
        attributes.put("meta",metaContext);

        if (factory instanceof ListableBeanFactory) {
            ListableBeanFactory listable = (ListableBeanFactory)factory;
            attributes.put("beans", new SpringMap(listable));
        }

        //
        PluginDiscovery discovery = new SpringPluginDiscovery(loader, factory);

        // Preload FS so we can hook into lifecycle and keep FS for mounting additional directories later
        String cmdPath = resolveCmdMountPointConfig();
        commandMounts.add(cmdPath);
        cmdFS = createFS(cmdPath);
        confFS = createFS(resolveConfMountPointConfig());

        //
        start(Collections.unmodifiableMap(attributes), discovery, loader);

        Grape.getInstance()

        // Add path to locate dynamically produced template commands
        addToCmdPath(new File(TEMP_COMMAND_PATH))
    }

    @Override
    protected FS createCommandFS() throws IOException {
        return cmdFS;
    }

    @Override
    protected FS createConfFS() throws IOException {
        return confFS;
    }

    /**
     * Add a command path driver.
     *
     * @param driver the command driver
     * @return this bootstrap
     * @throws NullPointerException when the driver is null
     * @throws IOException any io exception
     */
    public BetterBootstrap addToCmdPath(FSDriver<?> driver) throws IOException, NullPointerException {
        if (driver == null) {
            throw new NullPointerException("No null conf driver");
        }
        log.info("Added " + driver + " driver to command path");
        cmdFS.mount(driver);
        commandMounts.add(driver.toString())
        return this;
    }

    /**
     * Add a command path directory.
     *
     * @param path the command path
     * @return this bootstrap
     * @throws NullPointerException when the path argument is null
     * @throws IOException any io exception
     */
    public BetterBootstrap addToCmdPath(File path) throws NullPointerException, IOException {
        if (path == null) {
            throw new NullPointerException("No null command path");
        }
        log.info("Added " + path.getAbsolutePath() + " file to command path");
        cmdFS.mount(path);
        commandMounts.add(path.getAbsolutePath())
        return this;
    }

    /**
     * Add a command path directory.
     *
     * @param path the command path
     * @return this bootstrap
     * @throws NullPointerException when the path argument is null
     * @throws IOException any io exception
     * @throws URISyntaxException any uri syntax exception
     */
    public BetterBootstrap addToCmdPath(Path path) throws NullPointerException, IOException, URISyntaxException {
        if (path == null) {
            throw new NullPointerException("No null command path");
        }
        log.info("Added " + path.getValue() + " path to command path");
        cmdFS.mount(loader, path);
        commandMounts.add(path.getValue())
        return this;
    }

    public List<String> getCmdMounts(){
        return commandMounts;
    }

    @Override
    protected Map<String, FSMountFactory<?>> getMountFactories() {
        return drivers;
    }

    @Override
    protected String resolveConfMountPointConfig() {
        return confMountPointConfig != null ? confMountPointConfig : getDefaultConfMountPointConfig();
    }

    @Override
    protected String resolveCmdMountPointConfig() {
        return cmdMountPointConfig != null ? cmdMountPointConfig : getDefaultCmdMountPointConfig();
    }

    protected String getDefaultCmdMountPointConfig() {
        return "classpath:/crash/commands/";
    }

    protected String getDefaultConfMountPointConfig() {
        return "classpath:/crash/";
    }

    public void destroy() throws Exception {
        stop();
    }

}
