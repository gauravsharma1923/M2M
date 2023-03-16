package com.digispice.m2m.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.digispice.m2m.entity.VoiceConfigLog;

public interface VoiceConfigLogRepository extends JpaRepository<VoiceConfigLog, Long>  {
    
	@Modifying
    @Query("UPDATE VoiceConfig v SET v.prov_msisdn = :prov_msisdn_new, v.rec_updateDateTime=now() where v.usr_msisdn=:usr_msisdn and v.prov_flag=:prov_flag and v.prov_msisdn=:prov_msisdn")
    public int update(@Param("usr_msisdn") long msisdn, @Param("prov_flag") int prov_flag, @Param("prov_msisdn") long prov_msisdn, @Param("prov_msisdn_new") long prov_msisdn_new);

    
    
}
