package com.greyzone.integration.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.greyzone.integration.IntegrationDownloader;

@Service
public class Sabnzbd implements IntegrationDownloader {

	private Logger log = Logger.getLogger(this.getClass());
	
	@Override
	public void orderDownloadByIds(List<String> ids) {
		HttpClient client = new HttpClient();
		
		for (String id : ids) {
			GetMethod method = new GetMethod(":8080/sabnzbd/api");
			
			List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
			queryParams.add(new NameValuePair("mode", "addid"));
			queryParams.add(new NameValuePair("name", id));
			queryParams.add(new NameValuePair("apikey", ""));

			NameValuePair[] valuePairs = queryParams
					.toArray(new NameValuePair[] {});
			
			method.setQueryString(valuePairs);
			
			
			try {
				client.executeMethod(method);
				log.debug("Sabnzbd result: " + method.getResponseBodyAsString());
			} catch (HttpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	
}
