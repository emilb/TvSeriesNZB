package com.greyzone.domain.tv;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.greyzone.util.FormatConverter;
import com.greyzone.util.QualityConverter;
import com.greyzone.util.ToStringStyleNewLine;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamAlias("show")
public class Show {

	@XStreamAlias("name")
	private String name;

	@XStreamAlias("season")
	private String currentlyWatchingSeason;

	@XStreamAlias("episode")
	private String lastDownloadedEpisode;

	@XStreamAlias("id")
	private String id;

	@XStreamAlias("status")
	private String status;

	@XStreamConverter(QualityConverter.class)
	private Quality quality;
	
	@XStreamConverter(FormatConverter.class)
	private Format format;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCurrentlyWatchingSeason() {
		return currentlyWatchingSeason;
	}

	public void setCurrentlyWatchingSeason(String currentlyWatchingSeason) {
		this.currentlyWatchingSeason = currentlyWatchingSeason;
	}

	public String getLastDownloadedEpisode() {

		// The episode counter must always be two digits (except for when it
		// is zero or empty. This prepends a zero should it be only one digit.
		if (StringUtils.isEmpty(lastDownloadedEpisode)) {
			lastDownloadedEpisode = "0";
		} else if (lastDownloadedEpisode.length() == 1) {
			lastDownloadedEpisode = "0" + lastDownloadedEpisode;
		}

		return lastDownloadedEpisode;
	}

	public void setLastDownloadedEpisode(String lastDownloadedEpisode) {
		this.lastDownloadedEpisode = lastDownloadedEpisode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Quality getQuality() {
		return quality;
	}

	public void setQuality(Quality quality) {
		this.quality = quality;
	}

	public Format getFormat() {
		return format;
	}

	public void setFormat(Format format) {
		this.format = format;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((currentlyWatchingSeason == null) ? 0
						: currentlyWatchingSeason.hashCode());
		result = prime * result + ((format == null) ? 0 : format.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((lastDownloadedEpisode == null) ? 0 : lastDownloadedEpisode
						.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((quality == null) ? 0 : quality.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Show other = (Show) obj;
		if (currentlyWatchingSeason == null) {
			if (other.currentlyWatchingSeason != null)
				return false;
		} else if (!currentlyWatchingSeason
				.equals(other.currentlyWatchingSeason))
			return false;
		if (format != other.format)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastDownloadedEpisode == null) {
			if (other.lastDownloadedEpisode != null)
				return false;
		} else if (!lastDownloadedEpisode.equals(other.lastDownloadedEpisode))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (quality != other.quality)
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				new ToStringStyleNewLine());
	}
}
