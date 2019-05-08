/**
 * 
 */
package com.sainsbury.serversidetest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * An config class for loading properties from file
 * 
 * @author szetop
 */
@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Value("${target.url}")
    private String url;

    @Bean(name="url")
    public String getUrl(){
        return url;
    }
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
