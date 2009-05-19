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
			log.debug("Checking show: " + show.getName());
			
			List<Episode> episodes = tvScraper.getAllUnseenEpisodes(show);
			if (episodes != null && episodes.size() > 0) {
				log.debug("Found " + episodes.size() + " noof episodes");
				
				// Search
				List<String> indexIds = indexSearcher.getIndexIds(show, episodes);
				
				log.debug("Ordering dowload of " + indexIds.size() + " episodes");
				
				// Order download
				if (indexIds != null && indexIds.size() > 0) {
					integrationDownloader.orderDownloadByIds(indexIds);
				}
				
				// Update last seen show
				log.debug("Storing new state");
				show.setLastDownloadedEpisode(episodes.get(episodes.size()-1).getEpisodeNo());
				show.setCurrentlyWatchingSeason(episodes.get(episodes.size()-1).getSeason());
			}
		}
		
		// Save updated state
		showStorage.storeShows(shows);
	}
}
