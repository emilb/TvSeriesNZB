package com.greyzone.scraper.impl.tvrage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greyzone.cache.TvRageCache;
import com.greyzone.domain.tv.Episode;
import com.greyzone.domain.tv.Show;
import com.greyzone.scraper.TvScraper;
import com.greyzone.scraper.impl.tvrage.xml.TvRageEpisode;
import com.greyzone.scraper.impl.tvrage.xml.TvRageEpisodeList;
import com.greyzone.scraper.impl.tvrage.xml.TvRageResultShow;
import com.greyzone.scraper.impl.tvrage.xml.TvRageResults;
import com.greyzone.scraper.impl.tvrage.xml.TvRageSeason;
import com.greyzone.scraper.impl.tvrage.xml.TvRageShow;
import com.greyzone.settings.ApplicationSettings;
import com.greyzone.util.HttpUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;
import com.thoughtworks.xstream.mapper.MapperWrapper;

@Service("TvRage")
public class TvRage implements TvScraper {

    private final Logger           log = Logger.getLogger(this.getClass());
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    TvRageCache cache;
    
    @Autowired
	ApplicationSettings settings;
    
    @Override
    public List<Episode> getAllAvailableEpisodes(Show show) {
        if (StringUtils.isEmpty(show.getId())) {
            populateTvRageId(show);
        }

        try {

            log.debug("Searching TVRage available episodes for show: " + show.getName());

            TvRageShow tShow = null;
            if (cache.isInCache(show.getId())) {
            	tShow = cache.getFromCache(show.getId());
            } else {
            	tShow = (TvRageShow) queryTvRage("http://services.tvrage.com/feeds/episode_list.php",
            			new BasicNameValuePair("sid", show.getId()));
            	
            	cache.put(show.getId(), tShow);
            }
            
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
                                show.getId(),
                                episode.getTitle(), epDate));
                    } else {
                        allEp.add(new Episode(tShow.getName(), season.getNo(), episode.getSeasonnum(),
                                show.getId(), episode.getTitle()));
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
                    "http://services.tvrage.com/feeds/search.php", new BasicNameValuePair("show", show.getName()));
            for (TvRageResultShow rShow : results.getShows()) {
                if (StringUtils.equalsIgnoreCase(show.getName(), rShow.getName())) {
                    show.setId(rShow.getShowid());
                    show.setName(rShow.getName());
                    show.setStatus(rShow.getStatus());
                    log.debug(show.getName() + " has TVRage id: " + rShow.getShowid());
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
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, settings.getHttpClientUserAgent());

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (NameValuePair nvp : parameters) {
        	params.add(nvp);
        }
        
        String getUrl = HttpUtils.createGetURI(url, parameters);
        
        HttpGet method = new HttpGet(getUrl);
        
        HttpResponse response = client.execute(method);
        String resp = HttpUtils.getContentAsString(response.getEntity());
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
