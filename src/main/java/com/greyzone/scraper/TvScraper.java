package com.greyzone.scraper;

import java.util.List;

import com.greyzone.domain.Episode;
import com.greyzone.domain.Show;

public interface TvScraper {

	public List<Episode> getAllUnseenEpisodes(Show show);

	public List<Episode> getEpisodesForSeason(Show show, String season);

	public List<Episode> getAllAvailableEpisodes(Show show);

	public void testIntegration(Show show);
}
