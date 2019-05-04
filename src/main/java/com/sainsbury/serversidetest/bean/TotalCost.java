package com.sainsbury.serversidetest.bean;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.google.gson.GsonBuilder;

import lombok.Getter;

@Getter
public class TotalCost {
	private static final BigDecimal VAT_PERCENTAGE = new BigDecimal(".2");
	protected static final int DEFAULT_PRECISION = 2;
	
	
	// Formula to extract VAT amount from gross amount: 1 -  1/(1 + r) where r is the VAT percentage.
	protected static final BigDecimal VAT_FACTOR = 
			BigDecimal.ONE.subtract(BigDecimal.ONE.divide(VAT_PERCENTAGE.add(BigDecimal.ONE), 5, RoundingMode.HALF_UP));

	private BigDecimal gross = BigDecimal.ZERO;
	private BigDecimal vat = BigDecimal.ZERO;

	public void add(BigDecimal price) {
		gross = gross.add(price).setScale(DEFAULT_PRECISION, RoundingMode.UP);
		vat = gross.multiply(VAT_FACTOR).setScale(DEFAULT_PRECISION, RoundingMode.HALF_UP);
	}

	public String toJSON() {
		return new GsonBuilder().disableHtmlEscaping().create().toJson(this);
	}
}
