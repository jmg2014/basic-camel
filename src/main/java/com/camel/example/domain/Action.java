package com.camel.example.domain;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.math.BigDecimal;

@CsvRecord(separator = ",", skipFirstLine = true)
public class Action {

	@DataField(pos = 1)
	private String transactionType;

	@DataField(pos = 2)
	private String itemID;

	@DataField(pos = 3)
	private String itemDescription;

	@DataField(pos = 4, precision = 2)
	private BigDecimal price;

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public BigDecimal getPrice() {
		return price;
	}

	@Override
	public String toString() {
		return "Item{" +
				"transactionType='" + transactionType + '\'' +
				", itemID='" + itemID + '\'' +
				", itemDescription='" + itemDescription + '\'' +
				", price=" + price +
				'}';
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
