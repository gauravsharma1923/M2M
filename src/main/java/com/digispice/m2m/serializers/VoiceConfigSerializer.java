package com.digispice.m2m.serializers;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.digispice.m2m.dto.VoiceConfigDto;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;


	public class VoiceConfigSerializer extends StdSerializer<Set<VoiceConfigDto>> {
		
		private Logger logger = LoggerFactory.getLogger(getClass());
		
		private static final long serialVersionUID = 1L;
		
		private  Set<VoiceConfigDto> voiceConfigDto;//=new HashSet<VoiceConfigDto>();
		
		private List<Long> call_list_o;
		
		private List<Long> call_list_t;
		
		public VoiceConfigSerializer() { 
	        this(null); 
	    } 

	    public VoiceConfigSerializer(Class<?> vc) { 
	        super((Class<Set<VoiceConfigDto>>) vc); 
	    }
		
		
		
		/*@SuppressWarnings("unchecked")
		public VoiceConfigSerializer() {
			this((Class<Set<VoiceConfigDto>>) voiceConfigDto.getClass());     
		}
		
		protected VoiceConfigSerializer(Class<Set<VoiceConfigDto>> t) {
		    super(t);
		}
		*/
	
		
		@Override
		public synchronized void serialize(Set<VoiceConfigDto> voiceConfigDto, final JsonGenerator jsonGenerator,final SerializerProvider provider) throws IOException {
			
			try
			{
					voiceConfigDto=new HashSet<VoiceConfigDto>();
					jsonGenerator.writeStartObject();
					
				    call_list_o=new ArrayList<Long>(voiceConfigDto.size());
				    
			        call_list_t=new ArrayList<Long>(voiceConfigDto.size());
			        
			        call_list_o=voiceConfigDto.stream().filter(v->v.getProv_flag()==0).map(VoiceConfigDto::getProv_msisdn).collect(Collectors.toList());
			        
					call_list_t=voiceConfigDto.stream().filter(v->v.getProv_flag()==1).map(VoiceConfigDto::getProv_msisdn).collect(Collectors.toList());
					
					if(call_list_o.size()>0)
					{	
			            jsonGenerator.writeObjectField("list_o", call_list_o);
					}    
			        
					jsonGenerator.writeObjectField("list_t", call_list_t);
					
					jsonGenerator.writeEndObject();
		   	
			}
		   
	        catch(JsonParseException e)
			{  
				logger.error("Exception["+e+"]");
				
			}
		}
		
	} 

