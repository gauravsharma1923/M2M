package com.digispice.m2m.exception.models;


public final class InvalidTokenException extends CustomOAuth2Exception {
	
	
   private static final long serialVersionUID = 1L;
   
   private ApiErrorResponsePojo apiError = null;
   
   public InvalidTokenException(String message) {
	      super(message);
	      
   }

   public InvalidTokenException(String msg, Throwable t) {
      super(msg, t);
   }

   public ApiErrorResponsePojo getApiError() {
	return apiError;
   }

   public void setApiError(ApiErrorResponsePojo apiError) {
	this.apiError = apiError;
  }
   
}