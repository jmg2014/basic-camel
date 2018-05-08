package com.camel.example.process;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.camel.example.domain.Action;
import com.camel.example.exception.DataException;

@Component
public class BuildSQLProcessor implements org.apache.camel.Processor {

	public static final Logger logger = LoggerFactory.getLogger(BuildSQLProcessor.class);

	@Override
	public void process(Exchange exchange) throws Exception {

		Action action = (Action) exchange.getIn().getBody();
		logger.info(" Action in Processor is : {}", action);
		String tableName = "ACTION";
		StringBuilder query = new StringBuilder();

		if (ObjectUtils.isEmpty(action.getItemID())) {
			throw new DataException("ITEMID is null for " + action.getItemDescription());
		}

		query.append("INSERT INTO " + tableName + "(ITEMID, ACTION_TYPE,ITEM_DESCRIPTION,PRICE) VALUES ('");
		query.append(action.getItemID() + "','" + action.getTransactionType() + "','" + action.getItemDescription() + "'," + action.getPrice() + ")");

		logger.info("Final Query is : {}", query);
		exchange.getIn().setBody(query.toString());

	}
}
