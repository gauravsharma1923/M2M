package com.digispice.m2m.main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.DispatcherServlet;

import com.digispice.m2m.config.FilterConfig;
import com.digispice.m2m.config.PersistenceJpaConfig;
import com.digispice.m2m.config.SecurityConfig;
import com.digispice.m2m.config.ServiceConfig;
import com.digispice.m2m.config.WebConfig;
import com.digispice.m2m.config.AuthenticationManagerProvider;
import com.digispice.m2m.config.AuthorizationServerConfiguration;

@SpringBootApplication

@Import({ 
    
    PersistenceJpaConfig.class,
    ServiceConfig.class,
    SecurityConfig.class,
    FilterConfig.class,
    AuthenticationManagerProvider.class, 
    AuthorizationServerConfiguration.class,
    WebConfig.class
   
})

public class M2MAPI extends SpringBootServletInitializer{

    public static void main(String[] args){
    	

    	SpringApplication.run(M2MAPI.class, args);
		
	}
    
   
    
}
