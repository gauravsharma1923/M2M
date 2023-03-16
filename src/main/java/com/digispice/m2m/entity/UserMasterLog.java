package com.digispice.m2m.entity;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;


@Entity
@Table(name="tbl_user_master_log")
public class UserMasterLog implements Serializable  {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="usr_id")
    private int usr_id;
    @Column(name="usr_parent_id")
    private Long usr_parent_id;
    @Column(name="name")
    private String name;
    @Column(name="msisdn")
    private Long msisdn;
	@Column(name="status")
	private int status;
	@Column(name="activationDateTime")
    private Timestamp activationDateTime;
    @Column(name="updateDateTime")
    private Timestamp updateDateTime;
    @Column(name="imsi")
	private Long imsi;
    @Column(name="action")
    private String action;
    @CreationTimestamp
    @Column(name="action_DateTime")
	private Timestamp action_DateTime;
    
    public Long getImsi() {
		return imsi;
	}

	public void setImsi(Long imsi) {
		this.imsi = imsi;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getUsr_id() {
		return usr_id;
	}

	public void setUsr_id(int usr_id) {
		this.usr_id = usr_id;
	}

	public Long getUsr_parent_id() {
		return usr_parent_id;
	}

	public void setUsr_parent_id(Long usr_parent_id) {
		this.usr_parent_id = usr_parent_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Timestamp getActivationDateTime() {
		return activationDateTime;
	}

	public void setActivationDateTime(Timestamp activationDateTime) {
		this.activationDateTime = activationDateTime;
	}

	public Timestamp getUpdateDateTime() {
		return updateDateTime;
	}

	public void setUpdateDateTime(Timestamp updateDateTime) {
		this.updateDateTime = updateDateTime;
	}
	
 
	public Long getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(Long msisdn) {
		this.msisdn = msisdn;
	}


	public UserMasterLog() {
        super();

     
    }

	
}
