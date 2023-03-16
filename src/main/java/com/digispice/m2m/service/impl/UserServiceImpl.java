package com.digispice.m2m.service.impl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.digispice.m2m.dto.SmsConfigDto;
import com.digispice.m2m.dto.UserMasterDto;
import com.digispice.m2m.dto.UserMasterUpdateDto;
import com.digispice.m2m.dto.UserMasterWrapperDto;
import com.digispice.m2m.dto.VoiceConfigDto;
import com.digispice.m2m.entity.GlobalMaster;
import com.digispice.m2m.entity.ProductInfo;
import com.digispice.m2m.entity.SmsConfig;
import com.digispice.m2m.entity.SmsConfigId;
import com.digispice.m2m.entity.SmsConfigLog;
import com.digispice.m2m.entity.UserMaster;
import com.digispice.m2m.entity.UserMasterLog;
import com.digispice.m2m.entity.VoiceConfig;
import com.digispice.m2m.entity.VoiceConfigId;
import com.digispice.m2m.entity.VoiceConfigLog;
import com.digispice.m2m.exception.models.BadRequestException;
import com.digispice.m2m.exception.models.ResourceNotFoundException;
import com.digispice.m2m.model.ApiResponse;
import com.digispice.m2m.model.ApiResponsePojo;
import com.digispice.m2m.model.ApiResponsePojo.Sms;
import com.digispice.m2m.model.ApiResponsePojo.Voice;
import com.digispice.m2m.repository.ProductInfoRepository;
import com.digispice.m2m.repository.SmsConfigLogRepository;
import com.digispice.m2m.repository.SmsConfigRepository;
import com.digispice.m2m.repository.UserMasterLogRepository;
import com.digispice.m2m.repository.UserMasterRepository;
import com.digispice.m2m.repository.VoiceConfigLogRepository;
import com.digispice.m2m.repository.VoiceConfigRepository;
import com.digispice.m2m.service.UserService;
import com.digispice.m2m.utilities.RestPreconditions;
import com.digispice.m2m.utilities.UtilityFunctions;

@Service
@Transactional
public class UserServiceImpl implements UserService{
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	UserMasterRepository  userMasterRepository;

	@Autowired
	GlobalMasterServiceImpl globalMasterServiceImpl;
	
	@Autowired
	UserMasterLogRepository userMasterLogRepository;
	
	@Autowired
	ProductInfoRepository productInfoRepository;
	
	@Autowired
	ProductInfoServiceImpl productInfoServiceImpl;
	
	@Autowired
	VoiceConfigLogRepository voiceConfigLogRepository;
	
	@Autowired
	SmsConfigLogRepository smsConfigLogRepository;
	
	@Autowired
	SmsConfigRepository smsConfigRepository;
	
	@Autowired
	VoiceConfigRepository voiceConfigRepository;
	
	@Autowired
	private ModelMapper modelmapper;
	
    private ApiResponse responsePojo = null;
    
	private ApiResponsePojo apiResponsePojo=null;
	
	private Set<VoiceConfigDto> voiceConfigDto;//=new LinkedHashSet<VoiceConfigDto>();
	

	
	private Set<SmsConfigDto> smsConfigDto;//=new LinkedHashSet<SmsConfigDto>();

	private Set<VoiceConfigDto> voice_whitelist_msisdn_mo;
	
	private Set<VoiceConfigDto> voice_whitelist_msisdn_mt;
	
	private Set<SmsConfigDto> sms_whitelist_msisdn_mo;
	
	private Set<SmsConfigDto> sms_whitelist_msisdn_mt;

	private  Set<Voice> mergedSetVoice;
	
	private Set<Sms> mergedSetSms;
	
	private Set<Long> provMsisdnSmsMo;
	
	private Set<Long> provMsisdnSmsMt ;

	private Set<Long> provMsisdnVoiceMo;
	
	private Set<Long> provMsisdnVoiceMt;
	
	private Set<Sms> smsConfigResponse;//=new LinkedHashSet<Sms>();
	//Set<Sms> smsConfigResponse =Collections.synchronizedSet(new HashSet());

	private Set<Voice> voiceConfigResponse;//=new LinkedHashSet<Voice>();
	
	

	private int succCount=0;
	
	private int failCount=0;
	
	private int succCountVoice=0;
	
	private int failCountVoice=0;
	
	private long count=0L;
	
	private int max_sms_whitelist_msisdn_mo=0;
	
	private int max_sms_whitelist_msisdn_mt=0;
	
	private int max_voice_whitelist_msisdn_mo=0;
	
	private int max_voice_whitelist_msisdn_mt=0;
	
	private String prov_msisdn_voice_new;
	
	Set<SmsConfigDto> smsConfigDtoMo=new LinkedHashSet<SmsConfigDto>();
	
	Set<SmsConfigDto> smsConfigDtoMt=new LinkedHashSet<SmsConfigDto>();
	
    Optional<SmsConfig> smsConfigOptional=Optional.empty();
    
    Optional<VoiceConfig> voiceConfigOptional=Optional.empty();
    
    private ProductInfo apiInfo=null;
    
 
	
    @PostConstruct
    public void postConstruct()
    {
      this.apiInfo = productInfoServiceImpl.fetchProductDetails();
      
      logger.info("API Info Loaded Into Memory");
    }
	
	@Override
	@Transactional(readOnly = true)
	public synchronized ApiResponsePojo findMsisdn(final UserMasterWrapperDto resource) {
	
    int count=this.countByMsisdn(resource.getMsisdn().longValue());
    
    if(count==0)
    {        
    	     logger.error("Msisdn is not present in the system");
    	     
    		 throw new ResourceNotFoundException("Msisdn is not present in the system",  Long.toString(resource.getMsisdn().longValue()));
    }
    else
    {
    	    List<UserMasterDto> enterpriseUserDetails=userMasterRepository.findMsisdn(resource.getMsisdn()).stream().map(this::convertToDto).collect(Collectors.toList());
    	    
			apiResponsePojo=responseBuilder(enterpriseUserDetails,apiInfo);
			
			return apiResponsePojo;	
    }
	}
	@Override
	public ApiResponse deleteUserByMsisdn(final UserMasterWrapperDto resource) {
		
		
        try{
        	
        	int rowsDeleted=0;
        	
        	int count=countByMsisdn(resource.getMsisdn().longValue());
        	
    		if(count==0)
        	{   
        		 throw new ResourceNotFoundException("Msisdn is not present in the system",  Long.toString(resource.getMsisdn()));
        	}
        	else
        	{   
        		List<UserMaster> userDetails=  (List<UserMaster>) userMasterRepository.findMsisdn(resource.getMsisdn());
        	
        	    Set<VoiceConfigLog> voiceConfig=userDetails.stream().flatMap(s -> s.getVoiceConfig().stream())
        	    		                        .map(vConfig ->
        	    		                        {
				    	    		                  VoiceConfigLog voiceConfigLog = new VoiceConfigLog();
				    	    		                  voiceConfigLog.setUsr_msisdn(vConfig.getUsr_msisdn());
				    	    		                  voiceConfigLog.setProv_flag(vConfig.getProv_flag());
				    	    		                  voiceConfigLog.setProv_msisdn(vConfig.getProv_msisdn());
				    	    		                  voiceConfigLog.setProv_startTime(vConfig.getProv_startTime());
				    	    		                  voiceConfigLog.setProv_endTime(vConfig.getProv_endTime());
				    	    		                  voiceConfigLog.setProv_startDate(vConfig.getProv_startDate());
													  voiceConfigLog.setProv_endDate(vConfig.getProv_endDate());
													  voiceConfigLog.setProv_startMonth(vConfig.getProv_startMonth());
													  voiceConfigLog.setProv_endMonth(vConfig.getProv_endMonth());
				    	    		                  voiceConfigLog.setRec_createDateTime(vConfig.getRec_createDateTime());
				    	    		                  voiceConfigLog.setRec_updateDateTime(vConfig.getRec_updateDateTime());
				    	    		                  voiceConfigLog.setAction("Delete");
				    	    	                      return voiceConfigLog;
    	    	                                 }).collect(Collectors.toSet());
    	    	
    	    	Set<SmsConfigLog> smsConfig =  userDetails.stream().flatMap(s -> s.getSmsConfig().stream()) .map(sConfig->
    	    			                       {
										    		SmsConfigLog smsConfigLog =new SmsConfigLog();
										    		smsConfigLog.setUsr_msisdn(sConfig.getUsr_msisdn());
										    		smsConfigLog.setProv_flag(sConfig.getProv_flag());
										    		smsConfigLog.setProv_msisdn(sConfig.getProv_msisdn());
										    		smsConfigLog.setProv_startTime(sConfig.getProv_startTime());
										    		smsConfigLog.setProv_endTime(sConfig.getProv_endTime());
										    		smsConfigLog.setProv_endDate(sConfig.getProv_endDate());
										    		smsConfigLog.setProv_startMonth(sConfig.getProv_startMonth());
										    		smsConfigLog.setProv_endMonth(sConfig.getProv_endMonth());
										    		smsConfigLog.setRec_createDateTime(sConfig.getRec_createDateTime());
										    		smsConfigLog.setRec_updateDateTime(sConfig.getRec_updateDateTime());
										    		smsConfigLog.setAction("Delete");
										    	    return smsConfigLog;
    	    	                               }).collect(Collectors.toSet());
    	    	
 
    	        List<UserMasterLog> userMasterLog = userDetails.stream().map(s->
    	        		                            { 
					        		                      UserMasterLog userLog=new UserMasterLog();
					        		                      userLog.setUsr_id(s.getUsr_id());
					    			                      userLog.setUsr_parent_id(s.getCategory());
					    			                      userLog.setName(s.getUsername());
					    			                      userLog.setStatus(s.getStatus());
					    			                      userLog.setMsisdn(s.getMsisdn());
					    			                      userLog.setActivationDateTime(s.getActivationDateTime());
					    			                      userLog.setUpdateDateTime(s.getUpdateDateTime());
					    			                      return userLog;
        	                                      }).collect(Collectors.toList());
    	        
    	    
    	    	UserMasterLog userLog=new UserMasterLog();
    	        userLog.setUsr_id(userMasterLog.get(0).getUsr_id());
        	    userLog.setUsr_parent_id(userMasterLog.get(0).getUsr_parent_id());
    		    userLog.setName(userMasterLog.get(0).getName());
    		    userLog.setMsisdn(userMasterLog.get(0).getMsisdn());
    	        userLog.setStatus(userMasterLog.get(0).getStatus());
    		    userLog.setActivationDateTime(userMasterLog.get(0).getActivationDateTime());
    	        userLog.setUpdateDateTime(userDetails.get(0).getUpdateDateTime());
    		    userLog.setImsi(userDetails.get(0).getImsi());
    		    userLog.setAction("deleteUser");
    		    
                UserMasterLog userMasterLogObj =userMasterLogRepository.saveAndFlush(userLog);
                
    	    	List<SmsConfigLog> smsConfigLogObj=smsConfigLogRepository.saveAll(smsConfig);
    	    	smsConfigLogRepository.flush();
    	    	
    	    	List<VoiceConfigLog> voiceConfigLogObj=voiceConfigLogRepository.saveAll(voiceConfig);
    	    	voiceConfigLogRepository.flush();
    	    	
    	  
    	    	if (userMasterLogObj!=null && smsConfigLogObj!=null && voiceConfigLogObj!=null )
    	    	{ 	
    	    		rowsDeleted=userMasterRepository.deleteByMsisdn(resource.getMsisdn());
            		
    	    	
    	    	}
        		
        		if(rowsDeleted>0)
        		{
        			
        			responsePojo =new ApiResponse(String.valueOf(resource.getMsisdn()),HttpStatus.OK.value(),"success","deleted","Requested number has been deleted from the system"); 
        			
        	}
        		
        	}
        	return responsePojo;
        }
        catch(ResourceNotFoundException e) {
        throw new ResourceNotFoundException("Msisdn is not present in the system", Long.toString(resource.getMsisdn()));
    
    }
		
	}
		
