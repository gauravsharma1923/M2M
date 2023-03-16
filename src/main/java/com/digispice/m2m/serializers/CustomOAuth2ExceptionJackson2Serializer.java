package com.digispice.m2m.serializers;
import com.digispice.m2m.exception.models.CustomOAuth2Exception;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.Map;
import org.springframework.web.util.HtmlUtils;

public class CustomOAuth2ExceptionJackson2Serializer extends StdSerializer<CustomOAuth2Exception> {
	
private static final long serialVersionUID = 1L;

public CustomOAuth2ExceptionJackson2Serializer() 
{
	  super(CustomOAuth2Exception.class); 
 }

  public void serialize(CustomOAuth2Exception value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
    
	jgen.writeStartObject();
    jgen.writeStringField("returnCode", "error");
    jgen.writeStringField("remarks", value.getOAuth2ErrorCode());
    
    String errorMessage = value.getMessage();
    if (errorMessage != null) 
    {
       errorMessage = HtmlUtils.htmlEscape(errorMessage);
    }
    jgen.writeStringField("description", errorMessage);
    
    if (value.getAdditionalInformation() != null) {
      for (Map.Entry<String, String> entry : value.getAdditionalInformation().entrySet()) {
        String key = (String)entry.getKey();
        String add = (String)entry.getValue();
        jgen.writeStringField(key, add);
      } 
    }
    jgen.writeEndObject();
  }
}
