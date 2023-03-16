package com.digispice.m2m.exception.models;

public final class InvalidGrantException extends CustomOAuth2Exception {
	
  private static final long serialVersionUID = 1L;
  
  private ApiErrorResponsePojo apiError = null;
  
  public InvalidGrantException(String msg) {
    super(msg);

  }
 
  public InvalidGrantException(String msg, Throwable t) {
	  super(msg, t); 
 }

}
