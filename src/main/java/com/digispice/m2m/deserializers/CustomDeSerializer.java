package com.digispice.m2m.deserializers;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.digispice.m2m.dto.SmsConfigDto;
import com.digispice.m2m.dto.UserMasterDto;
import com.digispice.m2m.dto.VoiceConfigDto;
import com.digispice.m2m.exception.models.BadRequestException;
import com.digispice.m2m.utilities.RestPreconditions;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

	public class CustomDeSerializer extends StdDeserializer<UserMasterDto> {
		
		private Logger logger = LoggerFactory.getLogger(getClass());
		
		private static final long serialVersionUID = 1L;
		
		private  UserMasterDto userMasterDto;
		//=new UserMasterDto();
		
		public CustomDeSerializer() { 
	        this(null); 
	    } 

	    public CustomDeSerializer(Class<?> vc) { 
	        super(vc); 
	    }
		
		/*protected CustomDeSerializer(Class<UserMasterDto> t) {
		    super(t);
		}
		@SuppressWarnings("unchecked")
		public CustomDeSerializer() {
			this((Class<UserMasterDto>) userMasterDto.getClass());     
		}*/
		public synchronized UserMasterDto deserialize(final JsonParser parser, final DeserializationContext deserializer) throws IOException {
			try
			{    
					userMasterDto=new UserMasterDto();
					
					Set<SmsConfigDto> smsConfig = new LinkedHashSet<SmsConfigDto>();
					
					Set<VoiceConfigDto> voiceConfig = new LinkedHashSet<VoiceConfigDto>();
					
					ObjectMapper objectMapper = new ObjectMapper();
					
				
					JsonNode rootNode = objectMapper.readTree(parser);
					
					if(rootNode.has("msisdn"))
					{	
		                logger.info("Deserializing Add User Block");
		                
						JsonNode msisdn = rootNode.path("msisdn");
		            
		                 
		            if(msisdn.textValue()==null)
		            {
			             	
			              
			              throw new BadRequestException(msisdn.textValue(), "Bad Request Format");
		            }
		            
		            else if(msisdn.textValue().trim()!=null)
		            { 	
		            	
		            
		            	
		                RestPreconditions.checkRequestElementIsValid(msisdn.textValue().trim(), "Bad Request Format");
		            }
					
					
		            JsonNode imsi = rootNode.path("imsi");
		       
	            
		            JsonNode name = rootNode.path("username");
		         
		          
		            if ((name.textValue() == null) || (name.textValue() == ""))
		                    {
		                      this.logger.info("Username" + name.textValue());
		                  throw new BadRequestException(msisdn.textValue().trim(), "Bad Request Format");
		                    }
		            
		            JsonNode category =rootNode.path("category");
		         
		            if(category.asLong()==0)
		            {	
			             
			              throw new BadRequestException(msisdn.textValue().trim(), "Bad Request Format");
		            }
	            
		            userMasterDto.setMsisdn(msisdn.asLong());
		            
		           
		            userMasterDto.setUsername(name.textValue().trim());
		            
		            userMasterDto.setCategory(category.asLong());
		            
		        
		            
		            
		            if(imsi.textValue()!=null && imsi.textValue()!="" && imsi.textValue().matches("\\d+") )
		            {	
		                userMasterDto.setImsi(imsi.asLong());
		            }
		            
		           
		           else if(imsi.textValue()!=null && !(imsi.textValue().matches("\\d+")) && imsi.textValue()!="")	
		            {	
		            	 throw new BadRequestException(msisdn.textValue().trim(), "Bad Request Format");
			        }
	            
		            else
		            {
		            	  userMasterDto.setImsi(Long.valueOf(0));
		            }
		            
					
					
					
	            
		            JsonNode sms_list_o_Node = rootNode.path("sms").path("list_o");
		            
		            Iterator<JsonNode> nodeElements_sms_mo = sms_list_o_Node.elements();
		            
		            while(nodeElements_sms_mo.hasNext())
		            {
			        	
		            	SmsConfigDto  smsConfigDto_mo =new SmsConfigDto();
		            	
			        	JsonNode list_sms_mo= nodeElements_sms_mo.next();
			        	
			          	smsConfigDto_mo.setUsr_msisdn(msisdn.asLong());
			          	
			          	smsConfigDto_mo.setProv_flag(0);
			          	
			          	if(list_sms_mo.textValue().trim().equals(""))
			          	{
			          		logger.info("Msisdn_Sms_mo["+list_sms_mo.textValue().trim());
			          		continue;
			          	}
			          	smsConfigDto_mo.setProv_msisdn_sms(list_sms_mo.textValue().trim());
			          	
			            smsConfig.add(smsConfigDto_mo);
		          	
	                }
	          
			          JsonNode sms_list_t_Node = rootNode.path("sms").path("list_t");
			          
			          Iterator<JsonNode> nodeElements_sms_mt = sms_list_t_Node.elements();
			          
			          while(nodeElements_sms_mt.hasNext())
			          {
			        	  
				          SmsConfigDto  smsConfigDto_mt =new SmsConfigDto();
				          
				          JsonNode list_sms_mt = nodeElements_sms_mt.next();
				          
				          smsConfigDto_mt.setUsr_msisdn(msisdn.asLong());
				          
				          smsConfigDto_mt.setProv_flag(1);
				          
				          if(list_sms_mt.textValue().trim().equals(""))
				          {	  
				        	  logger.info("Msisdn_sms_mt["+list_sms_mt.textValue().trim());
				        	  
				        	  continue;
				          }
				          
				          smsConfigDto_mt.setProv_msisdn_sms(list_sms_mt.textValue().trim());
				          
				          smsConfig.add(smsConfigDto_mt);
				          
	          
	                  }
	          
			          JsonNode voice_list_o_Node = rootNode.path("call").path("list_o");
			          Iterator<JsonNode> nodeElements_call_mo = voice_list_o_Node.elements();
			          
			          while(nodeElements_call_mo.hasNext())
			          {
				        	
			        	    VoiceConfigDto voiceConfigDto_mo =new VoiceConfigDto();
				          	JsonNode list_voice_mo = nodeElements_call_mo.next();
				        	voiceConfigDto_mo.setUsr_msisdn(msisdn.asLong());
				          	voiceConfigDto_mo.setProv_flag(0);
				          	if(list_voice_mo.textValue().trim().equals(""))
				          	{	
				          		logger.info("Msisdn_Voice_Mo["+list_voice_mo.textValue().trim()+"]");
				          	    continue;
				          	}   
				          	
				          	voiceConfigDto_mo.setProv_msisdn_voice(list_voice_mo.textValue());
				          	voiceConfig.add(voiceConfigDto_mo);
				          	   
			          	
			          }
	          
			          JsonNode voice_list_t_Node = rootNode.path("call").path("list_t");
			          
			          Iterator<JsonNode> nodeElements_call_mt = voice_list_t_Node.elements();
			          
			          while(nodeElements_call_mt.hasNext())
			          {
				          VoiceConfigDto voiceConfigDto_mt =new VoiceConfigDto();
				          
				          JsonNode list_voice_mt = nodeElements_call_mt.next();
				          
				          voiceConfigDto_mt.setUsr_msisdn(msisdn.asLong());
				          
				          voiceConfigDto_mt.setProv_flag(1);
				          if(list_voice_mt.textValue().trim().equals(""))
				          {
				        	  logger.info("Msisdn_Voice_Mt["+list_voice_mt.textValue().trim()+"]");
				        	  continue;
				          }
				          voiceConfigDto_mt.setProv_msisdn_voice(list_voice_mt.textValue().trim());
				          
				          voiceConfig.add(voiceConfigDto_mt);
	          	
	                  }
	         
				          userMasterDto.setVoiceConfig(voiceConfig);
				          
				          userMasterDto.setSmsConfig(smsConfig);
					}
					
					
					//add Userwrapper if msisdn key is not present
					
					if(!(rootNode.has("msisdn")))
					{	
		                
						logger.info("Deserializing Add User Wrapper Block");
						
			            JsonNode imsi = rootNode.path("imsi");
			       
		            
			            JsonNode name = rootNode.path("username");
			         
			            
			            if(name.textValue()==null)
			            {	
			              	
			              throw new BadRequestException(null, "Bad Request Format");
			            }
			            
			            JsonNode category =rootNode.path("category");
			         
			            if(category.asLong()==0)
			            {	
				             
				              throw new BadRequestException(null, "Bad Request Format");
			            }
	            
		           		            
		            userMasterDto.setUsername(name.textValue());
		            
		            userMasterDto.setCategory(category.asLong());
		            
		        
		            
		            
		            if(imsi.textValue()!=null && imsi.textValue()!="" && imsi.textValue().matches("\\d+") )
		            {	
		                userMasterDto.setImsi(imsi.asLong());
		            }
		            
		          
		            else if(imsi.textValue()!=null && !(imsi.textValue().matches("\\d+")) && imsi.textValue()!="")	
		            {	
		            	 throw new BadRequestException(null, "Bad Request Format");
			        }
	            
		            else
		            {
		            	  userMasterDto.setImsi(Long.valueOf(0));
		            }
		
	            
		            JsonNode sms_list_o_Node = rootNode.path("sms").path("list_o");
		            
		            Iterator<JsonNode> nodeElements_sms_mo = sms_list_o_Node.elements();
		            
		            while(nodeElements_sms_mo.hasNext())
		            {
			        	
		            	SmsConfigDto  smsConfigDto_mo =new SmsConfigDto();
		            	
			        	JsonNode list_sms_mo= nodeElements_sms_mo.next();
			        	
			     
			          	
			          	smsConfigDto_mo.setProv_flag(0);
			          	
			          	if(list_sms_mo.textValue().trim().equals(""))
			          	{
			          		logger.info("Msisdn_sms_mo["+list_sms_mo.textValue()+"");
			          		continue;
			          	}
			          	smsConfigDto_mo.setProv_msisdn_sms(list_sms_mo.textValue().trim());
			          	
			            smsConfig.add(smsConfigDto_mo);
		          	
	                }
	          
			          JsonNode sms_list_t_Node = rootNode.path("sms").path("list_t");
			          
			          Iterator<JsonNode> nodeElements_sms_mt = sms_list_t_Node.elements();
			          
			          while(nodeElements_sms_mt.hasNext())
			          {
			        	  
				          SmsConfigDto  smsConfigDto_mt =new SmsConfigDto();
				          
				          JsonNode list_sms_mt = nodeElements_sms_mt.next();
				          
				 
				          smsConfigDto_mt.setProv_flag(1);
				          
				          if(list_sms_mt.textValue().trim().equals(""))
				          {
				        	  logger.info("Msisdn_Sms_Mt["+list_sms_mt.textValue().trim()+"]");
				        	  continue;
				          }
				          smsConfigDto_mt.setProv_msisdn_sms(list_sms_mt.textValue().trim());
				          
				          smsConfig.add(smsConfigDto_mt);
				          
	          
	                  }
	          
			          JsonNode voice_list_o_Node = rootNode.path("call").path("list_o");
			          Iterator<JsonNode> nodeElements_call_mo = voice_list_o_Node.elements();
			          
			          while(nodeElements_call_mo.hasNext())
			          {
				        	
			        	    VoiceConfigDto voiceConfigDto_mo =new VoiceConfigDto();
				          	JsonNode list_voice_mo = nodeElements_call_mo.next();
				       
				          	voiceConfigDto_mo.setProv_flag(0);
				          	if(list_voice_mo.textValue().trim().equals(""))
				          	{
				          		logger.info("Msisdn_Voice_Mo["+list_voice_mo.textValue().trim()+"]");
				          		continue;
				          	}
				          	voiceConfigDto_mo.setProv_msisdn_voice(list_voice_mo.textValue().trim());
				          	voiceConfig.add(voiceConfigDto_mo);
			          	
			          }
	          
			          JsonNode voice_list_t_Node = rootNode.path("call").path("list_t");
			          
			          Iterator<JsonNode> nodeElements_call_mt = voice_list_t_Node.elements();
			          
			          while(nodeElements_call_mt.hasNext())
			          {
				          VoiceConfigDto voiceConfigDto_mt =new VoiceConfigDto();
				          
				          JsonNode list_voice_mt = nodeElements_call_mt.next();
				          
				          voiceConfigDto_mt.setProv_flag(1);
				          if(list_voice_mt.textValue().trim().equals(""))
				          {
				        	  logger.info("Msisdn_Voice_mt["+list_voice_mt.textValue().trim()+"]");
				        	  continue;
				          }
				          voiceConfigDto_mt.setProv_msisdn_voice(list_voice_mt.textValue().trim());
				          
				          voiceConfig.add(voiceConfigDto_mt);
	          	
	                  }
	         
				          userMasterDto.setVoiceConfig(voiceConfig);
				          
				          userMasterDto.setSmsConfig(smsConfig);
					}
					
			}
		   
	        catch(JsonParseException e)
			
			{   
	        		
				logger.error("Error["+e+"]");
			
			    throw new BadRequestException(null, "Bad Request Format");
			    
			}
			
			return userMasterDto;
		}
	
	
		
		}
		


