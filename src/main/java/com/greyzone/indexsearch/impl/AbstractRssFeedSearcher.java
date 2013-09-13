package com.greyzone.indexsearch.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.greyzone.util.HttpUtils;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.greyzone.cache.NzbsOrgRssFeedCache;
import com.greyzone.domain.tv.Episode;
import com.greyzone.domain.tv.Show;
import com.greyzone.indexsearch.IndexSearcher;
import com.greyzone.settings.ApplicationSettings;
import com.greyzone.util.FuzzyStringUtils;
import com.sun.syndication.feed.synd.SyndEnclosure;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;

public abstract class AbstractRssFeedSearcher implements IndexSearcher {

	@Autowired
	private ApplicationSettings settings;

	@Autowired
	private NzbsOrgRssFeedCache cache;
	
	private final Logger log = Logger.getLogger(this.getClass());

    @Override
    public List<Episode> getIndexIds(Show show, List<Episode> episodes) {
        List<Episode> result = new ArrayList<Episode>();
        List<SearchResultItem> rssList = getRssFeed(show, episodes);

        try {

            for (Episode episode : episodes) {

                // Download for this episode might have already been found
                if (StringUtils.isNotBlank(episode.getNzbFileUri()))
                    continue;

                boolean foundLastEntry = false;

                for (SearchResultItem entry : rssList) {

                    if (entry.isMatch(episode)) {
                        episode.setNzbFileUri(entry.url);
                        result.add(episode);
                        foundLastEntry = true;
                    }

                    if (foundLastEntry)
                        break;
                }

                if (!foundLastEntry) {
                    log.debug("Could not find " + episode + " in rss feed search");
                }
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

	protected abstract String getRssFeedUrl(Show show, List<Episode> episodes);
	
	private List<SearchResultItem> getRssFeed(Show show, List<Episode> episodes) {
		
		String feedUrl = getRssFeedUrl(show, episodes);
        log.debug("feedUrl: " + feedUrl);
		if (cache.isInCache(feedUrl)) {
			return cache.getFromCache(feedUrl);
		}

		try {

            List<SearchResultItem> searchResultItems = new ArrayList<SearchResultItem>();

            URL rssUrl = new URL(feedUrl);
            List<Object> items = JsonPath.read(rssUrl, "$..item");
            for (Object o : items) {
                JSONObject jo = (JSONObject)o;

                String title = (String)jo.get("title");
                String link = (String)jo.get("link");

                String season = getAttribute("season", jo);
                String episode = getAttribute("episode", jo);
                String rageId = getAttribute("rageid", jo);
                String tvtitle = getAttribute("tvtitle", jo);

                searchResultItems.add(new SearchResultItem(title, link, rageId, episode, season));
            }
            cache.put(settings.getNzbsOrgFeed(), searchResultItems);

            return searchResultItems;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unused")
	private byte[] getNzbFile(String uri) throws Exception {

		URL url = new URL(uri);

		ByteArrayOutputStream bais = new ByteArrayOutputStream();
		InputStream is = null;
		try {
			is = url.openStream();
			byte[] byteChunk = new byte[4096]; // Or whatever size you want to
												// read in at a time.
			int n;

			while ((n = is.read(byteChunk)) > 0) {
				bais.write(byteChunk, 0, n);
			}
		} finally {
			if (is != null) {
				is.close();
			}
		}

		return bais.toByteArray();

	}

    private String getAttribute(String attribute, JSONObject jsonObject) {
        JSONArray jArr = (JSONArray)JsonPath.read(jsonObject, "$.attr.*[?(@.name == '" + attribute + "')].value");
        if (jArr != null && jArr.size() > 0) {
            return (String)jArr.get(0);
        }
        return null;
    }


}
