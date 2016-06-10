package com.metashift.template.rythm

import groovy.util.logging.Slf4j
import org.rythmengine.RythmEngine
import org.rythmengine.conf.RythmConfigurationKey
import org.rythmengine.extension.ITemplateResourceLoader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
/**
 * Created by navid on 1/19/15.
 * You can provide a configuration file with any of the templates properties defined her
 * http://rythmengine.org/doc/configuration.md
 */
@Configuration
@Slf4j
class RythmConfig {

    private String RESOURCE_LOADER_PATH_KEY = 'resources-loader-path'

    @Autowired
    private RythmConfigHolder holder

    @Autowired
    private ResourceLoader resourceLoader

    @Bean
    public SpringI18nMessageResolver springI18nMessageResolver() {
        new SpringI18nMessageResolver()
    }

    @Bean
    public RythmEngine rythm(SpringI18nMessageResolver springI18nMessageResolver) {
        // merge user props with internal
        Map<String, Object> p = new HashMap<String, Object>()
        if (holder?.config) {
            p << holder.config
        }else{
            log.warn('No Rythm config was found')
        }

        // Set a resources loader path, if required.
        List<ITemplateResourceLoader> loaders = null;
        if (p.containsKey(RESOURCE_LOADER_PATH_KEY)) {

            loaders = new ArrayList<ITemplateResourceLoader>();
            String[] paths = StringUtils.commaDelimitedListToStringArray((String) p[RESOURCE_LOADER_PATH_KEY]);
            for (String path : paths) {
                loaders.add(new SpringResourceLoader(path, resourceLoader));
            }
            p.put(RythmConfigurationKey.RESOURCE_LOADER_IMPLS.getKey(), loaders);
            p.put(RythmConfigurationKey.RESOURCE_DEF_LOADER_ENABLED.getKey(), false);
        }

        p.put(RythmConfigurationKey.I18N_MESSAGE_RESOLVER.getKey(), springI18nMessageResolver);

        // Apply properties to RythmEngine.
        RythmEngine engine = new RythmEngine(p)

        if (null != loaders) {
            for (ITemplateResourceLoader loader : loaders) {
                loader.setEngine(engine);
            }
        }

        return engine
    }

}

    @Component
    @ConfigurationProperties(prefix = "rythm")
    class RythmConfigHolder {
        private Map<String, String> config

        Map<String, String> getConfig() {
            return config
        }

        void setConfig(Map<String, String> config) {
            this.config = config
        }
    }
