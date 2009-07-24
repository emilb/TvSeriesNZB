package com.greyzone;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greyzone.domain.tv.Episode;
import com.greyzone.domain.tv.Show;
import com.greyzone.exceptions.AuthenticationException;
import com.greyzone.indexsearch.IndexSearcher;
import com.greyzone.integration.IntegrationDownloader;
import com.greyzone.scraper.TvScraper;
import com.greyzone.settings.ApplicationSettings;
import com.greyzone.storage.ShowStorage;

@Service("TvSeriesNzbChecker")
public class TvSeriesNzbChecker {

	@Autowired
	private IndexSearcher indexSearcher;

	@Autowired
	private TvScraper tvScraper;

	@Autowired
	private IntegrationDownloader integrationDownloader;

	@Autowired
	private ShowStorage showStorage;

	@Autowired
	private ApplicationSettings appSettings;

	private Logger log = Logger.getLogger(this.getClass());

	public void checkForDownloads() {

		// Get wanted shows from storage
		List<Show> shows = showStorage.getShows();

		for (Show show : shows) {
			log.debug("Checking for new episodes in show: " + show.getName());
			try {
				List<Episode> episodes = tvScraper.getAllUnseenEpisodes(show);
				if (episodes != null && episodes.size() > 0) {
					log.debug("Found " + episodes.size()
							+ " episodes that might have been aired.");

					// Search
					List<Episode> episodesWithIndexId = indexSearcher
							.getIndexIds(show, episodes);

					log.debug("Ordering dowload of "
							+ episodesWithIndexId.size() + " episodes.");

					Episode lastSuccessfullyDownloadedEpisode = null;

					for (Episode ep : episodesWithIndexId) {
						try {
							log.info("Downloading " + ep + " (nzbid: "
									+ ep.getIndexId() + ")");
							integrationDownloader.orderDownloadByEpisode(ep);
							lastSuccessfullyDownloadedEpisode = ep;
						} catch (Exception e) {
							log.error("Failed to download episode: " + ep, e);
						}
					}

					// TODO: add datecheck, warn only if show has been aired
					if (episodesWithIndexId.size() != episodes.size()) {

						Date now = new Date();

						// Find any episodes that definately have been aired but
						// were not found
						// at newzbin
						for (Episode ep : episodes) {
							if (StringUtils.isEmpty(ep.getIndexId())
									&& ep.getDateAired() != null) {
								if (ep.getDateAired().before(now)) {
									log
											.warn(ep
													+ " was not found, manual download will be needed.");
								}
							}
						}
					}

					// Update last seen show
					if (lastSuccessfullyDownloadedEpisode != null) {
						log
								.debug("Updating last downloaded episode and season for "
										+ show.getName());
						show
								.setLastDownloadedEpisode(lastSuccessfullyDownloadedEpisode
										.getEpisodeNo());
						show
								.setCurrentlyWatchingSeason(lastSuccessfullyDownloadedEpisode
										.getSeason());
					}
				}
			} catch (AuthenticationException ae) {
				log.error(ae.getMessage());
				throw new RuntimeException("Fatal exception, exit.");
			} catch (Exception e) {
				log.error("Something failed when checking/downloading show "
						+ show.getName() + " Err: " + e.getMessage());
			}
		}

		// Save updated state only if not in Dry run mode
		if (!appSettings.isDryRun()) {
			showStorage.storeShows(shows);
		}
	}

	public void testShowCorrectness() {

	}
}
