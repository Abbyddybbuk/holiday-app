package com.sap.holidayapp.service;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommand.Setter;
import com.sap.holidayapp.model.HolidayDetail;

public class HolidayApproval extends HystrixCommand<List<HolidayDetail>> {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private RestTemplate restTemplate;
	private List<HolidayDetail> holidaydetail;
	private String url;
	private Supplier<List<HolidayDetail>> fallbackFunction;

	public HolidayApproval(RestTemplate restTemplate, List<HolidayDetail> holidaydetail, String url) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("HolidayApproval"))
				.andCommandKey(HystrixCommandKey.Factory.asKey("HolidayApproval.Update")));

		this.restTemplate = restTemplate;
		this.holidaydetail = holidaydetail;
		this.url = url;
	}

	@Override
	protected List<HolidayDetail> run() throws Exception {
		logger.info("sending request {}", this.url);
		ResponseEntity<List<HolidayDetail>> rrmExecResponse = null;
		try {
			rrmExecResponse = restTemplate.exchange(url, HttpMethod.POST, this.getHttpEntity(this.holidaydetail),
					new ParameterizedTypeReference<List<HolidayDetail>>() {
					});
		} catch (HttpStatusCodeException error) {
			logger.error("received HTTP status code: {}", error.getStatusCode());
			throw error;
		}

		List<HolidayDetail> rrmExecResponseData = rrmExecResponse.getBody();
		return rrmExecResponseData;
	}

	@Override
	protected List<HolidayDetail> getFallback() {
		logger.info("enter fallback method");

		if (isResponseTimedOut()) {
			logger.error("execution timed out after {} ms (HystrixCommandKey:{})", getTimeoutInMs(),
					this.getCommandKey().name());
		}
		if (isFailedExecution()) {
			logger.error("execution failed", getFailedExecutionException());
		}
		if (isResponseRejected()) {
			logger.warn("request was rejected");
		}
		return fallbackFunction.get();
	}

	private HttpEntity<Object> getHttpEntity(List<HolidayDetail> holidaydetail2) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		return new HttpEntity<Object>(holidaydetail2, headers);
	}

	protected int getTimeoutInMs() {
		return this.properties.executionTimeoutInMilliseconds().get();
	}

}
