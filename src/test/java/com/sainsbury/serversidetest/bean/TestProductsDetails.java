package com.sainsbury.serversidetest.bean;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Test;

public class TestProductsDetails {

	@Test
	public void testAddSingleProduct() {
		ProductsDetails details = new ProductsDetails();
		Product item = new Product("title", "description", "2.99", 111);
		details.add(item);
		
		assertEquals(1, details.getResults().size());
		assertEquals(item, details.getResults().get(0));
		assertEquals(new BigDecimal(2.99).setScale(TotalCost.DEFAULT_PRECISION, RoundingMode.HALF_UP), details.getTotal().getGross());
	}

	@Test
	public void testAddMulitpleProduct() {
		ProductsDetails details = new ProductsDetails();
		Product item1 = new Product("title1", "description1", "2.99", 111);
		Product item2 = new Product("title2", "description2", "3.50", 222);
		details.add(item1);
		details.add(item2);
		
		assertEquals(2, details.getResults().size());
		assertEquals(item1, details.getResults().get(0));
		assertEquals(item2, details.getResults().get(1));
		assertEquals(new BigDecimal(6.49).setScale(TotalCost.DEFAULT_PRECISION, RoundingMode.HALF_UP), details.getTotal().getGross());
	}
}
