package com.greyzone.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Show")
public class Show {
	
	@XStreamAlias("Name")
	private String name;
	
	@XStreamAlias("Season")
	private String currentlyWatchingSeason;
	
	@XStreamAlias("Episode")
	private String lastDownloadedEpisode;
	
	@XStreamAlias("ID")
	private String id;
	
	@XStreamAlias("SearchSettings")
	private SearchSettings searchSettings;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCurrentlyWatchingSeason() {
		return currentlyWatchingSeason;
	}
	public void setCurrentlyWatchingSeason(String currentlyWatchingSeason) {
		this.currentlyWatchingSeason = currentlyWatchingSeason;
	}
	public String getLastDownloadedEpisode() {
		return lastDownloadedEpisode;
	}
	public void setLastDownloadedEpisode(String lastDownloadedEpisode) {
		this.lastDownloadedEpisode = lastDownloadedEpisode;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public SearchSettings getSearchSettings() {
		return searchSettings;
	}
	public void setSearchSettings(SearchSettings searchSettings) {
		this.searchSettings = searchSettings;
	}
	
	
	
}
