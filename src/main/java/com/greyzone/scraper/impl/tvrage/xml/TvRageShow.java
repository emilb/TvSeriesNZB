package com.greyzone.scraper.impl.tvrage.xml;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("Show")
public class TvRageShow {

	private String name;
	private String totalseasons;

	@XStreamAlias("Episodelist")
	private TvRageEpisodeList episodeList;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTotalseasons() {
		return totalseasons;
	}

	public void setTotalseasons(String totalseasons) {
		this.totalseasons = totalseasons;
	}

	public TvRageEpisodeList getEpisodeList() {
		return episodeList;
	}

	public void setEpisodeList(TvRageEpisodeList episodeList) {
		this.episodeList = episodeList;
	}

}