	@Override
	public synchronized ApiResponsePojo createUser(final UserMasterDto resource)
	{   
		
		fetchMaxWhitelistCapacity();
		
		//smsConfigDto=null;
		
		//voiceConfigDto=null;
		smsConfigResponse =new LinkedHashSet<Sms>();
		voiceConfigDto=new LinkedHashSet<VoiceConfigDto>();
		smsConfigDto=new LinkedHashSet<SmsConfigDto>();
		voiceConfigResponse=new LinkedHashSet<Voice>();
		
		if (!smsConfigDto.isEmpty()) 
		{
			      
		      logger.info("Clear SmsConfigDto Set");
			   smsConfigDto.clear();
	    } 
			     
		if (!this.voiceConfigDto.isEmpty())
		{
			      
			      logger.info("Clear VoiceConfigDto Set");
			      voiceConfigDto.clear();
		} 
		
	    RestPreconditions.checkRequestElementIsValid(resource.getMsisdn(), "Bad Request Format");
		
        RestPreconditions.checkRequestElementNotNull(resource.getCategory(),"Bad Request Format");
	    
        RestPreconditions.checkRequestElementNotNull(resource.getUsername(),"Bad Request Format");
        
        if(resource.getSmsConfig().isEmpty() && resource.getVoiceConfig().isEmpty())
        {    
    	    	 logger.info("Json payload comprising of empty Sms and Voice Block");
    	    	 
    		     throw new BadRequestException(String.valueOf(resource.getMsisdn()),"Bad Request Format");
        }
	    
		int cnt=countByMsisdn(resource.getMsisdn());
		
		RestPreconditions.checkEntityExists(cnt, resource.getMsisdn(), "Msisdn is present in the system");
		
		long voice_msisdn_mo_filtered_count=resource.getVoiceConfig().stream().filter(v->v.getProv_flag()==0).count();
		
     	long voice_msisdn_mt_filtered_count=resource.getVoiceConfig().stream().filter(v->v.getProv_flag()==1).count();
		
	    long sms_msisdn_mo_filtered_count=resource.getSmsConfig().stream().filter(s->s.getProv_flag()==0).count();
	    
		long sms_msisdn_mt_filtered_count=resource.getSmsConfig().stream().filter(s->s.getProv_flag()==1).count();
		
		List<UserMasterDto> jsonPayload= new ArrayList<UserMasterDto>(Arrays.asList(resource));
		
		Set<VoiceConfigDto> jsonPayload_VoiceConfig=jsonPayload.stream().flatMap(v -> v.getVoiceConfig().stream()).collect(Collectors.toCollection(LinkedHashSet::new));
		
		Set<SmsConfigDto> jsonPayload_SmsConfig=jsonPayload.stream().flatMap(v -> v.getSmsConfig().stream()).collect(Collectors.toCollection(LinkedHashSet::new));
		
	   
		 if(resource.getVoiceConfig()!=null && !(resource.getVoiceConfig().isEmpty()))
		 {   
			 
			logger.info("Inside Voice Config Block");
            if(voice_msisdn_mo_filtered_count<=max_voice_whitelist_msisdn_mo && voice_msisdn_mt_filtered_count<=max_voice_whitelist_msisdn_mt )
            {	       
            	       logger.info("voice_mo<=max_voice_mo && voice_mt<=max_voice_mt");
            	       
			           if(voice_msisdn_mo_filtered_count>0 && voice_msisdn_mt_filtered_count>0)
			           {	       
			        	            
			        	            logger.info("0<voice_mo<=max_voice_mo && 0<voice_mt<=max_voice_mt");
			        	            
				                    voice_whitelist_msisdn_mo=jsonPayload_VoiceConfig.stream()
						                                     .filter(p->p.getProv_flag()==0)
						                                     .filter(UtilityFunctions.distinctByKey(p -> p.getProv_msisdn_voice()))
						                                     .filter(p->p.getProv_msisdn_voice().matches("\\d+"))
	                                                         .filter(p->p.getProv_msisdn_voice().length() > 9 && p.getProv_msisdn_voice().length() < 14)
						                                     .map(p ->{
											                                  VoiceConfigDto configDto =new VoiceConfigDto();
																			  configDto.setUsr_msisdn(resource.getMsisdn());
																			  configDto.setProv_flag(p.getProv_flag());
																			  configDto.setProv_msisdn(Long.parseLong(p.getProv_msisdn_voice()));
																			  return configDto;
						                                               }).collect(Collectors.toCollection(LinkedHashSet::new)) ; 
				               
			                       voice_whitelist_msisdn_mt=jsonPayload_VoiceConfig.stream()
	                                                         .filter(p->p.getProv_flag()==1)
	                                                         .filter(UtilityFunctions.distinctByKey(p -> p.getProv_msisdn_voice()))
	                                                         .filter(p->p.getProv_msisdn_voice().matches("\\d+"))
                                                             .filter(p->p.getProv_msisdn_voice().length() > 9 && p.getProv_msisdn_voice().length() < 14)
	                                                         .map(p ->{
											                                  VoiceConfigDto configDto =new VoiceConfigDto();
																			  configDto.setUsr_msisdn(resource.getMsisdn());
																			  configDto.setProv_flag(p.getProv_flag());
																			  configDto.setProv_msisdn(Long.parseLong(p.getProv_msisdn_voice()));
																			  return configDto;
	                                                                   }).collect(Collectors.toCollection(LinkedHashSet::new)) ; 
		                     
		          	                voiceConfigDto = Stream.concat(voice_whitelist_msisdn_mo.stream(), voice_whitelist_msisdn_mt.stream()).collect(Collectors.toSet());
		          	                
			            } 
			         
			           else if(voice_msisdn_mo_filtered_count>0 && voice_msisdn_mt_filtered_count==0)
			           {	   
			        	       logger.info("0>voice_mo<=max_voice_mo && voice_mt==0");
			        	       
			        	       logger.info("Voice_msisdn_mt_filtered_count["+voice_msisdn_mt_filtered_count+"]");
			        	       
			                   voice_whitelist_msisdn_mo=jsonPayload_VoiceConfig.stream()
					                                    .filter(p->p.getProv_flag()==0)
					                                    .filter(UtilityFunctions.distinctByKey(p -> p.getProv_msisdn_voice()))
					                                    .filter(p->p.getProv_msisdn_voice().matches("\\d+"))
                                                        .filter(p->p.getProv_msisdn_voice().length() > 9 && p.getProv_msisdn_voice().length() < 14)
					                                    .map(p ->{
									                                  VoiceConfigDto configDto =new VoiceConfigDto();
																	  configDto.setUsr_msisdn(resource.getMsisdn());
																	  configDto.setProv_flag(p.getProv_flag());
																	  configDto.setProv_msisdn(Long.parseLong(p.getProv_msisdn_voice()));
																	  return configDto;
					                                              }).collect(Collectors.toCollection(LinkedHashSet::new)) ; 
			                   voiceConfigDto = voice_whitelist_msisdn_mo;
		          	        
			           } 
			           else  if(voice_msisdn_mo_filtered_count==0 && voice_msisdn_mt_filtered_count>0)
			        	   
			           {	
			        	   
			        	     logger.info("voice_mo==0 && 0<voice_mt<max_voice_mt");
			                   
			                  voice_whitelist_msisdn_mt=jsonPayload_VoiceConfig.stream()
	                                                   .filter(p->p.getProv_flag()==1)
	                                                   .filter(UtilityFunctions.distinctByKey(p -> p.getProv_msisdn_voice()))
	                                                   .filter(p->p.getProv_msisdn_voice().matches("\\d+"))
                                                       .filter(p->p.getProv_msisdn_voice().length() > 9 && p.getProv_msisdn_voice().length() < 14)
	                                                   .map(p ->{
									                                  VoiceConfigDto configDto =new VoiceConfigDto();
																	  configDto.setUsr_msisdn(resource.getMsisdn());
																	  configDto.setProv_flag(p.getProv_flag());
																	  configDto.setProv_msisdn(Long.parseLong(p.getProv_msisdn_voice()));
																	  return configDto;
	                                                             }).collect(Collectors.toCollection(LinkedHashSet::new)) ; 
		                   
		          	          voiceConfigDto = voice_whitelist_msisdn_mt;
		          	         
			            } 
			          
						           
			           logger.info("Next else if condition");
            }  
		 		    
            else if(voice_msisdn_mo_filtered_count>max_voice_whitelist_msisdn_mo && voice_msisdn_mt_filtered_count>max_voice_whitelist_msisdn_mt )
            {	                   
		    	                   logger.info("call_mo>max_voice_mo && call_mt>max_voice_mt");
            	                   voice_whitelist_msisdn_mo=jsonPayload_VoiceConfig.stream()
						                                    .filter(p->p.getProv_flag()==0)
						                                    .filter(UtilityFunctions.distinctByKey(p -> p.getProv_msisdn_voice()))
						                                    .filter(p->p.getProv_msisdn_voice().matches("\\d+"))
	                                                        .filter(p->p.getProv_msisdn_voice().length() > 9 && p.getProv_msisdn_voice().length() < 14).limit(max_voice_whitelist_msisdn_mo)
						                                    .map(p ->{
										                                  VoiceConfigDto configDto =new VoiceConfigDto();
																		  configDto.setUsr_msisdn(resource.getMsisdn());
																		  configDto.setProv_flag(p.getProv_flag());
																		  configDto.setProv_msisdn(Long.parseLong(p.getProv_msisdn_voice()));
																		  return configDto;
						                                             }).collect(Collectors.toCollection(LinkedHashSet::new)) ; 
				                
				                   voice_whitelist_msisdn_mt=jsonPayload_VoiceConfig.stream()
		                                                    .filter(p->p.getProv_flag()==1)
		                                                    .filter(UtilityFunctions.distinctByKey(p -> p.getProv_msisdn_voice()))
		                                                    .filter(p->p.getProv_msisdn_voice().matches("\\d+"))
	                                                        .filter(p->p.getProv_msisdn_voice().length() > 9 && p.getProv_msisdn_voice().length() < 14).limit(max_voice_whitelist_msisdn_mt)
		                                                    .map(p ->{
										                                  VoiceConfigDto configDto =new VoiceConfigDto();
																		  configDto.setUsr_msisdn(p.getUsr_msisdn());
																		  configDto.setProv_flag(p.getProv_flag());
																		  configDto.setProv_msisdn(Long.parseLong(p.getProv_msisdn_voice()));
																		  return configDto;
		                                                       }).collect(Collectors.toCollection(LinkedHashSet::new)) ; 
			                   
			          	          voiceConfigDto = Stream.concat(voice_whitelist_msisdn_mo.stream(), voice_whitelist_msisdn_mt.stream()).collect(Collectors.toSet());
			          	         
		     } 
		    
		    else if(voice_msisdn_mo_filtered_count<=max_voice_whitelist_msisdn_mo && voice_msisdn_mt_filtered_count>max_voice_whitelist_msisdn_mt )
            {	               
		    	              logger.info("call_mo<=max_voice_mo && call_mt>max_voice_mt");
		    	              if(voice_msisdn_mo_filtered_count>0)
                              {	   
           	                   voice_whitelist_msisdn_mo=jsonPayload_VoiceConfig.stream()
						                                 .filter(p->p.getProv_flag()==0)
						                                 .filter(UtilityFunctions.distinctByKey(p -> p.getProv_msisdn_voice()))
						                                 .filter(p->p.getProv_msisdn_voice().matches("\\d+"))
	                                                     .filter(p->p.getProv_msisdn_voice().length() > 9 && p.getProv_msisdn_voice().length() < 14)
						                                  .map(p ->{
										                                  VoiceConfigDto configDto =new VoiceConfigDto();
																		  configDto.setUsr_msisdn(resource.getMsisdn());
																		  configDto.setProv_flag(p.getProv_flag());
																		  configDto.setProv_msisdn(Long.parseLong(p.getProv_msisdn_voice()));
																		  return configDto;
						                                             }).collect(Collectors.toCollection(LinkedHashSet::new)); 
                              }
				                   voice_whitelist_msisdn_mt=jsonPayload_VoiceConfig.stream()
		                                                    .filter(p->p.getProv_flag()==1)
		                                                    .filter(UtilityFunctions.distinctByKey(p -> p.getProv_msisdn_voice()))
		                                                    .filter(p->p.getProv_msisdn_voice().matches("\\d+"))
	                                                        .filter(p->p.getProv_msisdn_voice().length() > 9 && p.getProv_msisdn_voice().length() < 14).limit(max_voice_whitelist_msisdn_mt)
		                                                    .map(p ->{
										                                  VoiceConfigDto configDto =new VoiceConfigDto();
																		  configDto.setUsr_msisdn(resource.getMsisdn());
																		  configDto.setProv_flag(p.getProv_flag());
																		  configDto.setProv_msisdn(Long.parseLong(p.getProv_msisdn_voice()));
																		  return configDto;
		                                                             }).collect(Collectors.toCollection(LinkedHashSet::new)) ; 
				              
			                      if(voice_whitelist_msisdn_mo!=null) 
			                      {	  
			          	              voiceConfigDto = Stream.concat(voice_whitelist_msisdn_mo.stream(), voice_whitelist_msisdn_mt.stream()).collect(Collectors.toSet());
			                      }
			                      else
			                      {	  
			                    	  voiceConfigDto =voice_whitelist_msisdn_mt;
			                      } 	  
	   } 

       else if(voice_msisdn_mo_filtered_count>max_voice_whitelist_msisdn_mo && voice_msisdn_mt_filtered_count<=max_voice_whitelist_msisdn_mt )
       {	               
	    	               logger.info("call_mo>max_voice_mo && call_mt<=max_voice_mt");
	    	               
      	                   voice_whitelist_msisdn_mo=jsonPayload_VoiceConfig.stream()
					                                 .filter(p->p.getProv_flag()==0)
					                                 .filter(UtilityFunctions.distinctByKey(p -> p.getProv_msisdn_voice()))
					                                 .filter(p->p.getProv_msisdn_voice().matches("\\d+"))
                                                     .filter(p->p.getProv_msisdn_voice().length() > 9 && p.getProv_msisdn_voice().length() < 14).limit(max_voice_whitelist_msisdn_mo)
					                                 .map(p ->{
									                                  VoiceConfigDto configDto =new VoiceConfigDto();
														              configDto.setUsr_msisdn(resource.getMsisdn());
																	  configDto.setProv_flag(p.getProv_flag());
																	  configDto.setProv_msisdn(Long.parseLong(p.getProv_msisdn_voice()));
																	  return configDto;
					                                             }).collect(Collectors.toCollection(LinkedHashSet::new)); 
                            if(voice_msisdn_mt_filtered_count>0)
                            {	
			                  voice_whitelist_msisdn_mt=jsonPayload_VoiceConfig.stream()
	                                                  .filter(p->p.getProv_flag()==1)
	                                                  .filter(UtilityFunctions.distinctByKey(p -> p.getProv_msisdn_voice()))
	                                                  .filter(p->p.getProv_msisdn_voice().matches("\\d+"))
                                                      .filter(p->p.getProv_msisdn_voice().length() > 9 && p.getProv_msisdn_voice().length() < 14)
	                                                  .map(p ->{
									                                  VoiceConfigDto configDto =new VoiceConfigDto();
																	  configDto.setUsr_msisdn(resource.getMsisdn());
																	  configDto.setProv_flag(p.getProv_flag());
																	  configDto.setProv_msisdn(Long.parseLong(p.getProv_msisdn_voice()));
																	  return configDto;
	                                                           }).collect(Collectors.toCollection(LinkedHashSet::new)) ; 
                             }
			                   
		                      if(voice_whitelist_msisdn_mt!=null) 
		                      {	  
		          	              voiceConfigDto = Stream.concat(voice_whitelist_msisdn_mo.stream(), voice_whitelist_msisdn_mt.stream()).collect(Collectors.toSet());
		                      }
		                      else
		                      {	  
		                    	  voiceConfigDto =voice_whitelist_msisdn_mo;
		                      } 	  
	  } 
		 }
	
          
		 if(resource.getSmsConfig()!=null && !(resource.getSmsConfig().isEmpty()))
		 {  
			 
			 logger.debug("Start of Sms Block && sms_provMsisdnList_Size<max_sms_whitelist_msisdn");
		 
            if(sms_msisdn_mo_filtered_count<=max_sms_whitelist_msisdn_mo && sms_msisdn_mt_filtered_count<=max_sms_whitelist_msisdn_mt )
            {	     
            	
            	      logger.info("sms_mo<=max_sms_mo && sms_mt<=max_sms_mt");
            	      
		              if(sms_msisdn_mo_filtered_count>0 && sms_msisdn_mt_filtered_count>0)
		              {	  
		            	   logger.info("0<sms_mo<=max_sms_mo && 0<sms_mt<=max_sms_mt");
		            	   
			               sms_whitelist_msisdn_mo=jsonPayload_SmsConfig.stream()
		            		                     .filter(p->p.getProv_flag()==0)
							                     .filter(UtilityFunctions.distinctByKey(p -> p.getProv_msisdn_sms()))
							                     .filter(p->p.getProv_msisdn_sms().matches("\\d+"))
		                                         .filter(p->p.getProv_msisdn_sms().length() > 9 && p.getProv_msisdn_sms().length() < 14)
							                     .map(p ->{
																SmsConfigDto configDto =new SmsConfigDto();
															    configDto.setUsr_msisdn(resource.getMsisdn());
																configDto.setProv_flag(p.getProv_flag());
																configDto.setProv_msisdn(Long.parseLong(p.getProv_msisdn_sms()));
																return configDto;
							                               }).collect(Collectors.toCollection(LinkedHashSet::new)) ;
		               
		                 sms_whitelist_msisdn_mt=jsonPayload_SmsConfig.stream()
		                                        .filter(p->p.getProv_flag()==1)
		                                        .filter(UtilityFunctions.distinctByKey(p -> p.getProv_msisdn_sms()))
		                                        .filter(p->p.getProv_msisdn_sms().matches("\\d+"))
		                                        .filter(p->p.getProv_msisdn_sms().length() > 9 && p.getProv_msisdn_sms().length() < 14)
		                                        .map(p ->{
															SmsConfigDto configDto =new SmsConfigDto();
															configDto.setUsr_msisdn(resource.getMsisdn());
															configDto.setProv_flag(p.getProv_flag());
														    configDto.setProv_msisdn(Long.parseLong(p.getProv_msisdn_sms()));
															return configDto;
		                                                  }).collect(Collectors.toCollection(LinkedHashSet::new)) ;
	                    
	                    smsConfigDto = Stream.concat(sms_whitelist_msisdn_mo.stream(), sms_whitelist_msisdn_mt.stream()).collect(Collectors.toSet());
	                  
		             }
		             else if(sms_msisdn_mo_filtered_count>0 && sms_msisdn_mt_filtered_count==0)
		             {	  
		            	  
		            	  logger.info("0>sms_mo<=max_sms_mo && sms_mt==0");
			              sms_whitelist_msisdn_mo=jsonPayload_SmsConfig.stream()
		            		                     .filter(p->p.getProv_flag()==0)
							                     .filter(UtilityFunctions.distinctByKey(p -> p.getProv_msisdn_sms()))
							                     .filter(p->p.getProv_msisdn_sms().matches("\\d+"))
		                                         .filter(p->p.getProv_msisdn_sms().length() > 9 && p.getProv_msisdn_sms().length() < 14)
							                     .map(p ->{
																SmsConfigDto configDto =new SmsConfigDto();
															    configDto.setUsr_msisdn(resource.getMsisdn());
																configDto.setProv_flag(p.getProv_flag());
																configDto.setProv_msisdn(Long.parseLong(p.getProv_msisdn_sms()));
																return configDto;
							                               }).collect(Collectors.toCollection(LinkedHashSet::new)) ;
		                
		                  smsConfigDto = sms_whitelist_msisdn_mo;
		                  logger.info("Size of SmsConfigDto["+smsConfigDto+"]");
		                
		             } 
		             else if(sms_msisdn_mo_filtered_count==0 && sms_msisdn_mt_filtered_count>0)
		             {	 
		            	logger.info("sms_mo==0 && 0<sms_mt<=max_sms_mt"); 
			            sms_whitelist_msisdn_mt=jsonPayload_SmsConfig.stream()
		                                      .filter(p->p.getProv_flag()==1)
		                                      .filter(UtilityFunctions.distinctByKey(p -> p.getProv_msisdn_sms()))
		                                      .filter(p->p.getProv_msisdn_sms().matches("\\d+"))
		                                      .filter(p->p.getProv_msisdn_sms().length() > 9 && p.getProv_msisdn_sms().length() < 14)
		                                      .map(p ->{
															SmsConfigDto configDto =new SmsConfigDto();
															configDto.setUsr_msisdn(resource.getMsisdn());
															configDto.setProv_flag(p.getProv_flag());
															configDto.setProv_msisdn(Long.parseLong(p.getProv_msisdn_sms()));
															return configDto;
		                                              }).collect(Collectors.toCollection(LinkedHashSet::new)) ;
	                 
	                   smsConfigDto = sms_whitelist_msisdn_mt;
	                  
		             } 
                     } 
            
            else if(sms_msisdn_mo_filtered_count>max_sms_whitelist_msisdn_mo && sms_msisdn_mt_filtered_count>max_sms_whitelist_msisdn_mt )
            {	                   
   		    	                   logger.info("sms_mo>max_sms_mo && sms_mt>max_sms_mt");
   		    	                   
               	                   sms_whitelist_msisdn_mo=jsonPayload_SmsConfig.stream()
   						                                   .filter(p->p.getProv_flag()==0)
   						                                   .filter(UtilityFunctions.distinctByKey(p -> p.getProv_msisdn_sms()))
   						                                   .filter(p->p.getProv_msisdn_sms().matches("\\d+"))
   	                                                       .filter(p->p.getProv_msisdn_sms().length() > 9 && p.getProv_msisdn_sms().length() < 14).limit(max_sms_whitelist_msisdn_mo)
   						                                   .map(p ->{
   										                                  SmsConfigDto configDto =new SmsConfigDto();
   																		  configDto.setUsr_msisdn(resource.getMsisdn());
   																		  configDto.setProv_flag(p.getProv_flag());
   																		  configDto.setProv_msisdn(Long.parseLong(p.getProv_msisdn_sms()));
   																		  return configDto;
   						                                             }).collect(Collectors.toCollection(LinkedHashSet::new)); 
   				                
   				                   sms_whitelist_msisdn_mt=jsonPayload_SmsConfig.stream()
   		                                                   .filter(p->p.getProv_flag()==1)
   		                                                   .filter(UtilityFunctions.distinctByKey(p -> p.getProv_msisdn_sms()))
   		                                                   .filter(p->p.getProv_msisdn_sms().matches("\\d+"))
   	                                                       .filter(p->p.getProv_msisdn_sms().length() > 9 && p.getProv_msisdn_sms().length() < 14).limit(max_sms_whitelist_msisdn_mt)
   		                                                   .map(p ->{
   										                                  SmsConfigDto configDto =new SmsConfigDto();
   																		  configDto.setUsr_msisdn(resource.getMsisdn());
   																		  configDto.setProv_flag(p.getProv_flag());
   																		  configDto.setProv_msisdn(Long.parseLong(p.getProv_msisdn_sms()));
   																		  return configDto;
   		                                                              }).collect(Collectors.toCollection(LinkedHashSet::new)); 
   			                   
   			          	          smsConfigDto = Stream.concat(sms_whitelist_msisdn_mo.stream(), sms_whitelist_msisdn_mt.stream()).collect(Collectors.toSet());
   			          	         
   		    } 
   		    
   		    else if(sms_msisdn_mo_filtered_count<=max_sms_whitelist_msisdn_mo && sms_msisdn_mt_filtered_count>max_sms_whitelist_msisdn_mt )
            {	               
   		    	              logger.info("sms_mo<=max_sms_mo && sms_mt>max_sms_mt");
   		    	              
   		    	              if(sms_msisdn_mo_filtered_count>0)
                              {	   
              	                   sms_whitelist_msisdn_mo=jsonPayload_SmsConfig.stream()
   						                                 .filter(p->p.getProv_flag()==0)
   						                                 .filter(UtilityFunctions.distinctByKey(p -> p.getProv_msisdn_sms()))
   						                                 .filter(p->p.getProv_msisdn_sms().matches("\\d+"))
   	                                                     .filter(p->p.getProv_msisdn_sms().length() > 9 && p.getProv_msisdn_sms().length() < 14)
   						                                  .map(p ->{
   										                                  SmsConfigDto configDto =new SmsConfigDto();
   																		  configDto.setUsr_msisdn(resource.getMsisdn());
   																		  configDto.setProv_flag(p.getProv_flag());
   																		  configDto.setProv_msisdn(Long.parseLong(p.getProv_msisdn_sms()));
   																		  return configDto;
   						                                             }).collect(Collectors.toCollection(LinkedHashSet::new)); 
                               }
   				                   sms_whitelist_msisdn_mt=jsonPayload_SmsConfig.stream()
   		                                                   .filter(p->p.getProv_flag()==1)
   		                                                   .filter(UtilityFunctions.distinctByKey(p -> p.getProv_msisdn_sms()))
   		                                                   .filter(p->p.getProv_msisdn_sms().matches("\\d+"))
   	                                                       .filter(p->p.getProv_msisdn_sms().length() > 9 && p.getProv_msisdn_sms().length() < 14).limit(max_sms_whitelist_msisdn_mt)
   		                                                   .map(p ->{
   										                                  SmsConfigDto configDto =new SmsConfigDto();
   																	      configDto.setUsr_msisdn(resource.getMsisdn());
   																		  configDto.setProv_flag(p.getProv_flag());
   																	      configDto.setProv_msisdn(Long.parseLong(p.getProv_msisdn_sms()));
   																		  return configDto;
   		                                                             }).collect(Collectors.toCollection(LinkedHashSet::new)) ; 
   				                  
   			                      if(sms_whitelist_msisdn_mo!=null) 
   			                      {	  
   			          	              smsConfigDto = Stream.concat(sms_whitelist_msisdn_mo.stream(), sms_whitelist_msisdn_mt.stream()).collect(Collectors.toSet());
   			                      }
   			                      else
   			                      {	  
   			                    	  smsConfigDto =sms_whitelist_msisdn_mt;
   			                      } 	  
   		   } 
          
           else if(sms_msisdn_mo_filtered_count>max_sms_whitelist_msisdn_mo && sms_msisdn_mt_filtered_count<=max_sms_whitelist_msisdn_mt )
           {	               
   	    	                   
	                        	   logger.info("sms_mo>max_sms_mo && sms_mt<=max_sms_mt");
	                        	   
	         	                   sms_whitelist_msisdn_mo=jsonPayload_SmsConfig.stream()
	   					                                  .filter(p->p.getProv_flag()==0)
	   					                                  .filter(UtilityFunctions.distinctByKey(p -> p.getProv_msisdn_sms()))
	   					                                  .filter(p->p.getProv_msisdn_sms().matches("\\d+"))
	                                                      .filter(p->p.getProv_msisdn_sms().length() > 9 && p.getProv_msisdn_sms().length() < 14).limit(max_sms_whitelist_msisdn_mo)
	   					                                  .map(p ->{
	   									                                  SmsConfigDto configDto =new SmsConfigDto();
	   																	  configDto.setUsr_msisdn(resource.getMsisdn());
	   																	  configDto.setProv_flag(p.getProv_flag());
	   																      configDto.setProv_msisdn(Long.parseLong(p.getProv_msisdn_sms()));
	   																	  return configDto;
	   					                                             }).collect(Collectors.toCollection(LinkedHashSet::new)); 
	         	                   
	                               if(sms_msisdn_mt_filtered_count>0)
	                               {	
	   			                      
	                            	   sms_whitelist_msisdn_mt=jsonPayload_SmsConfig.stream()
	   	                                                     .filter(p->p.getProv_flag()==1)
	   	                                                     .filter(UtilityFunctions.distinctByKey(p -> p.getProv_msisdn_sms()))
	   	                                                     .filter(p->p.getProv_msisdn_sms().matches("\\d+"))
	                                                         .filter(p->p.getProv_msisdn_sms().length() > 9 && p.getProv_msisdn_sms().length() < 14)
	   	                                                     .map(p ->{
	   									                                  SmsConfigDto configDto =new SmsConfigDto();
	   																      configDto.setUsr_msisdn(resource.getMsisdn());
	   																	  configDto.setProv_flag(p.getProv_flag());
	   																	  configDto.setProv_msisdn(Long.parseLong(p.getProv_msisdn_sms()));
	   																	  return configDto;
	   	                                                             }).collect(Collectors.toCollection(LinkedHashSet::new)) ; 
	                              }
	   			                   
	   		                      if(sms_whitelist_msisdn_mt!=null) 
	   		                      {	  
	   		          	              smsConfigDto = Stream.concat(sms_whitelist_msisdn_mo.stream(), sms_whitelist_msisdn_mt.stream()).collect(Collectors.toSet());
	   		                      }
	   		                      else
	   		                      {	  
	   		                    	  smsConfigDto =sms_whitelist_msisdn_mo;
	   		                      } 	  
   	    } 
	                  	  
		}
	
		 UserMasterDto userMasterDto=new UserMasterDto();
	     userMasterDto.setUsr_id(1);
	     userMasterDto.setCategory(resource.getCategory());
	     userMasterDto.setUsername(resource.getUsername());
	     userMasterDto.setMsisdn(resource.getMsisdn());
	     userMasterDto.setStatus(1);
	    
	     userMasterDto.setImsi(resource.getImsi());
	     if(smsConfigDto!=null && !(smsConfigDto.isEmpty()) && smsConfigDto.size()>0)
	     {	 
	    	  logger.info("SmsConfig Size["+smsConfigDto.size()+"]");
	          userMasterDto.setSmsConfig(smsConfigDto);
	     }
	     if(voiceConfigDto!=null && !(voiceConfigDto.isEmpty()) && voiceConfigDto.size()>0)
	     { 	 
	    	  logger.info("VoiceConfig Size["+voiceConfigDto.size()+"]");
	          userMasterDto.setVoiceConfig(voiceConfigDto);
	     }
	     UserMaster userMaster=userMasterRepository.saveAndFlush(convertToEntity(userMasterDto));
	     
         List<UserMasterDto> dbResponse= new ArrayList<UserMasterDto>(Arrays.asList(convertToDto(userMaster)));
         
         if(smsConfigDto!=null && !(smsConfigDto.isEmpty()) && smsConfigDto.size()>0)
	     {	 
            
        	   provMsisdnSmsMo = dbResponse.stream().flatMap(s->s.getSmsConfig().stream()).filter(s->s.getProv_flag()==0).map(SmsConfigDto :: getProv_msisdn).collect(Collectors.toCollection(LinkedHashSet::new));
        	   
        	   provMsisdnSmsMt = dbResponse.stream().flatMap(s->s.getSmsConfig().stream()).filter(s->s.getProv_flag()==1).map(SmsConfigDto :: getProv_msisdn).collect(Collectors.toCollection(LinkedHashSet::new));
	     }
         if(voiceConfigDto!=null && !(voiceConfigDto.isEmpty()) && voiceConfigDto.size()>0)
         {	 
            
             provMsisdnVoiceMo = dbResponse.stream().flatMap(v->v.getVoiceConfig().stream()).filter(v->v.getProv_flag()==0).map(VoiceConfigDto :: getProv_msisdn).collect(Collectors.toCollection(LinkedHashSet::new));
             
             provMsisdnVoiceMt = dbResponse.stream().flatMap(v->v.getVoiceConfig().stream()).filter(v->v.getProv_flag()==1).map(VoiceConfigDto :: getProv_msisdn).collect(Collectors.toCollection(LinkedHashSet::new));
         }
         if(jsonPayload_SmsConfig.size()>0)
         { 	 
       
             mergedSetSms = jsonPayload.stream().flatMap(s->s.getSmsConfig().stream())
     	                    .map(s->{
			 							Sms smsObj=new Sms();
			 							
			 							smsObj.setUsr_msisdn(s.getUsr_msisdn());
			 							
			 							smsObj.setProv_flag(s.getProv_flag());
			 							logger.info("s---->"+s+" |s.getProv_msisdn_sms() --->"+s.getProv_msisdn_sms()+"|provMsisdnSmsMo|"+provMsisdnSmsMo+"|s.getProv_flag()|"+s.getProv_flag());
			 							//&& provMsisdnSmsMo.contains(Long.parseLong(s.getProv_msisdn_sms()))
			 							if(s.getProv_msisdn_sms()!=null && provMsisdnSmsMo!=null  && s.getProv_flag()==0)
			 							{	
			 								smsObj.setProv_msisdn(new StringBuilder().append(s.getProv_msisdn_sms()).append(":added").toString());
			 							}
			 							else if(s.getProv_msisdn_sms()!=null && provMsisdnSmsMt != null  && s.getProv_flag()==1)
			 							{	// && provMsisdnSmsMt.contains(Long.parseLong(s.getProv_msisdn_sms()))
			 								smsObj.setProv_msisdn(new StringBuilder().append(s.getProv_msisdn_sms()).append(":added").toString());
			 							}
			 							else
			 							{
			 								smsObj.setProv_msisdn(new StringBuilder().append(s.getProv_msisdn_sms()).append(":failed").toString());
			 							}
			 							
 							            return smsObj;
                                     }).collect(Collectors.toCollection(LinkedHashSet::new));
      }
      if(jsonPayload_VoiceConfig.size()>0)
      { 	  
    
           mergedSetVoice = jsonPayload.stream().flatMap(s->s.getVoiceConfig().stream())
    		               .map(v->{
		    		            	  Voice voice=new Voice();
		    		            	  
		    		            	  voice.setUsr_msisdn(v.getUsr_msisdn());
		    		            	  
		    		            	  voice.setProv_flag(v.getProv_flag());
		    		            	  
		    		            	  if(provMsisdnVoiceMo.contains(Long.parseLong(v.getProv_msisdn_voice())) && v.getProv_flag()==0)
		    		            	  {	
		    		            		  voice.setProv_msisdn(new StringBuilder().append(v.getProv_msisdn_voice()).append(":added").toString());
		    		            	  }
		    		            	  else if(provMsisdnVoiceMt.contains(Long.parseLong(v.getProv_msisdn_voice())) && v.getProv_flag()==1)
		    		            	  {	
		    		            		  voice.setProv_msisdn(new StringBuilder().append(v.getProv_msisdn_voice()).append(":added").toString());
		    		            	  }
		    		            	  else
		    		            	  {
		    		            		  voice.setProv_msisdn(new StringBuilder().append(v.getProv_msisdn_voice()).append(":failed").toString());
		    		            	  }
		    		            	  return voice;
                                  }).collect(Collectors.toCollection(LinkedHashSet::new));
     }
     apiResponsePojo =new ApiResponsePojo();
     
     apiResponsePojo.setProduct(apiInfo.getProduct());
     
     apiResponsePojo.setVersion(apiInfo.getVersion());
     
     apiResponsePojo.setRelease(apiInfo.getRelease());
     
     apiResponsePojo.setMsisdn(resource.getMsisdn().toString());
     
     apiResponsePojo.setAction("addUser");
     
   
     if(userMaster.getSmsConfig()!=null && userMaster.getVoiceConfig()!=null && !(userMaster.getSmsConfig().isEmpty()) && !(userMaster.getVoiceConfig().isEmpty()) && userMaster.getSmsConfig().size()==resource.getSmsConfig().size() && userMaster.getVoiceConfig().size()==resource.getVoiceConfig().size())
     {		
		      logger.info("Sms & Call Block added");
		      
		      apiResponsePojo.setReturnCode("success");
		      
		      apiResponsePojo.setRemarks("added");
     }
     else if(userMaster.getSmsConfig()!=null && !(userMaster.getSmsConfig().isEmpty()) && userMaster.getSmsConfig().size()==resource.getSmsConfig().size() && resource.getVoiceConfig().isEmpty())
     {		
		      logger.info("Sms Config Block Added");
		      
		      apiResponsePojo.setReturnCode("success");
		      
		      apiResponsePojo.setRemarks("added");
     }
     else if(userMaster.getVoiceConfig()!=null && !(userMaster.getVoiceConfig().isEmpty()) && userMaster.getVoiceConfig().size()==resource.getVoiceConfig().size() &&  resource.getSmsConfig().isEmpty())
     {		
    	      
		      logger.info("Voice Config Block Added");
		      
		      apiResponsePojo.setReturnCode("success");
		      
		      apiResponsePojo.setRemarks("added");
     }
     else if(userMaster.getVoiceConfig()!=null && !(userMaster.getVoiceConfig().isEmpty()) && userMaster.getVoiceConfig().size()==resource.getVoiceConfig().size() && userMaster.getSmsConfig()!=null && !(userMaster.getSmsConfig().isEmpty()) && userMaster.getSmsConfig().size()!=resource.getSmsConfig().size() )
     {		
	    	 logger.info("Multi-Status[Partial Addition of Sms Config Block ]");
	    	 
		     apiResponsePojo.setReturnCode("Multi-Status");
		     
		     apiResponsePojo.setRemarks("partial add");
     }
     else if(userMaster.getVoiceConfig()!=null && !(userMaster.getVoiceConfig().isEmpty()) && userMaster.getVoiceConfig().size()!=resource.getVoiceConfig().size() && userMaster.getSmsConfig()!=null && !(userMaster.getSmsConfig().isEmpty()) && userMaster.getSmsConfig().size()==resource.getSmsConfig().size() )
     {		
	    	 logger.info("Multi-Status[Partial Addition of Voice Config Block ]");
	    	 
		     apiResponsePojo.setReturnCode("Multi-Status");
		     
		     apiResponsePojo.setRemarks("partial add");
     }
     
     else if(userMaster.getVoiceConfig()!=null && !(userMaster.getVoiceConfig().isEmpty()) && userMaster.getVoiceConfig().size()!=resource.getVoiceConfig().size() && userMaster.getSmsConfig()!=null && !(userMaster.getSmsConfig().isEmpty()) && userMaster.getSmsConfig().size()!=resource.getSmsConfig().size() )
     {		
	    	 logger.info("Multi-Status[Partial Addition of Sms Config & Voice Config Block ]");
	    	 
		     apiResponsePojo.setReturnCode("Multi-Status");
		     
		     apiResponsePojo.setRemarks("partial add");
     }
     
  
    else 
    {    
	    	 logger.info("Multi-Status");
	    	 
		     apiResponsePojo.setReturnCode("Multi-Status");
		     
		     apiResponsePojo.setRemarks("partial add");
    }
    if(resource.getSmsConfig()!=null && !(resource.getSmsConfig().isEmpty()) && resource.getSmsConfig().size()>0)
    {	
    	    logger.info("SMS Block Response");
    	    
	        apiResponsePojo.setSms(mergedSetSms);
    }
    if(resource.getVoiceConfig()!=null && !(resource.getVoiceConfig().isEmpty()) && resource.getVoiceConfig().size()>0)
    {	
    	   logger.info("Voice Block Response");
    	   
	       apiResponsePojo.setVoice(mergedSetVoice);
    }
		return apiResponsePojo;
		
    }
	
