package com.digispice.m2m.model;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;
import com.digispice.m2m.serializers.ApiResponseSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using= ApiResponseSerializer.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponsePojo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonIgnore
    private int status;
    private String message;
    private String msisdn;
    private String returnCode;
    private String error;
    private Timestamp release;
    private String product;
    private String version;
    private String action;
    private String imsi;
    private Set<Sms> sms;
	private Set<Voice> voice;
	private String remarks;
    private String description;
   

    public ApiResponsePojo() {
    	
    }
   
    public Set<Sms> getSms() {
		return sms;
	}

	public void setSms(Set<Sms> sms) {
		this.sms = sms;
	}

	public Set<Voice> getVoice() {
		return voice;
	}

	public void setVoice(Set<Voice> voice) {
		this.voice = voice;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

   
    public ApiResponsePojo(final String msisdn, final String returnCode,final String remarks, final String description) {
        
    	this.msisdn = msisdn;
        this.returnCode = returnCode;
        this.remarks=remarks;
        this.description=description;
  
    }
    
    public ApiResponsePojo(final int status, final String error) {
        this.status = status;
        this.error = error;
  
    }
    
    public ApiResponsePojo(final String error) {
        this.error = error;
        
  
    }
    
    
    public ApiResponsePojo(final String msisdn, final int status, final String returnCode, final String remarks, final String description) {
        this.msisdn=msisdn;
    	this.status = status;
        this.returnCode=returnCode;
        this.remarks=remarks;
        this.description=description;
     
    }

  
    public int getStatus() {
        return status;
    }

    public void setStatus(final int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }


    @Override
    public final String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ApiError [status=").append(status).append(", message=").append(message).append("]");
        return builder.toString();
    }

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}
	public Timestamp getRelease() {
		return release;
	}
	public void setRelease(Timestamp release) {
		this.release = release;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}

	public static class Sms 
    {
    	public Long usr_msisdn;
    	public int prov_flag;
    	public String prov_msisdn;
		public Long getUsr_msisdn() {
			return usr_msisdn;
		}
		public void setUsr_msisdn(Long usr_msisdn) {
			this.usr_msisdn = usr_msisdn;
		}
		public int getProv_flag() {
			return prov_flag;
		}
		public void setProv_flag(int prov_flag) {
			this.prov_flag = prov_flag;
		}
		public String getProv_msisdn() {
			return prov_msisdn;
		}
		public void setProv_msisdn(String prov_msisdn) {
			this.prov_msisdn = prov_msisdn;
		}
		public Sms() {
			
		}
		public Sms(final Long usr_msisdn, int prov_flag,String prov_msisdn)
		{
			 this.usr_msisdn=usr_msisdn;
		     this.prov_flag = prov_flag;
		     this.prov_msisdn=prov_msisdn;
		    
		}
	
    	
    }
    
    public  static class Voice
    {
    	public Long usr_msisdn;
    	public int prov_flag;
    	public String prov_msisdn;
		public Long getUsr_msisdn() {
			return usr_msisdn;
		}
		public void setUsr_msisdn(Long usr_msisdn) {
			this.usr_msisdn = usr_msisdn;
		}
		public int getProv_flag() {
			return prov_flag;
		}
		public void setProv_flag(int prov_flag) {
			this.prov_flag = prov_flag;
		}
		public String getProv_msisdn() {
			return prov_msisdn;
		}
		public void setProv_msisdn(String prov_msisdn) {
			this.prov_msisdn = prov_msisdn;
		}
		public Voice(final Long usr_msisdn, int prov_flag,String prov_msisdn)
		{
			 this.usr_msisdn=usr_msisdn;
		     this.prov_flag = prov_flag;
		     this.prov_msisdn=prov_msisdn;
		    
		}
		public Voice() {
			
		}
    	
    }  	
    }

