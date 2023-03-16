package com.digispice.m2m.service.impl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.digispice.m2m.entity.CCIUserDetails;
import com.digispice.m2m.repository.CCIUserDetailsRepository;
import com.digispice.m2m.service.CCIUserService;

@Service
@Transactional(readOnly=true)
public class CCIUserServiceImpl implements CCIUserService{
	
	protected final Log logger = LogFactory.getLog(getClass());
	@Autowired
	CCIUserDetailsRepository userInfo;
	
	@Override
	public CCIUserDetails findByName(String name) {
		
		return userInfo.findByName(name);
	}
	
	
}
