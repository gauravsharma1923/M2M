package com.digispice.m2m.exception;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.validation.ConstraintViolationException;
import org.apache.coyote.http11.HeadersTooLargeException;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.digispice.m2m.exception.models.ApiErrorResponsePojo;
import com.digispice.m2m.exception.models.BadRequestException;
import com.digispice.m2m.exception.models.ConflictException;
import com.digispice.m2m.exception.models.PreconditionFailedException;
import com.digispice.m2m.exception.models.ResourceNotFoundException;


@RestControllerAdvice
@ComponentScan({ "com.digispice.m2m.controller"})
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	
	
    @Override
    protected final ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.error("Bad Request: " + ex.getMessage());
        final ApiErrorResponsePojo apiError = new ApiErrorResponsePojo(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return handleExceptionInternal(ex, apiError, headers, HttpStatus.BAD_REQUEST, request);
    }
    
    @Override
	protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex,HttpHeaders headers, HttpStatus status, WebRequest request) {
    	    logger.info("Bad Request: " + ex.getMessage());
            final ApiErrorResponsePojo apiError = new ApiErrorResponsePojo(HttpStatus.BAD_REQUEST.value(), "Bad Request, Username or Password Missing");
            return handleExceptionInternal(ex, apiError, headers, HttpStatus.BAD_REQUEST, request);
	}
    
    @Override
	protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
    	  logger.error("Internal Server Error", ex);
          final ApiErrorResponsePojo apiError = new ApiErrorResponsePojo(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Something went wrong.Please try again later.");
		return handleExceptionInternal(ex, apiError, headers, status, request);
	}
    
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
			NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    	 logger.info("Not Found: " + ex.getMessage());
         final ApiErrorResponsePojo apiError = new ApiErrorResponsePojo(HttpStatus.NOT_FOUND.value(), ex.getMessage());
		return handleExceptionInternal(ex, apiError, headers, status, request);
	}
    
    @Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
   	    logger.info("Media Type Not Supported: " + ex.getMessage());
		List<MediaType> mediaTypes = ex.getSupportedMediaTypes();
		if (!CollectionUtils.isEmpty(mediaTypes)) {
			headers.setAccept(mediaTypes);
		}
		 final ApiErrorResponsePojo apiError = new ApiErrorResponsePojo(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), ex.getMessage());
		return handleExceptionInternal(ex, apiError, headers, status, request);
	}
    
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request)
    {
    	logger.info("Not Found: " + ex.getMessage());
        final ApiErrorResponsePojo apiError = new ApiErrorResponsePojo(HttpStatus.NOT_ACCEPTABLE.value(), ex.getMessage());
        logger.info("acceptable MIME type:" + MediaType.APPLICATION_JSON_VALUE);
		return handleExceptionInternal(ex, apiError, headers, status, request);
	}
  
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,HttpHeaders headers, HttpStatus status, WebRequest request) {

    	String  msisdn=null;
    	
    	logger.info("METHOD_NOT_ALLOWED " , ex);
        
        logger.info("RequestUri["+request.getDescription(false)+"]");
       
        msisdn=request.getDescription(false).substring(request.getDescription(true).lastIndexOf("/")+1);
        
        if(msisdn!=null && msisdn!="")
        {	
    	 
           logger.info("Msisdn["+msisdn+"]"); 
           
           final ApiErrorResponsePojo apiError = new ApiErrorResponsePojo(msisdn,HttpStatus.METHOD_NOT_ALLOWED.value(), "error", "Method Not Allowed", "Requested Operation not allowed");
           
           return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED, request);
        }  
        
        else
        {	
           final ApiErrorResponsePojo apiError = new ApiErrorResponsePojo(HttpStatus.METHOD_NOT_ALLOWED.value(), "error", "Method Not Allowed", "Requested Operation not allowed");
          
           return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED, request);
        }   
    }
   
    
    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request)
    {
    	
    	    logger.info("MissingPathVariable" + ex.getLocalizedMessage());
    	    
            final ApiErrorResponsePojo apiError = new ApiErrorResponsePojo(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
            
		    return handleExceptionInternal(ex, apiError, headers, status, request);
	}

    @ExceptionHandler(value = { ConstraintViolationException.class, DataIntegrityViolationException.class})
    protected final ResponseEntity<Object> handleBadRequest(final RuntimeException ex, final WebRequest request) {
        logger.info("Bad Request: " + ex.getLocalizedMessage());
        
        final ApiErrorResponsePojo apiError = new ApiErrorResponsePojo(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
  
    @ExceptionHandler({BadRequestException.class})
    protected final ResponseEntity<Object> handleBadRequests(final BadRequestException ex, final WebRequest request) {
    	
    	String msisdn=null;
    	
        logger.info("Bad Request: " + ex.getLocalizedMessage());
        
        logger.info("RequestUri["+request.getDescription(true)+"]");
        
       
        msisdn=request.getDescription(false).substring(request.getDescription(true).lastIndexOf("/")+1);
      
        if(msisdn!=null && msisdn!="" && !(msisdn.isEmpty()))
        {	
    	 
           logger.info("Msisdn["+msisdn+"]");   
           
    	   final ApiErrorResponsePojo apiError = new ApiErrorResponsePojo(msisdn,HttpStatus.NOT_FOUND.value(), "error", "Bad Request Format", "Requested number format is not correct");
    	   
    	   return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    	 
        }
        else
        {	
        	logger.info("else block");
        	
            return handleExceptionInternal(ex, ex.getApiError(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
        }  
    }

    @ExceptionHandler({ UsernameNotFoundException.class })
    protected ResponseEntity<Object> handleAuthenticationException(final UsernameNotFoundException ex, final WebRequest request) {
       
    	logger.error("401 Status Code", ex);
      
       final ApiErrorResponsePojo apiError = new ApiErrorResponsePojo(HttpStatus.UNAUTHORIZED.value(), "error", "Authentication Failure", "Bad Username or Password");
       
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

   @ExceptionHandler({ BadCredentialsException.class })
   protected ResponseEntity<Object> handleException(final BadCredentialsException ex, final WebRequest request) {
       
	   logger.error("401 Status Code", ex);
       
      final ApiErrorResponsePojo apiError = new ApiErrorResponsePojo(HttpStatus.UNAUTHORIZED.value(), "error", "Authentication Failure", "Bad Username or Password");
      logger.info("Response["+handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request)+"]");
      return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
       
   }
   
   @ExceptionHandler({ AccessDeniedException.class })
   protected ResponseEntity<Object> handleAccessDeniedException(final AccessDeniedException ex, final WebRequest request) {
       logger.error("401 Status Code", ex);
       final ApiErrorResponsePojo apiError = new ApiErrorResponsePojo(HttpStatus.UNAUTHORIZED.value(), "error", "Authentication Failure", "Bad Username or Password");
       return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
   }
   
    @ExceptionHandler({  ResourceNotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(final ResourceNotFoundException ex, final WebRequest request) {
        logger.warn("Not Found: " + ex.getMessage());
        return handleExceptionInternal(ex, ex.getApiError(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
   
    @ExceptionHandler({ InvalidDataAccessApiUsageException.class, DataAccessException.class })
    protected ResponseEntity<Object> handleConflict(final RuntimeException ex, final WebRequest request) {
        logger.warn("Conflict: " + ex.getMessage());
        final ApiErrorResponsePojo apiError = new ApiErrorResponsePojo(HttpStatus.CONFLICT.value(), ex.getMessage());
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
    
    @ExceptionHandler({ ConflictException.class })
    protected ResponseEntity<Object> handleConflictException(final ConflictException ex, final WebRequest request) {
        logger.warn("Conflict: " + ex.getMessage());
        return handleExceptionInternal(ex,  ex.getApiError(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
    
    @ExceptionHandler({ PreconditionFailedException.class })
    protected ResponseEntity<Object> handlePreconditionFailed(final RuntimeException ex, final WebRequest request) 
    {
        final String bodyOfResponse = "This should be application specific";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.PRECONDITION_FAILED, request);
    }
    
    @ExceptionHandler({ NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class, IOException.class,ServletException.class })
    protected ResponseEntity<Object> handle500s(final RuntimeException ex, final WebRequest request) {
        logger.info("500 Status Code", ex);
      
        final ApiErrorResponsePojo apiError = new ApiErrorResponsePojo(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Something went wrong.Please try again later.");
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
    
    
    @ExceptionHandler({ HeadersTooLargeException.class })
    protected ResponseEntity<ApiErrorResponsePojo> handleHeadersTooLarge(final HeadersTooLargeException ex, final WebRequest request) {
        logger.info("431 Status Code", ex);
        
        final ApiErrorResponsePojo apiError = new ApiErrorResponsePojo("431");
        
        return new  ResponseEntity<ApiErrorResponsePojo>(apiError,HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE);
      
    }
    
    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
    	    logger.info("500  Status Code", ex);
    	   final ApiErrorResponsePojo apiError = new ApiErrorResponsePojo(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Something went wrong.Please try again later.");
		return handleExceptionInternal(ex, apiError, headers, status, request);
	}
    
  @ExceptionHandler({ Exception.class })
    protected ResponseEntity<Object> handleException(final RuntimeException ex, final WebRequest request) {
        logger.info("500 Status Code"+ ex);
       
        final ApiErrorResponsePojo apiError = new ApiErrorResponsePojo(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Something went wrong.Please try again later.");
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

  
    
}
