package com.digispice.m2m.service.impl;
import java.util.Optional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.digispice.m2m.exception.models.ResourceNotFoundException;
import com.digispice.m2m.entity.GlobalMaster;
import com.digispice.m2m.repository.GlobalMasterRepository;
import com.digispice.m2m.service.GlobalMasterService;

@Service
@Transactional(readOnly=true)
public class GlobalMasterServiceImpl implements GlobalMasterService{
	
	protected final Log logger = LogFactory.getLog(getClass());
	private GlobalMaster globalMaster;
    @Autowired
	GlobalMasterRepository  globalMasterRepository;
   
	@Override
	public boolean  existsById(long id) {
		
		boolean isIdExists=globalMasterRepository.existsById(id);
		logger.info("isIdExists["+isIdExists+"]");
		return isIdExists;
	}

	@Override
	public GlobalMaster findById(long id) {
	
		Optional<GlobalMaster> globalMasterOptional =globalMasterRepository.findById(id);
		if(globalMasterOptional.isPresent())
		{
			globalMaster=globalMasterOptional.get();
		}
		else
		{
			throw new ResourceNotFoundException();
		}
		return globalMaster;
	}

	
}
