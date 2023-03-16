package com.digispice.m2m.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.io.Serializable;
import com.digispice.m2m.deserializers.CustomDeSerializerWrapper;

@JsonDeserialize(using = CustomDeSerializerWrapper.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserMasterWrapperDto implements Serializable
{
         private static final long serialVersionUID = 1L;
         
         @JsonProperty("msisdn")
         private Long msisdn;

		 public Long getMsisdn() {
			return msisdn;
		 }

		 public void setMsisdn(Long msisdn) {
			this.msisdn = msisdn;
		 }
         
}