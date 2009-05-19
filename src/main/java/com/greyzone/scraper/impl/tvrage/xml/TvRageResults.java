package com.greyzone.scraper.impl.tvrage.xml;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("Results")
public class TvRageResults {

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
