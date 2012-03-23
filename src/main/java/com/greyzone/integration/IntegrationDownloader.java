package com.greyzone.integration;

import com.greyzone.domain.tv.Episode;

public interface IntegrationDownloader {

    public void orderDownloadByUrl(String url, String name);

    public void orderDownloadByEpisode(Episode ep);

    public void testIntegration();
}
