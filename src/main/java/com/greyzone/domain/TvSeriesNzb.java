package com.greyzone.domain;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("TvSeriesNzb")
public class TvSeriesNzb {

	@XStreamImplicit
	@XStreamAlias("Shows")
	private List<Show> shows;

	public List<Show> getShows() {
		return shows;
	}

	public void setShows(List<Show> shows) {
		this.shows = shows;
	}

}
