package com.sap.holidayapp.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sap.holidayapp.model.HolidayDetail;
import com.sap.holidayapp.model.HolidayHeader;
import com.sap.holidayapp.repository.HolidayDetailRepository;
import com.sap.holidayapp.repository.HolidayHeaderRepository;

@Service
public class HolidayService {

  private HolidayHeaderRepository headerRepository;
//  private HolidayDetailRepository detailRepository;
  
  @Autowired
  public HolidayService(HolidayHeaderRepository headerRepository) {
    super();
    this.headerRepository = headerRepository;
//    this.detailRepository = detailRepository;
  }

  public HolidayHeader createHolidayData(HolidayHeader holidayHeader,
      ODataJPAContext oDataJPAContext) {
    Date date = new Date();
    holidayHeader.setCreatedBy("ABHIJEET");
    holidayHeader.setCreatedOn(new Timestamp(date.getTime()));
    holidayHeader.setUpdatedBy("ABHIJEET");
    holidayHeader.setUpdatedOn(new Timestamp(date.getTime()));
    
    for (HolidayDetail detail: holidayHeader.getHolidayDetails()) {
      detail.setCreatedBy("ABHIJEET");
      detail.setCreatedOn(new Timestamp(date.getTime()));
      detail.setUpdatedBy("ABHIJEET");
      detail.setUpdatedOn(new Timestamp(date.getTime()));     
      detail.setHolidayHeader(holidayHeader);
    }
    return this.headerRepository.save(holidayHeader);
  }
}
