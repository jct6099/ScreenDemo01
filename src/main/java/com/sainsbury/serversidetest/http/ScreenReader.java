package com.sainsbury.serversidetest.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.sainsbury.serversidetest.config.AppConfig;
import com.sainsbury.serversidetest.exception.PageNotFoundException;

/**
 * Read HTML contents of a screen and return a string or a DOM
 *   
 * @author szetop
 */
@Component
public class ScreenReader {
	
	/**
	 * Get HTML contents as string 
	 * 
	 * @param url page URL
	 * @return a string of HTML
	 * @throws IOException
	 */
	public String getHtmlContents(final String url) throws IOException {
		StringBuilder result = new StringBuilder();
		URLConnection conn = new URL(url).openConnection();

		try(BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
			String line;
			while((line = in.readLine()) != null) {
				result.append(line);
			}
		}
	
		return result.toString();
	}

	/**
	 * Get HTML content as parsed document
	 * 
	 * @param url page URL
	 * @return the HTML document
	 */
	public Document getDocument(final String url) {
		try {
			String htmlContent = getHtmlContents(url);
			return Jsoup.parse(htmlContent, url);
		} catch(IOException ex) {
			throw new PageNotFoundException(ex.getMessage());
		}
	}

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		String url = (String) context.getBean("url");

		try {
			System.out.println(new ScreenReader().getDocument(url));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
