package com.digispice.m2m.security;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.digispice.m2m.exception.models.ApiErrorResponsePojo;
import com.fasterxml.jackson.databind.ObjectMapper;


@Component("restAuthenticationEntryPoint")
public final class RestAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private ObjectMapper jacksonObjectMapper;
	@Autowired 
	CustomUserDetailsService customUserDetailsService;
	 @Override
	    public final void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authException) throws IOException {
	    	   logger.info("Request["+request+"]");
	    	   String authorizationHeader = request.getHeader("Authorization");
	    	   logger.info("Header["+authorizationHeader+"]");
	    	   if(authorizationHeader ==null || authorizationHeader== "")
	    	   {
	    		  
	    		   response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	    		   response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	    		   response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
	    		   response.getWriter().write( generateJson(response));
	    	   }
	    	   
	    	   else
	    	   {   logger.info("AuthenticationException["+authException+"]");
		    	   response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	    		   response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	    		   response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
	    		   response.getWriter().write( generateJson(response));
	    		   generateJson(response);
	    	   }
	    
	    	       	    	
	 }
	    public void afterPropertiesSet() throws Exception {
	        setRealmName("M2M");
	        super.afterPropertiesSet();
	    }
	   public String generateJson(HttpServletResponse httpServletResponse)
	   {   
		  
	    ApiErrorResponsePojo apiError=new ApiErrorResponsePojo(HttpStatus.UNAUTHORIZED.value(), "error", "Authentication Failure", "Bad Username or Password");
	    String responseJson="NA";
 	   try {
            responseJson =  jacksonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(apiError);
          
          
        } catch (Exception e) {
            logger.error("Exception["+e.getStackTrace()+"]");
          
        }
	 return responseJson;
	   }
}
	  
