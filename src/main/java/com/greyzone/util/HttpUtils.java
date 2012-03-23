package com.greyzone.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.log4j.Logger;

public class HttpUtils {
	
	private final static Logger log = Logger.getLogger(HttpUtils.class);
	
	public static String getContentAsString(HttpEntity entity) {
		
		try {
			return IOUtils.toString(entity.getContent());
	    } catch (Exception e) {
	        return "";
	    } finally {
	    	try {
	    		entity.getContent().close();
	    	} catch (IOException e) {
	    		log.warn("Failed to close entity inputstream", e);
	    	}
	    }
	}
	
	public static String createGetURI(String baseUrl, List<NameValuePair> valuePairsList) {
		NameValuePair[] valuePairs = valuePairsList.toArray(new NameValuePair[] {});
		return createGetURI(baseUrl, valuePairs);
	}
	
	public static String createGetURI(String baseUrl, NameValuePair... valuePairs) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (NameValuePair nvp : valuePairs) {
        	params.add(nvp);
        }
        
        return baseUrl + "?" + URLEncodedUtils.format(params, "UTF-8");
	}
}
