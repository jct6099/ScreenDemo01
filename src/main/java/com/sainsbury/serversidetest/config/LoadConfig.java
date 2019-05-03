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
 * @author Patrick
 */
@Configuration
@PropertySource("classpath:application.properties")
public class LoadConfig {

    @Value("${target.url}")
    private String url;

    @Bean(name="appConfig")
    public AppConfig getConfig(){
    	AppConfig conf = new AppConfig();
        conf.setUrl(url);
        return conf;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
