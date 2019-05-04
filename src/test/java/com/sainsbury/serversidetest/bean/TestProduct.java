package com.sainsbury.serversidetest.bean;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestProduct {

	@Test
	public void testCreateItemWithNoKcal() {
		Product product = new Product("Apple", "The apple", "1.23");
		assertEquals("Apple", product.getTitle());
		assertEquals("The apple", product.getDescription());
		assertEquals("1.23", product.getUnitPrice().toString());
		assertNull(product.getKcalPer100g());
	}

	@Test
	public void testUnitPriceRoundToTwoDecimals() {
		Product product = new Product("Apple", "The apple", "1.4567244");
		assertEquals("1.46", product.getUnitPrice().toString());
	}

	@Test
	public void testLessThanTwoDecimalUnitPrice() {
		Product product = new Product("Apple", "The apple", "3");
		assertEquals("3", product.getUnitPrice().toString());

		product = new Product("Apple", "The apple", "3.2");
		assertEquals("3.2", product.getUnitPrice().toString());
	}

	@Test
	public void testFullDetailsInJson() {
		Product product = new Product("Apple", "The apple", "1.23", 100);
		assertEquals("{" +
			"\"title\":\"Apple\"," +
			"\"kcal_per_100g\":100," +
			"\"unit_price\":1.23," +
			"\"description\":\"The apple\"" +
			"}", product.toJson());
	}

	@Test
	public void testNoCaloriesIsIgnoredInJson() {
		Product product = new Product("Apple", "The apple", "1.23");
		assertEquals("{" +
			"\"title\":\"Apple\"," +
			"\"unit_price\":1.23," +
			"\"description\":\"The apple\"" +
			"}", product.toJson());
	}
}
