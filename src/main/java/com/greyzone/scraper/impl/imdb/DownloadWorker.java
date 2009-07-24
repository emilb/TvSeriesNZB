package com.greyzone.scraper.impl.imdb;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;

public class DownloadWorker {

	public static String downloadHtml(String url) {
		return downloadHtml(url, null);
	}
	
	public static String downloadHtml(String url, NameValuePair... params) {
		try {
			HttpClient client = new HttpClient();
			GetMethod get = new GetMethod(url);
			if (params != null)
				get.setQueryString(params);
			
			client.executeMethod(get);
			
			StringWriter sw = new StringWriter();
			IOUtils.copy(get.getResponseBodyAsStream(), sw);
			return sw.toString();
			
		} catch (Exception e) {
			throw new RuntimeException("Failed to get html from " + url, e);
		}
	}
	
	public static byte[] downloadBinary(String url) {
		try {
			HttpClient client = new HttpClient();
			GetMethod get = new GetMethod(url);
			
			client.executeMethod(get);
			
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			IOUtils.copy(get.getResponseBodyAsStream(), b);
			return b.toByteArray();
			
		} catch (Exception e) {
			throw new RuntimeException("Failed to get binary from " + url, e);
		}
	}
}
