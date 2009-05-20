package com.greyzone.domain;


public class Episode {
	private String show;
	private String season;
	private String episodeNo;
	private String episodeName;
	private String indexId;
	
	public Episode() {
		
	}
	
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
	
	public String getIndexId() {
		return indexId;
	}

	public void setIndexId(String indexId) {
		this.indexId = indexId;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(show); sb.append(" ");
		sb.append(season); sb.append("x"); sb.append(episodeNo); sb.append(" ");
		sb.append(episodeName);
		
		return sb.toString();
	}
}
