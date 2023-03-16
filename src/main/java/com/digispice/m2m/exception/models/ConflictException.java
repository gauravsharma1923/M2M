package com.digispice.m2m.exception.models;

import org.springframework.http.HttpStatus;

public final class ConflictException extends RuntimeException {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ApiErrorResponsePojo apiError=null;
    public ConflictException() {
        super();
    }

    public ConflictException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ConflictException(final String message, final Long msisdn) {
      
        super(message);
        this.setApiError( new ApiErrorResponsePojo( msisdn.toString(), "error","Conflict", "Msisdn is already present in the system"));
    }
    
    public ConflictException(final String message, final String category) {
        
        super(message);
        this.setApiError( new ApiErrorResponsePojo( category, "error","Conflict", message));
    }
    
    
    public ConflictException(final String message) {
        
        super(message);
        this.setApiError(new ApiErrorResponsePojo(HttpStatus.BAD_REQUEST.value(), message));
    }


    public ConflictException(final Throwable cause) {
        super(cause);
    }

	public ApiErrorResponsePojo getApiError() {
		return apiError;
	}

	public void setApiError(ApiErrorResponsePojo apiError) {
		this.apiError = apiError;
	}

}
