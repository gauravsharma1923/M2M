package com.digispice.m2m.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
	
	
	@Entity
    @Table(name="tbl_global_master")
	public class GlobalMaster   {
		
		@Id
		@Column(name="id")
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		
		@Column(name="max_voice_calls_whitelist_msisdn")
	    private int max_voice_whitelist_msisdn;
		
		@Column(name="max_sms_whitelist_msisdn")
		private int max_sms_whitelist_msisdn;
		
		@Column(name="max_data_whitelist_msisdn")
		private int max_data_whitelist_msisdn;
		
		@Column(name="max_alpha_whitelist_cli")
		private int max_alpha_whitelist_cli;
		
	    @Column(name="max_calls_per_day")
		private int max_calls_per_day;
	    
		@Column(name="max_calls_per_week")
		private int max_calls_per_week;
		
		@Column(name="max_calls_per_month")
		private int max_calls_per_month;
		
		@Column(name="max_calls_per_year")
		private int max_calls_per_year;
		
		@Column(name="max_sms_per_day")
		private int max_sms_per_day;
		
		@Column(name="max_sms_per_week")
		private int max_sms_per_week;
		
		@Column(name="max_sms_per_month")
		private int max_sms_per_month;
		
		@Column(name="max_sms_per_year")
		private int max_sms_per_year;
		
		@Column(name="max_data_per_day")
		private int max_data_per_day;
		
		@Column(name="max_data_per_week")
		private int max_data_per_week;
		
		@Column(name="max_data_per_month")
		private int max_data_per_month;
		
		@Column(name="max_data_per_year")
		private int max_data_per_year;
		
		
		@Column(name="recordCreateDateTime")
		private Timestamp recordCreateDateTime;
		
		@Column(name="recordUpdateDateTime")
		private Timestamp recordUpdateDateTime;
	    

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public int getMax_voice_whitelist_msisdn() {
			return max_voice_whitelist_msisdn;
		}

		public void setMax_voice_whitelist_msisdn(int max_voice_whitelist_msisdn) {
			this.max_voice_whitelist_msisdn = max_voice_whitelist_msisdn;
		}

		public int getMax_sms_whitelist_msisdn() {
			return max_sms_whitelist_msisdn;
		}

		public void setMax_sms_whitelist_msisdn(int max_sms_whitelist_msisdn) {
			this.max_sms_whitelist_msisdn = max_sms_whitelist_msisdn;
		}

		public int getMax_data_whitelist_msisdn() {
			return max_data_whitelist_msisdn;
		}

		public void setMax_data_whitelist_msisdn(int max_data_whitelist_msisdn) {
			this.max_data_whitelist_msisdn = max_data_whitelist_msisdn;
		}

		public int getMax_alpha_whitelist_cli() {
			return max_alpha_whitelist_cli;
		}

		public void setMax_alpha_whitelist_cli(int max_alpha_whitelist_cli) {
			this.max_alpha_whitelist_cli = max_alpha_whitelist_cli;
		}


		public int getMax_calls_per_day() {
			return max_calls_per_day;
		}


		public void setMax_calls_per_day(int max_calls_per_day) {
			this.max_calls_per_day = max_calls_per_day;
		}

		public int getMax_calls_per_week() {
			return max_calls_per_week;
		}

		public void setMax_calls_per_week(int max_calls_per_week) {
			this.max_calls_per_week = max_calls_per_week;
		}

		public int getMax_calls_per_month() {
			return max_calls_per_month;
		}

		public void setMax_calls_per_month(int max_calls_per_month) {
			this.max_calls_per_month = max_calls_per_month;
		}

		public int getMax_sms_per_day() {
			return max_sms_per_day;
		}

		public void setMax_sms_per_day(int max_sms_per_day) {
			this.max_sms_per_day = max_sms_per_day;
		}

		public int getMax_sms_per_week() {
			return max_sms_per_week;
		}

		public void setMax_sms_per_week(int max_sms_per_week) {
			this.max_sms_per_week = max_sms_per_week;
		}

		public int getMax_sms_per_month() {
			return max_sms_per_month;
		}
		
		public void setMax_sms_per_month(int max_sms_per_month) {
			this.max_sms_per_month = max_sms_per_month;
		}

		public int getMax_data_per_day() {
			return max_data_per_day;
		}

		public void setMax_data_per_day(int max_data_per_day) {
			this.max_data_per_day = max_data_per_day;
		}

		public int getMax_data_per_week() {
			return max_data_per_week;
		}

		public void setMax_data_per_week(int max_data_per_week) {
			this.max_data_per_week = max_data_per_week;
		}
		
		public int getMax_data_per_month() {
			return max_data_per_month;
		}

		public void setMax_data_per_month(int max_data_per_month) {
			this.max_data_per_month = max_data_per_month;
		}
		
	
		public int getMax_calls_per_year() {
			return max_calls_per_year;
		}

		public void setMax_calls_per_year(int max_calls_per_year) {
			this.max_calls_per_year = max_calls_per_year;
		}

		public int getMax_sms_per_year() {
			return max_sms_per_year;
		}

		public void setMax_sms_per_year(int max_sms_per_year) {
			this.max_sms_per_year = max_sms_per_year;
		}

		public int getMax_data_per_year() {
			return max_data_per_year;
		}

		public void setMax_data_per_year(int max_data_per_year) {
			this.max_data_per_year = max_data_per_year;
		}

		public Timestamp getRecordCreateDateTime() {
			return recordCreateDateTime;
		}

		public void setRecordCreateDateTime(Timestamp recordCreateDateTime) {
			this.recordCreateDateTime = recordCreateDateTime;
		}
		
		public Timestamp getRecordUpdateDateTime() {
			return recordUpdateDateTime;
		}

		public void setRecordUpdateDateTime(Timestamp recordUpdateDateTime) {
			this.recordUpdateDateTime = recordUpdateDateTime;
		}

		public GlobalMaster() {
	        super();
	    }

		/*@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			result = prime * result + max_alpha_whitelist_cli;
			result = prime * result + max_calls_per_day;
			result = prime * result + max_calls_per_month;
			result = prime * result + max_calls_per_week;
			result = prime * result + max_calls_per_year;
			result = prime * result + max_data_per_day;
			result = prime * result + max_data_per_month;
			result = prime * result + max_data_per_week;
			result = prime * result + max_data_per_year;
			result = prime * result + max_data_whitelist_msisdn;
			result = prime * result + max_sms_per_day;
			result = prime * result + max_sms_per_month;
			result = prime * result + max_sms_per_week;
			result = prime * result + max_sms_per_year;
			result = prime * result + max_sms_whitelist_msisdn;
			result = prime * result + max_voice_whitelist_msisdn;
			result = prime * result + ((recordCreateDateTime == null) ? 0 : recordCreateDateTime.hashCode());
			result = prime * result + ((recordUpdateDateTime == null) ? 0 : recordUpdateDateTime.hashCode());
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
			GlobalMaster other = (GlobalMaster) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			if (max_alpha_whitelist_cli != other.max_alpha_whitelist_cli)
				return false;
			if (max_calls_per_day != other.max_calls_per_day)
				return false;
			if (max_calls_per_month != other.max_calls_per_month)
				return false;
			if (max_calls_per_week != other.max_calls_per_week)
				return false;
			if (max_calls_per_year != other.max_calls_per_year)
				return false;
			if (max_data_per_day != other.max_data_per_day)
				return false;
			if (max_data_per_month != other.max_data_per_month)
				return false;
			if (max_data_per_week != other.max_data_per_week)
				return false;
			if (max_data_per_year != other.max_data_per_year)
				return false;
			if (max_data_whitelist_msisdn != other.max_data_whitelist_msisdn)
				return false;
			if (max_sms_per_day != other.max_sms_per_day)
				return false;
			if (max_sms_per_month != other.max_sms_per_month)
				return false;
			if (max_sms_per_week != other.max_sms_per_week)
				return false;
			if (max_sms_per_year != other.max_sms_per_year)
				return false;
			if (max_sms_whitelist_msisdn != other.max_sms_whitelist_msisdn)
				return false;
			if (max_voice_whitelist_msisdn != other.max_voice_whitelist_msisdn)
				return false;
			if (recordCreateDateTime == null) {
				if (other.recordCreateDateTime != null)
					return false;
			} else if (!recordCreateDateTime.equals(other.recordCreateDateTime))
				return false;
			if (recordUpdateDateTime == null) {
				if (other.recordUpdateDateTime != null)
					return false;
			} else if (!recordUpdateDateTime.equals(other.recordUpdateDateTime))
				return false;
			return true;
		}
*/
	   
	}



