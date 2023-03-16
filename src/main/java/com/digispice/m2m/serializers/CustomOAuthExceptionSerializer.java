package com.digispice.m2m.serializers;
import com.digispice.m2m.model.CustomOAuthException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;


public class CustomOAuthExceptionSerializer extends StdSerializer<CustomOAuthException>
{
  private static final long serialVersionUID = 1L;
  
  public CustomOAuthExceptionSerializer() { 
	  
	  super(CustomOAuthException.class); 
	  
  }


  public void serialize(CustomOAuthException exception, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
    jsonGenerator.writeStartObject();
    
    jsonGenerator.writeObjectField("returnCode", "error");
    jsonGenerator.writeObjectField("remarks", exception.getOAuth2ErrorCode());
    jsonGenerator.writeObjectField("description", exception.getMessage());
    jsonGenerator.writeEndObject();
  }
}
