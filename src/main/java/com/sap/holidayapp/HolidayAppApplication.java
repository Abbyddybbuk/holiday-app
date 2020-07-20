package com.sap.holidayapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.sap.icd.odatav2.spring.SpringODataLibraryPackageMarker;

@SpringBootApplication
@ComponentScan(basePackageClasses = {HolidayPackageMarker.class, SpringODataLibraryPackageMarker.class}, basePackages = {"com.sap.holidayapp", "com.sap.icd.odatav2"})
@EntityScan("com.sap.holidayapp.model")
@EnableJpaRepositories("com.sap.holidayapp.repository")
public class HolidayAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(HolidayAppApplication.class, args);
	}

}