	@Override
	public synchronized ApiResponsePojo addUserWrapper(final UserMasterDto resource)
	{    
		smsConfigResponse =new LinkedHashSet<Sms>();
		voiceConfigDto=new LinkedHashSet<VoiceConfigDto>();
		smsConfigDto=new LinkedHashSet<SmsConfigDto>();
		voiceConfigResponse=new LinkedHashSet<Voice>();
		
		
		
		if(!smsConfigResponse.isEmpty())
		{	
		  logger.info("Clear SmsConfigResponse Set");	
		  smsConfigResponse.clear();
		}
		
		if(!voiceConfigResponse.isEmpty())
		{	
		  logger.info("Clear VoiceConfigResponse Set");		
		  voiceConfigResponse.clear();
		}
		
		if (!smsConfigDto.isEmpty()) 
		{
			       
			logger.info("Clear SmsConfigDto Set");
			smsConfigDto.clear();
	     } 
	     
		if (!voiceConfigDto.isEmpty()) 
		{
			
			logger.info("Clear VoiceConfigDto Set");
			voiceConfigDto.clear();
		} 
		
		succCount=0;
		
		failCount=0;
		
		succCountVoice=0;
		
		failCountVoice=0;
		
		//smsConfigDto=null;
		
		//voiceConfigDto=null;
		
		int msisdnCount=this.countByMsisdn(resource.getMsisdn());
		
		logger.info("Count["+msisdnCount+"]");
		

		if(msisdnCount==0)
		{	
			logger.info("Msisdn is not present in the system");
			
			//resource.setMsisdn(resource.getMsisdn());
			
			apiResponsePojo=this.createUser(resource);
		}
		
		
		else
		{
					logger.info("Partial Update");
					
				    fetchMaxWhitelistCapacity();
				    
					//resource.setMsisdn(msisdn);
					
					if(resource.getSmsConfig()!=null && !(resource.getSmsConfig().isEmpty()) && resource.getSmsConfig().size()>0)
					{	
						      
						     logger.info("Starting Sms Config Block");
						    
						      smsConfigDto=resource.getSmsConfig().stream().collect(Collectors.toCollection(LinkedHashSet::new));
						      
					          sms_whitelist_msisdn_mo=resource.getSmsConfig().stream().filter(s->s.getProv_flag()==0).collect(Collectors.toCollection(LinkedHashSet::new));
					          
					          sms_whitelist_msisdn_mt=resource.getSmsConfig().stream().filter(s->s.getProv_flag()==1).collect(Collectors.toCollection(LinkedHashSet::new));
					          
					       
					          if(sms_whitelist_msisdn_mo.size()>0)
					          { 	
					    	  
					        	   logger.info("Processing SmsConfig_Mo Block");
					        	   
					               for(SmsConfigDto smsConfigMo : sms_whitelist_msisdn_mo)
					               { 	
								    	
									    	logger.info("SMS MO for Loop");
									    	
									        
											
											if(smsConfigMo.getProv_msisdn_sms().matches("\\d+") && !(smsConfigMo.getProv_msisdn_sms().length() > 9 && smsConfigMo.getProv_msisdn_sms().length() < 14))
								        	{
								        		  logger.info("Prov_msisdn_sms_mo is invalid");
												  Sms smsObj=new Sms();
								           	      smsObj.setUsr_msisdn(resource.getMsisdn());
								           	      smsObj.setProv_flag(smsConfigMo.getProv_flag());
								           	      smsObj.setProv_msisdn(new StringBuilder().append(smsConfigMo.getProv_msisdn_sms()).append(":").append("failed").toString());
									              smsConfigResponse.add(smsObj);
									              failCount++;
								        	}
											
											else if(!smsConfigMo.getProv_msisdn_sms().matches("\\d+"))
								        	{
								        		  logger.info("Prov_msisdn_sms_mo is invalid");
												  Sms smsObj=new Sms();
								           	      smsObj.setUsr_msisdn(resource.getMsisdn());
								           	      smsObj.setProv_flag(smsConfigMo.getProv_flag());
								           	      smsObj.setProv_msisdn(new StringBuilder().append(smsConfigMo.getProv_msisdn_sms()).append(":").append("failed").toString());
									              smsConfigResponse.add(smsObj);
									              failCount++;
								        	}
										    
											else if(smsConfigMo.getProv_msisdn_sms().matches("\\d+") && (smsConfigMo.getProv_msisdn_sms().length() > 9 && smsConfigMo.getProv_msisdn_sms().length() < 14))
								        	{
												
												
												SmsConfigId compositeId=new SmsConfigId(resource.getMsisdn(),Long.parseLong(smsConfigMo.getProv_msisdn_sms()),smsConfigMo.getProv_flag());
											    smsConfigOptional=smsConfigRepository.findById(compositeId);
											    smsConfigOptional.ifPresent(System.out::println);
													
										        if(smsConfigOptional.isPresent())
										        {   
										            	logger.info("Ist Case : Prov_msisdn_sms_mo is present in the system");
												        Sms smsObj=new Sms();
												        smsObj.setUsr_msisdn(resource.getMsisdn());
												        smsObj.setProv_flag(smsConfigMo.getProv_flag());
												        smsObj.setProv_msisdn(new StringBuilder().append(smsConfigMo.getProv_msisdn_sms()).append(":").append("failed_409").toString());
													    smsConfigResponse.add(smsObj);
														failCount++;
											    }
													       
											     else if(!smsConfigOptional.isPresent())
											     {	
											    	  logger.info("Case : Prov_msisdn_sms_mo is not present in database");
											    	  count=smsConfigRepository.countByUsr_msisdnAndProv_flag(resource.getMsisdn(),smsConfigMo.getProv_flag());
													  logger.info("Prov-msisdn_sms_mo_Count["+count+"]");
												  
										        	  if(count==max_sms_whitelist_msisdn_mo)
								                      {	   
										            	  
										        		  logger.info("sms_msisdn_mo_count==max_sms_whitelist_msisdn_mo");
														
													      Sms smsObj=new Sms();
										           	      smsObj.setUsr_msisdn(resource.getMsisdn());
										           	      smsObj.setProv_flag(smsConfigMo.getProv_flag());
										           	      smsObj.setProv_msisdn(new StringBuilder().append(smsConfigMo.getProv_msisdn_sms()).append(":").append("failed").toString());
											              smsConfigResponse.add(smsObj);
											              failCount++;
											              
								                      }
									        	   
										        	  else if(count<max_sms_whitelist_msisdn_mo)
								                      {  
											            	 
										        		     logger.info("sms_msisdn_mo_count<max_sms_whitelist_msisdn_mo"); 
											                 SmsConfig smsConfig =new SmsConfig();
											                 smsConfig.setUsr_msisdn(resource.getMsisdn());
											                 smsConfig.setProv_flag(smsConfigMo.getProv_flag());
											                 smsConfig.setProv_msisdn(Long.parseLong(smsConfigMo.getProv_msisdn_sms()));
											                 smsConfigRepository.saveAndFlush(smsConfig);
												          
											                 Sms smsObj=new Sms();
								           	                 smsObj.setUsr_msisdn(resource.getMsisdn());
								           	                 smsObj.setProv_flag(smsConfigMo.getProv_flag());
								           	                 smsObj.setProv_msisdn(new StringBuilder().append(smsConfigMo.getProv_msisdn_sms()).append(":").append("added").toString());
												             smsConfigResponse.add(smsObj);
												             succCount++;
												           
								                     }
									        	  
										        	  logger.info("End of Case : Prov_msisdn_sms_mo is not present in database");
											     } 	  
												
											    logger.info("End of valid Prov_msisdn_sms_mo Block");	
											}	
											
									        logger.info("End of Sms Mo for loop");      
								        	
					                }
					                 
					               logger.info("End of Sms_mo block");
					          }
					               
					          if(sms_whitelist_msisdn_mt.size()>0)
							  { 	
							    	 
					        	     logger.info("Processing SmsConfig_Mo Block");
					        	     
					        	     for(SmsConfigDto smsConfigMt : sms_whitelist_msisdn_mt)
							         { 	
										    
					        	    	    logger.info("SMS MT for Loop");
									      
											if(smsConfigMt.getProv_msisdn_sms().matches("\\d+") && !(smsConfigMt.getProv_msisdn_sms().length() > 9 && smsConfigMt.getProv_msisdn_sms().length() < 14))
								        	{
								        		  logger.info("Prov_msisdn_sms_mt is invalid");
												  Sms smsObj=new Sms();
								           	      smsObj.setUsr_msisdn(resource.getMsisdn());
								           	      smsObj.setProv_flag(smsConfigMt.getProv_flag());
								           	      smsObj.setProv_msisdn(new StringBuilder().append(smsConfigMt.getProv_msisdn_sms()).append(":").append("failed").toString());
									              smsConfigResponse.add(smsObj);
									              failCount++;
								        	}
											
											else if(!smsConfigMt.getProv_msisdn_sms().matches("\\d+"))
								        	{
								        		  logger.info("Prov_msisdn_sms_mt is invalid");
												  Sms smsObj=new Sms();
								           	      smsObj.setUsr_msisdn(resource.getMsisdn());
								           	      smsObj.setProv_flag(smsConfigMt.getProv_flag());
								           	      smsObj.setProv_msisdn(new StringBuilder().append(smsConfigMt.getProv_msisdn_sms()).append(":").append("failed").toString());
									              smsConfigResponse.add(smsObj);
									              failCount++;
								        	}
										    
											else if(smsConfigMt.getProv_msisdn_sms().matches("\\d+") && (smsConfigMt.getProv_msisdn_sms().length() > 9 && smsConfigMt.getProv_msisdn_sms().length() < 14))
								        	{
												SmsConfigId compositeId=new SmsConfigId(resource.getMsisdn(),Long.parseLong(smsConfigMt.getProv_msisdn_sms()),smsConfigMt.getProv_flag());
											    smsConfigOptional=smsConfigRepository.findById(compositeId);
											    smsConfigOptional.ifPresent(System.out::println);
														
											    if(smsConfigOptional.isPresent())
											    {   
											            	logger.info("Ist Case : Prov_msisdn_sms_mt is present in the system");
													        Sms smsObj=new Sms();
													        smsObj.setUsr_msisdn(resource.getMsisdn());
													        smsObj.setProv_flag(smsConfigMt.getProv_flag());
													        smsObj.setProv_msisdn(new StringBuilder().append(smsConfigMt.getProv_msisdn_sms()).append(":").append("failed_409").toString());
															smsConfigResponse.add(smsObj);
															failCount++;
												 }
															       
												 else if(!smsConfigOptional.isPresent())
												 {
													  count=smsConfigRepository.countByUsr_msisdnAndProv_flag(resource.getMsisdn(),smsConfigMt.getProv_flag());
													 
													  
										        	  if(count==max_sms_whitelist_msisdn_mt)
								                      {	   
										            	  logger.info("sms_msisdn_mt_count==max_sms_whitelist_msisdn_mt");
														
													      Sms smsObj=new Sms();
										           	      smsObj.setUsr_msisdn(resource.getMsisdn());
										           	      smsObj.setProv_flag(smsConfigMt.getProv_flag());
										           	      smsObj.setProv_msisdn(new StringBuilder().append(smsConfigMt.getProv_msisdn_sms()).append(":").append("failed").toString());
											              smsConfigResponse.add(smsObj);
											              failCount++;
								                      }
										        	   
										        	  else if(count<max_sms_whitelist_msisdn_mt)
								                      {  
											            	 
										        		     logger.info("sms_msisdn_mt_count<max_sms_whitelist_msisdn_mt"); 
											                 SmsConfig smsConfig =new SmsConfig();
											                 smsConfig.setUsr_msisdn(resource.getMsisdn());
											                 smsConfig.setProv_flag(smsConfigMt.getProv_flag());
											                 smsConfig.setProv_msisdn(Long.parseLong(smsConfigMt.getProv_msisdn_sms()));
											                 smsConfigRepository.saveAndFlush(smsConfig);
												          
											                 Sms smsObj=new Sms();
								           	                 smsObj.setUsr_msisdn(resource.getMsisdn());
								           	                 smsObj.setProv_flag(smsConfigMt.getProv_flag());
								           	                 smsObj.setProv_msisdn(new StringBuilder().append(smsConfigMt.getProv_msisdn_sms()).append(":").append("added").toString());
												             smsConfigResponse.add(smsObj);
												             succCount++;
												           
								                     }
										        	   logger.info("End of Case : Prov_msisdn_sms_mt is not present in database");
												 }	
											    
											     logger.info("End of valid Prov_msisdn_sms_mt Block");	
								        	} 	
										 	 
					        	    	 logger.info("End of Sms Mt for loop");      
										        	
							         }
							         
							       logger.info("End of Sms_mt block");
							  }
					            logger.info("End of Sms Block");
					}         
					          
					
					if(resource.getVoiceConfig()!=null && !(resource.getVoiceConfig().isEmpty()) && resource.getVoiceConfig().size()>0)
					{	
					  			     
				                  logger.info("Starting Voice Config Block");
				                  
				  			     
				  			       voiceConfigDto=resource.getVoiceConfig().stream().collect(Collectors.toCollection(LinkedHashSet::new));
				  		         
				  			       voice_whitelist_msisdn_mo=resource.getVoiceConfig().stream().filter(v->v.getProv_flag()==0).collect(Collectors.toCollection(LinkedHashSet::new));
				  		          
				  			       voice_whitelist_msisdn_mt=resource.getVoiceConfig().stream().filter(v->v.getProv_flag()==1).collect(Collectors.toCollection(LinkedHashSet::new));
				  		          
				  		        
					  		          
					  		        if(voice_whitelist_msisdn_mo.size()>0)
					  		        { 	
					  		    	  
					  		        	   logger.info("Processing VoiceConfig_Mo Block");
					  		        	   
					  		               for(VoiceConfigDto voiceConfigMo : voice_whitelist_msisdn_mo)
					  		               { 	
					  					    	
					  						    	logger.info("Voice MO for Loop");
					  						      
					  								
					  								if(voiceConfigMo.getProv_msisdn_voice().matches("\\d+") && !(voiceConfigMo.getProv_msisdn_voice().length() > 9 && voiceConfigMo.getProv_msisdn_voice().length() < 14))
					  					        	{
					  					        		  
					  									  logger.info("Prov_msisdn_call_mo is invalid");
					  									  Voice voiceObj=new Voice();
					  									  voiceObj.setUsr_msisdn(resource.getMsisdn());
					  									  voiceObj.setProv_flag(voiceConfigMo.getProv_flag());
					  									  voiceObj.setProv_msisdn(new StringBuilder().append(voiceConfigMo.getProv_msisdn_voice()).append(":").append("failed").toString());
					  						              voiceConfigResponse.add(voiceObj);
					  						              failCountVoice++;
					  					        	}
					  								else if(!voiceConfigMo.getProv_msisdn_voice().matches("\\d+"))
					  					        	{
					  					        		  
					  									  logger.info("Prov_msisdn_call_mo is invalid");
					  									  Voice voiceObj=new Voice();
					  									  voiceObj.setUsr_msisdn(resource.getMsisdn());
					  									  voiceObj.setProv_flag(voiceConfigMo.getProv_flag());
					  									  voiceObj.setProv_msisdn(new StringBuilder().append(voiceConfigMo.getProv_msisdn_voice()).append(":").append("failed").toString());
					  						              voiceConfigResponse.add(voiceObj);
					  						              failCountVoice++;
					  					        	}
					  								else if(voiceConfigMo.getProv_msisdn_voice().matches("\\d+") && (voiceConfigMo.getProv_msisdn_voice().length() > 9 && voiceConfigMo.getProv_msisdn_voice().length() < 14))
					  					        	{
					  									
					  									logger.info("Operations On Valid Prov_msisdn_voice_mo");
					  									VoiceConfigId compositeId=new VoiceConfigId(resource.getMsisdn(),Long.parseLong(voiceConfigMo.getProv_msisdn_voice()),voiceConfigMo.getProv_flag());
					  								    voiceConfigOptional=voiceConfigRepository.findById(compositeId);
					  								    voiceConfigOptional.ifPresent(System.out::println);
					  										
					  							        if(voiceConfigOptional.isPresent())
					  							        {   
						  							               logger.info("Ist Case : Prov_msisdn_voice_mo is present in the system");
						  							                
						  									        Voice voiceObj=new Voice();
			  									                    voiceObj.setUsr_msisdn(resource.getMsisdn());
						  									        voiceObj.setProv_flag(voiceConfigMo.getProv_flag());
						  									        voiceObj.setProv_msisdn(new StringBuilder().append(voiceConfigMo.getProv_msisdn_voice()).append(":").append("failed_409").toString());
						  											voiceConfigResponse.add(voiceObj);
						  											failCountVoice++;
					  								    }
					  										       
					  								    else if(!voiceConfigOptional.isPresent())
					  								    {	
					  								    	  logger.info("Case : Prov_msisdn_voice_mo is not present in database");
					  								    	  count=voiceConfigRepository.countByUsr_msisdnAndProv_flag(resource.getMsisdn(),voiceConfigMo.getProv_flag());
					  										
					  									      
					  							        	  if(count==max_voice_whitelist_msisdn_mo)
					  					                      {	   
					  							            	  
					  							        		  logger.info("voice_msisdn_mo_count==max_voice_whitelist_msisdn_mo");
					  											 
					  										      Voice voiceObj=new Voice();
					  										      voiceObj.setUsr_msisdn(resource.getMsisdn());
					  										      voiceObj.setProv_flag(voiceConfigMo.getProv_flag());
					  							           	      voiceObj.setProv_msisdn(new StringBuilder().append(voiceConfigMo.getProv_msisdn_voice()).append(":").append("failed").toString());
					  								              voiceConfigResponse.add(voiceObj);
					  								              failCountVoice++;
					  								              
					  					                      }
					  						        	   
					  							        	  else if(count<max_voice_whitelist_msisdn_mo)
					  					                      {  
					  								            	 
					  							        		     logger.info("voice_msisdn_mo_count<max_voice_whitelist_msisdn_mo"); 
					  								                 VoiceConfig voiceConfig =new VoiceConfig();
					  								                 voiceConfig.setUsr_msisdn(resource.getMsisdn());
					  								                 voiceConfig.setProv_flag(voiceConfigMo.getProv_flag());
					  								                 voiceConfig.setProv_msisdn(Long.parseLong(voiceConfigMo.getProv_msisdn_voice()));
					  								                 voiceConfigRepository.saveAndFlush(voiceConfig);
					  									          
					  								                 Voice voiceObj=new Voice();
					  					           	                 voiceObj.setUsr_msisdn(resource.getMsisdn());
					  					           	                 voiceObj.setProv_flag(voiceConfigMo.getProv_flag());
					  					           	                 voiceObj.setProv_msisdn(new StringBuilder().append(voiceConfigMo.getProv_msisdn_voice()).append(":").append("added").toString());
					  									             voiceConfigResponse.add(voiceObj);
					  									             succCountVoice++;
					  									           
					  					                     }
					  						        	  
					  							        	  logger.info("End of Case : Prov_msisdn_voice_mo is not present in database");
					  								     } 	  
					  									
					  								    logger.info("End of operations on valid Prov_msisdn_voice_mo Block");	
					  								}	
					  								
					  						        logger.info("End of Voice Mo for loop");      
					  					        	
					  		                }
					  		                 
					  		               logger.info("End of Voice_mo block");
					  		          }
					  		               
					  		          if(voice_whitelist_msisdn_mt.size()>0)
					  				  { 	
					  				    	 
					  		        	     logger.info("Processing VoiceConfig_Mo Block");
					  		        	     
					  		        	     for(VoiceConfigDto voiceConfigMt : voice_whitelist_msisdn_mt)
					  				          { 	
					  							        
					  				        	        logger.info("Voice MT for Loop");
					  							       
					  									
					  									if(voiceConfigMt.getProv_msisdn_voice().matches("\\d+") && !(voiceConfigMt.getProv_msisdn_voice().length() > 9 && voiceConfigMt.getProv_msisdn_voice().length() < 14))
						  					        	{
						  					        		  
						  									  logger.info("Prov_msisdn_call_mo is invalid");
						  									  Voice voiceObj=new Voice();
						  									  voiceObj.setUsr_msisdn(resource.getMsisdn());
						  									  voiceObj.setProv_flag(voiceConfigMt.getProv_flag());
						  									  voiceObj.setProv_msisdn(new StringBuilder().append(voiceConfigMt.getProv_msisdn_voice()).append(":").append("failed").toString());
						  						              voiceConfigResponse.add(voiceObj);
						  						              failCountVoice++;
						  					        	}
					  									
					  									else if(!voiceConfigMt.getProv_msisdn_voice().matches("\\d+") )
						  					        	{
						  					        		  
						  									  logger.info("Prov_msisdn_call_mt is invalid");
						  									  Voice voiceObj=new Voice();
						  									  voiceObj.setUsr_msisdn(resource.getMsisdn());
						  									  voiceObj.setProv_flag(voiceConfigMt.getProv_flag());
						  									  voiceObj.setProv_msisdn(new StringBuilder().append(voiceConfigMt.getProv_msisdn_voice()).append(":").append("failed").toString());
						  						              voiceConfigResponse.add(voiceObj);
						  						              failCountVoice++;
						  					        	}
					  								    
					  									else if(voiceConfigMt.getProv_msisdn_voice().matches("\\d+") && (voiceConfigMt.getProv_msisdn_voice().length() > 9 && voiceConfigMt.getProv_msisdn_voice().length() < 14))
						  					        	{

						  									VoiceConfigId compositeId=new VoiceConfigId(resource.getMsisdn(),Long.parseLong(voiceConfigMt.getProv_msisdn_voice()),voiceConfigMt.getProv_flag());
						  								    voiceConfigOptional=voiceConfigRepository.findById(compositeId);
						  								    voiceConfigOptional.ifPresent(System.out::println);
						  											
						  								    if(voiceConfigOptional.isPresent())
						  								    {   
						  								            	logger.info("Ist Case : Prov_msisdn_voice_mt is present in the system");
						  										        Voice voiceObj=new Voice();
						  										        voiceObj.setUsr_msisdn(resource.getMsisdn());
						  										        voiceObj.setProv_flag(voiceConfigMt.getProv_flag());
						  										        voiceObj.setProv_msisdn(new StringBuilder().append(voiceConfigMt.getProv_msisdn_voice()).append(":").append("failed_409").toString());
						  											    voiceConfigResponse.add(voiceObj);
						  												failCountVoice++;
						  									 }
						  												       
						  									 else if(!voiceConfigOptional.isPresent())
						  									 {
						  										  count=voiceConfigRepository.countByUsr_msisdnAndProv_flag(resource.getMsisdn(),voiceConfigMt.getProv_flag());
						  									
						  										  
						  							        	  if(count==max_voice_whitelist_msisdn_mt)
						  					                      {	   
						  							            	  logger.info("voice_msisdn_mt_count==max_voice_whitelist_msisdn_mt");
						  											
						  										      Voice voiceObj=new Voice();
						  							           	      voiceObj.setUsr_msisdn(resource.getMsisdn());
						  							           	      voiceObj.setProv_flag(voiceConfigMt.getProv_flag());
						  							           	      voiceObj.setProv_msisdn(new StringBuilder().append(voiceConfigMt.getProv_msisdn_voice()).append(":").append("failed").toString());
						  								            
						  								              voiceConfigResponse.add(voiceObj);
						  								              failCountVoice++;
						  					                      }
					  							        	   
						  							        	  else if(count<max_voice_whitelist_msisdn_mt)
						  					                      {  
						  								            	 
						  							        		     logger.info("voice_msisdn_mt_count<max_voice_whitelist_msisdn_mt"); 
						  								                 VoiceConfig voiceConfig =new VoiceConfig();
						  								                 voiceConfig.setUsr_msisdn(resource.getMsisdn());
						  								                 voiceConfig.setProv_flag(voiceConfigMt.getProv_flag());
						  								                 voiceConfig.setProv_msisdn(Long.parseLong(voiceConfigMt.getProv_msisdn_voice()));
						  								                 voiceConfigRepository.saveAndFlush(voiceConfig);
						  									          
						  								                 Voice voiceObj=new Voice();
						  					           	                 voiceObj.setUsr_msisdn(resource.getMsisdn());
						  					           	                 voiceObj.setProv_flag(voiceConfigMt.getProv_flag());
						  					           	                 voiceObj.setProv_msisdn(new StringBuilder().append(voiceConfigMt.getProv_msisdn_voice()).append(":").append("added").toString());
						  									          
						  									             voiceConfigResponse.add(voiceObj);
						  									             succCountVoice++;
						  									           
						  					                     }
						  							        	  
						  							        	 logger.info("End of Case : Prov_msisdn_voice_mt is not present in database");
					  									     }	
						  								         logger.info("End of operations on valid Prov_msisdn_voice_mt Block");	
						  					        	}	
					  									  
					  								      logger.info("End of Voice Mt for loop");           	
					  				             }
					  				         
					  				         logger.info("End of Voice_mt block");
					  				  }  
					  		         
					                   logger.info("End of Voice Block");
					 }    
				    
					
					    apiResponsePojo=new ApiResponsePojo();
					    apiResponsePojo.setProduct(apiInfo.getProduct());
						apiResponsePojo.setVersion(apiInfo.getVersion());
						apiResponsePojo.setRelease(apiInfo.getRelease());
						apiResponsePojo.setMsisdn(resource.getMsisdn().toString());
						apiResponsePojo.setAction("updateUser");
					
						
					
					    if(voiceConfigDto!=null  && succCountVoice!=voiceConfigDto.size() && voiceConfigDto.size()>0 && smsConfigDto!=null  && succCount!=smsConfigDto.size() && smsConfigDto.size()>0)
				        {      
							    
						        logger.info("AddUserWrapper[Partial Addition of Voice && Sms Block]");  
						        apiResponsePojo.setReturnCode("Multi-Status");
						        apiResponsePojo.setRemarks("partial add");
						        apiResponsePojo.setSms(smsConfigResponse);
						        apiResponsePojo.setVoice(voiceConfigResponse);
					        
						  
				         }
					    
					    else if(voiceConfigDto!=null  && succCountVoice!=voiceConfigDto.size() && voiceConfigDto.size()>0 && smsConfigDto!=null && succCount==smsConfigDto.size() && smsConfigDto.size()>0 )
				        {      
							    
						        logger.info("AddUserWrapper[Partial Addition of Voice Block]");  
						        apiResponsePojo.setReturnCode("Multi-Status");
						        apiResponsePojo.setRemarks("partial add");
						        apiResponsePojo.setSms(smsConfigResponse);
						        apiResponsePojo.setVoice(voiceConfigResponse);
					        
						  
				         }
					    
					    else if(voiceConfigDto!=null && succCountVoice==voiceConfigDto.size() && voiceConfigDto.size()>0 && smsConfigDto!=null && succCount!=smsConfigDto.size() && smsConfigDto.size()>0)
				        {      
							    
						        logger.info("AddUserWrapper[Partial Addition of Sms Block]");  
						        apiResponsePojo.setReturnCode("Multi-Status");
						        apiResponsePojo.setRemarks("partial add");
						        apiResponsePojo.setSms(smsConfigResponse);
						        apiResponsePojo.setVoice(voiceConfigResponse);
					        
						  
				         }
					    
					    else if(voiceConfigDto!=null  && succCountVoice==voiceConfigDto.size() && voiceConfigDto.size()>0 && smsConfigDto!=null && succCount==smsConfigDto.size() && smsConfigDto.size()>0)
				        {      
							    
						        logger.info("AddUserWrapper[Voice && Sms Block Success]");  
						        apiResponsePojo.setReturnCode("success");
						        apiResponsePojo.setRemarks("added");
						        apiResponsePojo.setSms(smsConfigResponse);
						        apiResponsePojo.setVoice(voiceConfigResponse);
					        
						  
				        }
					
				         //else if(voiceConfigDto!=null &&  voiceConfigDto.size()>0 && succCountVoice==voiceConfigDto.size() && smsConfigDto==null)
				         else if (voiceConfigDto != null && voiceConfigDto.size() > 0 && succCountVoice == voiceConfigDto.size() && smsConfigDto != null && smsConfigDto.size() == 0) 	 
			             {      
								    
								    logger.info(" Succ block Voice");
								    apiResponsePojo.setReturnCode("success");
							        apiResponsePojo.setRemarks("added");
							        apiResponsePojo.setVoice(voiceConfigResponse);
							       
				         }	
				 
				        // else if(smsConfigDto!=null && smsConfigDto.size()>0 && succCount==smsConfigDto.size() && voiceConfigDto==null)
				         else if (smsConfigDto != null && smsConfigDto.size() > 0 && succCount == smsConfigDto.size() && voiceConfigDto != null && voiceConfigDto.size() == 0) 	 
			             {      
								    
								    logger.info(" Succ block Sms");
								    apiResponsePojo.setReturnCode("success");
							        apiResponsePojo.setRemarks("added");
							        apiResponsePojo.setSms(smsConfigResponse);
							       
				         }	
			
			             //else if(voiceConfigDto!=null && succCountVoice!=voiceConfigDto.size() && failCountVoice!=0 && smsConfigDto==null)
			             else if (voiceConfigDto != null && succCountVoice != voiceConfigDto.size() && failCountVoice != 0 && smsConfigDto != null && smsConfigDto.size() == 0) 
			            		 	 
			             {      
								    
								    logger.info("AddUserWrapper[Partial Addition of Voice Block Only , Sms Block not present]");
								    apiResponsePojo.setReturnCode("Multi-Status");
							        apiResponsePojo.setRemarks("partial add");
							        apiResponsePojo.setVoice(voiceConfigResponse);
							       
				         }	
			    
				         //else if(smsConfigDto!=null && succCount!=smsConfigDto.size() && failCount!=0 && voiceConfigDto==null)
				         else if (smsConfigDto != null && succCount != smsConfigDto.size() && failCount != 0 && voiceConfigDto != null && voiceConfigDto.size() == 0) 
			             {      
							    
							    logger.debug("AddUserWrapper[Partial Addition of Sms Block Only, Voice Block not present]");
							    apiResponsePojo.setAction("addUserWrapper");
						        apiResponsePojo.setReturnCode("Multi-Status");
						        apiResponsePojo.setRemarks("partial add");
						        apiResponsePojo.setSms(smsConfigResponse);
						       
			             }
				            
					    
					    logger.info("End Of Partial Addition Block");
					    
		}
		
		     return apiResponsePojo;
		
    }
	
