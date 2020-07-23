package com.sap.holidayapp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.sap.holidayapp.model.HolidayHeader;

public interface HolidayHeaderRepository extends JpaRepository<HolidayHeader, String> {
  @Query("SELECT i FROM HolidayHeader i WHERE i.companyId = :companyId")
  public List<HolidayHeader> findByStatus(@Param("companyId") String companyId);
}
