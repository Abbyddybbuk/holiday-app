package com.sap.holidayapp.service;

import java.util.List;

import org.slf4j.MDC;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommand.Setter;
import com.sap.holidayapp.config.LocalRabbitConfig;
import com.sap.holidayapp.model.HolidayDetail;

public class HolidayApprovalRabbit extends HystrixCommand<String> {

	private AmqpTemplate rabbitTemplate;
	private List<HolidayDetail> holidaydetail;
	private String correlationId;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public HolidayApprovalRabbit(AmqpTemplate rabbitTemplate, List<HolidayDetail> holidaydetail) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("HolidayApproval"))
				.andCommandKey(HystrixCommandKey.Factory.asKey("HolidayApproval.Update")));

		this.rabbitTemplate = rabbitTemplate;
		this.holidaydetail = holidaydetail;
		this.correlationId = MDC.get(CorrelationInterceptor.CORRELATION_ID_LOG_VAR_NAME);
	}

	@Override
	protected String run() throws Exception {
		this.logger.info("Sending data via RabbitMQ");
		this.rabbitTemplate.convertAndSend(null, LocalRabbitConfig.HOLIDAY_QUEUE_NAME, this.holidaydetail,
				new MessagePostProcessor() {

					@Override
					public Message postProcessMessage(Message message) throws AmqpException {
						message.getMessageProperties().setCorrelationId(correlationId);
						return message;
					}
				});
		return "Queue Triggered";
	}

	@Override
	protected String getFallback() {
		this.logger.error("Failure to send message to statistics service");
		return "Failed to trigger the queue";
	}
}
