package com.digispice.m2m.serializers;
import com.digispice.m2m.exception.models.CustomOAuth2Exception;
import java.io.IOException;
import java.util.Map;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.springframework.web.util.HtmlUtils;

public class CustomOAuth2ExceptionJackson1Serializer extends JsonSerializer<CustomOAuth2Exception> {
	
  public void serialize(CustomOAuth2Exception value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
    jgen.writeStartObject();
    jgen.writeStringField("returnCode", "error");
    jgen.writeStringField("remarks", value.getOAuth2ErrorCode());
    String errorMessage = value.getMessage();
    if (errorMessage != null) {
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
