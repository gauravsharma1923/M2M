package com.digispice.m2m.exception.models;
import org.springframework.http.HttpStatus;

public final class EntityNotFoundException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private ApiErrorResponsePojo apiError=null;
	public EntityNotFoundException() {
        super();
    }

    public EntityNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EntityNotFoundException(final String message) {
        super(message);
    }
    
    public EntityNotFoundException(final String message, final String msisdn) {
        super(message);
        this.setApiError(new ApiErrorResponsePojo(msisdn,HttpStatus.NOT_FOUND.value(), "error", "Not Found", "Msisdn is not present in the system"));
    }

    public EntityNotFoundException(final Throwable cause) {
        super(cause);
    }

	public ApiErrorResponsePojo getApiError() {
		return apiError;
	}

	public void setApiError(ApiErrorResponsePojo apiError) {
		this.apiError = apiError;
	}

}
