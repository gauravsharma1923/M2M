package com.digispice.m2m.controller;
import java.util.Arrays;
import java.util.WeakHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.digispice.m2m.entity.CCIUserDetails;
import com.digispice.m2m.service.impl.CCIUserServiceImpl;
import com.digispice.m2m.utilities.RestPreconditions;
import org.springframework.core.env.Environment;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

@RestController
@PropertySource({"classpath:persistence-dev.properties"})
public class AuthenticationController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
    private CCIUserServiceImpl userService;
	
	@Autowired
	private Environment env;

    private MessageDigestPasswordEncoder passwordEncoder= new MessageDigestPasswordEncoder("SHA-256");
    
    private WeakHashMap<String, String> hashMap=new WeakHashMap<String, String>();
    
    private  ResponseEntity<WeakHashMap<String, String>> responseEntity=null;
     
    public AuthenticationController(){
        super();
    }
    
    @PostMapping(value ="/login/")
    @ResponseStatus(HttpStatus.OK)
    public String createAuthentication(@RequestHeader(name="username", required=true) String username,  @RequestHeader(name="password", required=true) String password) throws UsernameNotFoundException{      
    	   
    	    RestPreconditions.checkRequestElementNotNull(username.trim(), "Bad Request,Username Missing");
    	    
            RestPreconditions.checkRequestElementNotNull(password.trim(), "Bad Request, Password Missing");
           
            CCIUserDetails userDetails = userService.findByName(username.trim());
    	    
    	    if (userDetails == null) {
         	    
          	   throw new UsernameNotFoundException("Authentication Failure, Bad Username or Password");
 		    }
    	            
		    else{        
		    	       
		    	        boolean passMatch=passwordEncoder.matches(password.trim(), userDetails.getPassword());
		    	       System.out.println(passMatch+":::::::::::::::::::::");
		    	        if(!passMatch)
		    	        {
		    	        	 logger.error("Authentication failed: password does not match stored value");
		    	        	 
						     throw new BadCredentialsException("Authentication Failure");
		    	        }
		    	        
		    	        ResponseEntity<String> response = null;
		    	            
		    	        RestTemplate restTemplate = new RestTemplate();
		    	       
		    	        String credentials = "digispice:RCFnISRwaWNlIUAjQCQ0ODVA";
		    	             
		    	        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
		    	         
		    	        HttpHeaders headers = new HttpHeaders();
		    	        
		    	        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		    	        
		    	        headers.add("Authorization", "Basic " + encodedCredentials);
		    	          
		    	        HttpEntity<String> request = new HttpEntity<String>(headers);
		    	        
		    	       String access_token_url = "http://localhost:" + this.env.getProperty("server.port") + "/m2m/oauth/token";
		    	        //String access_token_url = "http://localhost:8080/m2m/oauth/token";
		    	        
		    	        access_token_url = access_token_url + "?grant_type=password";
		    	        
		    	        access_token_url = access_token_url + "&client_id=digispice";
		    	        
		    	        access_token_url = access_token_url + "&username=" + username.trim();
		    	        
		    	        access_token_url = access_token_url + "&password=" + password.trim();
		    	        //System.out.println("<<<<<<<<<<<<<"+access_token_url);
		    	        
		    	        response = restTemplate.exchange(access_token_url, HttpMethod.POST, request, String.class, new Object[0]);
		    	        
		    	        this.logger.info("Access Token Response[" + response.getBody() + "]");
		    	    
		    	        return response.getBody();
		    	    }
				   
		        }	
    	  }
  	   
    	
   
   


