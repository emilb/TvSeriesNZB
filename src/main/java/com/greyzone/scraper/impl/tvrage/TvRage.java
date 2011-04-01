package com.greyzone.scraper.impl.tvrage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.greyzone.domain.tv.Episode;
import com.greyzone.domain.tv.Show;
import com.greyzone.scraper.TvScraper;
import com.greyzone.scraper.impl.tvrage.xml.TvRageEpisode;
import com.greyzone.scraper.impl.tvrage.xml.TvRageEpisodeList;
import com.greyzone.scraper.impl.tvrage.xml.TvRageResultShow;
import com.greyzone.scraper.impl.tvrage.xml.TvRageResults;
import com.greyzone.scraper.impl.tvrage.xml.TvRageSeason;
import com.greyzone.scraper.impl.tvrage.xml.TvRageShow;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;
import com.thoughtworks.xstream.mapper.MapperWrapper;

@Service("TvRage")
public class TvRage implements TvScraper {

    private final Logger           log = Logger.getLogger(this.getClass());
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<Episode> getAllAvailableEpisodes(Show show) {
        if (StringUtils.isEmpty(show.getId())) {
            populateTvRageId(show);
        }

        try {

            log.debug("Searching TVRage available episodes for show: " + show.getName());

            TvRageShow tShow = (TvRageShow) queryTvRage("http://services.tvrage.com/feeds/episode_list.php",
                    new NameValuePair("sid", show.getId()));

            ArrayList<Episode> allEp = new ArrayList<Episode>();

            for (TvRageSeason season : tShow.getEpisodeList().getSeasons()) {

                for (TvRageEpisode episode : season.getEpisodes()) {
                    Date epDate = null;
                    try {
                        // Seems like TvRage uses 0000-00-00 (or variants
                        // thereof) to indicate an unaired show.
                        if (!(episode.getAirdate().indexOf("-00") > 0))
                            epDate = sdf.parse(episode.getAirdate());
                    } catch (Exception e) {}

                    if (epDate != null) {
                        allEp.add(new Episode(tShow.getName(), season.getNo(), episode.getSeasonnum(),
                                episode.getTitle(), epDate));
                    } else {
                        allEp.add(new Episode(tShow.getName(), season.getNo(), episode.getSeasonnum(),
                                episode.getTitle()));
                    }

                }
            }

            return allEp;

        } catch (Exception e) {
            log.error("Failed to search TVRage available episodes!", e);
        }

        return null;
    }

    @Override
    public List<Episode> getAllUnseenEpisodes(Show show) {
        List<Episode> allEp = getAllAvailableEpisodes(show);
        ArrayList<Episode> unseenEp = new ArrayList<Episode>();

        for (Episode ep : allEp) {

            if ((ep.getSeason().compareTo(show.getCurrentlyWatchingSeason()) == 0 && ep.getEpisodeNo()
                    .compareTo(show.getLastDownloadedEpisode()) > 0)
                    || (ep.getSeason().compareTo(show.getCurrentlyWatchingSeason()) > 0)) {

                // Only add the episode if it really has been aired
                if (ep.getDateAired() != null && ep.getDateAired().before(new Date())) {
                    unseenEp.add(ep);
                }

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

    @Override
    public void testIntegration(Show show) {
        log.info("Testing TVRage integration for show: " + show.getName() + "...");
        populateTvRageId(show);
        log.info("... TVRate integration for show: " + show.getName() + " worked fine!");
    }

    private void populateTvRageId(Show show) {
        try {
            log.debug("Updating TVRage ID for show: " + show.getName());

            TvRageResults results = (TvRageResults) queryTvRage(
                    "http://services.tvrage.com/feeds/search.php", new NameValuePair("show", show.getName()));
            for (TvRageResultShow rShow : results.getShows()) {
                if (StringUtils.equalsIgnoreCase(show.getName(), rShow.getName())) {
                    show.setId(rShow.getShowid());
                    show.setName(rShow.getName());
                    show.setStatus(rShow.getStatus());
                    return;
                }
            }

            // No show found
            throw new RuntimeException("Failed to find show: " + show.getName());

        } catch (Exception e) {
            log.error("Failed to update TVRage ID for show: " + show.getName());
            throw new RuntimeException("TVRage failed to get ID", e);
        }

    }

    private Object queryTvRage(String url, NameValuePair... parameters) throws Exception {
        HttpClient client = new HttpClient();

        HttpMethod method = new GetMethod(url);
        method.setQueryString(parameters);
        client.executeMethod(method);
        String resp = method.getResponseBodyAsString();

        XStream xstream = new XStream() {
        	// Skip unknown tags
        	protected MapperWrapper wrapMapper(MapperWrapper next) {
                return new MapperWrapper(next) {
                	@SuppressWarnings("rawtypes")
                    public boolean shouldSerializeMember(Class definedIn, String fieldName) {
                        try {
                            return definedIn != Object.class || realClass(fieldName) != null;
                        } catch(CannotResolveClassException cnrce) {
                            return false;
                        }
                    }
                };
            }
        };
        xstream.autodetectAnnotations(true);

        // Register top objects
        xstream.alias("Show", TvRageShow.class);
        xstream.alias("Results", TvRageResults.class);

        // Omit certain xml tags
        xstream.omitField(TvRageResultShow.class, "genres");
        xstream.omitField(TvRageEpisodeList.class, "Special");

        return xstream.fromXML(resp);
    }
}
