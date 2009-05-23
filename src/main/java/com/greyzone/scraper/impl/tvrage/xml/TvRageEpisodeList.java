package com.greyzone.scraper.impl.tvrage.xml;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("Episodelist")
public class TvRageEpisodeList {

	@XStreamImplicit
	private List<TvRageSeason> seasons;

	public List<TvRageSeason> getSeasons() {
		return seasons;
	}

	public void setSeasons(List<TvRageSeason> seasons) {
		this.seasons = seasons;
	}

}
