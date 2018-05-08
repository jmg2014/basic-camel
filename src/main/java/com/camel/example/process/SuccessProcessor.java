package com.camel.example.process;

import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component
public class SuccessProcessor implements org.apache.camel.Processor {
	@Override
	public void process(Exchange exchange) throws Exception {
		exchange.getIn().setBody("Data Updated SuccessFully");
	}
}
