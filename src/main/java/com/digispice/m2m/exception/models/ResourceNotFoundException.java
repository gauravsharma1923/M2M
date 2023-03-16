package com.digispice.m2m.exception.models;
import org.springframework.http.HttpStatus;

public final class ResourceNotFoundException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ApiErrorResponsePojo apiError=null;
	
    public ApiErrorResponsePojo getApiError() {
		return apiError;
	}

	public void setApiError(ApiErrorResponsePojo apiError) {
		this.apiError = apiError;
	}

	public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(final String message) {
        super(message);
    }
    public ResourceNotFoundException( final String message,  final String msisdn) {
        super(message);
        this.apiError = new ApiErrorResponsePojo(msisdn,HttpStatus.NOT_FOUND.value(), "error", "Not Found", "Msisdn is not present in the system");
    }
    public ResourceNotFoundException(final Throwable cause) {
        super(cause);
    }

}
