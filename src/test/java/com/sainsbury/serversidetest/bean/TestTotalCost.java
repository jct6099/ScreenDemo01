package com.sainsbury.serversidetest.bean;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Test;

public class TestTotalCost {

	@Test(expected = NullPointerException.class)
	public void testCreateCostWithNullValue() {
		new TotalCost().add(null);
	}

	@Test
	public void testCreateCostWithValidValue() {
		TotalCost cost = new TotalCost();
		BigDecimal expected = BigDecimal.TEN.multiply(TotalCost.VAT_FACTOR).setScale(TotalCost.DEFAULT_PRECISION, RoundingMode.UP);

		cost.add(BigDecimal.TEN);
		
		assertEquals(BigDecimal.TEN.setScale(TotalCost.DEFAULT_PRECISION), cost.getGross());
		assertEquals(expected, cost.getVat());
	}
	
	@Test
	public void testTotalCostInJson() {
		TotalCost cost = new TotalCost();
		BigDecimal expected = BigDecimal.TEN.multiply(TotalCost.VAT_FACTOR).setScale(TotalCost.DEFAULT_PRECISION, RoundingMode.UP);
		cost.add(BigDecimal.TEN);

		assertEquals("{\"gross\":" + BigDecimal.TEN.setScale(TotalCost.DEFAULT_PRECISION) + ",\"vat\":" + expected + "}", cost.toJSON());
	}
}
