package com.sainsbury.serversidetest.bean;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Class that represent a item on the self
 * 
 * @author Patrick
 *
 */
@Getter @Setter
public class Product {
	private static final int DEFAULT_PERSSION = 3;
	
	private String title;

	@SerializedName("kcal_per_100g")
	private Integer kcalPer100g;

	@SerializedName("unit_price")
	private BigDecimal unitPrice;
	
	private String description;

	public Product() {
		super();
	}
	
	public Product(String title, String description, String unitPrice) {
		this.title = title;
		this.description = description;
		this.unitPrice = new BigDecimal(unitPrice, new MathContext(DEFAULT_PERSSION, RoundingMode.UP));
	}

	public Product(String title, String description, String unitPrice, Integer kcalPer100g) {
		this(title, description, unitPrice);
		this.kcalPer100g = kcalPer100g;
	}

	public String toJson() {
		return new GsonBuilder().disableHtmlEscaping().create().toJson(this);
	}
}
