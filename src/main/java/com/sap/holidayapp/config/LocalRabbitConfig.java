package com.sap.holidayapp.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory.ConfirmType;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({ "default" })
public class LocalRabbitConfig {
	public static final String HOLIDAY_QUEUE_NAME = "HOLIDAY_REQUEST";

	@Bean
	public ConnectionFactory rabbitMQConnectionFactory(@Value("${spring.rabbitmq.host}") String host,
			@Value("${spring.rabbitmq.port}") int port, @Value("${spring.rabbitmq.username}") String username,
			@Value("${spring.rabbitmq.password}") String password) {
		CachingConnectionFactory factory = this.getConnectionFactory(host, port, username, password);
		factory.setPublisherConfirmType(ConfirmType.NONE);
		factory.setPublisherReturns(true);
		factory.setChannelCacheSize(100);
		return factory;
	}

	@Bean
	public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
		RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
		rabbitAdmin.declareQueue(new Queue(LocalRabbitConfig.HOLIDAY_QUEUE_NAME));
		return rabbitAdmin;
	}

	@Bean
	public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMandatory(true);
		return rabbitTemplate;
	}

	private CachingConnectionFactory getConnectionFactory(String host, int port, String username, String password) {
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
		cachingConnectionFactory.setHost(host);
		cachingConnectionFactory.setPort(port);
		cachingConnectionFactory.setUsername(username);
		cachingConnectionFactory.setPassword(password);
		return cachingConnectionFactory;
	}
}
