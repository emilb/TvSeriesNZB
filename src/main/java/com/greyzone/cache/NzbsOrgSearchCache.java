package com.greyzone.cache;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greyzone.settings.ApplicationSettings;

@Service("NzbsOrgSearchCache")
public class NzbsOrgSearchCache {
	
	private Cache<String, String> cache;
	
	@Autowired
	public NzbsOrgSearchCache(CacheManager cacheManager, ApplicationSettings settings) {
		cacheManager.defineConfiguration("nzbsorg-search-cache", new ConfigurationBuilder()
				.expiration().lifespan(settings.getNzbsOrgCacheLifespan())
				.loaders().addFileCacheStore().fetchPersistentState(true)
				.location(settings.getCacheLocation())
				.build());
		
		cache = cacheManager.getCache("nzbsorg-search-cache");
	}
	
	public void clearCache() {
		cache.clear();
	}
	
	public boolean isInCache(String id) {
		return cache.containsKey(id);
	}
	
	public String getFromCache(String id) {
		return cache.get(id);
	}
	
	public void put(String id, String html) {
		cache.put(id, html);
	}
}
