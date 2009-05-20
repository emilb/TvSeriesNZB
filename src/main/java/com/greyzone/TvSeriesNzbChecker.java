package com.greyzone;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greyzone.domain.Episode;
import com.greyzone.domain.Show;
import com.greyzone.indexsearch.IndexSearcher;
import com.greyzone.integration.IntegrationDownloader;
import com.greyzone.scraper.TvScraper;
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
	
	private Logger log = Logger.getLogger(this.getClass());
	
	public void checkForDownloads() {
		
		// Get wanted shows from storage
		List<Show> shows = showStorage.getShows();
		
		for (Show show : shows) {
			log.debug("Checking for new episodes in show: " + show.getName());
			try {
				List<Episode> episodes = tvScraper.getAllUnseenEpisodes(show);
				if (episodes != null && episodes.size() > 0) {
					log.debug("Found " + episodes.size() + " episodes that might have been aired.");
					
					// Search
					List<Episode> episodesWithIndexId = indexSearcher.getIndexIds(show, episodes);
					
					log.debug("Ordering dowload of " + episodesWithIndexId.size() + " episodes.");
					
					Episode lastSuccessfullyDownloadedEpisode = null;
					
					for (Episode ep : episodesWithIndexId) {
						try {
							integrationDownloader.orderDownloadByEpisode(ep);
							lastSuccessfullyDownloadedEpisode = ep;
						} catch (Exception e) {
							log.error("Failed to download episode: " + ep, e);
						}
					}
					
					if (episodesWithIndexId != null && episodesWithIndexId.size() != episodes.size()) {
						log.warn("Failed to find all unseen episodes for " + show.getName() + " to download. Manual download may be required!");
					}
					
					// Update last seen show
					if (lastSuccessfullyDownloadedEpisode != null) {
						log.debug("Updating last downloaded episode and season for " + show.getName());
						show.setLastDownloadedEpisode(lastSuccessfullyDownloadedEpisode.getEpisodeNo());
						show.setCurrentlyWatchingSeason(lastSuccessfullyDownloadedEpisode.getSeason());
					}
				}
			} catch (Exception e) {
				log.error("Something failed when checking/downloading show " + show.getName());
			}
		}
		
		// Save updated state
		showStorage.storeShows(shows);
	}
}
