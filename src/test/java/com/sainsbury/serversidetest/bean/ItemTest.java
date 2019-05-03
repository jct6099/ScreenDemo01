package com.sainsbury.serversidetest.bean;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ItemTest {

	@Test
	public void testCreateItemWithNoKcal() {
		Item item = new Item("Apple", "The apple", "1.23");
		assertEquals("Apple", item.getTitle());
		assertEquals("The apple", item.getDescription());
		assertEquals("1.23", item.getUnitPrice().toString());
		assertNull(item.getKcalPer100g());
	}

	@Test
	public void testUnitPriceRoundToTwoDecimals() {
		Item item = new Item("Apple", "The apple", "1.4567244");
		assertEquals("1.46", item.getUnitPrice().toString());
	}

	@Test
	public void testLessThanTwoDecimalUnitPrice() {
		Item item = new Item("Apple", "The apple", "3");
		assertEquals("3", item.getUnitPrice().toString());

		item = new Item("Apple", "The apple", "3.2");
		assertEquals("3.2", item.getUnitPrice().toString());
	}

	@Test
	public void testFullDetailsInJson() {
		Item item = new Item("Apple", "The apple", "1.23", 100);
		assertEquals("{" +
			"\"title\":\"Apple\"," +
			"\"kcal_per_100g\":100," +
			"\"unit_price\":1.23," +
			"\"description\":\"The apple\"" +
			"}", item.toJson());
	}

	@Test
	public void testNoCaloriesIsIgnoredInJson() {
		Item item = new Item("Apple", "The apple", "1.23");
		assertEquals("{" +
			"\"title\":\"Apple\"," +
			"\"unit_price\":1.23," +
			"\"description\":\"The apple\"" +
			"}", item.toJson());
	}
}
