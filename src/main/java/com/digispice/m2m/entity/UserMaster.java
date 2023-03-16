package com.digispice.m2m.entity;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name="tbl_user_master")

public class UserMaster implements Serializable  {

    private static final long serialVersionUID = 1L;
    @Column(name="usr_id") 
	private int usr_id;
    @Column(name="usr_parent_id")
    private Long category;
	@Column(name="name")
	private String username;
	@NotNull(message="Msisdn cannot be blank")
	@Id
	@Column(name="msisdn")
    private Long msisdn;
	
	@Column(name="status")
	private int status;
	
	@CreationTimestamp
	@Column(name="activationDateTime")
	private Timestamp activationDateTime;
	
	@UpdateTimestamp
	@Column(name="updateDateTime")
	private Timestamp updateDateTime;
	
	@Column(name="imsi")
	private Long imsi;
	
	 @OneToMany(mappedBy="userMaster",fetch=FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval=true)
	   // @Fetch(FetchMode.SELECT)
	    //@JoinColumn(name="u_id")
	 @Fetch(FetchMode.JOIN)
	 private Set<SmsConfig> smsConfig;
    
	@OneToMany(mappedBy="userMaster",fetch=FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval=true)
	//@Fetch(FetchMode.SELECT)
    @Fetch(FetchMode.JOIN)
    private Set<VoiceConfig> voiceConfig;
    
   
    
    public Set<VoiceConfig> getVoiceConfig() {
		return voiceConfig;
	}

	public void setVoiceConfig(Set<VoiceConfig> voiceConfig) {
		this.voiceConfig = voiceConfig;
	}
	

	public Long getCategory() {
		return category;
	}

	public void setCategory(Long category) {
		this.category = category;
	}

	public int getUsr_id() {
		return usr_id;
	}

	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getStatus() {
		return status;
	}

	public Timestamp getActivationDateTime() {
		return activationDateTime;
	}

	public Timestamp getUpdateDateTime() {
		return updateDateTime;
	}
	
	public Long getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(Long msisdn) {
		this.msisdn = msisdn;
	}

	public Set<SmsConfig> getSmsConfig() {
		return smsConfig;
	}

	public void setSmsConfig(Set<SmsConfig> smsConfig) {
		this.smsConfig = smsConfig;
	}
    
	public void setUsr_id(int usr_id) {
		this.usr_id = usr_id;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setActivationDateTime(Timestamp activationDateTime) {
		this.activationDateTime = activationDateTime;
	}

	public void setUpdateDateTime(Timestamp updateDateTime) {
		this.updateDateTime = updateDateTime;
	}

	public Long getImsi() {
		return imsi;
	}

	public void setImsi(Long imsi) {
		this.imsi = imsi;
	}

	public UserMaster() {
        super();

     
    }

	
}
