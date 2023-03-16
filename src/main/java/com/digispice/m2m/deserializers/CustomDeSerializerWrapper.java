package com.digispice.m2m.deserializers;
import com.digispice.m2m.dto.UserMasterWrapperDto;
import com.digispice.m2m.exception.models.BadRequestException;
import com.digispice.m2m.utilities.RestPreconditions;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CustomDeSerializerWrapper extends StdDeserializer<UserMasterWrapperDto> {
           
	       private Logger logger = LoggerFactory.getLogger(getClass());
   
           private static final long serialVersionUID = 1L;
  
           private  UserMasterWrapperDto userMasterWrapperDto;
           //= new UserMasterWrapperDto();

   		public CustomDeSerializerWrapper() { 
   	        this(null); 
   	    } 

   	    public CustomDeSerializerWrapper(Class<?> vc) { 
   	        super(vc); 
   	    }
   		
           
        /*   protected CustomDeSerializerWrapper(Class<UserMasterWrapperDto> t) 
           { 
	            super(t); 
	            
           }
 

           @SuppressWarnings("unchecked")
           public CustomDeSerializerWrapper()
           {
	        
	           this((Class<UserMasterWrapperDto>) userMasterWrapperDto.getClass());
	        }
           
  
          */  public synchronized UserMasterWrapperDto deserialize(final JsonParser parser, final DeserializationContext deserializer) throws IOException {
    
	           try 
	           {
	        	   	 userMasterWrapperDto = new UserMasterWrapperDto();
	        	   	 
			         ObjectMapper objectMapper = new ObjectMapper();
			         
			         JsonNode rootNode = (JsonNode)objectMapper.readTree(parser);
			      
			         if (rootNode.has("msisdn"))
			         {
			            logger.info("Deserializing Delete/GET User Block");
			        
			            JsonNode msisdn = rootNode.path("msisdn");
		 
         
                        if (msisdn.textValue() == null)
                        {
 
                            throw new BadRequestException(msisdn.textValue(), "Bad Request Format");
                        }

                        if (msisdn.textValue().trim() != null)
                        {
 
                           RestPreconditions.checkRequestElementIsValid(msisdn.textValue().trim(), "Bad Request Format");
                        }
 
                         userMasterWrapperDto.setMsisdn(Long.valueOf(msisdn.asLong()));

			         }

    
               }
               catch (JsonParseException e) 
	           {
                  logger.error("Error[" + e + "]");
      
                  throw new BadRequestException(null, "Bad Request Format");
               } 

    
          return userMasterWrapperDto;
  
            }


   }



