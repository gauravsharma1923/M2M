package com.digispice.m2m.dto;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;
import com.digispice.m2m.entity.ProductInfo;
import com.digispice.m2m.deserializers.CustomDeSerializerBeforeUpdate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using=CustomDeSerializerBeforeUpdate.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value= {"category", "usr_id", "username"}, allowSetters=true)
public   class UserMasterUpdateDto implements Serializable  {

	private static final long serialVersionUID = 1L;
	@JsonProperty("msisdn")
    private Long msisdn;
    private String product;
    private String version;
    private Timestamp release;
    private Long category;
    private int usr_id;
    private String username;
    
    @JsonProperty("call")
    private Set<VoiceConfigDto> voiceConfig;
    
    @JsonProperty("sms")
    private Set<SmsConfigDto> smsConfig;
    private ProductInfo apiInfo;
 
    public ProductInfo getApiInfo() {
		return apiInfo;
	}

	public void setApiInfo(ProductInfo apiInfo) {
		this.apiInfo = apiInfo;
	}

	public Long getMsisdn() {
		return msisdn;
	}
    
	public void setMsisdn(Long msisdn) {
		this.msisdn = msisdn;
	}
	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Timestamp getRelease() {
		return release;
	}

	public void setRelease(Timestamp release) {
		this.release = release;
	}

	public UserMasterUpdateDto() {
        super();

    }

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Set<SmsConfigDto> getSmsConfig() {
		return smsConfig;
	}

	public void setSmsConfig(Set<SmsConfigDto> smsConfig) {
		this.smsConfig = smsConfig;
	}

	public Set<VoiceConfigDto> getVoiceConfig() {
		return voiceConfig;
	}

	public void setVoiceConfig(Set<VoiceConfigDto> voiceConfig) {
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
	public void setUsr_id(int usr_id) {
		this.usr_id = usr_id;
	}  
	
    
}
