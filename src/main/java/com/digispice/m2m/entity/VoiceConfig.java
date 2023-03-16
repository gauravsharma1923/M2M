package com.digispice.m2m.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name="tbl_voice_config")
@IdClass(VoiceConfigId.class)
public class VoiceConfig implements Serializable  {


	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	@Id
	@Column(name="usr_msisdn")
	private Long usr_msisdn;
	
	@Id
	@Column(name="prov_msisdn")
	private Long prov_msisdn;
	
	@Id
	@Column(name="prov_flag")
	private int prov_flag;
	
	@Column(name="prov_startTime", nullable=false,insertable=false, updatable=false)
    private byte prov_startTime;
	
    @Column(name="prov_endTime", nullable=false,insertable=false, updatable=false)
    private byte prov_endTime;
    
    @Column(name="prov_startDate", nullable=false,insertable=false, updatable=false)
	private byte prov_startDate;
    
	@Column(name="prov_endDate", nullable=false,insertable=false, updatable=false)
	private byte prov_endDate;
	
	@Column(name="prov_startMonth", nullable=false,insertable=false, updatable=false)
	private byte prov_startMonth;
	
	@Column(name="prov_endMonth", nullable=false,insertable=false, updatable=false)
	private byte prov_endMonth;
	
	@CreationTimestamp
	@Column(name="rec_createDateTime", nullable=false, updatable=false)
	private Timestamp rec_createDateTime;
	
	@UpdateTimestamp
	@Column(name="rec_updateDateTime", nullable=false, updatable=false)
	private Timestamp rec_updateDateTime;
	
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usr_msisdn",foreignKey=@ForeignKey(name="fk_voice_transaction_relationship"),insertable = false, updatable = false)
    private UserMaster userMaster;
    

	public Long getUsr_msisdn() {
		return usr_msisdn;
	}

	public void setUsr_msisdn(Long usr_msisdn) {
		this.usr_msisdn = usr_msisdn;
	}

	public Long getProv_msisdn() {
		return prov_msisdn;
	}

	public void setProv_msisdn(Long prov_msisdn) {
		this.prov_msisdn = prov_msisdn;
	}

	public int getProv_flag() {
		return prov_flag;
	}

	public void setProv_flag(int prov_flag) {
		this.prov_flag = prov_flag;
	}
	
  
	public UserMaster getUserMaster() {
		return userMaster;
	}

	public void setUserMaster(UserMaster userMaster) {
		this.userMaster = userMaster;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Timestamp getRec_createDateTime() {
		return rec_createDateTime;
	}

	public void setRec_createDateTime(Timestamp rec_createDateTime) {
		this.rec_createDateTime = rec_createDateTime;
	}

	public Timestamp getRec_updateDateTime() {
		return rec_updateDateTime;
	}

	public void setRec_updateDateTime(Timestamp rec_updateDateTime) {
		this.rec_updateDateTime = rec_updateDateTime;
	}
    
	
	public byte getProv_startTime() {
		return prov_startTime;
	}

	public void setProv_startTime(byte prov_startTime) {
		this.prov_startTime = prov_startTime;
	}

	public byte getProv_endTime() {
		return prov_endTime;
	}

	public void setProv_endTime(byte prov_endTime) {
		this.prov_endTime = prov_endTime;
	}

	public byte getProv_startDate() {
		return prov_startDate;
	}

	public void setProv_startDate(byte prov_startDate) {
		this.prov_startDate = prov_startDate;
	}

	public byte getProv_endDate() {
		return prov_endDate;
	}

	public void setProv_endDate(byte prov_endDate) {
		this.prov_endDate = prov_endDate;
	}

	public byte getProv_startMonth() {
		return prov_startMonth;
	}

	public void setProv_startMonth(byte prov_startMonth) {
		this.prov_startMonth = prov_startMonth;
	}

	public byte getProv_endMonth() {
		return prov_endMonth;
	}

	public void setProv_endMonth(byte prov_endMonth) {
		this.prov_endMonth = prov_endMonth;
	}

	public VoiceConfig() {
        super();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + prov_endDate;
		result = prime * result + prov_endMonth;
		result = prime * result + prov_endTime;
		result = prime * result + prov_flag;
		result = prime * result + ((prov_msisdn == null) ? 0 : prov_msisdn.hashCode());
		result = prime * result + prov_startDate;
		result = prime * result + prov_startMonth;
		result = prime * result + prov_startTime;
		result = prime * result + ((rec_createDateTime == null) ? 0 : rec_createDateTime.hashCode());
		result = prime * result + ((rec_updateDateTime == null) ? 0 : rec_updateDateTime.hashCode());
		result = prime * result + ((usr_msisdn == null) ? 0 : usr_msisdn.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VoiceConfig other = (VoiceConfig) obj;
		if (prov_endDate != other.prov_endDate)
			return false;
		if (prov_endMonth != other.prov_endMonth)
			return false;
		if (prov_endTime != other.prov_endTime)
			return false;
		if (prov_flag != other.prov_flag)
			return false;
		if (prov_msisdn == null) {
			if (other.prov_msisdn != null)
				return false;
		} else if (!prov_msisdn.equals(other.prov_msisdn))
			return false;
		if (prov_startDate != other.prov_startDate)
			return false;
		if (prov_startMonth != other.prov_startMonth)
			return false;
		if (prov_startTime != other.prov_startTime)
			return false;
		if (rec_createDateTime == null) {
			if (other.rec_createDateTime != null)
				return false;
		} else if (!rec_createDateTime.equals(other.rec_createDateTime))
			return false;
		if (rec_updateDateTime == null) {
			if (other.rec_updateDateTime != null)
				return false;
		} else if (!rec_updateDateTime.equals(other.rec_updateDateTime))
			return false;
		if (usr_msisdn == null) {
			if (other.usr_msisdn != null)
				return false;
		} else if (!usr_msisdn.equals(other.usr_msisdn))
			return false;
		return true;
	}

  
}
