package com.sap.holidayapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sap.holidayapp.model.HolidayHeader;

public interface HolidayHeaderRepository extends JpaRepository<HolidayHeader, String> {

}
