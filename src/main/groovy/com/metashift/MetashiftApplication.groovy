package com.metashift
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration
import org.springframework.context.annotation.PropertySource

@SpringBootApplication
@EnableAutoConfiguration(exclude = [JmxAutoConfiguration])
@PropertySource(value = ['classpath:default.properties', 'file:${properties.location}'],
                ignoreResourceNotFound = true)
class MetashiftApplication {

    static void main(String[] args) {
        SpringApplication sa = new SpringApplication(MetashiftApplication)
        sa.setShowBanner false
        sa.setWebEnvironment false
        sa.run args
    }

}
