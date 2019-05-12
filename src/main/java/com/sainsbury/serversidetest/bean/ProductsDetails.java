package com.sainsbury.serversidetest.bean;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.GsonBuilder;

import lombok.Getter;

/**
 * A class that hold a list of products and the total cost/vat
 * 
 * @author szetop
 */
@Getter
public class ProductsDetails {

	private List<Product> products = new ArrayList<>();
	
	private TotalCost total = new TotalCost();

	public void addAll(List<Product> products) {
		products.stream().forEach(this::add);
	}
	
	public void add(Product product) {
		if (product != null) {
			this.products.add(product);
			this.total.add(product.getUnitPrice());
		}
	}

	public String toJson() {
		GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping();
		gsonBuilder.setPrettyPrinting();
		return gsonBuilder.create().toJson(this);
	}
	
}
