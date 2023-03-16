package com.digispice.m2m.serializers;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.digispice.m2m.dto.SmsConfigDto;
import com.digispice.m2m.dto.VoiceConfigDto;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

	public class SmsConfigSerializer extends StdSerializer<Set<SmsConfigDto>> {
		
		private Logger logger = LoggerFactory.getLogger(getClass());
		
		private static final long serialVersionUID = 1L;
		
		private  Set<SmsConfigDto> smsConfigDto;//=new HashSet<SmsConfigDto>();
		
        private List<Long> sms_list_o;
        
		private List<Long> sms_list_t;
		
		/*@SuppressWarnings("unchecked")
		public SmsConfigSerializer() {
			this((Class<Set<SmsConfigDto>>) smsConfigDto.getClass());     
		}
		
		protected SmsConfigSerializer(Class<Set<SmsConfigDto>> t) {
		    super(t);
		}*/
		public SmsConfigSerializer() { 
	        this(null); 
	    } 

	    public SmsConfigSerializer(Class<?> vc) { 
	        super((Class<Set<SmsConfigDto>>) vc); 
	    }
	
		
		@Override
		public synchronized void  serialize(Set<SmsConfigDto> smsConfigDto,final JsonGenerator jsonGenerator, final SerializerProvider provider) throws IOException {
			try
			{
				smsConfigDto=new HashSet<SmsConfigDto>();
				
				jsonGenerator.writeStartObject();
				
				sms_list_o=new ArrayList<Long>(smsConfigDto.size());
				
				sms_list_t=new ArrayList<Long>(smsConfigDto.size());
				
				sms_list_o=smsConfigDto.stream().filter(v->v.getProv_flag()==0).map(SmsConfigDto::getProv_msisdn).collect(Collectors.toList());
				
				sms_list_t=smsConfigDto.stream().filter(v->v.getProv_flag()==1).map(SmsConfigDto::getProv_msisdn).collect(Collectors.toList());
				
				if(sms_list_o.size()>0)
				{	
		           jsonGenerator.writeObjectField("list_o", sms_list_o);
				}   
		        
				if(sms_list_t.size()>0)
				{		
				   jsonGenerator.writeObjectField("list_t", sms_list_t);
				}   
				
				jsonGenerator.writeEndObject();
		   			  
				
			}
		   
	        catch(JsonParseException e)
			
			{   
	        
				logger.error("Error["+e+"]");
			}
		}
		
	} 

