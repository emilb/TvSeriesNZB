package com.greyzone.util;

import org.apache.log4j.Logger;

public class ThreadUtils {

	private static final Logger log = Logger.getLogger(ThreadUtils.class);
	
	public static void sleep(long ms) {
		try {
			log.debug("Sleeping for " + ms + "ms");
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			log.warn("Sleep was interrupted. WTF!");
		}
	}
	
}
