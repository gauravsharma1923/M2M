package com.digispice.m2m.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import com.digispice.m2m.security.CustomUserDetailsService;
import com.digispice.m2m.security.RestAuthenticationEntryPoint;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;


@Configuration
@EnableResourceServer
@EnableWebSecurity
//
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
//@ComponentScan({ "com.baeldung.um.security" })
@ComponentScan({ "com.digispice.m2m.security" })


public class SecurityConfig extends ResourceServerConfigurerAdapter { 
      
    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    
    public SecurityConfig() {
        super();
    }

   
    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
    	 auth.authenticationProvider(authenticationProvider());
    }

    @Override
    public void configure(final HttpSecurity http) throws Exception {
      
		 http
		.csrf().disable()
	    .authorizeRequests()
		 .antMatchers("/m2m/login*").permitAll()
		 .antMatchers("/m2m/outh/access-token/").permitAll()
		.antMatchers("/m2m/users/**").authenticated().
		 and()
		.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint()).
		and().
		sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	 
      
    }
    

    @Bean
    public BasicAuthenticationEntryPoint authenticationEntryPoint(){
    	System.out.println("<<<<<<<<<<<<<<<");
    	RestAuthenticationEntryPoint entryPoint = new RestAuthenticationEntryPoint();
        return entryPoint;
    }
    
  
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
    	
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
     
    @Bean
    public PasswordEncoder passwordEncoder() {
    	
       return  new MessageDigestPasswordEncoder("SHA-256");
    }
}
