package com.digispice.m2m.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.digispice.m2m.entity.UserMaster;


public interface UserMasterRepository extends JpaRepository<UserMaster, Long>  {
    
	@Query("SELECT DISTINCT u FROM UserMaster u LEFT JOIN FETCH  u.smsConfig s  LEFT JOIN FETCH u.voiceConfig v where u.msisdn = :msisdn")
	List<UserMaster> findMsisdn(@Param("msisdn") Long msisdn);
	
    @Transactional
    int deleteByMsisdn(@Param("msisdn") Long msisdn);
    
    int countByMsisdn(@Param("msisdn") Long msisdn);
   
    @Query("SELECT u FROM UserMaster u where u.msisdn = :msisdn")
    UserMaster findByMsisdn(@Param("msisdn") Long msisdn);
    
    public boolean existsById(@Param("msisdn") Long msisdn);
    
    
}
