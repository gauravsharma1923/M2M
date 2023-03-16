package com.digispice.m2m.utilities;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.digispice.m2m.exception.models.BadRequestException;
import com.digispice.m2m.exception.models.ConflictException;
import com.digispice.m2m.exception.models.EntityNotFoundException;
import com.digispice.m2m.exception.models.ResourceNotFoundException;

public final class RestPreconditions {
	protected final static Log logger = LogFactory.getLog(RestPreconditions.class);
    private RestPreconditions() {
        throw new AssertionError();
    }

      public static <T> T checkNotNull(final T reference) {
        return checkNotNull(reference, null);
    }
    
    public static void checkEntityExists(final boolean entityExists) {
        if (entityExists) {
            throw new ConflictException();
        }
    }

 
    public static <T> T checkNotNull(final T reference, final String message) {
        if (reference == null) {
            throw new ResourceNotFoundException(message);
        }
        return reference;
    }

    
    public static <T> T checkRequestElementNotNull(final T reference) {
        return checkRequestElementNotNull(reference, null);
    }
    
    public static <T> T checkEntityExists(final T entity) {
        if (entity == null) {
            throw new EntityNotFoundException();
        }
        return entity;
    }

    
    public static <T> T checkRequestElementCount(final T reference) {
        return checkRequestElementNotNull(reference, null);
    }
  
    public static <T> T checkRequestElementNotNull(final T reference, final String message) {
        if (reference == null || reference =="") {
        	 logger.error("Bad Request Exception");
            throw new BadRequestException(message);
        }
        return reference;
    }
    
    public static <T> T checkRequestElementIsValid(final T reference, final String message) {
    	if(reference != null && reference.toString().matches("\\d+") 
    			&& reference.toString().length()>9 && reference.toString().length()<14)
    	 {  
    		  return reference;
          
        }
    	else
    	{     logger.error("Bad Request Exception");
    		  throw new BadRequestException(reference.toString(), message);
    	}
    }
    public static void checkEntityExists(final int count, Long msisdn, String message) {
        if (count >0) {
            throw new ConflictException(message, msisdn);
        }
      
    }


}
