package com.digispice.m2m.controller;
import com.digispice.m2m.entity.CCIUserDetails;
import com.digispice.m2m.exception.models.ApiErrorResponsePojo;
import com.digispice.m2m.exception.models.HttpClientErrorException;
import com.digispice.m2m.service.impl.CCIUserServiceImpl;
import com.digispice.m2m.utilities.RestPreconditions;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@PropertySource({"classpath:persistence-dev.properties"})
public class RefreshTokenController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  private CCIUserServiceImpl userService;
  
  @Autowired
  private Environment env;
  
  @Autowired
  private ObjectMapper jacksonObjectMapper;
  
  private MessageDigestPasswordEncoder passwordEncoder = new MessageDigestPasswordEncoder("SHA-256");


  @PostMapping({"/oauth/access-token/"})
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public String fetchAccessToken(@RequestHeader(name = "username", required = true) String username, @RequestHeader(name = "password", required = true) String password, @RequestHeader(name = "refresh_token", required = true) String refresh_token) throws UsernameNotFoundException {
    
	RestPreconditions.checkRequestElementNotNull(username.trim(), "Bad Request,Username Missing");
    
    RestPreconditions.checkRequestElementNotNull(password.trim(), "Bad Request, Password Missing");
    
    CCIUserDetails userDetails = this.userService.findByName(username.trim());
    
    ResponseEntity<String> response = null;
    
    if (userDetails == null)
    {
      
      throw new UsernameNotFoundException("Authentication Failure, Bad Username or Password");
    }
    
    else
    { 	
    
		    boolean passMatch = this.passwordEncoder.matches(password.trim(), userDetails.getPassword());
		    
		    if (!passMatch) 
		    {
		      
		      this.logger.error("Authentication failed: password does not match stored value");
		      
		      throw new BadCredentialsException("Authentication Failure");
		    } 
	
		    else
		    { 	
	
	    
			    RestPreconditions.checkRequestElementNotNull(refresh_token.trim(), "Bad Request,Refresh Token Missing");
			    
			    RestTemplate restTemplate = new RestTemplate();
			    
			    String credentials = "digispice:RCFnISRwaWNlIUAjQCQ0ODVA";
			    
			    String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
			    
			    HttpHeaders headers = new HttpHeaders();
			    
			    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			    
			    headers.add("Authorization", "Basic " + encodedCredentials);
			    
			    HttpEntity<String> request = new HttpEntity<String>(headers);
			    
			    String access_token_url = "http://localhost:" + env.getProperty("server.port") + "/m2m/oauth/token";
			    
			    access_token_url = access_token_url + "?grant_type=refresh_token";
			    
			    access_token_url = access_token_url + "&refresh_token=" + refresh_token;
	
	    
	            try 
	            {
	                  response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class, new Object[0]);
	      
	                  logger.info("Access Token Response[" + response.getBody() + "]");
	    
	            }
	            
	            catch (Exception e)
	            {
	      
	              logger.info("Exception[" + e.getMessage() + "]");
	      
	              throw new HttpClientErrorException("Invalid Refresh Token:" + refresh_token);
	              
	            } 
	
	           return response.getBody();
	       }
	    
        }
   }

  public String generateJson(ResponseEntity<String> response, String refresh_token)
  {
    
		 ApiErrorResponsePojo apiError = new ApiErrorResponsePojo(response.getStatusCode().value(), "error", "invalid_refreh_token", "Invalid Refresh Token :" + refresh_token);
	    
	     String responseJson = null;
	
	    try 
	    {
	       responseJson = this.jacksonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(apiError);
	    }
	    catch (Exception e)
	    {
	      
	      logger.error("Exception[" + e.getStackTrace() + "]");
	      
	    } 
	    
	    return responseJson;
  }
  
}
