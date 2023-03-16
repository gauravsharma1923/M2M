package com.digispice.m2m.model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

@JsonSerialize(using = com.digispice.m2m.serializers.CustomOAuthExceptionSerializer.class)
public class CustomOAuthException extends OAuth2Exception
{
  
	private static final long serialVersionUID = 1L;
  
    public CustomOAuthException(String msg)
    { 
    	super(msg); 
    }
}
