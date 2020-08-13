package com.sap.holidayapp.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.UuidGenerator;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "T_HOLIDAY_HEADER")
@NamedQuery(name = "HolidayHeader.findAll", query = "SELECT s FROM HolidayHeader s")
public class HolidayHeader implements Serializable {

	/**
	 * 
	 */

	private static final long serialVersionUID = 4628308265794999918L;

	@Id
	@UuidGenerator(name = "hid")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "hid")
	@Column(name = "HID", length = 36)
	private String hid;

	@Column(name = "COMPANY_ID")
	private String companyId;

	@Column(name = "LOCATION")
	private String location;

	@Column(name = "COUNTRY")
	private String country;

	@CreatedBy
	@Column(name = "CREATED_BY")
	private String createdBy;

	@CreatedDate
	@Column(name = "CREATED_ON")
	private Timestamp createdOn;

	@LastModifiedBy
	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@LastModifiedDate
	@Column(name = "UPDATED_ON")
	private Timestamp updatedOn;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "holidayHeader", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<HolidayDetail> holidayDetails = new ArrayList<>();

	public String getHid() {
		return hid;
	}

	public void setHid(String hid) {
		this.hid = hid;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedOn() {
		return (Timestamp) this.createdOn.clone();
	}

	public void setCreatedOn(Timestamp createdOn) {
		if (createdOn != null)
			this.createdOn = (Timestamp) createdOn.clone();
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedOn() {
		return (Timestamp) this.updatedOn.clone();
	}

	public void setUpdatedOn(Timestamp updatedOn) {
		if (updatedOn != null)
			this.updatedOn = (Timestamp) updatedOn.clone();
	}

	public List<HolidayDetail> getHolidayDetails() {
		return holidayDetails != null ? Collections.unmodifiableList(holidayDetails) : null;
	}

	public void setHolidayDetails(List<HolidayDetail> holidayDetails) {
		this.holidayDetails = holidayDetails != null ? Collections.unmodifiableList(holidayDetails) : null;
	}
}
