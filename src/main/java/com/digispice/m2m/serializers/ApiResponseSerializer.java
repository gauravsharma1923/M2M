package com.digispice.m2m.serializers;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.digispice.m2m.dto.SmsConfigDto;
import com.digispice.m2m.model.ApiResponsePojo;
import com.digispice.m2m.model.ApiResponsePojo.Sms;
import com.digispice.m2m.model.ApiResponsePojo.Voice;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;


public class ApiResponseSerializer extends StdSerializer<ApiResponsePojo> {

	private  ApiResponsePojo responsePojo;// =new ApiResponsePojo();
	
	
	public ApiResponseSerializer() { 
        this(null); 
    } 

    public ApiResponseSerializer(Class<?> vc) { 
        super( (Class<ApiResponsePojo>) vc); 
    }

	/*@SuppressWarnings("unchecked")
	public ApiResponseSerializer() {
		this((Class<ApiResponsePojo>)responsePojo.getClass());     
	}
	
	protected ApiResponseSerializer(Class<ApiResponsePojo> t) {
		super(t);

	}
*/
	private Logger logger = LoggerFactory.getLogger(getClass());
	private static final long serialVersionUID = 1L;
	private static Set<Voice> voiceConfig=new HashSet<Voice>();
	private static Set<Sms> smsConfig=new HashSet<Sms>();
	private List<String> call_list_o;
	private List<String> call_list_t;
	private List<String> sms_list_o;
	private List<String> sms_list_t;

	@Override
	public synchronized void serialize(final ApiResponsePojo apiResponsePojo, final JsonGenerator jsonGenerator, SerializerProvider provider) {
		try
		{   
			responsePojo =new ApiResponsePojo();
					
			jsonGenerator.writeStartObject();
			jsonGenerator.writeStringField("product", apiResponsePojo.getProduct());
			jsonGenerator.writeStringField("version", apiResponsePojo.getVersion());
			jsonGenerator.writeStringField("release", apiResponsePojo.getRelease().toString());
			jsonGenerator.writeStringField("msisdn",  apiResponsePojo.getMsisdn());
			
			if(!apiResponsePojo.getAction().equalsIgnoreCase("getUser"))
			{

				jsonGenerator.writeStringField("returnCode", apiResponsePojo.getReturnCode());
				jsonGenerator.writeStringField("remarks", apiResponsePojo.getRemarks());
				jsonGenerator.writeObjectFieldStart("description");
			
				if(apiResponsePojo.getVoice()==null)
				{
					logger.error("VoiceConfig["+"apiResponsePojo.getVoice().isEmpty()]");
			     }
				if(apiResponsePojo.getVoice()!=null && apiResponsePojo.getVoice().size()>0)
				{   
					
					voiceConfig =apiResponsePojo.getVoice();
					
				    call_list_o =new ArrayList<String>(voiceConfig.size());
				    
					call_list_t =new ArrayList<String>(voiceConfig.size());
					
					jsonGenerator.writeObjectFieldStart("call");
					
				    call_list_o=voiceConfig.stream().filter(v->v.getProv_flag()==0).map(Voice::getProv_msisdn).collect(Collectors.toList());
				    
					call_list_t=voiceConfig.stream().filter(v->v.getProv_flag()==1).map(Voice::getProv_msisdn).collect(Collectors.toList());
				
					if(call_list_o.size()>0)
					{		
					jsonGenerator.writeObjectField("list_o", call_list_o);
					}
					if(call_list_t.size()>0)
					{	
					jsonGenerator.writeObjectField("list_t", call_list_t);
					}
					jsonGenerator.writeEndObject();
				}
				
				if(apiResponsePojo.getSms()==null)
				{
					logger.error("SmsConfig["+"apiResponsePojo.getSms().isEmpty()]");
			     }
				if(apiResponsePojo.getSms()!=null && apiResponsePojo.getSms().size()>0)
				{   
					 
					smsConfig =apiResponsePojo.getSms();
					//logger.info("SmsConfig Size["+smsConfig.size()+"]");
					sms_list_o  =new ArrayList<String>(smsConfig.size());
					sms_list_t =new ArrayList<String>(smsConfig.size());
					jsonGenerator.writeObjectFieldStart("sms");
					sms_list_o=smsConfig.stream().filter(s->s.getProv_flag()==0).map(Sms::getProv_msisdn).collect(Collectors.toList());
					sms_list_t=smsConfig.stream().filter(s->s.getProv_flag()==1).map(Sms::getProv_msisdn).collect(Collectors.toList());
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
				jsonGenerator.writeEndObject();

			}

			else
			{   jsonGenerator.writeStringField("imsi"  ,  apiResponsePojo.getImsi());
				if(apiResponsePojo.getVoice().size()>0)
				{
					voiceConfig =apiResponsePojo.getVoice();
					
					call_list_o =new ArrayList<String>(voiceConfig.size());
					call_list_t =new ArrayList<String>(voiceConfig.size());
					jsonGenerator.writeObjectFieldStart("call");
				
					call_list_o=voiceConfig.stream().filter(v->v.getProv_flag()==0).map(Voice::getProv_msisdn).collect(Collectors.toList());
					call_list_t=voiceConfig.stream().filter(v->v.getProv_flag()==1).map(Voice::getProv_msisdn).collect(Collectors.toList());
				
					if(call_list_o.size()>0)
					{	
					jsonGenerator.writeObjectField("list_o", call_list_o);
					}
					if(call_list_t.size()>0)
					{	
					jsonGenerator.writeObjectField("list_t", call_list_t);
					}
					jsonGenerator.writeEndObject();
				}
				if(apiResponsePojo.getSms().size()>0)
				{
					smsConfig =apiResponsePojo.getSms();
					logger.info("SmsConfig Size["+smsConfig.size()+"]");
					sms_list_o  =new ArrayList<String>(smsConfig.size());
					sms_list_t =new ArrayList<String>(smsConfig.size());
					jsonGenerator.writeObjectFieldStart("sms");
					sms_list_o=smsConfig.stream().filter(s->s.getProv_flag()==0).map(Sms::getProv_msisdn).collect(Collectors.toList());
					sms_list_t=smsConfig.stream().filter(s->s.getProv_flag()==1).map(Sms::getProv_msisdn).collect(Collectors.toList());
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
				
			}
			jsonGenerator.writeEndObject();
		}

		catch(Exception e)

		{  
			logger.error("Error["+e+"]");
			

		}

	} 

}