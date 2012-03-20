package com.greyzone.domain.tv;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.greyzone.util.ToStringStyleNewLine;
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

	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				new ToStringStyleNewLine());
	}
}
