package com.camel.example.process;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.camel.Exchange;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HealthCheckProcessor implements org.apache.camel.Processor {

	public static final Logger logger = LoggerFactory.getLogger(HealthCheckProcessor.class);

	@Override
	public void process(Exchange exchange) throws Exception {

		String healthCheckResult = exchange.getIn().getBody(String.class);

		logger.info("Health Check String of the APP is : {}", healthCheckResult);

		ObjectMapper objectMapper = new ObjectMapper();

		Map<String, Object> map = objectMapper.readValue(healthCheckResult, new TypeReference<Map<String, Object>>() {
		});

		logger.info("map read is : " + map);

		StringBuilder builder = null;

		for (String key : map.keySet()) {

			if (map.get(key).toString().contains("DOWN")) {

				if (builder == null) {
					builder = new StringBuilder();
				}

				builder.append(key + " component in the route is down \n ");
			}
		}

		if (builder != null) {

			logger.info("Exception message is : {}" + builder.toString());

			exchange.getIn().setHeader("error", true);
			exchange.getIn().setBody(builder.toString());
			exchange.setProperty(Exchange.EXCEPTION_CAUGHT, builder.toString());
		}

	}
}
