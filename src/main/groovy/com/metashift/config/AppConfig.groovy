package com.metashift.config
import com.metashift.crash.BetterBootstrap
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
/**
 * Created by navid on 1/16/15.
 */
@Configuration
class AppConfig {

    @Autowired
    private ApplicationContext applicationContext

    @Bean
    public GroovyRefreshableApplicationContext meta(){
        GroovyRefreshableApplicationContext context = new GroovyRefreshableApplicationContext();
        context.setDisplayName("Metashift Groovy Context")
        context.refresh()
        context.setParent(applicationContext)
        context
    }

    @Bean
    @ConfigurationProperties(prefix = "crash")
    @DependsOn('meta')
    public BetterBootstrap crashBootstrap() {
        new BetterBootstrap()
    }


    @Bean
    public ServiceLocator serviceLocator(){
        ServiceLocator.getCurrentInstance()
    }

}
