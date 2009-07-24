package com.greyzone;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.greyzone.domain.tv.Show;
import com.greyzone.integration.impl.Sabnzbd;
import com.greyzone.scraper.impl.tvrage.TvRage;
import com.greyzone.settings.ApplicationSettings;
import com.greyzone.storage.impl.XmlShowStorage;

public class TvSeriesNzbStarter {

	private CommandLineParser parser;
	private Options options;
	private String[] args;
	private ClassPathXmlApplicationContext ctx;

	private Logger log = Logger.getLogger(TvSeriesNzbStarter.class);

	public TvSeriesNzbStarter(String[] args) {
		this.args = args;
		initCLI();

		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
	}

	public TvSeriesNzbStarter() {
		this(new String[0]);
	}

	public void actionRouter() {
		try {
			CommandLine line = parser.parse(options, args);
			if (line.hasOption('h')) {
				doPrintHelp();
				System.exit(0);
			}
			if (line.hasOption('d')) {
				ApplicationSettings appSettings = (ApplicationSettings) ctx
						.getBean("ApplicationSettings");
				appSettings.setDryRun(true);
			}
			if (line.hasOption('c')) {
				doSystemsCheck();
				System.exit(0);
			}

			doTvCheck();
		} catch (ParseException pe) {
			log.error("Invalid program options.", pe);
		}
	}

	private void doPrintHelp() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("tvseriesnzb", options);
	}

	private void doTvCheck() {
		TvSeriesNzbChecker checker = (TvSeriesNzbChecker) ctx
				.getBean("TvSeriesNzbChecker");
		checker.checkForDownloads();
	}

	private void doSystemsCheck() {
		log.info("Doing Systems Check...");
		Sabnzbd sab = (Sabnzbd) ctx.getBean("Sabnzbd");
		sab.testIntegration();

		XmlShowStorage xml = (XmlShowStorage) ctx.getBean("XmlShowStorage");
		xml.testShowParsing();

		TvRage tvrage = (TvRage) ctx.getBean("TvRage");
		for (Show show : xml.getShows()) {
			tvrage.testIntegration(show);
		}
		log.info("Systems check completed successfully!");

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TvSeriesNzbStarter starter = new TvSeriesNzbStarter(args);
		starter.actionRouter();
	}

	private void initCLI() {
		parser = new PosixParser();

		options = new Options();
		options
				.addOption("d", "dry-run", false,
						"Dry run, do everything except contact SABnzbd and won't update shows.xml");
		options
				.addOption("c", "check-configuration", false,
						"Test SABnzbd integration and parsing of shows.xml with TVRage lookups");
		options.addOption("h", "help", false, "Print (this) help page");
	}
}
