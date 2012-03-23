package com.greyzone.cache;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greyzone.scraper.impl.tvrage.xml.TvRageShow;
import com.greyzone.settings.ApplicationSettings;

@Service("TvRageCache")
public class TvRageCache {
	
	private Cache<String, TvRageShow> cache;
	
	@Autowired
	public TvRageCache(CacheManager cacheManager, ApplicationSettings settings) {
		cacheManager.defineConfiguration("tvrage-cache", new ConfigurationBuilder()
				.expiration().lifespan(settings.getTvRageCacheLifespan())
				.loaders().addFileCacheStore().fetchPersistentState(true)
				.location(settings.getCacheLocation())
				.build());
		
		cache = cacheManager.getCache("tvrage-cache");
	}

	public void clearCache() {
		cache.clear();
	}
	
	public boolean isInCache(String id) {
		return cache.containsKey(id);
	}
	
	public TvRageShow getFromCache(String id) {
		return cache.get(id);
	}
	
	public void put(String id, TvRageShow show) {
		cache.put(id, show);
	}
}
