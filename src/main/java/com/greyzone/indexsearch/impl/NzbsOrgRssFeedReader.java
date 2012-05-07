package com.greyzone.indexsearch.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greyzone.domain.tv.Episode;
import com.greyzone.domain.tv.Show;
import com.greyzone.settings.ApplicationSettings;

@Service("NzbsOrgRssFeedReader")
public class NzbsOrgRssFeedReader extends AbstractRssFeedSearcher {

	@Autowired
	private ApplicationSettings settings;

	@Override
	protected String getRssFeedUrl(Show show, List<Episode> episodes) {
		return settings.getNzbsOrgFeed();
	}
}
