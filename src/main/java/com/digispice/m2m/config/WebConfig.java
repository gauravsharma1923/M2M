package com.digispice.m2m.config;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;

//@SuppressWarnings("deprecation")

@Configuration
//@EnableAutoConfiguration 
@ComponentScan({  "com.digispice.m2m.controller", "com.digispice.m2m.exception" })
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {
//public class WebConfig implements WebMvcConfigurer  {

    public WebConfig() {
    	
    }

 
    @Override
    public void extendMessageConverters(final List<HttpMessageConverter<?>> converters) {
        final java.util.Optional<HttpMessageConverter<?>> jsonConverterFound = converters.stream().filter(c -> c instanceof MappingJackson2HttpMessageConverter).findFirst();
        if (jsonConverterFound.isPresent()) 
        {
           
            final AbstractJackson2HttpMessageConverter converter = (AbstractJackson2HttpMessageConverter) jsonConverterFound.get();
            converter.getObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            converter.getObjectMapper().enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            converter.getObjectMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        }  
  }
    @Bean
    public ModelMapper modelMapper()
    {
        return new ModelMapper();
    }
    

    }
