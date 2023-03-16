package com.digispice.m2m.service;
import com.digispice.m2m.entity.GlobalMaster;

	public interface GlobalMasterService {		
    public boolean existsById(long id);
	    
	    public GlobalMaster findById(long id);

	}
