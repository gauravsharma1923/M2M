package com.digispice.m2m.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@JsonIgnoreProperties(value= {"usr_msisdn", "prov_msisdn", "prov_flag"})
@Embeddable
public class VoiceConfigId implements Serializable  {

	@Column(name="usr_msisdn")
	private Long usr_msisdn;
	@Column(name="prov_msisdn")
	private Long prov_msisdn;
	@Column(name="prov_flag")
	private int prov_flag;
    private static final long serialVersionUID = 1L;
	
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
	public VoiceConfigId()
	{
		
	}
	public VoiceConfigId( long usr_msisdn, long prov_msisdn,int  prov_flag)
	{
		this.usr_msisdn=usr_msisdn;
		this.prov_msisdn=prov_msisdn;
		this.prov_flag=prov_flag;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + prov_flag;
		result = prime * result + ((prov_msisdn == null) ? 0 : prov_msisdn.hashCode());
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
		VoiceConfigId other = (VoiceConfigId) obj;
		if (prov_flag != other.prov_flag)
			return false;
		if (prov_msisdn == null) {
			if (other.prov_msisdn != null)
				return false;
		} else if (!prov_msisdn.equals(other.prov_msisdn))
			return false;
		if (usr_msisdn == null) {
			if (other.usr_msisdn != null)
				return false;
		} else if (!usr_msisdn.equals(other.usr_msisdn))
			return false;
		return true;
	}
	  
}
