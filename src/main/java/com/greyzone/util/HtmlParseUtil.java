package com.greyzone.util;

import org.apache.commons.lang.StringUtils;

public class HtmlParseUtil {

	public static boolean assertContains(String html, String search) {
		return StringUtils.containsIgnoreCase(html, search);
	}

	public static  String findString(String body, String startsWith, String upTill) {
		int pos = body.indexOf(startsWith);

		if (pos < 0) {
			return "";
		}
		body = body.substring(pos + startsWith.length(), body.length());

		pos = body.indexOf(upTill);

		return body.substring(0, pos);
	}
	
	public static  String stripFrom(String body, String upTill) {
		int pos = body.indexOf(upTill);
		
		if (pos < 0)
			return "";
		
		return body.substring(pos + upTill.length(), body.length());
	}
}
