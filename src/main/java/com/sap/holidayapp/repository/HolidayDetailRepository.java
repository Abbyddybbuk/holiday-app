package com.sap.holidayapp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.sap.holidayapp.model.HolidayDetail;

public interface HolidayDetailRepository extends JpaRepository<HolidayDetail, String> {
	@Query("SELECT i FROM HolidayDetail i WHERE i.year = :year")
	public List<HolidayDetail> findByYear(@Param("year") String year);

	@Query("SELECT i FROM HolidayDetail i WHERE i.month = :month")
	public List<HolidayDetail> findByMonth(@Param("month") String month);
}
