package com.digispice.m2m.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthenticationManagerProvider extends WebSecurityConfigurerAdapter
{
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception
  {

	  return super.authenticationManagerBean();
  }

  @Bean
  public PasswordEncoder passwordEncoder()
  {
	  return new MessageDigestPasswordEncoder("SHA-256"); 
  }


}


