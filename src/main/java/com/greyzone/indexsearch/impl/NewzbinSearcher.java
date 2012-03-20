package com.greyzone.indexsearch.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import au.com.bytecode.opencsv.CSVReader;

import com.greyzone.domain.tv.Episode;
import com.greyzone.domain.tv.Show;
import com.greyzone.exceptions.AuthenticationException;
import com.greyzone.exceptions.NeedToTryAgainException;
import com.greyzone.exceptions.ServiceUnavailableException;
import com.greyzone.indexsearch.IndexSearcher;
import com.greyzone.settings.ApplicationSettings;

//@Service
public class NewzbinSearcher implements IndexSearcher {

    @Autowired
    ApplicationSettings  settings;

    private final Logger log = Logger.getLogger(this.getClass());

    @Override
    public List<Episode> getIndexIds(Show show, List<Episode> episodes) {
        List<Episode> result = new ArrayList<Episode>();

        for (Episode ep : episodes) {
            String id = null;
            try {
                id = getBestSearchMatch(show, ep, 1);
            } catch (AuthenticationException ae) {
                throw ae;
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (id != null) {
                ep.setIndexId(id);
                result.add(ep);
            }
        }

        return result;
    }

    private HttpClient createHttpClient() {
        HttpClient client = new HttpClient();
        Credentials credentials = new UsernamePasswordCredentials(settings.getNewzbinUsername(), settings
                .getNewzbinPassword());
        client.getState().setCredentials(AuthScope.ANY, credentials);
        return client;
    }

    /**
     * 
     * @param show
     * @param episode
     * @return the first hit in the search, null if no hit
     * @throws IOException
     */
    private String getBestSearchMatch(Show show, Episode episode, int loopcount) throws IOException {

        HttpMethod method = createSearch(show, episode);
        HttpClient client = createHttpClient();

        log.debug("Searching for: " + episode);

        client.executeMethod(method);

        try {
            String nzbId = parseSearchResult(method, loopcount);
            if (nzbId == null) {
                log.debug("NO hits for:   " + episode);
                return null;
            }

            log.debug("FOUND NZB for: " + episode);

            return nzbId;

        } catch (NeedToTryAgainException tryAgain) {
            return getBestSearchMatch(show, episode, loopcount++);
        }
    }

    private String parseSearchResult(HttpMethod method, int loopCount) throws IOException {

        /*
         * This might be a litte crude. If newzbin replies with an HTML response it is most likely because we
         * have made too many searches but other issues might be the cause for this as well. Hence the
         * loopcount, if it exceeds 3 something else is wrong and it is time to abort.
         */
        if (method.getResponseHeader("Content-type").getValue().startsWith("text/html")) {

            // Wait for 3 seconds
            log.debug("Too many searches to Newzbin, need to wait for 5 seconds.");

            try {
                Thread.sleep(5500);
            } catch (InterruptedException e) {}

            // We have tried too many times, something is wrong with newzbin!
            if (loopCount > 3) {
                log.error("Could not get a proper searchresult from Newzbin despite " + loopCount
                        + " retries.");
                log.error("Response from Newzbin:\n" + method.getResponseBodyAsString());

                throw new ServiceUnavailableException(
                        "Could not get a proper searchresult from Newzbin despite " + loopCount + " retries.");
            }

            throw new NeedToTryAgainException();

        }

        if (method.getResponseHeader("Content-type").getValue().startsWith("text/csv")) {
            CSVReader reader = new CSVReader(new BufferedReader(new InputStreamReader(method
                    .getResponseBodyAsStream())));

            String[] firstLine = reader.readNext();

            if (firstLine != null && firstLine.length > 1) {

                return firstLine[1];
            }

            // Check if authentication to newzbin failed
            if (firstLine != null && firstLine.length == 1) {
                if (firstLine[0].startsWith("Error: Login Required")) {
                    throw new AuthenticationException(
                            "Failed to authenticate at Newzbin. Check configuration.");
                }
            }
        }

        return null;
    }

    /**
     * 
     * @param show
     * @param episode
     * @return the method to invoke the search at Newzbin
     */
    private HttpMethod createSearch(Show show, Episode episode) {
        GetMethod get = new GetMethod("http://v3.newzbin.com/search/");

        List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
        queryParams.add(new NameValuePair("q", createSearchQuery(show, episode)));
        queryParams.add(new NameValuePair("searchaction", "Search"));
        queryParams.add(new NameValuePair("fpn", "p"));
        queryParams.add(new NameValuePair("category", "8"));
        queryParams.add(new NameValuePair("area", "-1"));
        queryParams.add(new NameValuePair("u_nfo_posts_only", "0"));
        queryParams.add(new NameValuePair("u_comment_posts_only", "0"));
        queryParams.add(new NameValuePair("u_url_posts_only", "0"));
        queryParams.add(new NameValuePair("sort", "ps_edit_date"));
        queryParams.add(new NameValuePair("order", "desc"));
        queryParams.add(new NameValuePair("areadone", "-1"));
        queryParams.add(new NameValuePair("feed", "csv"));

        NameValuePair[] valuePairs = queryParams.toArray(new NameValuePair[] {});

        get.setQueryString(valuePairs);

        return get;
    }

    /**
     * 
     * @param show
     * @param episode
     * @return The query with any extra search attributes
     */
    private String createSearchQuery(Show show, Episode episode) {
        StringBuilder sb = new StringBuilder();
        sb.append("^");
        sb.append(show.getName());
        sb.append(" ");
        sb.append(episode.getSeason());
        sb.append("x");
        sb.append(episode.getEpisodeNo());
        sb.append(" ");

        /*
        if (show.getSearchSettings() != null) {
            // List<String> searchAttributes = show.getSearchSettings()
            // .getAllSearchAttributes();
            // for (String attrib : searchAttributes) {
            // sb.append(attrib);
            // sb.append(" ");
            // }

            sb.append(show.getSearchSettings().getAllSearchAttributesGroupedAndOred());

            if (!StringUtils.isEmpty(show.getSearchSettings().getExtraSearchParams())) {
                sb.append(show.getSearchSettings().getExtraSearchParams());
                sb.append(" ");
            }
        }
	*/
        return sb.toString();
    }
}
