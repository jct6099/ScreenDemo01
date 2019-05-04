package com.sainsbury.serversidetest.bean;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.GsonBuilder;

import lombok.Getter;

@Getter
public class ProductsDetails {

	private List<Product> results = new ArrayList<>();
	
	private TotalCost total = new TotalCost();

	public void addAll(List<Product> products) {
		products.stream().forEach(this::add);
	}
	
	public void add(Product product) {
		if (product != null) {
			this.results.add(product);
			this.total.add(product.getUnitPrice());
		}
	}

	public String toJson() {
		GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping();
		gsonBuilder.setPrettyPrinting();
		return gsonBuilder.create().toJson(this);
	}
	
}
