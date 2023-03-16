package com.digispice.m2m.dto;
import java.io.Serializable;


public class SmsConfigDto implements Serializable  {

	private static final long serialVersionUID = 1L;

	private Long usr_msisdn;
	private Long prov_msisdn;
	private String prov_msisdn_sms;
	private String prov_msisdn_sms_new;
	private int prov_flag;

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

	public SmsConfigDto() {
		super();
	}

	public String getProv_msisdn_sms() {
		return prov_msisdn_sms;
	}

	public void setProv_msisdn_sms(String prov_msisdn_sms) {
		this.prov_msisdn_sms = prov_msisdn_sms;
	}

	public String getProv_msisdn_sms_new() {
		return prov_msisdn_sms_new;
	}

	public void setProv_msisdn_sms_new(String prov_msisdn_sms_new) {
		this.prov_msisdn_sms_new = prov_msisdn_sms_new;
	}



}
