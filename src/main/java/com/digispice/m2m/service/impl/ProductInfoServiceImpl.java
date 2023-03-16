package com.digispice.m2m.service.impl;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.digispice.m2m.entity.ProductInfo;
import com.digispice.m2m.repository.ProductInfoRepository;
import com.digispice.m2m.service.ProductInfoService;

@Service
@Transactional(readOnly=true)
public class ProductInfoServiceImpl implements ProductInfoService{
	
	protected final Log logger = LogFactory.getLog(getClass());
	@Autowired
	ProductInfoRepository productInfoRepository;
	private ProductInfo apiInfo;

	@Override
	public ProductInfo fetchProductDetails() {
		
		Optional<ProductInfo> apiInfoOptional=productInfoRepository.findById(Long.valueOf("1"));
		
		if(apiInfoOptional.isPresent())
		{   
			apiInfo=apiInfoOptional.get();
	    }
		
		return apiInfo;
		
		
	}

}
