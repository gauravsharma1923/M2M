package com.digispice.m2m.deserializers;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.digispice.m2m.dto.SmsConfigDto;
import com.digispice.m2m.dto.UserMasterUpdateDto;
import com.digispice.m2m.dto.VoiceConfigDto;
import com.digispice.m2m.exception.models.BadRequestException;
import com.digispice.m2m.utilities.RestPreconditions;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public  class CustomDeSerializerBeforeUpdate extends StdDeserializer<UserMasterUpdateDto> {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final long serialVersionUID = 1L;
	
private   UserMasterUpdateDto userMasterupdateDto;
//=new UserMasterUpdateDto();

	//private Object lock_object=new Object();
	
	public CustomDeSerializerBeforeUpdate() { 
        this(null); 
    } 

    public CustomDeSerializerBeforeUpdate(Class<?> vc) { 
        super(vc); 
    }
	
	/*protected CustomDeSerializerBeforeUpdate(Class<UserMasterUpdateDto> t) {
		super(t);
	}*/
	/*@SuppressWarnings("unchecked")
	public CustomDeSerializerBeforeUpdate() {
		this((Class<UserMasterUpdateDto>) userMasterupdateDto.getClass()); 
		
		
	}
	*/
    public synchronized UserMasterUpdateDto deserialize(final JsonParser parser, final DeserializationContext deserializer) throws IOException {
    	
   // public synchronized UserMasterUpdateDto deserialize(final JsonParser parser, final DeserializationContext deserializer) throws IOException {
		try
		{    
                userMasterupdateDto =new UserMasterUpdateDto();

			//logger.info("msisdn["+userMasterDto.getMsisdn()+"]");
		
			Set<SmsConfigDto> smsConfig = new LinkedHashSet<SmsConfigDto>();

			Set<VoiceConfigDto> voiceConfig = new LinkedHashSet<VoiceConfigDto>();

			ObjectMapper objectMapper = new ObjectMapper();

			JsonNode rootNode = objectMapper.readTree(parser);
			
			
			JsonNode msisdn = rootNode.path("msisdn");
			 
			      if (msisdn.textValue() == null)
			   {
		        throw new BadRequestException(msisdn.textValue(), "Bad Request Format");
			      }
			
			      if (msisdn.textValue().trim() != null)
			     {
			       RestPreconditions.checkRequestElementIsValid(msisdn.textValue().trim(), "Bad Request Format");
			      }
			      

			JsonNode sms_list_o_Node = rootNode.path("sms").path("list_o");

			Iterator<JsonNode> nodeElements_sms_mo = sms_list_o_Node.elements();

			while(nodeElements_sms_mo.hasNext())
			{
				SmsConfigDto  smsConfigDto_mo =new SmsConfigDto();

				JsonNode list_sms_mo= nodeElements_sms_mo.next();

				smsConfigDto_mo.setProv_flag(0);

				JsonNode prov_msisdn_sms_mo_old = list_sms_mo.path("old");

				if(prov_msisdn_sms_mo_old.textValue()!=null && prov_msisdn_sms_mo_old.textValue()!="")
				{
					smsConfigDto_mo.setProv_msisdn_sms(prov_msisdn_sms_mo_old.textValue().trim());
					logger.info("SMS_MO_old::"+msisdn.textValue().trim()+":::"+prov_msisdn_sms_mo_old.textValue().trim());

				
				}
				else
				{
					smsConfigDto_mo.setProv_msisdn_sms("NA");
					logger.info("SMS_MO_old::"+msisdn.textValue().trim()+":::NA");

				
				}

				JsonNode prov_msisdn_sms_mo_new = list_sms_mo.path("new");

				if(prov_msisdn_sms_mo_new.textValue()!=null && prov_msisdn_sms_mo_new.textValue()!="")
				{
					smsConfigDto_mo.setProv_msisdn_sms_new(prov_msisdn_sms_mo_new.textValue().trim());
					logger.info("SMS_MO_NEW::"+msisdn.textValue().trim()+":::"+prov_msisdn_sms_mo_new.textValue().trim());
				}

				else 
				{
					smsConfigDto_mo.setProv_msisdn_sms_new("NA");
					logger.info("SMS_MO_NEW::"+msisdn.textValue().trim()+":::NA");
					
				}

				smsConfig.add(smsConfigDto_mo);

		  

			}

			JsonNode sms_list_t_Node = rootNode.path("sms").path("list_t");

			Iterator<JsonNode> nodeElements_sms_mt = sms_list_t_Node.elements();

			while(nodeElements_sms_mt.hasNext())
			{

				SmsConfigDto  smsConfigDto_mt =new SmsConfigDto();

				JsonNode list_sms_mt = nodeElements_sms_mt.next();

				smsConfigDto_mt.setProv_flag(1);

				JsonNode prov_msisdn_sms_mt_old = list_sms_mt.path("old");

		

				if(prov_msisdn_sms_mt_old.textValue()!=null && prov_msisdn_sms_mt_old.textValue()!="")
				{
					smsConfigDto_mt.setProv_msisdn_sms(prov_msisdn_sms_mt_old.textValue().trim());
					logger.info("SMS_Mt_old::"+msisdn.textValue().trim()+":::"+prov_msisdn_sms_mt_old.textValue().trim());

				
				}
				else
				{
					smsConfigDto_mt.setProv_msisdn_sms("NA");
					logger.info("SMS_Mt_old::"+msisdn.textValue().trim()+":::NA");


					
				}
				JsonNode prov_msisdn_sms_mt_new = list_sms_mt.path("new");

		

				if(prov_msisdn_sms_mt_new.textValue()!=null && prov_msisdn_sms_mt_new.textValue()!="")
				{
					smsConfigDto_mt.setProv_msisdn_sms_new(prov_msisdn_sms_mt_new.textValue().trim());
					logger.info("SMS_Mt_new::"+msisdn.textValue().trim()+":::"+prov_msisdn_sms_mt_new.textValue().trim());

				}

				else
				{   
					smsConfigDto_mt.setProv_msisdn_sms_new("NA");
					logger.info("SMS_Mt_new::"+msisdn.textValue().trim()+":::NA");

					
					
				}

				smsConfig.add(smsConfigDto_mt);

				

			}

			JsonNode voice_list_o_Node = rootNode.path("call").path("list_o");

			Iterator<JsonNode> nodeElements_call_mo = voice_list_o_Node.elements();

			while(nodeElements_call_mo.hasNext())
			{

				VoiceConfigDto voiceConfigDto_mo =new VoiceConfigDto();

				JsonNode list_voice_mo = nodeElements_call_mo.next();

				voiceConfigDto_mo.setProv_flag(0);

				JsonNode prov_msisdn_voice_mo_old = list_voice_mo.path("old");

				if(prov_msisdn_voice_mo_old.textValue()!=null && prov_msisdn_voice_mo_old.textValue()!="")
				{   
					voiceConfigDto_mo.setProv_msisdn_voice(prov_msisdn_voice_mo_old.textValue().trim());
					logger.info("voice_Mo_old::"+msisdn.textValue().trim()+":::"+prov_msisdn_voice_mo_old.textValue().trim());


					
				}

				else
				{
					voiceConfigDto_mo.setProv_msisdn_voice("NA");
					logger.info("voice_Mo_old::"+msisdn.textValue().trim()+":::NA");

					
				}

				JsonNode prov_msisdn_voice_mo_new = list_voice_mo.path("new");

				if(prov_msisdn_voice_mo_new.textValue()!=null && prov_msisdn_voice_mo_new.textValue()!="" )
				{
					voiceConfigDto_mo.setProv_msisdn_voice_new(prov_msisdn_voice_mo_new.textValue().trim());
					logger.info("voice_Mo_new::"+msisdn.textValue().trim()+":::"+prov_msisdn_voice_mo_new.textValue().trim());

					
				}

				else
				{
					voiceConfigDto_mo.setProv_msisdn_voice_new("NA");
					logger.info("voice_Mo_new::"+msisdn.textValue().trim()+":::NA");

				}

				    voiceConfig.add(voiceConfigDto_mo);


			}

			JsonNode voice_list_t_Node = rootNode.path("call").path("list_t");
			
			Iterator<JsonNode> nodeElements_call_mt = voice_list_t_Node.elements();
			
			while(nodeElements_call_mt.hasNext())
			{
				VoiceConfigDto voiceConfigDto_mt =new VoiceConfigDto();
				
				JsonNode list_voice_mt = nodeElements_call_mt.next();
				
				voiceConfigDto_mt.setProv_flag(1);
				
				JsonNode prov_msisdn_voice_mt_old = list_voice_mt.path("old");
				
				if(prov_msisdn_voice_mt_old.textValue()!=null && prov_msisdn_voice_mt_old.textValue()!="")
				{

					voiceConfigDto_mt.setProv_msisdn_voice(prov_msisdn_voice_mt_old.textValue().trim());
					logger.info("voice_Mt_old::"+msisdn.textValue().trim()+":::"+prov_msisdn_voice_mt_old.textValue().trim());

					
				}
				else
				{

					voiceConfigDto_mt.setProv_msisdn_voice("NA");
					logger.info("voice_Mt_old::"+msisdn.textValue().trim()+":::NA");

					
					
				}
				
				JsonNode prov_msisdn_voice_mt_new = list_voice_mt.path("new");
				
				
				
				if(prov_msisdn_voice_mt_new.textValue()!=null && prov_msisdn_voice_mt_new.textValue()!="")
				{
					voiceConfigDto_mt.setProv_msisdn_voice_new(prov_msisdn_voice_mt_new.textValue().trim());
					logger.info("voice_Mt_new::"+msisdn.textValue().trim()+":::"+prov_msisdn_voice_mt_new.textValue().trim());

				}
				else
				{
					voiceConfigDto_mt.setProv_msisdn_voice_new("NA");
					logger.info("voice_Mt_new::"+msisdn.textValue().trim()+":::NA");

				}
				
				voiceConfig.add(voiceConfigDto_mt);

				

			}
			//synchronized(lock_object)
			{
		    	  userMasterupdateDto.setMsisdn(Long.valueOf(msisdn.asLong()));
		    	  
		    	  userMasterupdateDto.setVoiceConfig(voiceConfig);
				  userMasterupdateDto.setSmsConfig(smsConfig);
		    }
			
			//logger.info(userMasterupdateDto.getMsisdn()+"::::::::"+userMasterupdateDto.getSmsConfig().toString()+"::::::"+userMasterupdateDto.getVoiceConfig().toString());

		}

		catch(JsonParseException e)

		{   


			logger.error("Error["+e+"]");
			
		    throw new BadRequestException(null, "Bad Request Format");
		}
		logger.info(">>>>>>>>>>>>>"+userMasterupdateDto.getMsisdn()+"::::::::"+userMasterupdateDto.getSmsConfig().toString()+"::::::"+userMasterupdateDto.getVoiceConfig().toString());

		return userMasterupdateDto;
	}



}



