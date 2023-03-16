package com.digispice.m2m.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.digispice.m2m.entity.CCIUserDetails;

public interface CCIUserDetailsRepository extends JpaRepository<CCIUserDetails, Long> {
	
	public CCIUserDetails findByName(final String name);

}


