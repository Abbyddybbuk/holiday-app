package com.sap.holidayapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sap.holidayapp.model.HolidayDetail;

public interface HolidayDetailRepository extends JpaRepository<HolidayDetail, String> {

}
