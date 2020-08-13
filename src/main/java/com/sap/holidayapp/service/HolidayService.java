package com.sap.holidayapp.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import org.jboss.logging.MDC;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.sap.holidayapp.model.HolidayDetail;
import com.sap.holidayapp.model.HolidayHeader;
import com.sap.holidayapp.repository.HolidayDetailRepository;
import com.sap.holidayapp.repository.HolidayHeaderRepository;

@Service
public class HolidayService {
	@Autowired 
	private HttpServletRequest request;
	
	private HolidayHeaderRepository headerRepository;
	private HolidayDetailRepository detailRepository;
    private CorrelationInterceptor correlationInterceptor;
	@Autowired
	@Qualifier("rabbitTemplateCustom")
	private AmqpTemplate rabbitTemplate;

	@Autowired
	public HolidayService(HolidayHeaderRepository headerRepository, HolidayDetailRepository detailRepository, CorrelationInterceptor correlationInterceptor) {
		super();
		this.headerRepository = headerRepository;
		this.detailRepository = detailRepository;
		this.correlationInterceptor = correlationInterceptor;
	}

	public HolidayHeader createHolidayData(HolidayHeader holidayHeader, ODataJPAContext oDataJPAContext) {
		Date date = new Date();
		holidayHeader.setCreatedBy("ABHIJEET");
		holidayHeader.setCreatedOn(new Timestamp(date.getTime()));
		holidayHeader.setUpdatedBy("ABHIJEET");
		holidayHeader.setUpdatedOn(new Timestamp(date.getTime()));

		for (HolidayDetail detail : holidayHeader.getHolidayDetails()) {
			detail.setCreatedBy("ABHIJEET");
			detail.setCreatedOn(new Timestamp(date.getTime()));
			detail.setUpdatedBy("ABHIJEET");
			detail.setUpdatedOn(new Timestamp(date.getTime()));
			detail.setHolidayHeader(holidayHeader);
		}
		return this.headerRepository.save(holidayHeader);
	}

	public String triggerApprovalviaRabbit(String month) throws Exception {
		List<HolidayDetail> holidayDetail = this.detailRepository.findByMonth(month);
		this.correlationInterceptor.preHandle(request, null, null);

		if (holidayDetail.isEmpty()) {
			return "No data found";
		}
		new HolidayApprovalRabbit(rabbitTemplate, holidayDetail).queue();
		return "triggered";
	}
}
