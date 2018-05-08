package com.camel.example.alert;

import java.util.Properties;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
public class MailProcessor implements Processor {

	public static final Logger logger = LoggerFactory.getLogger(MailProcessor.class);

	@Autowired
	JavaMailSender emailSender;

	@Autowired
	Environment environment;

	@Override
	public void process(Exchange exchange) throws Exception {

		Exception e = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
		logger.info("Exception Caught in mail processor : " + e.getMessage());

		String messageBody = "Exception happened in the camel Route : " + e.getMessage();

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(environment.getProperty("mailFrom"));
		message.setTo(environment.getProperty("mailto"));
		message.setSubject("Exception in Camel Route");
		message.setText(messageBody);

		Properties props = ((JavaMailSenderImpl) emailSender).getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");

		emailSender.send(message);

	}
}
