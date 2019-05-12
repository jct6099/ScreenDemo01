package com.sainsbury.serversidetest.bean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Test;

public class TestProductsDetails {

	@Test
	public void testAddSingleProduct() {
		ProductsDetails details = new ProductsDetails();
		Product item = new Product("title", "description", "2.99", 111);
		details.add(item);
		
		assertEquals(1, details.getProducts().size());
		assertEquals(item, details.getProducts().get(0));
		assertEquals(new BigDecimal(2.99).setScale(TotalCost.DEFAULT_PRECISION, RoundingMode.HALF_UP), details.getTotal().getGross());
	}

	@Test
	public void testAddMulitpleProduct() {
		ProductsDetails details = new ProductsDetails();
		Product item1 = new Product("title1", "description1", "2.99", 111);
		Product item2 = new Product("title2", "description2", "3.50", 222);
		details.add(item1);
		details.add(item2);
		
		assertEquals(2, details.getProducts().size());
		assertEquals(item1, details.getProducts().get(0));
		assertEquals(item2, details.getProducts().get(1));
		assertEquals(new BigDecimal(6.49).setScale(TotalCost.DEFAULT_PRECISION, RoundingMode.HALF_UP), details.getTotal().getGross());
	}

	@Test
	public void testInJson() {
		ProductsDetails details = new ProductsDetails();
		Product item1 = new Product("title1", "description1", "2.99", 111);
		Product item2 = new Product("title2", "description2", "3.50", 222);
		details.add(item1);
		details.add(item2);
		String jsonResult = details.toJson();

		assertTrue("Missing first item title", jsonResult.contains(item1.getTitle()));
		assertTrue("Missing first item description", jsonResult.contains(item1.getDescription()));
		assertTrue("Missing first item unit price", jsonResult.contains(item1.getUnitPrice().toString()));
		assertTrue("Missing first item kcal per 100g", jsonResult.contains(item1.getKcalPer100g().toString()));
		assertTrue("Missing second item title", jsonResult.contains(item2.getTitle()));
		assertTrue("Missing second item description", jsonResult.contains(item2.getDescription()));
		assertTrue("Missing second item unit price", jsonResult.contains(item2.getUnitPrice().toString()));
		assertTrue("Missing second item kcal per 100g", jsonResult.contains(item2.getKcalPer100g().toString()));
	}
}
