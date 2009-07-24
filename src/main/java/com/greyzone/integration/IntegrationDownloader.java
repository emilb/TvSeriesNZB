package com.greyzone.integration;

import java.util.List;

import com.greyzone.domain.tv.Episode;

public interface IntegrationDownloader {

	public void orderDownloadByIds(List<String> ids);

	public void orderDownloadByEpisode(Episode ep);

	public void testIntegration();
}
