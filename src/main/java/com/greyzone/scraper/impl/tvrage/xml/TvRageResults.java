package com.greyzone.scraper.impl.tvrage.xml;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("Results")
public class TvRageResults implements Serializable {

	private static final long serialVersionUID = -7686944724429880071L;

	@XStreamImplicit
	@XStreamAlias("show")
	private List<TvRageResultShow> shows;

	public List<TvRageResultShow> getShows() {
		return shows;
	}

	public void setShows(List<TvRageResultShow> shows) {
		this.shows = shows;
	}

}
