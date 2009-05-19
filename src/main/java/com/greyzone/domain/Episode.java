package com.greyzone.domain;

import org.apache.commons.lang.StringUtils;

public class Episode {
	private String show;
	private String season;
	private String episodeNo;
	private String episodeName;
	
	
	
	public Episode(String show, String season, String episodeNo, String episodeName) {
		this.show = show;
		this.season = season;
		this.episodeNo = episodeNo;
		this.episodeName = episodeName;
	}
	
	public String getShow() {
		return show;
	}
	public void setShow(String show) {
		this.show = show;
	}
	public String getSeason() {
		return season;
	}
	public void setSeason(String season) {
		this.season = season;
	}
	public String getEpisodeNo() {
		return episodeNo;
	}
	public void setEpisodeNo(String episodeNo) {
		this.episodeNo = episodeNo;
	}
	public String getEpisodeName() {
		return episodeName;
	}
	public void setEpisodeName(String episodeName) {
		this.episodeName = episodeName;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(show); sb.append(" ");
		sb.append("Season: "); sb.append(season); sb.append(" ");
		sb.append("Episode: "); sb.append(episodeNo); sb.append(" ");
		sb.append("Title: "); sb.append(episodeName); sb.append(" ");
		
		return sb.toString();
	}
}
