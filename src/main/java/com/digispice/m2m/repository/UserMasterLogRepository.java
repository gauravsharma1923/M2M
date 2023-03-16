package com.digispice.m2m.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import com.digispice.m2m.entity.UserMasterLog;


public interface UserMasterLogRepository extends JpaRepository<UserMasterLog, Long>  {
    
	int countByMsisdn(@Param("msisdn") Long msisdn);
    
    
}


