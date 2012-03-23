package com.greyzone.scraper.impl.tvrage.xml;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("episode")
public class TvRageEpisode implements Serializable {

	private static final long serialVersionUID = -5289889858476354795L;

	private String epnum;
	private String seasonnum;
	private String prodnum;
	private String airdate;
	private String link;
	private String title;

	public String getEpnum() {
		return epnum;
	}

	public void setEpnum(String epnum) {
		this.epnum = epnum;
	}

	public String getSeasonnum() {
		return seasonnum;
	}

	public void setSeasonnum(String seasonnum) {
		this.seasonnum = seasonnum;
	}

	public String getProdnum() {
		return prodnum;
	}

	public void setProdnum(String prodnum) {
		this.prodnum = prodnum;
	}

	public String getAirdate() {
		return airdate;
	}

	public void setAirdate(String airdate) {
		this.airdate = airdate;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
