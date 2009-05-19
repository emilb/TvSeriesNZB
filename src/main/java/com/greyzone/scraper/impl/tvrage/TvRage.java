package com.greyzone.scraper.impl.tvrage;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.omg.CORBA.NamedValue;
import org.springframework.stereotype.Service;

import com.greyzone.domain.Episode;
import com.greyzone.domain.Show;
import com.greyzone.scraper.TvScraper;
import com.greyzone.scraper.impl.tvrage.xml.TvRageEpisode;
import com.greyzone.scraper.impl.tvrage.xml.TvRageEpisodeList;
import com.greyzone.scraper.impl.tvrage.xml.TvRageResultShow;
import com.greyzone.scraper.impl.tvrage.xml.TvRageResults;
import com.greyzone.scraper.impl.tvrage.xml.TvRageSeason;
import com.greyzone.scraper.impl.tvrage.xml.TvRageShow;
import com.thoughtworks.xstream.XStream;

@Service
public class TvRage implements TvScraper {

	@Override
	public List<Episode> getAllAvailableEpisodes(Show show) {
		if (StringUtils.isEmpty(show.getId())) {
			populateTvRageId(show);
		}
		
		try {
			TvRageShow tShow = (TvRageShow) queryTvRage("http://services.tvrage.com/feeds/episode_list.php", new NameValuePair("sid", show.getId()));

			ArrayList<Episode> allEp = new ArrayList<Episode>();
			
			for (TvRageSeason season : tShow.getEpisodeList().getSeasons()) {
				
				for (TvRageEpisode episode : season.getEpisodes()) {
					allEp.add(new Episode(tShow.getName(), season.getNo(), episode.getSeasonnum(), episode.getTitle()));
				}
			}
			
			return allEp;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public List<Episode> getAllUnseenEpisodes(Show show) {
		List<Episode> allEp = getAllAvailableEpisodes(show);
		ArrayList<Episode> unseenEp = new ArrayList<Episode>();
		
		for (Episode ep : allEp) {
			if (   (ep.getSeason().compareTo(show.getCurrentlyWatchingSeason()) == 0 &&
					ep.getEpisodeNo().compareTo(show.getLastDownloadedEpisode()) > 0) ||
					(ep.getSeason().compareTo(show.getCurrentlyWatchingSeason()) > 0)) {
				unseenEp.add(ep);
			}
		}
		
		return unseenEp;
	}

	@Override
	public List<Episode> getEpisodesForSeason(Show show, String season) {
		List<Episode> allEp = getAllAvailableEpisodes(show);
		ArrayList<Episode> seasonEp = new ArrayList<Episode>();
		
		for (Episode ep : allEp) {
			if (ep.getSeason().equals(season)) {
				seasonEp.add(ep);
			}
		}
		
		return seasonEp;
	}
	
	private void populateTvRageId(Show show) {
		try {
			TvRageResults results = (TvRageResults) queryTvRage("http://services.tvrage.com/feeds/search.php", new NameValuePair("show", show.getName()));
			for (TvRageResultShow rShow : results.getShows()) {
				if (StringUtils.equalsIgnoreCase(show.getName(), rShow.getName())) {
					show.setId(rShow.getShowid());
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private Object queryTvRage(String url, NameValuePair... parameters) throws Exception {
		HttpClient client = new HttpClient();
		
		// "http://services.tvrage.com/feeds/episode_list.php?sid=3908"
		HttpMethod method = new GetMethod(url);
		method.setQueryString(parameters);
		client.executeMethod(method);
		String resp = method.getResponseBodyAsString();
		
		XStream xstream = new XStream();
		xstream.autodetectAnnotations(true);
		
		// Register top objects
		xstream.alias("Show", TvRageShow.class);
		xstream.alias("Results", TvRageResults.class);

		// Omit certain xml tags
		xstream.omitField(TvRageResultShow.class, "genres");
		xstream.omitField(TvRageEpisodeList.class, "Special");
		
		return xstream.fromXML(resp);
	}

	public static void main(String[] args) {
		TvRage t = new TvRage();
		try {
			t.test();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void test() throws Exception {
			
	
		System.out.println("\n\nTvRage show id:");
		Show s = new Show();
		s.setName("house");
		populateTvRageId(s);
		System.out.println(s.getName() + " id: " + s.getId());
		
		System.out.println("\n\nAll episodes:");
		List<Episode> allEp = getAllAvailableEpisodes(s);
		for (Episode ep : allEp) {
			System.out.println(ep.getSeason() + " " + ep.getEpisodeNo() + " " + ep.getEpisodeName());
		}
		
		System.out.println("\n\nUnseen after season 4, ep 5:");
		s.setCurrentlyWatchingSeason("4");
		s.setLastDownloadedEpisode("05");
		List<Episode> unseenEp = getAllUnseenEpisodes(s);
		for (Episode ep : unseenEp) {
			System.out.println(ep.getSeason() + " " + ep.getEpisodeNo() + " " + ep.getEpisodeName());
		}
	}
}
