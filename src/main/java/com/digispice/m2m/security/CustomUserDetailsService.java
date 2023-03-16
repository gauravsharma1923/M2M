package com.digispice.m2m.security;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import com.digispice.m2m.entity.CCIUserDetails;
import com.digispice.m2m.repository.CCIUserDetailsRepository;
import com.digispice.m2m.utilities.RestPreconditions;

@Component
public final class CustomUserDetailsService implements UserDetailsService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
    private CCIUserDetailsRepository cciUserDetailsRepository;
	
    public CustomUserDetailsService() {
        super();
    }

    //@Override
    public final UserDetails loadUserByUsername(final String username) {
    	
    	RestPreconditions.checkRequestElementNotNull(username);
        final CCIUserDetails userInfo = cciUserDetailsRepository.findByName(username);
        if (userInfo == null) {
           throw new UsernameNotFoundException("Username was not found: " + username);
           
        }
        
    	final String rolesOfUser = userInfo.getRole();
       
        final List<GrantedAuthority> auths = AuthorityUtils.createAuthorityList(rolesOfUser);
      
        return new User(userInfo.getName(), userInfo.getPassword(), auths);
    }
 
}
