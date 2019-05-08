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
import com.sainsbury.serversidetest.exception.MissingDetailsException;
import com.sainsbury.serversidetest.http.ScreenReader;

import lombok.Setter;

/**
 * Sainbury product scraping service
 * 
 * @author szeto
 */
@Component
@Setter
public class ScraperService {
	
	private static final String PRICE_REGEX = "\\d+(\\.\\d+)?";
	private static final String LEFT_KCAL_REGEX = "(\\d+)\\s*kcal";
	private static final String RIGHT_KCAL_REGEX = "kcal\\s*(\\d+)";
	private ScreenReader screenReader;

	/**
	 * Load a Sainbury web page and scrape relevant details
	 * 
	 * @param targetUrl URL of target Sainbury web page
	 * @return a ProductsDetails object 
	 */
	public ProductsDetails load(final String targetUrl) {
		final ProductsDetails productsDetails = new ProductsDetails();
		final Document document = screenReader.getDocument(targetUrl);
		final Elements elements = document.select(".productLister .product");
		
		for(Element element : elements) {
			String productUrl = element.selectFirst("h3 a").attr("abs:href");
			productsDetails.add(getProduct(productUrl));
		}
		
		return productsDetails;
	}

	/**
	 * Follow URL individual product page and get relevant details
	 * 
	 * @param individualProductUrl
	 * @return
	 */
	private Product getProduct(final String individualProductUrl) {
		Product product = null;
		
		if (StringUtils.isNoneBlank(individualProductUrl)) {
			final Element document = screenReader.getDocument(individualProductUrl);
			
			if (document != null) {
				final String title = getTitle(document);
				final String description = getDescription(document);
				final String unitPrice = getUnitPrice(document);
				
				product = new Product(title, description, unitPrice);
				getKcalPer100g(document).ifPresent(product::setKcalPer100g);
			}
		}
		return product;
	}

	/**
	 * Get product title
	 * 
	 * @param document HTML document to parse
	 * @return the title
	 */
	private String getTitle(final Element document) {
		final Element element = document.selectFirst(".productSummary .productTitleDescriptionContainer");
		if(element != null) {
			return element.text();
		} else {
			throw new MissingDetailsException("Missing product title");
		}
	}

	/**
	 * Get product description
	 * 
	 * @param document HTML document to parse
	 * @return the description
	 */
	private String getDescription(final Element document) {
		final Elements elements = document.select("#information .productText p");
		
		if(elements != null) {
			final Optional<Element> descElement = elements.stream().filter(e -> e.hasText()).findFirst();
			return descElement.isPresent() ? descElement.get().text() : "";
		} else {
			throw new MissingDetailsException("Missing product description");
		}
	}

	/**
	 * Get product unit price
	 * 
	 * @param document HTML document to parse
	 * @return the unit price
	 */
	private String getUnitPrice(final Element document) {
		final Element element = document.selectFirst(".pricing .pricePerUnit");
		if(element != null) {
			final Pattern decimalPattern = Pattern.compile(PRICE_REGEX);
			final Matcher matcher = decimalPattern.matcher(element.text());

			if(matcher.find() && !matcher.group().isEmpty()) {
				return matcher.group();
			} else {
				throw new MissingDetailsException("Price is not in the correct format (i.e. 1.29)");
			}
		} else {
			throw new MissingDetailsException("Missing product price per unit");
		}
	}

	/**
	 * Get product kcal per 100g
	 * 
	 * Note: This try suffix first (i.e 26 kcal) and then prefix (i.e. kcal 26)
	 * 
	 * @param document HTML document to parse
	 * @return the kcal per 100g
	 */
	private OptionalInt getKcalPer100g(final Element document) {
		final String contents = document.select(".nutritionTable").text();
		final OptionalInt kcal = getKcalMatch(LEFT_KCAL_REGEX, contents);
		
		return kcal.isPresent() ? kcal: getKcalMatch(RIGHT_KCAL_REGEX, contents);
	}

	/**
	 * Extract kcal from contents
	 * 
	 * @param regex The regular expression
	 * @param content Text contents
	 * @return an optionalInt
	 */
	private OptionalInt getKcalMatch(final String regex, final String content) {
		final Pattern kcalPattern = Pattern.compile(regex);
		final Matcher matcher = kcalPattern.matcher(content);

		if(matcher.find() && !matcher.group(1).isEmpty()) {
			return OptionalInt.of(Integer.parseInt(matcher.group(1)));
		}

		return OptionalInt.empty();
	}

}
