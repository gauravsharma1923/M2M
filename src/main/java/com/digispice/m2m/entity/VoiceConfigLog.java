package com.digispice.m2m.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import java.io.Serializable;
import java.sql.Timestamp;

@DynamicInsert
@Entity
@Table(name="tbl_voice_config_log")

public class VoiceConfigLog implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="usr_msisdn")
	private Long usr_msisdn;
	
	@Column(name="prov_msisdn")
	private Long prov_msisdn;
	
	@Column(name="prov_flag")
	private int prov_flag;

	@Column(name="prov_startTime")
    private byte prov_startTime;
	
    @Column(name="prov_endTime")
    private byte prov_endTime;
    
    @Column(name="prov_startDate")
  	private byte prov_startDate;
      
  	@Column(name="prov_endDate")
  	private byte prov_endDate;
  	
	@Column(name="prov_startMonth")
	private byte prov_startMonth;
	
	@Column(name="prov_endMonth")
	private byte prov_endMonth;
    
    @Column(name="rec_createDateTime")
	private Timestamp rec_createDateTime;
	
	@Column(name="rec_updateDateTime")
	private Timestamp rec_updateDateTime;
	
    @Column(name="action")
    private String action;
    
    @CreationTimestamp
    @Column(name="action_DateTime")
	private Timestamp action_DateTime;
    
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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Timestamp getAction_DateTime() {
		return action_DateTime;
	}

	public void setAction_DateTime(Timestamp action_DateTime) {
		this.action_DateTime = action_DateTime;
	}

	public Long getUsr_msisdn() {
		return usr_msisdn;
	}
    
	public void setUsr_msisdn(Long usr_msisdn) {
		this.usr_msisdn = usr_msisdn;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public VoiceConfigLog() {
        super();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((action_DateTime == null) ? 0 : action_DateTime.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		VoiceConfigLog other = (VoiceConfigLog) obj;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		if (action_DateTime == null) {
			if (other.action_DateTime != null)
				return false;
		} else if (!action_DateTime.equals(other.action_DateTime))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
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
