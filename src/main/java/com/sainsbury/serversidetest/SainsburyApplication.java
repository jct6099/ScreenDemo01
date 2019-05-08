package com.sainsbury.serversidetest;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.sainsbury.serversidetest.bean.ProductsDetails;
import com.sainsbury.serversidetest.config.AppConfig;
import com.sainsbury.serversidetest.http.ScreenReader;
import com.sainsbury.serversidetest.service.ScraperService;

/**
 * Main application class to run the Sainsbury screen scraper
 * 
 * @author szetop
 */
public class SainsburyApplication 
{
	public static void main( String[] args )
    {
	    @SuppressWarnings("resource")
		ApplicationContext context = new AnnotationConfigApplicationContext("com.sainsbury.serversidetest");
	    ScraperService scraperService = context.getBean(ScraperService.class);
	    ScreenReader screenReader = context.getBean(ScreenReader.class);
		AppConfig config = context.getBean(AppConfig.class);

		scraperService.setScreenReader(screenReader);
    	ProductsDetails productsDetails = scraperService.load(config.getUrl());
    	
    	System.out.println(productsDetails.toJson());
    }
}
