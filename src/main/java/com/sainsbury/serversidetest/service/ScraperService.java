package com.sainsbury.serversidetest.service;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.sainsbury.serversidetest.bean.Product;
import com.sainsbury.serversidetest.bean.ProductsDetails;
import com.sainsbury.serversidetest.exception.MissingDetailException;
import com.sainsbury.serversidetest.http.ScreenReader;

import lombok.Setter;

@Component
@Setter
public class ScraperService {
	
	private static final String PRICE_REGEX = "\\d+(\\.\\d+)?";
	private ScreenReader screenReader;

	public ProductsDetails load(final String siteUrl) {
		final ProductsDetails productsDetails = new ProductsDetails();
		final Document document = screenReader.getDocument(siteUrl);
		final Elements elements = document.select(".productLister .product");
		
		for(Element element : elements) {
			String productUrl = element.selectFirst("h3 a").attr("abs:href");
			productsDetails.add(getProduct(productUrl));
		}
		
		return productsDetails;
	}

	private Product getProduct(final String individualProductUrl) {
		Product product = null;
		
		if (StringUtils.isNoneBlank(individualProductUrl)) {
			final Element document = screenReader.getDocument(individualProductUrl);
			
			if (document != null) {
				final String title = getTitle(document);
				final String description = getDescription(document);
				final String unitPrice = getUnitPrice(document);
				
				product = new Product(title, description, unitPrice);
			}
		}
		return product;
	}

	private String getTitle(final Element document) {
		final Element element = document.selectFirst(".productSummary .productTitleDescriptionContainer");
		if(element != null) {
			return element.text();
		} else {
			throw new MissingDetailException("Missing product title");
		}
	}

	private String getDescription(final Element document) {
		final Elements elements = document.select("#information .productText p");
		
		if(elements != null) {
			final Optional<Element> descElement = elements.stream().filter(e -> e.hasText()).findFirst();
			return descElement.isPresent() ? descElement.get().text() : "";
		} else {
			throw new MissingDetailException("Missing product description");
		}
	}

	private String getUnitPrice(final Element document) {
		final Element element = document.selectFirst(".pricing .pricePerUnit");
		if(element != null) {
			final Pattern decimalPattern = Pattern.compile(PRICE_REGEX);
			final Matcher matcher = decimalPattern.matcher(element.text());

			if(matcher.find() && !matcher.group().isEmpty()) {
				return matcher.group();
			} else {
				throw new MissingDetailException("Price not in correct format (i.e. 1.29)");
			}
		} else {
			throw new MissingDetailException("Missing product price per unit");
		}
	}

	private OptionalInt getKcalPer100g(Element document) {
		Element element = document.selectFirst(".nutritionTable");
		//TODO: complete this 
		return null;
	}

}
