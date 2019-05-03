package com.sainsbury.serversidetest.bean;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
 * Class that represent a item on the self
 * 
 * @author Patrick
 *
 */
@Getter 
public class Item {
	private static final int DECIMAL_PLACE = 2;
	
	private String title;

	@SerializedName("kcal_per_100g")
	private Integer kcalPer100g;

	@SerializedName("unit_price")
	private BigDecimal unitPrice;
	
	private String description;

	public Item(String title, String description, String unitPrice) {
		this.title = title;
		this.description = description;
		this.unitPrice = new BigDecimal(unitPrice);

		if(this.unitPrice.scale() > DECIMAL_PLACE) {
			this.unitPrice = this.unitPrice.setScale(DECIMAL_PLACE, RoundingMode.UP);
		}
	}

	public Item(String title, String description, String unitPrice, int kcalPer100g) {
		this(title, description, unitPrice);
		this.kcalPer100g = kcalPer100g;
	}

	public String toJson() {
		return new GsonBuilder().disableHtmlEscaping().create().toJson(this);
	}
}
