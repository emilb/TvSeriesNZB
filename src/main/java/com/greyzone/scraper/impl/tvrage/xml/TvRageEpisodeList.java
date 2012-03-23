package com.greyzone.scraper.impl.tvrage.xml;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("Episodelist")
public class TvRageEpisodeList implements Serializable {

	private static final long serialVersionUID = -692048188953029415L;

	@XStreamImplicit
	private List<TvRageSeason> seasons;

	public List<TvRageSeason> getSeasons() {
		return seasons;
	}

	public void setSeasons(List<TvRageSeason> seasons) {
		this.seasons = seasons;
	}

}
