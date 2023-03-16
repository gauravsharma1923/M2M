package com.digispice.m2m.exception.models;
import com.digispice.m2m.serializers.CustomOAuth2ExceptionJackson1Serializer;
import com.digispice.m2m.serializers.CustomOAuth2ExceptionJackson2Serializer;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.exceptions.OAuth2ExceptionJackson1Deserializer;
import org.springframework.security.oauth2.common.exceptions.OAuth2ExceptionJackson2Deserializer;
import org.springframework.security.oauth2.common.exceptions.RedirectMismatchException;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedClientException;
import org.springframework.security.oauth2.common.exceptions.UnsupportedGrantTypeException;
import org.springframework.security.oauth2.common.exceptions.UnsupportedResponseTypeException;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;

@org.codehaus.jackson.map.annotate.JsonSerialize(using = CustomOAuth2ExceptionJackson1Serializer.class)
@org.codehaus.jackson.map.annotate.JsonDeserialize(using = OAuth2ExceptionJackson1Deserializer.class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = CustomOAuth2ExceptionJackson2Serializer.class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = OAuth2ExceptionJackson2Deserializer.class)

public class CustomOAuth2Exception extends OAuth2Exception {
	
   /**
	 * 
	 */
   private static final long serialVersionUID = 1L;
   public static final String ERROR = "error";
   public static final String DESCRIPTION = "error_description";
   public static final String URI = "error_uri";
   public static final String INVALID_REQUEST = "invalid_request";
   public static final String INVALID_CLIENT = "invalid_client";
   public static final String INVALID_GRANT = "invalid_grant";
   public static final String UNAUTHORIZED_CLIENT = "unauthorized_client";
   public static final String UNSUPPORTED_GRANT_TYPE = "unsupported_grant_type";
   public static final String INVALID_SCOPE = "invalid_scope";
   public static final String INSUFFICIENT_SCOPE = "insufficient_scope";
   public static final String INVALID_TOKEN = "invalid_token";
   public static final String REDIRECT_URI_MISMATCH = "redirect_uri_mismatch";
   public static final String UNSUPPORTED_RESPONSE_TYPE = "unsupported_response_type";
   public static final String ACCESS_DENIED = "access_denied";
   private Map<String, String> additionalInformation = null;

   public CustomOAuth2Exception(String msg, Throwable t) {
      super(msg, t);
   }

   public CustomOAuth2Exception(String msg) {
      super(msg);
   }

   public String getOAuth2ErrorCode() {
      return "invalid_token";
   }

   public int getHttpErrorCode() {
      return 401;
   }

   public Map<String, String> getAdditionalInformation() {
      return this.additionalInformation;
   }

   public void addAdditionalInformation(String key, String value) {
      if (this.additionalInformation == null) {
    	  this.additionalInformation = new TreeMap<String, String>();
      }

      this.additionalInformation.put(key, value);
   }

   public static OAuth2Exception create(String errorCode, String errorMessage) {
      if (errorMessage == null) {
         errorMessage = errorCode == null ? "OAuth Error" : errorCode;
      }
      
         System.out.println("ErrorCode:["+errorCode);
      if ("invalid_client".equals(errorCode)) {
         return new InvalidClientException(errorMessage);
      } else if ("unauthorized_client".equals(errorCode)) {
         return new UnauthorizedClientException(errorMessage);
      } else if ("invalid_grant".equals(errorCode)) {
         return new InvalidGrantException(errorMessage);
      } else if ("invalid_scope".equals(errorCode)) {
         return new InvalidScopeException(errorMessage);
      } else if ("invalid_token".equals(errorCode)) {
         return new InvalidTokenException(errorMessage);
      } else if ("invalid_request".equals(errorCode)) {
         return new InvalidRequestException(errorMessage);
      } else if ("redirect_uri_mismatch".equals(errorCode)) {
         return new RedirectMismatchException(errorMessage);
      } else if ("unsupported_grant_type".equals(errorCode)) {
         return new UnsupportedGrantTypeException(errorMessage);
      } else if ("unsupported_response_type".equals(errorCode)) {
         return new UnsupportedResponseTypeException(errorMessage);
      } else {
         return (OAuth2Exception)("access_denied".equals(errorCode) ? new UserDeniedAuthorizationException(errorMessage) : new CustomOAuth2Exception(errorMessage));
      }
   }

   public static OAuth2Exception valueOf(Map<String, String> errorParams) {
      String errorCode = (String)errorParams.get("error");
      String errorMessage = errorParams.containsKey("error_description") ? (String)errorParams.get("error_description") : null;
      OAuth2Exception ex = create(errorCode, errorMessage);
      Set<Entry<String, String>> entries = errorParams.entrySet();
      for (Map.Entry<String, String> entry : entries) {
			String key = entry.getKey();
         if (!"error".equals(key) && !"error_description".equals(key)) {
            ex.addAdditionalInformation(key, (String)entry.getValue());
         }
      }

      return ex;
   }

   public String toString() {
      return this.getSummary();
   }

   public String getSummary() {
      StringBuilder builder = new StringBuilder();
      String delim = "";
      builder.append(delim).append("returnCode=\"").append("error").append("\"");
      String error = this.getOAuth2ErrorCode();
      if (error != null) {
         builder.append(delim).append("remarks=\"").append(error).append("\"");
         delim = ", ";
      }

      String errorMessage = this.getMessage();
      if (errorMessage != null) {
         builder.append(delim).append("description=\"").append(errorMessage).append("\"");
         delim = ", ";
      }

      Map<String, String> additionalParams = this.getAdditionalInformation();
      if (additionalParams != null) {
    	  for (Map.Entry<String, String> param : additionalParams.entrySet()) {
    	        builder.append(delim).append((String)param.getKey()).append("=\"").append((String)param.getValue()).append("\"");
    	        delim = ", ";
    	      
         }
      }

      return builder.toString();
   }
}
    