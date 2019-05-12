package com.sainsbury.serversidetest.service;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;

import org.easymock.EasyMockSupport;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import com.sainsbury.serversidetest.bean.ProductsDetails;
import com.sainsbury.serversidetest.http.ScreenReader;

public class TestScraperService extends EasyMockSupport {

	private static final String BASE_URL = "https://any.URL";
	private static final String ANY_URL = BASE_URL + "/a/b/c/d/e/f/";
	private static final String STRAWBERRIES_URL = "/shop/gb/groceries/berries-cherries-currants/sainsburys-british-strawberries-400g.html";
	private static final String BLUEBERRIES_URL = "/shop/gb/groceries/berries-cherries-currants/sainsburys-blueberries-200g.html";

	private static final Document EMPTY_PAGE = Jsoup.parse("<HTML>something</HTML>");
    private static final Document EMPTY_PRODUCT_LIST = Jsoup.parse("<HTML><body><ul class=\"productLister\"></ul></body></HTML>");
	private static final Document PRODUCT_LIST = Jsoup.parse(
			"<HTML><body>"
			+ "<ul class=\"productLister\">"
			+ "  <div class=\"product\">"
			+ "    <h3>"
			+ "      <a href=\"../../../../../.." + STRAWBERRIES_URL + "\">Strawberries</a>"
			+ "    </h3>"
			+ "  </div>"
			+ "  <div class=\"product\">"
			+ "    <h3><a href=\"../../../../../.." + BLUEBERRIES_URL + "\">Blueberries</a></h3>"
			+ "  </div>"
			+ "</ul>"
			+ "</body></HTML>", ANY_URL);

	private static final String TITLE_STRAWBERRIES = "Sainsbury's Strawberries 400g";
	private static final String DESC_STRAWBERRIES = "by Sainsbury's strawberries";
	private static final String PRICE_STRAWBERRIES = "1.75";
	private static final Integer KCAL_STRAWBERRIES = 33;
	private static final Document STRAWBERRIES_PAGE = Jsoup.parse(
			"<HTML><body>"
			+ "<div class=\"productSummary\">"
			+ "  <div class=\"productTitleDescriptionContainer\">"
			+ "    <h1>" + TITLE_STRAWBERRIES + "</h1>"
			+ "  </div>"
			+ "</div>"
			+ "<div id=\"information\">"
			+ "  <div class=\"productText\">"
			+ "    <p></p><p>" + DESC_STRAWBERRIES + "</p><p><p></p></p>" 
			+ "  </div>"
			+ "</div>"
			+ "<div class=\"priceTab\">"
			+ "  <div class=\"pricing\">"
			+ "    <p class=\"pricePerUnit\">£" + PRICE_STRAWBERRIES + "</p>"
			+ "  </div>"
			+ "</div>"
			+ "<table class=\"nutritionTable\">"
			+ "  <tr class=\"tableTitleRow\">"
			+ "    <td>" + KCAL_STRAWBERRIES + "kcal</td>"
			+ "  </tr>"
			+ "</table>"
			+ "</body></HTML>", BASE_URL + STRAWBERRIES_URL);

	private static final String TITLE_BLUEBERRIES = "Sainsbury's Blueberries 200g";
	private static final String DESC_BLUEBERRIES = "by Sainsbury's blueberries";
	private static final String PRICE_BLUEBERRIES = "1.75";
	private static final Document BLUEBERRIES_PAGE = Jsoup.parse(
			"<HTML><body>"
			+ "<div class=\"productSummary\">"
			+ "  <div class=\"productTitleDescriptionContainer\">"
			+ "    <h1>" + TITLE_BLUEBERRIES + "</h1>"
			+ "  </div>"
			+ "</div>"
			+ "<div id=\"information\">"
			+ "  <div class=\"productText\">"
			+ "    <p></p><p>" + DESC_BLUEBERRIES + "</p><p><p></p></p>" 
			+ "  </div>"
			+ "</div>"
			+ "<div class=\"priceTab\">"
			+ "  <div class=\"pricing\">"
			+ "    <p class=\"pricePerUnit\">£" + PRICE_BLUEBERRIES + "</p>"
			+ "  </div>"
			+ "</div>"
			+ "</body></HTML>", BASE_URL + BLUEBERRIES_URL);

	private ScreenReader screenReader;
	private ScraperService testSubject;

	@Before
    public void setUp() throws Exception {
        // NiceMocks return default values for
        // unimplemented methods
    	screenReader = createNiceMock(ScreenReader.class);
    	testSubject = new ScraperService();
    	testSubject.setScreenReader(screenReader);
    }

    @Test
    public void testEmptyPage() throws IOException {
        expect(screenReader.getDocument(ANY_URL)).andReturn(EMPTY_PAGE);
        replayAll();

        ProductsDetails productsDetails = testSubject.load(ANY_URL);

        assertTrue(productsDetails.getProducts().isEmpty());
        assertEquals(BigDecimal.ZERO, productsDetails.getTotal().getGross());
        assertEquals(BigDecimal.ZERO, productsDetails.getTotal().getVat());
        verifyAll();    	
    }

    @Test
    public void testEmptyProductList() throws IOException {
        expect(screenReader.getDocument(ANY_URL)).andReturn(EMPTY_PRODUCT_LIST);
        replayAll();

        ProductsDetails productsDetails = testSubject.load(ANY_URL);

        assertTrue(productsDetails.getProducts().isEmpty());
        assertEquals(BigDecimal.ZERO, productsDetails.getTotal().getGross());
        assertEquals(BigDecimal.ZERO, productsDetails.getTotal().getVat());
        verifyAll();    	
    }

    @Test
    public void testWithProducts() throws IOException {
        expect(screenReader.getDocument(ANY_URL)).andReturn(PRODUCT_LIST);
        expect(screenReader.getDocument(BASE_URL + STRAWBERRIES_URL)).andReturn(STRAWBERRIES_PAGE);
        expect(screenReader.getDocument(BASE_URL + BLUEBERRIES_URL)).andReturn(BLUEBERRIES_PAGE);
        replayAll();

        ProductsDetails productsDetails = testSubject.load(ANY_URL);

        assertEquals(2, productsDetails.getProducts().size());
        assertEquals(TITLE_STRAWBERRIES, productsDetails.getProducts().get(0).getTitle());
        assertEquals(DESC_STRAWBERRIES, productsDetails.getProducts().get(0).getDescription());
        assertTrue(productsDetails.getProducts().get(0).getUnitPrice().compareTo(new BigDecimal(PRICE_STRAWBERRIES)) == 0);
        assertEquals(KCAL_STRAWBERRIES, productsDetails.getProducts().get(0).getKcalPer100g());
        assertEquals(TITLE_BLUEBERRIES, productsDetails.getProducts().get(1).getTitle());
        assertEquals(DESC_BLUEBERRIES, productsDetails.getProducts().get(1).getDescription());
        assertTrue(productsDetails.getProducts().get(1).getUnitPrice().compareTo(new BigDecimal(PRICE_BLUEBERRIES)) == 0);
        assertEquals(null, productsDetails.getProducts().get(1).getKcalPer100g());
        assertTrue(productsDetails.getTotal().getGross().compareTo(new BigDecimal(3.5)) == 0);
        verifyAll();    	
    }
}
