/*package com.digispice.m2m.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.digispice.m2m.entity.EnterpriseMaster;

public interface EnterpriseMasterRepository extends JpaRepository<EnterpriseMaster, Long> {
      
	@Query("select e from EnterpriseMaster e where e.category = :category")
	List<EnterpriseMaster> findByCategory(@Param("category") Long category);
	public boolean existsById(@Param("category") Long category);
}


*/