package com.sap.holidayapp.model;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.UuidGenerator;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "T_HOLIDAY_DETAIL")
public class HolidayDetail implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8984905366206357217L;

  @Id 
  @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy = "uuid")
  @Column(name = "D_ID", length = 36)
  private String dId;

  @Column(name = "PH_ID")
  private String phId;

  @Column(name = "PH_DESCRIPTION")
  private String phDescription;

  @Column(name = "YEAR")
  private String year;

  @Column(name = "MONTH")
  private String month;

  @Column(name = "DATE")
  private String date;

  @Column(name = "DAY_WEEK")
  private String dayWeek;

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

  @ManyToOne
  @JoinColumn(name = "HID")
  private HolidayHeader holidayHeader;


  public String getPhDescription() {
    return phDescription;
  }

  public void setPhDescription(String phDescription) {
    this.phDescription = phDescription;
  }


  public String getDId() {
    return dId;
  }

  public void setDId(String dId) {
    this.dId = dId;
  }

  public String getPhId() {
    return phId;
  }

  public void setPhId(String phId) {
    this.phId = phId;
  }

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public String getMonth() {
    return month;
  }

  public void setMonth(String month) {
    this.month = month;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getDayWeek() {
    return dayWeek;
  }

  public void setDayWeek(String dayWeek) {
    this.dayWeek = dayWeek;
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

  public HolidayHeader getHolidayHeader() {
    return holidayHeader;
  }

  public void setHolidayHeader(HolidayHeader holidayHeader) {
    this.holidayHeader = holidayHeader;
  }
}