	public synchronized ApiResponsePojo modifyUser(final UserMasterUpdateDto resource)
	{   
		
		smsConfigResponse =new LinkedHashSet<Sms>();
		voiceConfigDto=new LinkedHashSet<VoiceConfigDto>();
		smsConfigDto=new LinkedHashSet<SmsConfigDto>();
		voiceConfigResponse=new LinkedHashSet<Voice>();
		
		
		Long msisdn=resource.getMsisdn().longValue();
		int cnt=this.countByMsisdn(msisdn);
		if(cnt==0)
		{		
			 throw new ResourceNotFoundException("Msisdn is not present in the system",  Long.toString(msisdn));
		}
	  
		else
		{	
			if(!smsConfigResponse.isEmpty())
			{	
			  logger.info("Clear SmsConfigResponse Set");
			  smsConfigResponse.clear();
			 
			}
			
			if(!voiceConfigResponse.isEmpty())
			{	
				logger.info("Clear VoiceConfigResponse Set");
				voiceConfigResponse.clear();
			}
			
			 if (!smsConfigDto.isEmpty())
			 {
				       
				 logger.info("Clear SmsConfigDto Set");
				 smsConfigDto.clear();
			 } 
				    
		     if (!voiceConfigDto.isEmpty())
		     {
				       
				 logger.info("Clear VoiceConfigDto Set");
				 voiceConfigDto.clear();
			 } 
			
//			if(smsConfigDto!=null)
//			{   
//				logger.info("Clear SmsConfigDto Set");
//				smsConfigDto.clear();
//			}
//			
//			if(voiceConfigDto!=null)
//			{	
//			   logger.info("Clear VoiceConfigDto Set");	
//			   voiceConfigDto.clear();
//			}
			
		    long prov_msisdn=0L;
		    
		    fetchMaxWhitelistCapacity();
		    
		    int prov_flag=0;
		    
			String prov_msisdn_sms_new=null;
			
			succCount=0;
			
			failCount=0;
			
			succCountVoice=0;
			
			failCountVoice=0;
			
		
		if(resource.getSmsConfig()!=null && resource.getSmsConfig().size()>0)
		{	
			      logger.info("Starting Sms Config Block");
		          smsConfigDto=resource.getSmsConfig().stream().collect(Collectors.toCollection(LinkedHashSet::new));
		          sms_whitelist_msisdn_mo=resource.getSmsConfig().stream().filter(s->s.getProv_flag()==0).collect(Collectors.toCollection(LinkedHashSet::new));
		          sms_whitelist_msisdn_mt=resource.getSmsConfig().stream().filter(s->s.getProv_flag()==1).collect(Collectors.toCollection(LinkedHashSet::new));
		          logger.info("SmsConfigDtoMt["+sms_whitelist_msisdn_mt.size()+"]");
		          logger.info("SmsConfigDtoMo["+sms_whitelist_msisdn_mo.size()+"]");
		          
		          if(sms_whitelist_msisdn_mo.size()>0)
		          { 	
		    	       logger.info("Processing SmsConfig_Mo Block");
		              
		    	       for(SmsConfigDto smsConfigMo : sms_whitelist_msisdn_mo)
		               { 	
					    	
					    	//logger.info("SMS MO for Loop "+msisdn+":::"+resource.getMsisdn().longValue()+"-->"+smsConfigMo.getProv_msisdn_sms()+"--counter--"+succCount+"--fail counter--"+failCount);
					        
							if(!smsConfigMo.getProv_msisdn_sms().equalsIgnoreCase("NA"))
		                    {	   
						            prov_msisdn=Long.parseLong(smsConfigMo.getProv_msisdn_sms());
						            SmsConfigId compositeId=new SmsConfigId(msisdn,Long.parseLong(smsConfigMo.getProv_msisdn_sms()),smsConfigMo.getProv_flag());
						            
						            smsConfigOptional=smsConfigRepository.findById(compositeId);
								    //smsConfigOptional
									//smsConfigOptional.ifPresent(System.out::println);
						            //logger.info("smsConfigOptional.ifPresent"+smsConfigOptional.isPresent()+"--compositeId"+compositeId.getUsr_msisdn()+"--->"+compositeId.getProv_msisdn());
								    if(smsConfigOptional.isPresent())
						            {   
								    	logger.info("SmsConfigOptional Present");
						            	logger.info("Ist Case : Prov_Msisdn_Sms_Mo_Old Is Present In Database");
								        SmsConfig smsConfigobj=smsConfigOptional.get();
								        
							              //*****new prov_msisdn is NA ********//
							            if(smsConfigMo.getProv_msisdn_sms_new().equalsIgnoreCase("NA"))
							            {      
							            	   logger.info("Prov_Msisdn_Sms_Mo_New--->NA");
											   SmsConfigLog smsConfigLog =new SmsConfigLog();
											   smsConfigLog.setUsr_msisdn(smsConfigobj.getUsr_msisdn());
											   smsConfigLog.setProv_msisdn(smsConfigobj.getProv_msisdn());
										       smsConfigLog.setProv_flag(smsConfigobj.getProv_flag());
										       smsConfigLog.setProv_startTime(smsConfigobj.getProv_startTime());
										       smsConfigLog.setProv_endTime(smsConfigobj.getProv_endTime());
										       smsConfigLog.setProv_startDate(smsConfigobj.getProv_startDate());
										       smsConfigLog.setProv_endDate(smsConfigobj.getProv_endDate());
										       smsConfigLog.setProv_startMonth(smsConfigobj.getProv_startMonth());
										       smsConfigLog.setProv_endMonth(smsConfigobj.getProv_endMonth());
										       smsConfigLog.setRec_createDateTime(smsConfigobj.getRec_createDateTime());
										       smsConfigLog.setRec_updateDateTime(smsConfigobj.getRec_updateDateTime());
										       smsConfigLog.setAction("modifyUser");
									           SmsConfigLog dbResponse=smsConfigLogRepository.saveAndFlush(smsConfigLog);
									           
								         
				                           if(dbResponse!=null)
				                           {   
							                	   int rowsDeleted=smsConfigRepository.deleteByCompositeId(msisdn,smsConfigMo.getProv_flag(),Long.parseLong(smsConfigMo.getProv_msisdn_sms()));
							               		   logger.info("Rows Deleted["+rowsDeleted+"]");
							                	   Sms smsObj=new Sms();
							                	   smsObj.setUsr_msisdn(msisdn);
							                	   smsObj.setProv_flag(smsConfigMo.getProv_flag());
							                	   smsObj.setProv_msisdn(new StringBuilder().append(smsConfigMo.getProv_msisdn_sms()).append(":").append(smsConfigMo.getProv_msisdn_sms_new().replace("NA","")).append(":").append("updated").toString());
												   
												   smsConfigResponse.add(smsObj);
												   succCount++;
				                           }
				                             logger.info("End of prov_msisdn_sms_new [NA] Case");
				              
					                 }// end of prov_msisdn_sms_mo_new=NA Block
							          
							            else if(smsConfigMo.getProv_msisdn_sms_new()!="" && smsConfigMo.getProv_msisdn_sms_new()!="NA" && smsConfigobj.getProv_msisdn()!=Long.parseLong(smsConfigMo.getProv_msisdn_sms_new()) && smsConfigMo.getProv_msisdn_sms()!="NA" )
										{
												  logger.info("Update Block");
												  
												  logger.info("Update Prov_msisdn_sms_mo");
								        	      
								        	      logger.info("Check Status of Prov_msisdn_sms_mo_new in Database Before Updating");
				                                  
								        	      SmsConfigId compositeIdSmsMo=new SmsConfigId(msisdn,Long.parseLong(smsConfigMo.getProv_msisdn_sms_new()),smsConfigMo.getProv_flag());
												  
								        	      smsConfigOptional=smsConfigRepository.findById(compositeIdSmsMo);
								        	      
												 // smsConfigOptional.ifPresent(System.out::println);
												  
												  if(smsConfigOptional.isPresent())
												  {
													     logger.info("Prov_Msisdn_Sms_Mo_New is present in Database"); 
													     
														 Sms obj=new Sms();
														 obj.setUsr_msisdn(msisdn);
														 obj.setProv_flag(smsConfigMo.getProv_flag());
														 obj.setProv_msisdn(new StringBuilder().append(smsConfigMo.getProv_msisdn_sms()).append(":").append(smsConfigMo.getProv_msisdn_sms_new()).append(":").append("failed_409").toString());
														 
														 smsConfigResponse.add(obj);
														 failCount++;
												  }
												  else if(!smsConfigOptional.isPresent())
												  {	  
												       int rowsUpdated = smsConfigRepository.update(msisdn,smsConfigMo.getProv_flag(),Long.parseLong(smsConfigMo.getProv_msisdn_sms()),Long.parseLong(smsConfigMo.getProv_msisdn_sms_new()));
											           logger.info("Rows Updated["+ rowsUpdated+"]");
												       
											           if(rowsUpdated>0)
												       {
													    	 logger.info("Prov_Msisdn_Sms_Mo_New Updated"); 
															 Sms obj=new Sms();
															 obj.setUsr_msisdn(msisdn);
															 obj.setProv_flag(smsConfigMo.getProv_flag());
															 obj.setProv_msisdn(new StringBuilder().append(smsConfigMo.getProv_msisdn_sms()).append(":").append(smsConfigMo.getProv_msisdn_sms_new()).append(":").append("updated").toString());
															 
															 smsConfigResponse.add(obj);
															 succCount++;
												       }
										           
											           else
												       {     
											        	       logger.info("Else Block---Error Encountered while updating Prov_Msisdn_Sms_Mo");
											        	       logger.info("Prov_Msisdn_Sms_Mo --->"+smsConfigMo.getProv_msisdn_sms_new()+"]");
														       Sms obj=new Sms();
														       obj.setUsr_msisdn(msisdn);
														       obj.setProv_flag(smsConfigMo.getProv_flag());
														       obj.setProv_msisdn(new StringBuilder().append(smsConfigMo.getProv_msisdn_sms()).append(":").append(smsConfigMo.getProv_msisdn_sms_new()).append(":").append("failed").toString());
															  
														        smsConfigResponse.add(obj);
														        failCount++;
													   }
												  }
												  logger.info("End of Sms_Prov_Msisdn_Mo_New Updation Block");
											  } // end of Prov_Msisdn_Sms_Mo_New!=NA Block  
							            
							            
							         
							            
							            else if(smsConfigMo.getProv_msisdn_sms().equals(smsConfigMo.getProv_msisdn_sms_new()) && !smsConfigMo.getProv_msisdn_sms().equals("NA") && !smsConfigMo.getProv_msisdn_sms_new().equals("NA"))
									    {     
							            	      logger.info("Prov_Msisdn_Sms_mo_new same as Prov_Msisdn_Sms_mt_old Block");
											       Sms obj=new Sms();
											       obj.setUsr_msisdn(msisdn);
											       obj.setProv_flag(smsConfigMo.getProv_flag());
											       obj.setProv_msisdn(new StringBuilder().append(smsConfigMo.getProv_msisdn_sms()).append(":").append(smsConfigMo.getProv_msisdn_sms_new()).append(":").append("failed").toString());
											    
											        smsConfigResponse.add(obj);
											        failCount++;
										}
							         
							            logger.info("End of Prov_Msisdn_Sms_mo_new same as Prov_Msisdn_Sms_mt_old Block");
			                    }	
								  
								    
								     else
								       {     
							        	       logger.info("Else Block [prov_msisdn_sms_mo is not present in the system]");
							        	       
							        	       logger.info("Prov_Msisdn_Sms_Mo_old--->"+smsConfigMo.getProv_msisdn_sms()+"]");
							        	       
							        	       logger.info("Prov_Msisdn_Sms_Mo_new--->"+smsConfigMo.getProv_msisdn_sms_new()+"]");
										       Sms obj=new Sms();
										       obj.setUsr_msisdn(msisdn);
										       obj.setProv_flag(smsConfigMo.getProv_flag());
										       if(smsConfigMo.getProv_msisdn_sms_new().equalsIgnoreCase("NA"))
										       {
											       prov_msisdn_sms_new="";
											       obj.setProv_msisdn(new StringBuilder().append(smsConfigMo.getProv_msisdn_sms()).append(":").append(prov_msisdn_sms_new).append(":").append("failed").toString());
										       }
										       else
										       {   
								                obj.setProv_msisdn(new StringBuilder().append(smsConfigMo.getProv_msisdn_sms()).append(":").append(smsConfigMo.getProv_msisdn_sms_new()).append(":").append("failed").toString());
										       } 
										   
										        smsConfigResponse.add(obj);
										        logger.info("smsConfigResponse -->"+smsConfigResponse);
										        failCount++;
									   }
						       
		                    }
					          
					        if(smsConfigMo.getProv_msisdn_sms().equalsIgnoreCase("NA") )
		                    {     
					        	  logger.info("2nd Case : Prov_msisdn_sms_old is NA");
					        	  long count=smsConfigRepository.countByUsr_msisdnAndProv_flag(msisdn,smsConfigMo.getProv_flag());
								  logger.info("Count["+count+"]");
					        	  if(count==max_sms_whitelist_msisdn_mo)
			                      {	   
					            	  logger.info("sms_msisdn_mo_count==max_sms_whitelist_msisdn_mo");
									
								      Sms smsObj=new Sms();
					           	      smsObj.setUsr_msisdn(msisdn);
					           	      smsObj.setProv_flag(smsConfigMo.getProv_flag());
					           	      smsObj.setProv_msisdn(new StringBuilder().append(smsConfigMo.getProv_msisdn_sms().replace("NA", "")).append(":").append(smsConfigMo.getProv_msisdn_sms_new()).append(":").append("failed").toString());
						             
						              smsConfigResponse.add(smsObj);
						              failCount++;
			                       }
					        	  
			                       else if(count<max_sms_whitelist_msisdn_mo)
			                       {  
						            	 logger.info("sms_msisdn_mo_count<max_sms_whitelist_msisdn_mo"); 
						            	 
						            	 logger.info("Check Status of Prov_msisdn_sms_mo_new in Database Before Updating");
		                                  
						        	      SmsConfigId compositeIdSmsMoObj=new SmsConfigId(msisdn,Long.parseLong(smsConfigMo.getProv_msisdn_sms_new()),smsConfigMo.getProv_flag());
										  
						        	      smsConfigOptional=smsConfigRepository.findById(compositeIdSmsMoObj);
						        	      
										 // smsConfigOptional.ifPresent(System.out::println);
										  
										  if(smsConfigOptional.isPresent())
										  {
											     logger.info("Prov_Msisdn_Sms_Mo_New is present in Database"); 
											     
												 Sms obj=new Sms();
												 obj.setUsr_msisdn(msisdn);
												 obj.setProv_flag(smsConfigMo.getProv_flag());
												 obj.setProv_msisdn(new StringBuilder().append(smsConfigMo.getProv_msisdn_sms().replace("NA", "")).append(":").append(smsConfigMo.getProv_msisdn_sms_new()).append(":").append("failed_409").toString());
												 
												 smsConfigResponse.add(obj);
												 
												 failCount++;
												 
												 logger.info("failCount["+failCount+"]");
										  }
										  
										  else if(!smsConfigOptional.isPresent())
										  {	  
											 logger.info("Prov_Msisdn_Sms_Mo_New is not present in Database"); 
											  
											 SmsConfig smsConfig =new SmsConfig();
							                 smsConfig.setUsr_msisdn(msisdn);
							                 smsConfig.setProv_flag(smsConfigMo.getProv_flag());
							                 smsConfig.setProv_msisdn(Long.parseLong(smsConfigMo.getProv_msisdn_sms_new()));
							                 smsConfigRepository.saveAndFlush(smsConfig);
								          
							                 Sms smsObj=new Sms();
				           	                 smsObj.setUsr_msisdn(msisdn);
				           	                 smsObj.setProv_flag(smsConfigMo.getProv_flag());
				           	                 smsObj.setProv_msisdn(new StringBuilder().append(smsConfigMo.getProv_msisdn_sms().replace("NA", "")).append(":").append(smsConfigMo.getProv_msisdn_sms_new()).append(":").append("updated").toString());
								          
								             smsConfigResponse.add(smsObj);
								             succCount++;
								             logger.info("succCount["+succCount+"]");
								             
										  }   
							       
			                        }
					        	     
					        	   logger.info("End of prov_msisdn_sms_old [NA] Case");
		                    }
		               
					              logger.info("End of Sms Mo for loop");
		               }	
                                           
		    
		              
		               
		               
		               if(sms_whitelist_msisdn_mt.size()>0)
		               { 	
					        
		            	    logger.info("Processing SmsConfig_MT Block");
		                    for(SmsConfigDto smsConfigMt : sms_whitelist_msisdn_mt)
			                { 	
			    	
								    	//logger.info("SMS MT for Loop");
								    	//logger.info("SMS MT for Loop "+msisdn+"-->"+smsConfigMt.getProv_msisdn_sms()+"--counter--"+succCount+"--fail counter--"+failCount);
									      
								     
								    	 	
									    if(!smsConfigMt.getProv_msisdn_sms().equalsIgnoreCase("NA"))
					                    {	   
								            prov_msisdn=Long.parseLong(smsConfigMt.getProv_msisdn_sms());
								        
								            SmsConfigId compositeId=new SmsConfigId(msisdn,Long.parseLong(smsConfigMt.getProv_msisdn_sms()),smsConfigMt.getProv_flag());
										    smsConfigOptional=smsConfigRepository.findById(compositeId);
										    
										    
										//	smsConfigOptional.ifPresent(System.out::println);
											
											if(smsConfigOptional.isPresent())
								            {   
										    	logger.info("Ist Case : Optional Is Present");
											    SmsConfig smsConfigobj=smsConfigOptional.get();
											    
											    /*****PROV_MSISDN_SMS_NEW is NA ********/
										          
											    if(smsConfigMt.getProv_msisdn_sms_new().equalsIgnoreCase("NA"))
									            {  
											       logger.info("Prov_msisdn_sms_new [NA] Case");	
												   SmsConfigLog smsConfigLog =new SmsConfigLog();
												   smsConfigLog.setUsr_msisdn(smsConfigobj.getUsr_msisdn());
												   smsConfigLog.setProv_msisdn(smsConfigobj.getProv_msisdn());
											       smsConfigLog.setProv_flag(smsConfigobj.getProv_flag());
											       smsConfigLog.setProv_startTime(smsConfigobj.getProv_startTime());
											       smsConfigLog.setProv_endTime(smsConfigobj.getProv_endTime());
											       smsConfigLog.setProv_startDate(smsConfigobj.getProv_startDate());
											       smsConfigLog.setProv_endDate(smsConfigobj.getProv_endDate());
											       smsConfigLog.setProv_startMonth(smsConfigobj.getProv_startMonth());
											       smsConfigLog.setProv_endMonth(smsConfigobj.getProv_endMonth());
											       smsConfigLog.setRec_createDateTime(smsConfigobj.getRec_createDateTime());
											       smsConfigLog.setRec_updateDateTime(smsConfigobj.getRec_updateDateTime());
											       smsConfigLog.setAction("modifyUser");
										           SmsConfigLog dbResponse=smsConfigLogRepository.saveAndFlush(smsConfigLog);
										          
						                           if(dbResponse!=null)
						                           {   
									                	   int rowsDeleted=smsConfigRepository.deleteByCompositeId(msisdn,smsConfigMt.getProv_flag(),Long.parseLong(smsConfigMt.getProv_msisdn_sms()));
									               		   logger.info("Rows Deleted["+rowsDeleted+"]");
									                	   Sms smsObj=new Sms();
									                	   smsObj.setUsr_msisdn(msisdn);
									                	   smsObj.setProv_flag(smsConfigMt.getProv_flag());
									                	   smsObj.setProv_msisdn(new StringBuilder().append(smsConfigMt.getProv_msisdn_sms()).append(":").append(smsConfigMt.getProv_msisdn_sms_new().replace("NA", "")).append(":").append("updated").toString());
														  
														   smsConfigResponse.add(smsObj);
														   succCount++;
						                           }
						                             logger.info("End of Prov_msisdn_sms_mt_new [NA] Case");
						              
							                 }
											 
											 else if(smsConfigMt.getProv_msisdn_sms_new()!="" && smsConfigMt.getProv_msisdn_sms_new()!="NA" && smsConfigobj.getProv_msisdn()!=Long.parseLong(smsConfigMt.getProv_msisdn_sms_new()) && smsConfigMt.getProv_msisdn_sms()!="NA" )
											 {
													  logger.info("Update Block--->Sms_Prov_Msisdn_Mt_New Block");
													  
													  logger.info("Update Prov_msisdn_sms_mt");
									        	      
									        	      logger.info("Check Status of Prov_msisdn_sms_mt_new in Database Before Updating");
					                                  
									        	      SmsConfigId compositeIdSmsMt=new SmsConfigId(msisdn,Long.parseLong(smsConfigMt.getProv_msisdn_sms_new()),smsConfigMt.getProv_flag());
													  smsConfigOptional=smsConfigRepository.findById(compositeIdSmsMt);
													  //smsConfigOptional.ifPresent(System.out::println);
													  
													  if(smsConfigOptional.isPresent())
													  {
														    logger.info("Prov_Msisdn_Sms_Mt_New is present in the database"); 
															 Sms obj=new Sms();
															 obj.setUsr_msisdn(msisdn);
															 obj.setProv_flag(smsConfigMt.getProv_flag());
															 obj.setProv_msisdn(new StringBuilder().append(smsConfigMt.getProv_msisdn_sms()).append(":").append(smsConfigMt.getProv_msisdn_sms_new()).append(":").append("failed_409").toString());
														
															 smsConfigResponse.add(obj);
															 failCount++;
													  }
													  else if(!smsConfigOptional.isPresent())
													  {	   
														   logger.info("Prov_Msisdn_Sms_Mt_New is not present in the database"); 
														   logger.info("Updating Prov_Msisdn_Sms_Mt old with new value");
														   logger.info("Prov_Msisdn_Sms_Mt_New--->"+smsConfigMt.getProv_msisdn_sms_new()+"]");
													       int rowsUpdated = smsConfigRepository.update(msisdn,smsConfigMt.getProv_flag(),prov_msisdn,Long.parseLong(smsConfigMt.getProv_msisdn_sms_new()));
												           logger.info("Rows Updated["+ rowsUpdated+"]");
													       
												           if(rowsUpdated>0)
													       {
														    	 logger.info("Prov_Msisdn_Sms_Mt_New Updated"); 
																 Sms obj=new Sms();
																 obj.setUsr_msisdn(msisdn);
																 obj.setProv_flag(smsConfigMt.getProv_flag());
																 obj.setProv_msisdn(new StringBuilder().append(smsConfigMt.getProv_msisdn_sms()).append(":").append(smsConfigMt.getProv_msisdn_sms_new()).append(":").append("updated").toString());
															
																 smsConfigResponse.add(obj);
																 succCount++;
													       }
											           
												           else
													       {     
												        	       logger.info("Else Block---Error Encountered while updating Prov_Msisdn_Sms_Mt");
															       Sms obj=new Sms();
															       obj.setUsr_msisdn(msisdn);
															       obj.setProv_flag(smsConfigMt.getProv_flag());
															       
													                obj.setProv_msisdn(new StringBuilder().append(smsConfigMt.getProv_msisdn_sms()).append(":").append(smsConfigMt.getProv_msisdn_sms_new()).append(":").append("failed").toString());
															  
															        smsConfigResponse.add(obj);
															        failCount++;
														   }
													  }
													  logger.info("End of Prov_Msisdn_Sms_Mt_New Updation Block");
											       
												  }  
											    
											 // end of Prov_Msisdn_Sms_Mt_New!=NA Block  
									            
									            
										         
									            
									            else if(smsConfigMt.getProv_msisdn_sms().equals(smsConfigMt.getProv_msisdn_sms_new()) && !smsConfigMt.getProv_msisdn_sms().equals("NA") && !smsConfigMt.getProv_msisdn_sms_new().equals("NA"))
											    {     
										        	       logger.info("Prov_Msisdn_Sms_mt_new same as Prov_Msisdn_Sms_mt_old Block");
													       Sms obj=new Sms();
													       obj.setUsr_msisdn(msisdn);
													       obj.setProv_flag(smsConfigMt.getProv_flag());
													      
											                obj.setProv_msisdn(new StringBuilder().append(smsConfigMt.getProv_msisdn_sms()).append(":").append(smsConfigMt.getProv_msisdn_sms_new()).append(":").append("failed").toString());
													    
													   
													        smsConfigResponse.add(obj);
													        failCount++;
												}
									         
									            logger.info("End of Prov_Msisdn_Sms_mt_new same as Prov_Msisdn_Sms_mt_old Block");
											    
								            }    
											
											  else
										       {     
									        	       logger.info("Else Block [Prov_msisdn_sms_mt Case]");
									        	       
									        	       logger.info("Prov_Msisdn_Sms_Mt_old--->"+smsConfigMt.getProv_msisdn_sms()+"]");
									        	       
									        	       logger.info("Prov_Msisdn_Sms_Mt_new--->"+smsConfigMt.getProv_msisdn_sms_new()+"]");
									        	       
												       Sms obj=new Sms();
												       obj.setUsr_msisdn(msisdn);
												       obj.setProv_flag(smsConfigMt.getProv_flag());
												       if(smsConfigMt.getProv_msisdn_sms_new().equalsIgnoreCase("NA"))
												       {
													       prov_msisdn_sms_new="";
													       obj.setProv_msisdn(new StringBuilder().append(smsConfigMt.getProv_msisdn_sms()).append(":").append(prov_msisdn_sms_new).append(":").append("failed").toString());
												        }
												       else
												       {	   
										                obj.setProv_msisdn(new StringBuilder().append(smsConfigMt.getProv_msisdn_sms()).append(":").append(smsConfigMt.getProv_msisdn_sms_new()).append(":").append("failed").toString());
												       }
												      
												        smsConfigResponse.add(obj);
												        failCount++;
											   }
											
											  logger.info("End of Prov_msisdn_sms_mt_old !=NA Case");
									       
														
											
					                   }	
								       logger.info("End of Ist Case");
					                   if(smsConfigMt.getProv_msisdn_sms().equalsIgnoreCase("NA") && !smsConfigMt.getProv_msisdn_sms_new().equalsIgnoreCase("NA") )
					                   {     
								        	  logger.info("2nd Case : old sms_mt is empty");
								        	  long count=smsConfigRepository.countByUsr_msisdnAndProv_flag(msisdn,smsConfigMt.getProv_flag());
											  logger.info("Count["+count+"]");
								        	  
						              
								              if(count==max_sms_whitelist_msisdn_mt)
						                      {	   
								            	  logger.info("sms_msisdn_mo_count==max_sms_whitelist_msisdn_mo");
												
											      Sms smsObj=new Sms();
								           	      smsObj.setUsr_msisdn(msisdn);
								           	      smsObj.setProv_flag(smsConfigMt.getProv_flag());
								           	      smsObj.setProv_msisdn(new StringBuilder().append(smsConfigMt.getProv_msisdn_sms().replace("NA", "")).append(":").append(smsConfigMt.getProv_msisdn_sms_new()).append(":").append("failed").toString());
									     
									              smsConfigResponse.add(smsObj);
									              failCount++;
						                       }
						                      
								              else if(count<max_sms_whitelist_msisdn_mt)
						                      {  
									            	 logger.info("sms_msisdn_mt_count<max_sms_whitelist_msisdn_mt"); 
									            	 
                                                     logger.info("Check Status of Prov_msisdn_sms_mt_new in Database Before Updating");
					                                  
									        	      SmsConfigId compositeIdSmsMtObj=new SmsConfigId(msisdn,Long.parseLong(smsConfigMt.getProv_msisdn_sms_new()),smsConfigMt.getProv_flag());
													  
									        	      smsConfigOptional=smsConfigRepository.findById(compositeIdSmsMtObj);
													 
													  if(smsConfigOptional.isPresent())
													  {
														    logger.info("Prov_Msisdn_Sms_Mt_New is present in the database"); 
															 Sms obj=new Sms();
															 obj.setUsr_msisdn(msisdn);
															 obj.setProv_flag(smsConfigMt.getProv_flag());
															 obj.setProv_msisdn(new StringBuilder().append(smsConfigMt.getProv_msisdn_sms().replace("NA","")).append(":").append(smsConfigMt.getProv_msisdn_sms_new()).append(":").append("failed_409").toString());
														
															 smsConfigResponse.add(obj);
															 failCount++;
															 logger.info("failCount["+failCount+"]");
													  }
													  
													  else if(!smsConfigOptional.isPresent())
													  {	 
														 logger.info("Prov_Msisdn_Sms_Mt_New is not present in the database");
										                 SmsConfig smsConfig =new SmsConfig();
										                 smsConfig.setUsr_msisdn(msisdn);
										                 smsConfig.setProv_flag(smsConfigMt.getProv_flag());
										                 smsConfig.setProv_msisdn(Long.parseLong(smsConfigMt.getProv_msisdn_sms_new()));
										                 smsConfigRepository.saveAndFlush(smsConfig);
											          
										                 Sms smsObj=new Sms();
							           	                 smsObj.setUsr_msisdn(msisdn);
							           	                 smsObj.setProv_flag(smsConfigMt.getProv_flag());
							           	                 smsObj.setProv_msisdn(new StringBuilder().append(smsConfigMt.getProv_msisdn_sms().replace("NA", "")).append(":").append(smsConfigMt.getProv_msisdn_sms_new()).append(":").append("updated").toString());
											        
											             smsConfigResponse.add(smsObj);
											             succCount++;
											             logger.info("succCount["+succCount+"]");
													  }    
						                       }	
						                 }
						            
			                          logger.info("End of NA Block[");
			                          logger.info("Prov_flag["+prov_flag+"]");
			                          logger.info("Prov_msisdn_sms_new["+prov_msisdn_sms_new+"]");
						
			                
					
			                 }
			                  
		                     logger.info("end of sms_mt for loop");   
		                 }    
		        
		                  logger.info("End of Sms Block");
		          
		         
	     }
			logger.info("#################");
		  
		}	
		
		
		
		////Voice Config Block
		if(resource.getVoiceConfig()!=null && resource.getVoiceConfig().size()>0)
		{	
			logger.info("Starting Voice Config Block");
		    voiceConfigDto=resource.getVoiceConfig().stream().collect(Collectors.toCollection(LinkedHashSet::new));
		    voice_whitelist_msisdn_mo=resource.getVoiceConfig().stream().filter(v->v.getProv_flag()==0).collect(Collectors.toCollection(LinkedHashSet::new));
		    voice_whitelist_msisdn_mt=resource.getVoiceConfig().stream().filter(v->v.getProv_flag()==1).collect(Collectors.toCollection(LinkedHashSet::new));
		    if(voice_whitelist_msisdn_mo.size()>0)
		    { 	
		    	logger.info("Processing Voice Mo block"); 	
		        for(VoiceConfigDto voiceConfigMo : voice_whitelist_msisdn_mo)
		        { 
		        	
		    	       logger.info("Start of Mo for Loop");
		    	       logger.info("voice MO for Loop "+msisdn+"-->"+voiceConfigMo.getProv_msisdn_voice()+"--counter--"+succCountVoice+"--fail counter--"+failCountVoice);
					      
				       
				       if(!voiceConfigMo.getProv_msisdn_voice().equalsIgnoreCase("NA"))
		               {	   
				            prov_msisdn=Long.parseLong(voiceConfigMo.getProv_msisdn_voice());
				          
				            VoiceConfigId compositeId=new VoiceConfigId(msisdn,Long.parseLong(voiceConfigMo.getProv_msisdn_voice()),voiceConfigMo.getProv_flag());
				            voiceConfigOptional=voiceConfigRepository.findById(compositeId);
				            //voiceConfigOptional.ifPresent(System.out::println);
					    
				            if(voiceConfigOptional.isPresent())
					        {   
				            	logger.info("Ist Case : VoiceConfigMo_Optional Is Present");
							    VoiceConfig voiceConfigobj=voiceConfigOptional.get();
						        //*****new prov_msisdn is NA ********//*
						        if(voiceConfigMo.getProv_msisdn_voice_new().equalsIgnoreCase("NA"))
						        {      
						        	   logger.info("Prov_msisdn_voice_new [NA] Case");
									   VoiceConfigLog voiceConfigLog =new VoiceConfigLog();
									   voiceConfigLog.setUsr_msisdn(voiceConfigobj.getUsr_msisdn());
									   voiceConfigLog.setProv_msisdn(voiceConfigobj.getProv_msisdn());
									   voiceConfigLog.setProv_flag(voiceConfigobj.getProv_flag());
									   voiceConfigLog.setProv_startTime(voiceConfigobj.getProv_startTime());
									   voiceConfigLog.setProv_endTime(voiceConfigobj.getProv_endTime());
									   voiceConfigLog.setProv_startDate(voiceConfigobj.getProv_startDate());
									   voiceConfigLog.setProv_endDate(voiceConfigobj.getProv_endDate());
									   voiceConfigLog.setProv_startMonth(voiceConfigobj.getProv_startMonth());
									   voiceConfigLog.setProv_endMonth(voiceConfigobj.getProv_endMonth());
									   voiceConfigLog.setRec_createDateTime(voiceConfigobj.getRec_createDateTime());
									   voiceConfigLog.setRec_updateDateTime(voiceConfigobj.getRec_updateDateTime());
									   voiceConfigLog.setAction("modifyUser");
							           VoiceConfigLog dbResponse=voiceConfigLogRepository.saveAndFlush(voiceConfigLog);
							         
			                           if(dbResponse!=null)
			                           {   
						                	   int rowsDeleted=voiceConfigRepository.deleteByCompositeId(msisdn,voiceConfigMo.getProv_flag(),Long.parseLong(voiceConfigMo.getProv_msisdn_voice()));
						               		   logger.info("Rows Deleted["+rowsDeleted+"]");
						                	   Voice voiceObj=new Voice();
						                	   voiceObj.setUsr_msisdn(msisdn);
						                	   voiceObj.setProv_flag(voiceConfigMo.getProv_flag());
						                	   voiceObj.setProv_msisdn(new StringBuilder().append(voiceConfigMo.getProv_msisdn_voice()).append(":").append(voiceConfigMo.getProv_msisdn_voice_new().replace("NA", "")).append(":").append("updated").toString());
										
											   voiceConfigResponse.add(voiceObj);
											   succCountVoice++;
			                           }
			                           
			                           
			                             logger.info("End of Prov_msisdn_voice_new_mo [NA] Case");
			              
				               }
						                else if(voiceConfigMo.getProv_msisdn_voice_new()!="" && voiceConfigMo.getProv_msisdn_voice_new()!="NA" && Long.parseLong(voiceConfigMo.getProv_msisdn_voice())!=Long.parseLong(voiceConfigMo.getProv_msisdn_voice_new()) && voiceConfigMo.getProv_msisdn_voice()!="NA" )
							            {
										  
							        	      logger.info("Prov_msisdn_voice_new_mo !=NA Case");
							        	      
							        	      logger.info("Update Prov_msisdn_voice_mo");
							        	      
							        	      logger.info("Check Status of Prov_msisdn_voice_new in Database Before Updating");
										      
							        	      VoiceConfigId compositeIdVoiceMo=new VoiceConfigId(msisdn,Long.parseLong(voiceConfigMo.getProv_msisdn_voice_new()),voiceConfigMo.getProv_flag());
									          voiceConfigOptional=voiceConfigRepository.findById(compositeIdVoiceMo);
									        //  voiceConfigOptional.ifPresent(System.out::println);
									          if(voiceConfigOptional.isPresent())
									          {
									        	  logger.info("Prov_msisdn_voice_mo_new Already Exists"); 
									        	  
												  Voice voiceObj=new Voice();
												  voiceObj.setUsr_msisdn(msisdn);
												  voiceObj.setProv_flag(voiceConfigMo.getProv_flag());
												  voiceObj.setProv_msisdn(new StringBuilder().append(voiceConfigMo.getProv_msisdn_voice()).append(":").append(voiceConfigMo.getProv_msisdn_voice_new()).append(":").append("failed_409").toString());
													
												  voiceConfigResponse.add(voiceObj);
												  failCountVoice++;
									          }
									          else if(!voiceConfigOptional.isPresent())
									          {	  
										          int rowsUpdated = voiceConfigRepository.update(msisdn,voiceConfigMo.getProv_flag(),Long.parseLong(voiceConfigMo.getProv_msisdn_voice()),Long.parseLong(voiceConfigMo.getProv_msisdn_voice_new()));
									              logger.info("Rows Updated["+ rowsUpdated+"]");
									      
										           if(rowsUpdated>0)
										           {
												    	 logger.info("Prov_msisdn_voice_mo_updated"); 
														 Voice voiceObj=new Voice();
														 voiceObj.setUsr_msisdn(msisdn);
														 voiceObj.setProv_flag(voiceConfigMo.getProv_flag());
														 voiceObj.setProv_msisdn(new StringBuilder().append(voiceConfigMo.getProv_msisdn_voice()).append(":").append(voiceConfigMo.getProv_msisdn_voice_new()).append(":").append("updated").toString());
														
														 voiceConfigResponse.add(voiceObj);
														 succCountVoice++;
										            }
							              
								                    else
								                    {  
								                	
								                       logger.info("Else Block---Error Encountered while updating Prov_Msisdn_Voice_Mo");
												       Voice voiceObj=new Voice();
												       voiceObj.setUsr_msisdn(msisdn);
												       voiceObj.setProv_flag(voiceConfigMo.getProv_flag());
												       
												      
											       
												        voiceObj.setProv_msisdn(new StringBuilder().append(voiceConfigMo.getProv_msisdn_voice()).append(":").append(voiceConfigMo.getProv_msisdn_voice_new()).append(":").append("failed").toString());
												       
												        voiceConfigResponse.add(voiceObj);
												        failCountVoice++;
									               }
										           logger.info("End of Prov_Msisdn_Voice_Mo_New Updation Block");
									          } 
									           //logger.info("End of Prov_msisdn_voice_new_mo Already Present Block"); 
									           logger.info("End of Prov_msisdn_voice_new_mo !=NA Case"); 
							            }
						                  // end of Prov_Msisdn_Voice_Mo_New!=NA Block  
					            
					           
							            else if(voiceConfigMo.getProv_msisdn_voice().equals(voiceConfigMo.getProv_msisdn_voice_new()) && !voiceConfigMo.getProv_msisdn_voice().equals("NA") && !voiceConfigMo.getProv_msisdn_voice_new().equals("NA"))
									    {     
								        	       logger.info("Prov_Msisdn_Voice_mo_new same as Prov_Msisdn_Voice_mo_old Block");
								        	       Voice voiceObj=new Voice();
											       voiceObj.setUsr_msisdn(msisdn);
											       voiceObj.setProv_flag(voiceConfigMo.getProv_flag());
											       
											      
											       voiceObj.setProv_msisdn(new StringBuilder().append(voiceConfigMo.getProv_msisdn_voice()).append(":").append(voiceConfigMo.getProv_msisdn_voice_new()).append(":").append("failed").toString());
											    
											   
											        voiceConfigResponse.add(voiceObj);
											        failCountVoice++;
										}
							         
							          
						        logger.info("End of Prov_Msisdn_Sms_mt_new same as Prov_Msisdn_Sms_mt_old Block");
					        }
				            else
						             {  
							        	       logger.info("Else Block [Prov Msisdn_voice_mo Not Prsent in system]");
							        	       logger.info("Prov_Msisdn_voice_mo_new--->"+voiceConfigMo.getProv_msisdn_voice_new()+"]");
								        	   logger.info("Prov_Msisdn_voice_mo_old--->"+voiceConfigMo.getProv_msisdn_voice()+"]");
										       Voice voiceObj=new Voice();
										       voiceObj.setUsr_msisdn(msisdn);
										       voiceObj.setProv_flag(voiceConfigMo.getProv_flag());
										       
										       if(voiceConfigMo.getProv_msisdn_voice_new().equalsIgnoreCase("NA"))
										       {
											       prov_msisdn_voice_new="";
											       voiceObj.setProv_msisdn(new StringBuilder().append(voiceConfigMo.getProv_msisdn_voice()).append(":").append(prov_msisdn_voice_new).append(":").append("failed").toString());
										       }
										       
										        else
										        {
										        	voiceObj.setProv_msisdn(new StringBuilder().append(voiceConfigMo.getProv_msisdn_voice()).append(":").append(voiceConfigMo.getProv_msisdn_voice_new()).append(":").append("failed").toString());
										        }
										        
										        
										        voiceConfigResponse.add(voiceObj);
										        failCountVoice++;
							          }
					           logger.info("End of Prov_msisdn_voice_mo_old !=NA Case"); 
		                    }
		               
		              
		               if(voiceConfigMo.getProv_msisdn_voice().equalsIgnoreCase("NA") )
		               {     
					        	  
		            	          logger.info("2nd Case : old voice_mo [NA]");
					        	  long count=voiceConfigRepository.countByUsr_msisdnAndProv_flag(msisdn,voiceConfigMo.getProv_flag());
								  logger.info("Count["+count+"]");
					        	 
			                    
					              if(count==max_voice_whitelist_msisdn_mo)
			                      {	   
					            	  logger.info("voice_msisdn_mo_count==max_voice_whitelist_msisdn_mo");
									
								      Voice voiceObj=new Voice();
								      voiceObj.setUsr_msisdn(msisdn);
								      voiceObj.setProv_flag(voiceConfigMo.getProv_flag());
								      voiceObj.setProv_msisdn(new StringBuilder().append(voiceConfigMo.getProv_msisdn_voice().replace("NA", "")).append(":").append(voiceConfigMo.getProv_msisdn_voice_new()).append(":").append("failed").toString());
						       
						              voiceConfigResponse.add(voiceObj);
						              failCountVoice++;
			                     }
					              
			                     else if(count<max_voice_whitelist_msisdn_mo)
			                     {  
						            	 
			                    	     logger.info("voice_msisdn_mo_count<max_voice_whitelist_msisdn_mo"); 
			                    	     
			                    	     logger.info("Check Status of Prov_msisdn_voice_mo_new in Database Before Updating");
									      
						        	      VoiceConfigId compositeIdVoiceMoObj=new VoiceConfigId(msisdn,Long.parseLong(voiceConfigMo.getProv_msisdn_voice_new()),voiceConfigMo.getProv_flag());
						        	      
								          voiceConfigOptional=voiceConfigRepository.findById(compositeIdVoiceMoObj);
								     
								          if(voiceConfigOptional.isPresent())
								          {
								        	  logger.info("Prov_msisdn_voice_mo_new Already Exists In Database"); 
								        	  
											  Voice voiceObj=new Voice();
											  voiceObj.setUsr_msisdn(msisdn);
											  voiceObj.setProv_flag(voiceConfigMo.getProv_flag());
											  voiceObj.setProv_msisdn(new StringBuilder().append(voiceConfigMo.getProv_msisdn_voice().replaceAll("NA", "")).append(":").append(voiceConfigMo.getProv_msisdn_voice_new()).append(":").append("failed_409").toString());
												
											  voiceConfigResponse.add(voiceObj);
											  failCountVoice++;
											  logger.info("failCountVoice["+failCountVoice+"]");
								          }
								          
								          else if(!voiceConfigOptional.isPresent()) 
								          {	  
								        	 logger.info("Prov_msisdn_voice_mo_new  does not Exist In Database"); 
								        	 VoiceConfig voiceConfig =new VoiceConfig();
							                 voiceConfig.setUsr_msisdn(msisdn);
							                 voiceConfig.setProv_flag(voiceConfigMo.getProv_flag());
							                 voiceConfig.setProv_msisdn(Long.parseLong(voiceConfigMo.getProv_msisdn_voice_new()));
							                 voiceConfigRepository.saveAndFlush(voiceConfig);
								             Voice voiceObj=new Voice();
							                 voiceObj.setUsr_msisdn(msisdn);
							                 voiceObj.setProv_flag(voiceConfigMo.getProv_flag());
							                 voiceObj.setProv_msisdn(new StringBuilder().append(voiceConfigMo.getProv_msisdn_voice().replace("NA", "")).append(":").append(voiceConfigMo.getProv_msisdn_voice_new()).append(":").append("updated").toString());
								       
								             voiceConfigResponse.add(voiceObj);
								             succCountVoice++;
								             logger.info("succCountVoice["+succCountVoice+"]");
								          }    
			                   }	
					              
					              logger.info("End of prov_msisdn_voice_old [NA] Case");
			             }
			            
                       
                          logger.info("end of Voice Mo for loop");
                          
		        }         
                         
				logger.info("End of Voice Mo Block");
		    
		    }
		         
		         if(voice_whitelist_msisdn_mt.size()>0)
			     { 	
				         logger.info("Voice Mt block"); 	
		                 for(VoiceConfigDto voiceConfigMt : voice_whitelist_msisdn_mt)
			             { 	
			    	
						    	logger.info("Voice Mt For Loop");
						    	logger.info("voice Mt for Loop "+msisdn+"-->"+voiceConfigMt.getProv_msisdn_voice()+"--counter--"+succCountVoice+"--fail counter--"+failCountVoice);
								   
							    
							    if(!voiceConfigMt.getProv_msisdn_voice().equalsIgnoreCase("NA"))
			                    {	  
			                    	
							    	logger.info("Prov_voice_msisdn_old_mt !=NA");
						            VoiceConfigId compositeId=new VoiceConfigId(msisdn,Long.parseLong(voiceConfigMt.getProv_msisdn_voice()),voiceConfigMt.getProv_flag());
								    voiceConfigOptional=voiceConfigRepository.findById(compositeId);
									//voiceConfigOptional.ifPresent(System.out::println);
						            
									if(voiceConfigOptional.isPresent())
						            {   
								    	
										logger.info("Ist Case : VoiceConfigMt_Optional Is Present");
									    VoiceConfig voiceConfigobj=voiceConfigOptional.get();
									    logger.info("VoiceProv_msisdn_new["+voiceConfigMt.getProv_msisdn_voice_new()+"]");
								        //*****new prov_msisdn is NA ********//
							            if(voiceConfigMt.getProv_msisdn_voice_new().equalsIgnoreCase("NA"))
							            {
										       
							            	   logger.info("Prov_msisdn_voice_new [NA] Case");
							            	   VoiceConfigLog voiceConfigLog =new VoiceConfigLog();
											   voiceConfigLog.setUsr_msisdn(voiceConfigobj.getUsr_msisdn());
											   voiceConfigLog.setProv_msisdn(voiceConfigobj.getProv_msisdn());
											   voiceConfigLog.setProv_flag(voiceConfigobj.getProv_flag());
											   voiceConfigLog.setProv_startTime(voiceConfigobj.getProv_startTime());
											   voiceConfigLog.setProv_endTime(voiceConfigobj.getProv_endTime());
											   voiceConfigLog.setProv_startDate(voiceConfigobj.getProv_startDate());
											   voiceConfigLog.setProv_endDate(voiceConfigobj.getProv_endDate());
											   voiceConfigLog.setProv_startMonth(voiceConfigobj.getProv_startMonth());
											   voiceConfigLog.setProv_endMonth(voiceConfigobj.getProv_endMonth());
											   voiceConfigLog.setRec_createDateTime(voiceConfigobj.getRec_createDateTime());
											   voiceConfigLog.setRec_updateDateTime(voiceConfigobj.getRec_updateDateTime());
											   voiceConfigLog.setAction("modifyUser");
									           VoiceConfigLog dbResponse=voiceConfigLogRepository.saveAndFlush(voiceConfigLog);
									           
					                           if(dbResponse!=null)
					                           {   
								                	   int rowsDeleted=voiceConfigRepository.deleteByCompositeId(msisdn,voiceConfigMt.getProv_flag(),Long.parseLong(voiceConfigMt.getProv_msisdn_voice()));
								               		   logger.info("Rows Deleted["+rowsDeleted+"]");
								                	   Voice voiceObj=new Voice();
								                	   voiceObj.setUsr_msisdn(msisdn);
								                	   voiceObj.setProv_flag(voiceConfigMt.getProv_flag());
								                	   voiceObj.setProv_msisdn(new StringBuilder().append(voiceConfigMt.getProv_msisdn_voice()).append(":").append(voiceConfigMt.getProv_msisdn_voice_new().replace("NA", "")).append(":").append("updated").toString());
												
													   voiceConfigResponse.add(voiceObj);
													   succCountVoice++;
					                           }
					                             logger.info("End of Prov_msisdn_voice_new [NA] Case");
				              
					                   }
							       
							           else if(voiceConfigMt.getProv_msisdn_voice_new()!="" && voiceConfigMt.getProv_msisdn_voice_new()!="NA" && Long.parseLong(voiceConfigMt.getProv_msisdn_voice())!=Long.parseLong(voiceConfigMt.getProv_msisdn_voice_new()) && voiceConfigMt.getProv_msisdn_voice()!="NA" )
									   {
											  
							        	      logger.info("Prov_msisdn_voice_mt_new !=NA Case");
							        	      
							        	      logger.info("Update Prov_msisdn_voice_mt");
							        	      
										      //------------New Block Addition---------------------------
							        	      
							        	      logger.info("Check Status of Prov_Msisdn_voice_new_mt In Database Before Updating");
							        	      
									          VoiceConfigId compositeVoiceMtId=new VoiceConfigId(msisdn,Long.parseLong(voiceConfigMt.getProv_msisdn_voice_new()),voiceConfigMt.getProv_flag());
											  
									          voiceConfigOptional=voiceConfigRepository.findById(compositeVoiceMtId);
									          
											 // voiceConfigOptional.ifPresent(System.out::println);
											  
											  if(voiceConfigOptional.isPresent())
									          {   
											         logger.info("New Voice_Prov_Msisdn_new_mt already Present");
											         
												     Voice voiceObj=new Voice();
												     
													 voiceObj.setUsr_msisdn(msisdn);
													 
													 voiceObj.setProv_flag(voiceConfigMt.getProv_flag());
													 
													 voiceObj.setProv_msisdn(new StringBuilder().append(voiceConfigMt.getProv_msisdn_voice()).append(":").append(voiceConfigMt.getProv_msisdn_voice_new()).append(":").append("failed_409").toString());
												
													 voiceConfigResponse.add(voiceObj);
													 
													 failCountVoice++;
									          }
											  
											  else if(!voiceConfigOptional.isPresent())
											  {
												  int rowsUpdated = voiceConfigRepository.update(msisdn,voiceConfigMt.getProv_flag(),Long.parseLong(voiceConfigMt.getProv_msisdn_voice()),Long.parseLong(voiceConfigMt.getProv_msisdn_voice_new()));
										          
										          logger.info("Rows Updated["+ rowsUpdated+"]");
									      
										           if(rowsUpdated>0)
										           {
												    	 logger.info("Prov_msisdn_voice_mt_updated"); 
														 Voice voiceObj=new Voice();
														 voiceObj.setUsr_msisdn(msisdn);
														 voiceObj.setProv_flag(voiceConfigMt.getProv_flag());
														 voiceObj.setProv_msisdn(new StringBuilder().append(voiceConfigMt.getProv_msisdn_voice()).append(":").append(voiceConfigMt.getProv_msisdn_voice_new()).append(":").append("updated").toString());
													
														 voiceConfigResponse.add(voiceObj);
														 succCountVoice++;
										            }
								              
										           else
										           {     
										        	   logger.info("Else Block---Error Encountered while updating Prov_Msisdn_Voice_Mt");
												       Voice voiceObj=new Voice();
												       voiceObj.setUsr_msisdn(msisdn);
												       voiceObj.setProv_flag(voiceConfigMt.getProv_flag());
												  
												       
												        voiceObj.setProv_msisdn(new StringBuilder().append(voiceConfigMt.getProv_msisdn_voice()).append(":").append(voiceConfigMt.getProv_msisdn_voice_new()).append(":").append("failed").toString());
												    
												        voiceConfigResponse.add(voiceObj);
												        failCountVoice++;
											       }
									         
									                logger.info("End of Prov_msisdn_voice_new_mo Updation Block"); 
									         
							                    }
											         logger.info("End of Prov_msisdn_voice_new_mt !=NA Case"); 
						                              // end of Prov_Msisdn_Voice_Mo_New!=NA Block  
									           }		  
											  ///-----------------End of New Block-----------------
											  
											 
									            else if(voiceConfigMt.getProv_msisdn_voice().equals(voiceConfigMt.getProv_msisdn_voice_new()) && !voiceConfigMt.getProv_msisdn_voice().equals("NA") && !voiceConfigMt.getProv_msisdn_voice_new().equals("NA"))
											    {     
										        	       logger.info("Prov_Msisdn_Voice_mo_new same as Prov_Msisdn_Voice_mo_old Block");
										        	       Voice voiceObj=new Voice();
													       voiceObj.setUsr_msisdn(msisdn);
													       voiceObj.setProv_flag(voiceConfigMt.getProv_flag());
													       
													      
													       voiceObj.setProv_msisdn(new StringBuilder().append(voiceConfigMt.getProv_msisdn_voice()).append(":").append(voiceConfigMt.getProv_msisdn_voice_new()).append(":").append("failed").toString());
													    
													   
													        voiceConfigResponse.add(voiceObj);
													        failCountVoice++;
												}
									         
									          
								                logger.info("End of Prov_Msisdn_Voice_mt_new same as Prov_Msisdn_Voice_mt_old Block");
								       
							        
									   }     
									          
			                   
									
									 else
							         {     
						        	       
							        	   logger.info("Else Block [Prov Msisdn_voice_MT Not Present in system]");
							        	   logger.info("Prov_Msisdn_voice_mt_new--->"+voiceConfigMt.getProv_msisdn_voice_new()+"]");
							        	   logger.info("Prov_Msisdn_voice_mt_old--->"+voiceConfigMt.getProv_msisdn_voice()+"]");
									       Voice voiceObj=new Voice();
									       voiceObj.setUsr_msisdn(msisdn);
									       voiceObj.setProv_flag(voiceConfigMt.getProv_flag());
									       
									       if(voiceConfigMt.getProv_msisdn_voice_new().equalsIgnoreCase("NA"))
									       {
										       prov_msisdn_voice_new="";
										       voiceObj.setProv_msisdn(new StringBuilder().append(voiceConfigMt.getProv_msisdn_voice()).append(":").append(prov_msisdn_voice_new).append(":").append("failed").toString());
											    
									        }
									       else
									       { 	   
									        voiceObj.setProv_msisdn(new StringBuilder().append(voiceConfigMt.getProv_msisdn_voice()).append(":").append(voiceConfigMt.getProv_msisdn_voice_new()).append(":").append("failed").toString());
									       }
									        voiceConfigResponse.add(voiceObj);
									        failCountVoice++;
								       
						       
							       }	
								    logger.info("End of Prov_msisdn_voice_mt_old !=NA Case"); 
			              }
			               logger.info("End of Ist Case");
			               if(voiceConfigMt.getProv_msisdn_voice().equalsIgnoreCase("NA") )
			               {     
						        	  logger.info("2nd Case : prov_msisdn_voice_mt [NA]");
						        	  long count=voiceConfigRepository.countByUsr_msisdnAndProv_flag(msisdn,voiceConfigMt.getProv_flag());
									  logger.info("Count["+count+"]");
						        	
						              if(count==max_voice_whitelist_msisdn_mt)
						              {	   
								            	  
						            	          logger.info("voice_msisdn_mt_count==max_voice_whitelist_msisdn_mt");
												  Voice voiceObj=new Voice();
											      voiceObj.setUsr_msisdn(msisdn);
											      voiceObj.setProv_flag(voiceConfigMt.getProv_flag());
											      voiceObj.setProv_msisdn(new StringBuilder().append(voiceConfigMt.getProv_msisdn_voice().replace("NA", "")).append(":").append(voiceConfigMt.getProv_msisdn_voice_new()).append(":").append("failed").toString());
									           
									              voiceConfigResponse.add(voiceObj);
									              failCount++;
						                }
						                else if(count<max_voice_whitelist_msisdn_mt)
						                {  
								            	 logger.info("voice_msisdn_mt_count<max_voice_whitelist_msisdn_mt"); 
								            	 
								            	 logger.info("Check Status of Prov_Msisdn_voice_new_mt In Database Before Updating");
								        	      
										          VoiceConfigId compositeIdVoiceMt=new VoiceConfigId(msisdn,Long.parseLong(voiceConfigMt.getProv_msisdn_voice_new()),voiceConfigMt.getProv_flag());
												  
										          voiceConfigOptional=voiceConfigRepository.findById(compositeIdVoiceMt);
										          
												  if(voiceConfigOptional.isPresent())
										          {   
												         logger.info("New Voice_Prov_Msisdn already Present");
												         
													     Voice voiceObj=new Voice();
													     
														 voiceObj.setUsr_msisdn(msisdn);
														 
														 voiceObj.setProv_flag(voiceConfigMt.getProv_flag());
														 
														 voiceObj.setProv_msisdn(new StringBuilder().append(voiceConfigMt.getProv_msisdn_voice().replace("NA",  "")).append(":").append(voiceConfigMt.getProv_msisdn_voice_new()).append(":").append("failed_409").toString());
													
														 voiceConfigResponse.add(voiceObj);
														 
														 failCountVoice++;
														 
														 logger.info("failCountVoice["+failCountVoice+"]");
										          }
												  else if(!voiceConfigOptional.isPresent())
												  {	  
													 logger.info("New Voice_Prov_Msisdn is not Present in Database"); 
									                 VoiceConfig voiceConfig =new VoiceConfig();
									                 voiceConfig.setUsr_msisdn(msisdn);
									                 voiceConfig.setProv_flag(voiceConfigMt.getProv_flag());
									                 voiceConfig.setProv_msisdn(Long.parseLong(voiceConfigMt.getProv_msisdn_voice_new()));
									                 voiceConfigRepository.saveAndFlush(voiceConfig);
										             
									                 Voice voiceObj=new Voice();
									                 voiceObj.setUsr_msisdn(msisdn);
									                 voiceObj.setProv_flag(voiceConfigMt.getProv_flag());
									                 voiceObj.setProv_msisdn(new StringBuilder().append(voiceConfigMt.getProv_msisdn_voice().replace("NA", "")).append(":").append(voiceConfigMt.getProv_msisdn_voice_new()).append(":").append("updated").toString());
										           
										             voiceConfigResponse.add(voiceObj);
										             succCountVoice++;
										             logger.info("succCountVoice["+succCountVoice+"]");
												  }   
						                   }	
				             }
			                  
	                            logger.info("end of voice_mt for loop");    
					
			            }
		                       logger.info("End of Voice Mt config Block"); 
			     }
		
	                           logger.info("End of Voice Config Block");
		}
		        
		 
		
		
		    apiResponsePojo=new ApiResponsePojo();
		    apiResponsePojo.setProduct(apiInfo.getProduct());
			apiResponsePojo.setVersion(apiInfo.getVersion());
			apiResponsePojo.setRelease(apiInfo.getRelease());
			apiResponsePojo.setMsisdn(msisdn.toString());
			apiResponsePojo.setAction("updateUser");
			logger.info("SuccCountVoice["+succCountVoice+"]");
			logger.info("SuccCount["+succCount+"]");
			logger.info("voiceConfigDto.size()["+voiceConfigDto.size()+"] smsConfigDto.size()["+smsConfigDto.size()+"]");
			
		if(voiceConfigDto!=null && succCountVoice==voiceConfigDto.size() && voiceConfigDto.size()>0 && smsConfigDto!=null && succCount==smsConfigDto.size() && smsConfigDto.size()>0)
		{      
				    
			        logger.info("Succ Voice config  && Succ Sms Config Block");  
				    apiResponsePojo.setReturnCode("success");
			        apiResponsePojo.setRemarks("updated");
			        logger.info("-----");
			        apiResponsePojo.setSms(smsConfigResponse);
			        apiResponsePojo.setVoice(voiceConfigResponse);
		        
			  
	    }
		
		else if(voiceConfigDto!=null && succCountVoice!=voiceConfigDto.size() && voiceConfigDto.size()>0 && smsConfigDto!=null && succCount==smsConfigDto.size() && smsConfigDto.size()>0)
		{      
				    
			        logger.info("Partial Update Voice config  && Succ Sms Config Block ");  
				    apiResponsePojo.setReturnCode("Multi-Status");
			        apiResponsePojo.setRemarks("partial update");
			        logger.info("-----");
			        apiResponsePojo.setSms(smsConfigResponse);
			        apiResponsePojo.setVoice(voiceConfigResponse);
		        
			  
	    }
		
		else if(voiceConfigDto!=null && succCountVoice==voiceConfigDto.size() && voiceConfigDto.size()>0 && smsConfigDto!=null && succCount!=smsConfigDto.size() && smsConfigDto.size()>0)
		{      
				    
			        logger.info("Partial Update Sms Config  && Succ Voice Config Block ");  
				    apiResponsePojo.setReturnCode("Multi-Status");
			        apiResponsePojo.setRemarks("partial update");
			        logger.info("-----");
			        apiResponsePojo.setSms(smsConfigResponse);
			        apiResponsePojo.setVoice(voiceConfigResponse);
		        
			  
	    }
		
		else if(voiceConfigDto!=null && succCountVoice!=voiceConfigDto.size() && voiceConfigDto.size()>0 && smsConfigDto!=null && succCount!=smsConfigDto.size() && smsConfigDto.size()>0)
		{      
				    
			        logger.info("Partial Update Voice config && Partial Update Sms Config Block");  
			        apiResponsePojo.setReturnCode("Multi-Status");
			        apiResponsePojo.setRemarks("partial update");
			        logger.info("-----");
			        apiResponsePojo.setSms(smsConfigResponse);
			        apiResponsePojo.setVoice(voiceConfigResponse);
		        
			  
	    }
		
		else if(voiceConfigDto!=null && succCountVoice==voiceConfigDto.size() && voiceConfigDto.size()>0)
	    {              
			            logger.info("Successful Update Voice config  Block");  
			            logger.debug("VoiceSuccCount["+succCountVoice+"]");
			            logger.debug("VoiceConfigDto["+voiceConfigDto.size()+"]");
				        apiResponsePojo.setReturnCode("success");
				        apiResponsePojo.setRemarks("updated");
				        logger.info("-----");
				        apiResponsePojo.setVoice(voiceConfigResponse);
				        
					  
	    }
		
		else if(smsConfigDto!=null && succCount==smsConfigDto.size() && smsConfigDto.size()>0)
	    {               
			            logger.info("Successful Update Sms Config  Block");  
					    logger.debug("SmsSuccCount["+succCount+"]");
					    logger.debug("SmsConfigDto["+smsConfigDto.size()+"]");
				        apiResponsePojo.setReturnCode("success");
				        apiResponsePojo.setRemarks("updated");
				        logger.info("-----");
				        apiResponsePojo.setSms(smsConfigResponse);
				        
					  
	    }
		
		else if(voiceConfigDto!=null && succCountVoice!=voiceConfigDto.size() && failCountVoice!=0 )
	    {      
						    
			                logger.info("Partial Update Voice config  Block--->Multi-Status");  
						    apiResponsePojo.setReturnCode("Multi-Status");
					        apiResponsePojo.setRemarks("partial update");
					        logger.info("-----");
					        apiResponsePojo.setVoice(voiceConfigResponse);
					       
		}	
	    
		else if(smsConfigDto!=null && succCount!=smsConfigDto.size() && failCount!=0 )
	    {      
					    
			            logger.info("Partial Update Sms config  Block--->Multi-Status");
					    apiResponsePojo.setAction("updateUser");
				        apiResponsePojo.setReturnCode("Multi-Status");
				        apiResponsePojo.setRemarks("partial update");
				        logger.info("-----");
				        apiResponsePojo.setSms(smsConfigResponse);
				       
	   }
		
		
	    logger.info("#-------------------------------------------");
       
		}   
		 return apiResponsePojo;
	}
	
	
	protected UserMaster convertToEntity(UserMasterDto dto) 
	{
		return this.modelmapper.map(dto, UserMaster.class);
	}

