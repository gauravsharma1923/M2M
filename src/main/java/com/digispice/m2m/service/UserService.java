package com.digispice.m2m.service;
import com.digispice.m2m.dto.UserMasterDto;
import com.digispice.m2m.dto.UserMasterWrapperDto;
import com.digispice.m2m.model.ApiResponse;
import com.digispice.m2m.model.ApiResponsePojo;

	public interface UserService {
	
	    public boolean existsById(long msisdn);
	    public ApiResponse deleteUserByMsisdn(final UserMasterWrapperDto resource);
	    public ApiResponsePojo createUser(final UserMasterDto resource);
	    public ApiResponsePojo findMsisdn(final UserMasterWrapperDto resource);
	    public int countByMsisdn(long msisdn);
		public ApiResponsePojo addUserWrapper(final UserMasterDto resource);
	
	   	    
	}


