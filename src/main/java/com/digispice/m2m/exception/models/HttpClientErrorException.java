package com.digispice.m2m.exception.models;
import org.springframework.http.HttpStatus;

public final class HttpClientErrorException extends RuntimeException {
	
   private static final long serialVersionUID = 1L;
   
   private ApiErrorResponsePojo apiError = null;

   public HttpClientErrorException() 
   {
	   super();
   }
   
   public HttpClientErrorException(String message, Throwable cause) {
      super(message, cause);
   }

   public HttpClientErrorException(String message) {
      super(message);
      this.apiError = new ApiErrorResponsePojo(HttpStatus.UNAUTHORIZED.value(), "error", "invalid_token", message);
   }

   public HttpClientErrorException(Throwable cause) {
      super(cause);
   }

   public ApiErrorResponsePojo getApiError() {
      return this.apiError;
   }

   public void setApiError(ApiErrorResponsePojo apiError)
   {
      this.apiError = apiError;
   }
}
   
