package com.greyzone.integration;

import com.greyzone.domain.tv.Episode;
import com.sun.syndication.feed.synd.SyndEntry;

public interface IntegrationDownloader {

	public void orderDownloadByItem(SyndEntry entry);
	
    public void orderDownloadByUrl(String url, String name, String category);

    public void orderDownloadByEpisode(Episode ep);

    public void testIntegration();
}
