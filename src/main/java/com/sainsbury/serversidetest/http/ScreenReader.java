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

import com.sainsbury.serversidetest.config.AppConfig;
import com.sainsbury.serversidetest.config.LoadConfig;
import com.sainsbury.serversidetest.exception.ServiceNotAvailableException;

/**
 * Read HTML content of a screen with a URL
 *   
 * @author Patrick
 */
public class ScreenReader {
	
	private static AppConfig config;
	
	static {
		@SuppressWarnings("resource")
		ApplicationContext context = new AnnotationConfigApplicationContext(LoadConfig.class);
        config = (AppConfig) context.getBean("appConfig");
	}

	/**
	 * Get HTML content as string 
	 * 
	 * @return a string of HTML
	 * @throws IOException
	 */
	public String getContents() throws IOException {
		StringBuilder result = new StringBuilder();
		URLConnection conn = new URL(config.getUrl()).openConnection();

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
	 * @return the HTML document
	 */
	public Document getDocument() {
		try {
			String htmlContent = getContents();
			return Jsoup.parse(htmlContent);
		} catch(IOException ex) {
			throw new ServiceNotAvailableException(ex.getMessage());
		}
	}

	public static void main(String[] args) {
		try {
			System.out.println(new ScreenReader().getDocument());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
