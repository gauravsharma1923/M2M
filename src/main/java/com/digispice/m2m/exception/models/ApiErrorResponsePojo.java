package com.digispice.m2m.exception.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
	
public class ApiErrorResponsePojo {
	
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonIgnore
    private int status;
    private String message;
    private String msisdn;
    private String returnCode;
    private String error;
	private String remarks;
    private String description;
    private String username;
   
    
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

    public ApiErrorResponsePojo(final String msisdn, final String returnCode,final String remarks, final String description) {
        
    	this.msisdn = msisdn;
        this.returnCode = returnCode;
        this.remarks=remarks;
        this.description=description;
  
    }
    
    public ApiErrorResponsePojo(final int status, final String error) {
        this.status = status;
        this.error = error;
  
    }
    
    public ApiErrorResponsePojo(final String error) {
        this.error = error;
        
  
    }
    
    
    public ApiErrorResponsePojo(final String msisdn, final int status, final String returnCode, final String remarks, final String description) {
        this.msisdn=msisdn;
    	this.status = status;
        this.returnCode=returnCode;
        this.remarks=remarks;
        this.description=description;
     
    }

    public ApiErrorResponsePojo(final int status, final String returnCode, final String remarks, final String description) {
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
