package com.greyzone.cache;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.infinispan.manager.DefaultCacheManager;
import org.springframework.stereotype.Service;

@Service
public class CacheManager extends DefaultCacheManager {

	private final Logger log = Logger.getLogger(this.getClass());

	@PostConstruct
	public void startup() {
		log.debug("Starting cachemanager");
		this.start();
	}

	@PreDestroy
	public void shutdown() {
		log.debug("Shutting down cachemanager");
		this.stop();
	}
}
