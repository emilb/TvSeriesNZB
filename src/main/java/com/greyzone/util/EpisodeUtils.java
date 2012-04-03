package com.greyzone.util;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;

import com.greyzone.domain.tv.Episode;

public class EpisodeUtils {

	public static long getHoursAgoEpisodeAired(Episode ep) {

		// Unknown air date
		if (ep.getDateAired() == null)
			return -1;

		DateTime airDate = new DateTime(ep.getDateAired().getTime());
		DateTime now = new DateTime();

		if (airDate.isAfter(now))
			return -1;

		Duration duration = new Interval(airDate, now).toDuration();
		
		return duration.getStandardHours();
	}
}
