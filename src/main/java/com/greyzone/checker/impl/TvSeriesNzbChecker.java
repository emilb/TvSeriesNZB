package com.greyzone.checker.impl;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.greyzone.domain.tv.Episode;
import com.greyzone.domain.tv.Show;
import com.greyzone.exceptions.AuthenticationException;
import com.greyzone.indexsearch.IndexSearcher;
import com.greyzone.integration.IntegrationDownloader;
import com.greyzone.scraper.TvScraper;
import com.greyzone.settings.ApplicationSettings;
import com.greyzone.storage.Storage;

@Service("TvSeriesNzbChecker")
public class TvSeriesNzbChecker {

	@Autowired
	@Qualifier("NzbsOrgRssFeedReader")
	private IndexSearcher rssSearcher;

	@Autowired
	@Qualifier("NzbsOrgSearcher")
	private IndexSearcher webSearcher;

	@Autowired
	private TvScraper tvScraper;

	@Autowired
	private IntegrationDownloader integrationDownloader;

	@Autowired
	@Qualifier("XmlShowStorage")
	private Storage<Show> xmlShowStorage;

	@Autowired
	private ApplicationSettings appSettings;

	private final Logger log = Logger.getLogger(this.getClass());

	public void checkForDownloads() {

		// Get wanted shows from storage
		List<Show> shows = xmlShowStorage.getItems();

		for (Show show : shows) {
			if (show.isEnded()) {
				// TODO: Check if all episodes have been downloaded first
				log.debug(show.getName() + " is ended or canceled, skipping.");
				continue;
			}

			log.debug("Checking for new episodes in show: " + show.getName());
			try {
				List<Episode> episodes = tvScraper.getAllUnseenEpisodes(show);

				if (episodes == null || episodes.size() == 0) {
					log.debug("Did not find any unseen episodes for show: "
							+ show.getName());
					continue;
				}

				log.debug("Found " + episodes.size()
						+ " episodes that might have been aired.");

				// Search in RSS feed
				List<Episode> episodesWithDownloadUri = rssSearcher.getIndexIds(
						show, episodes);

				// Complement search with Web scrape
				episodesWithDownloadUri.addAll(webSearcher.getIndexIds(show, episodes));
				
				// Sort
				Collections.sort(episodesWithDownloadUri);
				
				log.debug("Ordering dowload of " + episodesWithDownloadUri.size()
						+ " episodes.");

				Episode lastSuccessfullyDownloadedEpisode = null;

				for (Episode ep : episodesWithDownloadUri) {
					try {
						log.info(":) Downloading " + ep);
						integrationDownloader.orderDownloadByEpisode(ep);
						lastSuccessfullyDownloadedEpisode = ep;
					} catch (Exception e) {
						log.error("Failed to download episode: " + ep, e);
					}
				}

				// Warn only if show has been aired
				if (episodesWithDownloadUri.size() != episodes.size()) {

					Date now = getCurrentDateWithNoTime();
					
					// Find any episodes that definitely have been aired but
					// were not found at nzb provider
					for (Episode ep : episodes) {
						
						if (StringUtils.isEmpty(ep.getNzbFileUri())
								&& ep.getDateAired() != null) {
							
							if (ep.getDateAired().before(now)
									&& !episodesWithDownloadUri.contains(ep)) {
								log.warn(":( " + ep
										+ " was not found, manual download might be needed.");
							}
						}
					}
				}

				// Update last seen show
				if (lastSuccessfullyDownloadedEpisode != null) {
					log.debug("Updating last downloaded episode and season for "
							+ show.getName());
					show.setLastDownloadedEpisode(lastSuccessfullyDownloadedEpisode
							.getEpisodeNo());
					show.setCurrentlyWatchingSeason(lastSuccessfullyDownloadedEpisode
							.getSeason());
				}

			} catch (AuthenticationException ae) {
				log.error(ae.getMessage());
				throw new RuntimeException("Fatal exception, exit.");
			} catch (Exception e) {
				log.error("Something failed when checking/downloading show "
						+ show.getName() + " Err: " + e.getMessage(), e);
			}
		}

		// Save updated state only if not in Dry run mode
		if (!appSettings.isDryRun()) {
			xmlShowStorage.storeItems(shows);
		}
	}

	private Date getCurrentDateWithNoTime() {
		// Get Calendar object set to the date and time of the given Date object  
		Calendar cal = Calendar.getInstance();  
		  
		// Set time fields to zero  
		cal.set(Calendar.HOUR_OF_DAY, 0);  
		cal.set(Calendar.MINUTE, 0);  
		cal.set(Calendar.SECOND, 0);  
		cal.set(Calendar.MILLISECOND, 0);
		
		return cal.getTime();
	}
}
