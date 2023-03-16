package com.digispice.m2m.config;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "com.digispice.m2m.filter" })
public class FilterConfig {

    public FilterConfig() {
        super();
    }
   
    
}