	protected UserMasterDto convertToDto(UserMaster entity) 
	{
		return this.modelmapper.map(entity, UserMasterDto.class);
	}

	@Override
	public boolean existsById(long msisdnexist) {

		boolean isMsisdnExists=userMasterRepository.existsById(msisdnexist);
		logger.info("IsMsisdnExists["+isMsisdnExists+"]");
		return isMsisdnExists;
	}
	@Override
	public int countByMsisdn(long msisdncount) {
		int cnt=userMasterRepository.countByMsisdn(msisdncount);
		logger.info("Count["+cnt+"] msisdn:: "+msisdncount);
		return cnt;
	}

	public ApiResponsePojo responseBuilder(List<UserMasterDto> userDetails, ProductInfo apiInfo)
	{   
		    
			apiResponsePojo =new ApiResponsePojo();
			apiResponsePojo.setProduct(apiInfo.getProduct());
			apiResponsePojo.setVersion(apiInfo.getVersion());
			apiResponsePojo.setRelease(apiInfo.getRelease());
			apiResponsePojo.setMsisdn(userDetails.get(0).getMsisdn().toString());
			apiResponsePojo.setAction("getUser");
	
			apiResponsePojo.setImsi(userDetails.get(0).getImsi().toString());
		
		    Optional<Set<VoiceConfigDto>> voiceConfigDtoOptional = Optional.of(userDetails.get(0).getVoiceConfig());
		    Optional<Set<SmsConfigDto>> smsConfigDtoOptional = Optional.of(userDetails.get(0).getSmsConfig());
		    if(voiceConfigDtoOptional.isPresent())
		    {	
			   	
			    voiceConfigResponse = userDetails.stream().flatMap(user->user.getVoiceConfig().stream())
			    		              .map(vc ->new Voice(vc.getUsr_msisdn(),vc.getProv_flag(),vc.getProv_msisdn().toString()))
						              .collect(Collectors.toCollection(LinkedHashSet::new));
			
				apiResponsePojo.setVoice(voiceConfigResponse);
		    }	
		    
		   if(smsConfigDtoOptional.isPresent())
		   {	   
			   smsConfigResponse =userDetails.stream().flatMap(user->user.getSmsConfig().stream())
					              .map(sc->new Sms(sc.getUsr_msisdn(), sc.getProv_flag(), sc.getProv_msisdn().toString()))
					              .collect(Collectors.toCollection(LinkedHashSet::new));
			
			   apiResponsePojo.setSms(smsConfigResponse);
		   }	   
		
		
		return apiResponsePojo;	
	}
	
	public void fetchMaxWhitelistCapacity()
	{
		  GlobalMaster globalMaster =globalMasterServiceImpl.findById(1);
		  
	      max_sms_whitelist_msisdn_mo=globalMaster.getMax_sms_whitelist_msisdn();
	      
          max_sms_whitelist_msisdn_mt=globalMaster.getMax_sms_whitelist_msisdn();
          
	      max_voice_whitelist_msisdn_mo=globalMaster.getMax_voice_whitelist_msisdn();
	      
          max_voice_whitelist_msisdn_mt=globalMaster.getMax_voice_whitelist_msisdn();
		
	}

}
