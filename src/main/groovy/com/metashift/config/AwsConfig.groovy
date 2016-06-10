package com.metashift.config

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created by navid on 3/23/15.
 */
@Configuration
public class AwsConfig {

    @Bean
    AWSCredentialsProvider awsCredentialsProvider() {
        return new DefaultAWSCredentialsProviderChain();
    }


}
