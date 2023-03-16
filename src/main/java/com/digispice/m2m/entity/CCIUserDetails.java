package com.digispice.m2m.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name="tbl_cci_user_details")
public class CCIUserDetails {

		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "u_id")
	    private Long id;
	
		@Column(name="u_name")
	    private String name;
		
	    @Column(name="u_password", nullable = false)
	    private String password;
	    
	    @Column(name="u_role")
	    private String role;
	    
	    @Column(name="u_authKey")
	    private String u_authKey;
	    
	    public String getRole() {
			return role;
		}
	
		public void setRole(String role) {
			this.role = role;
		}
	
		public String getU_authKey() {
			return u_authKey;
		}
	
		public void setU_authKey(String u_authKey) {
			this.u_authKey = u_authKey;
		}
	
		
	    public CCIUserDetails() {
	        super();
	
	     
	    }
	
	    public CCIUserDetails(final String nameToSet, final String passwordToSet, final String  roleToSet) {
	        super();
	
	        name = nameToSet;
	        password = passwordToSet;
	        role = roleToSet;
	    }
	
	       public Long getId() {
			return id;
		}
	
		public void setId(Long id) {
			this.id = id;
		}
	
	   
		public void setName(String name) {
			this.name = name;
		}
	
		public String getPassword() {
	        return password;
	    }
	
	    public void setPassword(final String passwordToSet) {
	        password = passwordToSet;
	    }
	
	   
	    @Override
	    public int hashCode() {
	        final int prime = 31;
	        int result = 1;
	        result = prime * result + ((name == null) ? 0 : name.hashCode());
	        return result;
	    }
	
	    @Override
	    public boolean equals(final Object obj) {
	        if (this == obj)
	            return true;
	        if (obj == null)
	            return false;
	        if (getClass() != obj.getClass())
	            return false;
	        final CCIUserDetails other = (CCIUserDetails) obj;
	        if (name == null) {
	            if (other.name != null)
	                return false;
	        } else if (!name.equals(other.name))
	            return false;
	        return true;
	    }
	
	  
	
		public String getName() {
			
			return name;
		}
	
	}
