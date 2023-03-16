package com.digispice.m2m.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.digispice.m2m.entity.ProductInfo;

public interface ProductInfoRepository extends JpaRepository<ProductInfo, Long> {
      
	@Query("select p from ProductInfo p")
	List<ProductInfo> fetchProductDetails();
	public boolean existsById(long ent_id);
}


