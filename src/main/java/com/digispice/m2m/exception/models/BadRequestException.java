package com.digispice.m2m.exception.models;

import org.springframework.http.HttpStatus;

public final class BadRequestException extends RuntimeException {
   
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ApiErrorResponsePojo apiError=null;
    public BadRequestException() {
        super();
    }

    public BadRequestException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public BadRequestException(final String message) {
    	
        super(message);
        this.apiError = new ApiErrorResponsePojo(HttpStatus.BAD_REQUEST.value(), message);
    }
  public BadRequestException(final String msisdn,final String message) {
    	
        super(message);
        this.apiError = new ApiErrorResponsePojo(msisdn,HttpStatus.NOT_FOUND.value(), "error", message, "Requested number format is not correct");
   }
 
    public BadRequestException(final Throwable cause) {
        super(cause);
    }

	public ApiErrorResponsePojo getApiError() {
		return apiError;
	}

	public void setApiError(ApiErrorResponsePojo apiError) {
		this.apiError = apiError;
	}

}
