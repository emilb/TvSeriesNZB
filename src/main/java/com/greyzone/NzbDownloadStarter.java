package com.greyzone;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.greyzone.cache.NzbsOrgRssFeedCache;
import com.greyzone.cache.NzbsOrgSearchCache;
import com.greyzone.cache.TvRageCache;
import com.greyzone.checker.impl.TvSeriesNzbChecker;
import com.greyzone.domain.tv.Show;
import com.greyzone.integration.impl.Sabnzbd;
import com.greyzone.scraper.impl.tvrage.TvRage;
import com.greyzone.settings.ApplicationSettings;
import com.greyzone.storage.impl.XmlShowStorage;

public class NzbDownloadStarter {

	private CommandLineParser parser;
	private Options options;
	private final String[] args;
	private final AnnotationConfigApplicationContext ctx;

	private final Logger log = Logger.getLogger(NzbDownloadStarter.class);

	public NzbDownloadStarter(String[] args) {
		this.args = args;
		initCLI();

		ctx = new AnnotationConfigApplicationContext();
		ctx.scan("com.greyzone");
		ctx.refresh();
	}

	public NzbDownloadStarter() {
		this(new String[0]);
	}

	public void actionRouter() {
		try {
			ApplicationSettings appSettings = (ApplicationSettings) ctx
					.getBean("ApplicationSettings");
			CommandLine line = parser.parse(options, args);
			if (line.hasOption('h')) {
				doPrintHelp();
				System.exit(0);
			}
			if (line.hasOption('v')) {
				ConsoleAppender appender = (ConsoleAppender) Logger
						.getRootLogger().getAppender("stdout");
				appender.setThreshold(Level.ALL);
				Logger.getLogger("stdout").setLevel(Level.ALL);
			}
			if (line.hasOption('d')) {
				appSettings.setDryRun(true);
			}
			if (line.hasOption('s')) {
				appSettings.setShowsFileName(line.getOptionValue('s'));
			}
			if (line.hasOption('w')) {
				clearCaches();
			}
			if (line.hasOption('c')) {
				doSystemsCheck();
				System.exit(0);
			}
			doTvCheck();

		} catch (ParseException pe) {
			log.error("Invalid program options.", pe);
		}

		ctx.close();
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

	private void clearCaches() {
		TvRageCache tvrageCache = (TvRageCache) ctx.getBean("TvRageCache");
		NzbsOrgRssFeedCache nzbsCache = (NzbsOrgRssFeedCache) ctx
				.getBean("NzbsOrgRssFeedCache");
		NzbsOrgSearchCache searchCache = (NzbsOrgSearchCache) ctx.getBean("NzbsOrgSearchCache");
		

		log.debug("Clearing caches");
		tvrageCache.clearCache();
		nzbsCache.clearCache();
		searchCache.clearCache();
	}

	private void doSystemsCheck() {
		log.info("Doing Systems Check...");
		Sabnzbd sab = (Sabnzbd) ctx.getBean("Sabnzbd");
		sab.testIntegration();

		XmlShowStorage xml = (XmlShowStorage) ctx.getBean("XmlShowStorage");
		xml.testParsing();

		TvRage tvrage = (TvRage) ctx.getBean("TvRage");
		for (Show show : xml.getItems()) {
			tvrage.testIntegration(show);
		}

		log.info("Systems check completed successfully!");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NzbDownloadStarter starter = new NzbDownloadStarter(args);
		starter.actionRouter();
	}

	private void initCLI() {
		parser = new PosixParser();

		options = new Options();
		options.addOption("d", "dry-run", false,
				"Dry run, do everything except contact SABnzbd and won't update shows.xml");
		options.addOption("c", "check-configuration", false,
				"Test SABnzbd integration and parsing of shows.xml with TVRage lookups");
		options.addOption("s", "shows-file", true,
				"Use specified xml file instead of shows.xml");
		options.addOption("w", "wipe-cache", false,
				"Wipe cache for TVRage and RSS feeds");
		options.addOption("k", "download-cart", false,
				"Download enqueued items in cart");
		options.addOption("h", "help", false, "Print (this) help page");
		options.addOption("v", "verbose", false,
				"Verbose, print all logging statments to stdout");

	}
}
