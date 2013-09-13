package com.greyzone.indexsearch.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greyzone.domain.tv.Episode;
import com.greyzone.domain.tv.Show;
import com.greyzone.settings.ApplicationSettings;

@Service("NzbsOrgSearcher")
public class NzbsOrgSearcher extends AbstractRssFeedSearcher {

	@Autowired
	private ApplicationSettings settings;

	private final Logger log = Logger.getLogger(this.getClass());
	
	@Override
	protected String getRssFeedUrl(Show show, List<Episode> episodes) {
		String feedUrl = settings.getNzbsOrgSearch() +
				"&" + createKeyValuePair("rid", show.getId()) +
				"&" + createKeyValuePair("cat", settings.getNzbsOrgCategory()) +
				"&" + createKeyValuePair("num", "100") +
				"&" + createKeyValuePair("apikey", settings.getNzbsOrgApiKey()) +
                "&" + createKeyValuePair("o", "json") +
                "&" + createKeyValuePair("extended", "1");
		
		log.debug("Search feed: " + feedUrl);
		return feedUrl;
	}
	
	private String createKeyValuePair(String key, String value) {
		return key + "=" + value;
	}
}
