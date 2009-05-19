package com.greyzone;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TvSeriesNzbStarter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		TvSeriesNzbChecker checker =  (TvSeriesNzbChecker) ctx.getBean("TvSeriesNzbChecker");
		checker.checkForDownloads();
	}

}
