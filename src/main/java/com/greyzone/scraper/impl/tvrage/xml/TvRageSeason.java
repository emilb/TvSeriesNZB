package com.greyzone.scraper.impl.tvrage.xml;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("Season")
public class TvRageSeason implements Serializable {

	private static final long serialVersionUID = -5397017193547510819L;

	@XStreamAsAttribute
	private String no;

	@XStreamImplicit
	@XStreamAlias("episode")
	private List<TvRageEpisode> episodes;

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public List<TvRageEpisode> getEpisodes() {
		return episodes;
	}

	public void setEpisodes(List<TvRageEpisode> episodes) {
		this.episodes = episodes;
	}
}
