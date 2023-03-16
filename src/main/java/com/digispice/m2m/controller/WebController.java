package com.digispice.m2m.controller;
import javax.validation.Valid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.digispice.m2m.dto.UserMasterDto;
import com.digispice.m2m.dto.UserMasterUpdateDto;
import com.digispice.m2m.dto.UserMasterWrapperDto;
import com.digispice.m2m.exception.models.ResourceNotFoundException;
import com.digispice.m2m.model.ApiResponse;
import com.digispice.m2m.model.ApiResponsePojo;
import com.digispice.m2m.service.impl.UserServiceImpl;
import com.digispice.m2m.utilities.RestPreconditions;

@RestController
//@RequestMapping("/users")
public class WebController {

	protected final Log logger = LogFactory.getLog(getClass());
	
	private ResponseEntity<Object> responseEntity=null;
	
	@Autowired 
	private UserServiceImpl userService;
	
	private ApiResponsePojo apiResponsePojo;
	private Object lock_object=new Object();
	
	 // @PostMapping(value = {"/users/details/"}, consumes = MediaType.APPLICATION_JSON_VALUE)
	  @PostMapping(value = {"/users/details/"}, consumes = MediaType.APPLICATION_JSON_VALUE)
      
	  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
	  public ResponseEntity<Object> fetchUser(@Valid @RequestBody final UserMasterWrapperDto resourcepost)  throws OAuth2Exception
	  {   
		 
		  ApiResponsePojo apiResponsePojo_fetchobj;
		  RestPreconditions.checkRequestElementNotNull(resourcepost);
	      
		  apiResponsePojo_fetchobj = userService.findMsisdn(resourcepost);
		  
		  responseEntity =new ResponseEntity<Object>(apiResponsePojo_fetchobj,HttpStatus.OK);
		  
		  return responseEntity;
	  }
	  
	
	  //@DeleteMapping(value = {"/users/"}, consumes = MediaType.APPLICATION_JSON_VALUE)
	    
	  @DeleteMapping(value = {"/users/"}, consumes = MediaType.APPLICATION_JSON_VALUE)
	   @Secured("ROLE_ADMIN")
	   public ResponseEntity<Object> delete(@Valid @RequestBody UserMasterWrapperDto resourcedelete)
	   
	   {
		
	        try
	        {       
	        	  
	        	    RestPreconditions.checkRequestElementNotNull(resourcedelete);
		        	
		        	ApiResponse responsePojo = userService.deleteUserByMsisdn(resourcedelete);
		        	
		        	responseEntity =new ResponseEntity<Object>(responsePojo,HttpStatus.OK);
		        	
		            return responseEntity;
	        }
	       
	        catch(ResourceNotFoundException e) 
	        {
	           throw new ResourceNotFoundException("Msisdn is not present in the system", resourcedelete.getMsisdn().toString());
	        }
	   }
	  	
	   // @PostMapping(value = {"/users/"}, consumes = MediaType.APPLICATION_JSON_VALUE)
	    
	    @PostMapping(value = {"/users/"}, consumes = MediaType.APPLICATION_JSON_VALUE)
	    @Secured("ROLE_ADMIN")
	    public  ResponseEntity<Object> createUser( @Valid @RequestBody final UserMasterDto resourcecreate) throws HttpRequestMethodNotSupportedException
		
		{
			
	    	 RestPreconditions.checkRequestElementNotNull(resourcecreate);
	    	 
	    	 apiResponsePojo= userService.createUser(resourcecreate); 
	    	 
	    	 if(apiResponsePojo.getReturnCode().equalsIgnoreCase("success"))
	    	 {		 
	            responseEntity =new ResponseEntity<Object>(apiResponsePojo,HttpStatus.CREATED);
	    	 }
	    	 
	    	 else if(apiResponsePojo.getReturnCode().equalsIgnoreCase("Multi-Status"))
	    	 {
	    		  responseEntity =new ResponseEntity<Object>(apiResponsePojo,HttpStatus.MULTI_STATUS);
	    	 }
	    	
	    	 
	    	 return responseEntity;
			
	    	
	    }
		
	   // @PutMapping(value = {"/users/"}, consumes = MediaType.APPLICATION_JSON_VALUE)
	    
	    @PutMapping(value = {"/users/"}, consumes = MediaType.APPLICATION_JSON_VALUE)
	    @Secured("ROLE_ADMIN")
	    public  ResponseEntity<Object> addUserWrapper( @Valid @RequestBody final UserMasterDto resourceadd) throws HttpRequestMethodNotSupportedException
		{    
			  
	    	ApiResponsePojo apiResponsePojo_addobj;
				 RestPreconditions.checkRequestElementNotNull(resourceadd);
	    	    
				 apiResponsePojo_addobj= userService.addUserWrapper(resourceadd);
				 
		    	 if(apiResponsePojo_addobj.getReturnCode().equalsIgnoreCase("success"))
		    	 {		 
					 responseEntity =new ResponseEntity<Object>(apiResponsePojo_addobj,HttpStatus.CREATED);
		    	 }
		    	 else if(apiResponsePojo_addobj.getReturnCode().equalsIgnoreCase("Multi-Status"))
		    	 {
		    		 responseEntity =new ResponseEntity<Object>(apiResponsePojo_addobj,HttpStatus.MULTI_STATUS);
		    	 }
		    	 
		    	
	    	     return responseEntity;
			
			 
	    }
		
	    //@PatchMapping(value = {"/users/"}, consumes = MediaType.APPLICATION_JSON_VALUE)
	    
		@PatchMapping(value = {"/users/"}, consumes = MediaType.APPLICATION_JSON_VALUE)
	    @Secured("ROLE_ADMIN")
		public  ResponseEntity<Object> modifyUser( @Valid @RequestBody  final UserMasterUpdateDto resourcemodify) throws HttpRequestMethodNotSupportedException
		{   
				ApiResponsePojo apiResponsePojo_obj;
				 RestPreconditions.checkRequestElementNotNull(resourcemodify);
				 //synchronized(lock_object) 
				 {
					 apiResponsePojo_obj= userService.modifyUser(resourcemodify); 
				 }
				 //logger.info("Patch User:: "+apiResponsePojo_obj.getMsisdn()+"::"+apiResponsePojo_obj.getReturnCode()+apiResponsePojo_obj.getDescription());
				
				 if(apiResponsePojo_obj.getReturnCode().equalsIgnoreCase("success"))
		    	 {		 
					 responseEntity =new ResponseEntity<Object>(apiResponsePojo_obj,HttpStatus.OK);
		    	 }
		    	 else if(apiResponsePojo_obj.getReturnCode().equalsIgnoreCase("Multi-Status"))
		    	 {
		    		 responseEntity =new ResponseEntity<Object>(apiResponsePojo_obj,HttpStatus.MULTI_STATUS);
		    	 }
				
				 return responseEntity;
			 
			 
		
        }

}