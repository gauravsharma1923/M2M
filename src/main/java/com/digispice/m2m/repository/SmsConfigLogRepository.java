package com.digispice.m2m.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.digispice.m2m.entity.SmsConfigLog;


public interface SmsConfigLogRepository extends JpaRepository<SmsConfigLog, Long>  {
	
	@Modifying
    @Query("UPDATE SmsConfig e SET e.prov_msisdn = :prov_msisdn_new, e.rec_updateDateTime=now() where e.usr_msisdn=:usr_msisdn and e.prov_flag=:prov_flag and e.prov_msisdn=:prov_msisdn")
    public int update(@Param("usr_msisdn") long msisdn, @Param("prov_flag") int prov_flag, @Param("prov_msisdn") long prov_msisdn, @Param("prov_msisdn_new") long prov_msisdn_new);
    
    
}


