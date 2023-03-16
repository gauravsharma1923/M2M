package com.digispice.m2m.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import com.digispice.m2m.entity.GlobalMaster;

public interface GlobalMasterRepository extends JpaRepository<GlobalMaster, Long> {
      

	public boolean existsById(@Param("id") Long id);
}


