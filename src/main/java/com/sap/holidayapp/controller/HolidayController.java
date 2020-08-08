package com.sap.holidayapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sap.holidayapp.service.HolidayService;

@RestController
public class HolidayController {

	@Autowired
	private HolidayService holidayService;

	@RequestMapping(value = "/scp/updateholidaymonth", method = RequestMethod.PUT)
	public String updateRrmExecHystrix(@RequestParam String month) {
		return holidayService.triggerApprovalviaRabbit(month);
	}

}
