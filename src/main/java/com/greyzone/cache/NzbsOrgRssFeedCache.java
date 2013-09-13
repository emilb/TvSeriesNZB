package com.greyzone.cache;

import java.util.List;

import com.greyzone.indexsearch.impl.AbstractRssFeedSearcher;
import com.greyzone.indexsearch.impl.SearchResultItem;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greyzone.settings.ApplicationSettings;
import com.sun.syndication.feed.synd.SyndEntry;

@Service("NzbsOrgRssFeedCache")
public class NzbsOrgRssFeedCache {
	
	private Cache<String, List<SearchResultItem>> cache;
	
	@Autowired
	public NzbsOrgRssFeedCache(CacheManager cacheManager, ApplicationSettings settings) {
		cacheManager.defineConfiguration("nzbsorg-rssfeed-cache", new ConfigurationBuilder()
				.expiration().lifespan(settings.getNzbsOrgCacheLifespan())
				.loaders().addFileCacheStore().fetchPersistentState(true)
				.location(settings.getCacheLocation())
				.build());
		
		cache = cacheManager.getCache("nzbsorg-rssfeed-cache");
	}
	
	public void clearCache() {
		cache.clear();
	}
	
	public boolean isInCache(String id) {
		return cache.containsKey(id);
	}
	
	public List<SearchResultItem> getFromCache(String id) {
		return cache.get(id);
	}
	
	public void put(String id, List<SearchResultItem> rss) {
		cache.put(id, rss);
	}
}
