package com.greyzone.scraper;

import java.util.List;

import com.greyzone.domain.tv.Episode;
import com.greyzone.domain.tv.Show;

public interface TvScraper {

	public List<Episode> getAllUnseenEpisodes(Show show);

	public List<Episode> getEpisodesForSeason(Show show, String season);

	public List<Episode> getAllAvailableEpisodes(Show show);

	public void testIntegration(Show show);
}
