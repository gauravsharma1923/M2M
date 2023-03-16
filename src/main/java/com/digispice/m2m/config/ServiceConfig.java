package com.digispice.m2m.config;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "com.digispice.m2m.service" })
public class ServiceConfig {

    public ServiceConfig() {
        super();
    }
   
    
}
