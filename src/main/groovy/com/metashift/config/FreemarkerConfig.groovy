package com.metashift.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean

/**
 * Created by navid on 4/10/15.
 */
@Configuration
class FreemarkerConfig {

    @Bean
    public FreeMarkerConfigurationFactoryBean configuration(){
        FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean()
        bean
    }

}
