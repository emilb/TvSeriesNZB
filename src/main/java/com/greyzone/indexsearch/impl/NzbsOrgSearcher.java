package com.greyzone.indexsearch.impl;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greyzone.cache.NzbsOrgSearchCache;
import com.greyzone.domain.tv.Episode;
import com.greyzone.domain.tv.Show;
import com.greyzone.exceptions.AuthenticationException;
import com.greyzone.exceptions.NoSearchHitException;
import com.greyzone.exceptions.ServiceUnavailableException;
import com.greyzone.indexsearch.IndexSearcher;
import com.greyzone.settings.ApplicationSettings;
import com.greyzone.util.FuzzyStringUtils;
import com.greyzone.util.HtmlParseUtil;
import com.greyzone.util.HttpUtils;
import com.greyzone.util.ThreadUtils;

@Service("NzbsOrgSearcher")
public class NzbsOrgSearcher implements IndexSearcher {

	@Autowired
	ApplicationSettings settings;

	@Autowired
	NzbsOrgSearchCache cache;

	private final Logger log = Logger.getLogger(this.getClass());

	private static ThreadLocal<Boolean> lastSearchFromCache = new ThreadLocal<Boolean>();

	private final static String TAG_NAME_START = "class=\"nzb\">";
	private final static String TAG_NAME_END = "</a>";
	private final static String TAG_ID_START = "&amp;nzbid=";
	private final static String TAG_ID_END = "\"";
	private final static String TAG_STRIP_FROM = "\">Download</a></b></td></tr>";

	private static final long WAIT_MS = 10000;
	private static final int MAX_NOOF_RETRIES = 3;

	private static final long NOMINAL_WAIT_BETWEEN_REQUESTS = 2500;
	private static final long EXTRA_RANDOM_WAIT_MAX = 1500;
	
	private HttpClient client;

	@Override
	public List<Episode> getIndexIds(Show show, List<Episode> episodes) {

		// Ahhgm, ugly way of securing a value in lastsearch
		try {
			if (lastSearchFromCache.get() != true)
				lastSearchFromCache.set(false);
		} catch (Exception e) {
			lastSearchFromCache.set(false);
		}
		
		List<Episode> result = new ArrayList<Episode>();

		for (Episode episode : episodes) {
			
			// Download for this episode might have already been found
			if (StringUtils.isNotBlank(episode.getNzbFileUri()))
				continue;

			try {
				boolean foundEntry = false;

				List<SearchResultItem> searchResult = getSearchResult(client,
						episode, show);
				for (SearchResultItem rsi : searchResult) {

					if (FuzzyStringUtils.fuzzyMatch(episode, show.getQuality(),
							show.getFormat(), rsi.getName())) {
						episode.setNzbFileUri(getDownloadURI(rsi));
						result.add(episode);

						// Only add once
						foundEntry = true;
					}

					if (foundEntry)
						break;
				}
			} catch (NoSearchHitException nse) {
				log.debug("Could not find " + episode + " in web search");
			}
		}

		return result;
	}

	private boolean isLoggedInCheckSimple(HttpClient client) {
		if (client == null) {
			log.debug("No current session at nzbs.org, not logged in");
			return false;
		}
		
		return true;
	}
	
	private boolean isLoggedIn(HttpClient client) {
		log.debug("Checking if logged in to nzbs.org");

		if (!isLoggedInCheckSimple(client))
			return false;
		
		try {
			HttpGet get = new HttpGet("http://www.nzbs.org/index.php");
			HttpResponse response = client.execute(get);
			ThreadUtils.sleep(randomSleepTime());
			
			String html = HttpUtils.getContentAsString(response.getEntity());

			if (!HtmlParseUtil.assertContains(html,
					settings.getNzbsOrgUsername())) {
				log.debug("Not logged in to nzbs.org");
				return false;
			}
			
			log.debug("Already logged in to nzbs.org");
			return true;
		} catch (Exception e) {
			log.error("Failed to check if logged in to nzbs.org", e);
			return false;
		}
	}

	@SuppressWarnings("unused")
	@PreDestroy
	private void logout() {
		if (client == null)
			return;
		
		log.debug("Logging out from NZBs.org");
		try {
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair("action", "logout"));

			String logoutURI = HttpUtils.createGetURI(
					"http://nzbs.org/user.php", formparams);

			HttpGet get = new HttpGet(logoutURI);

			HttpResponse response = client.execute(get);
			String html = HttpUtils.getContentAsString(response.getEntity());
			if (!HtmlParseUtil.assertContains(html, "You are now logged out."))
				log.warn("Logout of NZBs.org failed:\n" + html);

		} catch (ClientProtocolException cpe) {
			log.error("Failed to logout of NZBs.org", cpe);
		} catch (IOException ioe) {
			log.error("Failed to logout of NZBs.org", ioe);
		}
	}

	public HttpClient login() {
		try {
			log.debug("Attempting to login to nzbs.org");
			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, settings.getHttpClientUserAgent());
			HttpPost post = new HttpPost("http://www.nzbs.org/user.php");

			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair("action", "dologin"));
			formparams.add(new BasicNameValuePair("location", "/index.php"));
			formparams.add(new BasicNameValuePair("username", settings
					.getNzbsOrgUsername()));
			formparams.add(new BasicNameValuePair("password", settings
					.getNzbsOrgPassword()));
			formparams.add(new BasicNameValuePair("submit", "Login"));
			post.setEntity(new UrlEncodedFormEntity(formparams, "UTF-8"));

			HttpResponse response = client.execute(post);
			
			String html = HttpUtils.getContentAsString(response.getEntity());

			if (!HtmlParseUtil.assertContains(html, "SUCCESS"))
				throw new AuthenticationException("Login to NZBs.org failed");

			log.debug("Login succeeded");
			ThreadUtils.sleep(randomSleepTime());
			log.debug("Getting index.php at nzbs.org");
			HttpGet get = new HttpGet("http://www.nzbs.org/index.php");
			response = client.execute(get);
			ThreadUtils.sleep(randomSleepTime());
			html = HttpUtils.getContentAsString(response.getEntity());

			if (!HtmlParseUtil.assertContains(html,
					settings.getNzbsOrgUsername())) {
				log.error("Login to nzbs.org failed with unexpected response not containing " + settings.getNzbsOrgUsername());
				throw new AuthenticationException(
						"Unexpected response from NZBs.org /index.php, did not contain username:\n"
								+ html);
			}
			
			log.debug("Login to nzbs.org succeeded");			
			return client;
		} catch (ClientProtocolException cpe) {
			log.error("Failed to login at NZBs.org", cpe);
			throw new ServiceUnavailableException(
					"Could not initiate client protocol during login");
		} catch (ConnectException ce) {
			log.error("Connect exception when login at NZBs.org", ce);
			throw new ServiceUnavailableException(
					"Connect Exception during login");
		} catch (IOException ioe) {
			log.error("Failed to login at NZBs.org", ioe);
			throw new ServiceUnavailableException("IOException during login");
		}
	}

	private List<SearchResultItem> getSearchResult(HttpClient client,
			Episode episode, Show show) {
		try {
			String searchHtml = getSearchResultHtml(client, episode, show);
			return parseSearchResult(searchHtml);
		} catch (IOException ioe) {
			log.error("Failed to search for " + episode.toString()
					+ " at NZBs.org", ioe);
			throw new ServiceUnavailableException("IOException during login");
		}
	}

	private String getDownloadURI(SearchResultItem rsi) {
		return "http://nzbs.org/index.php?action=getnzb&nzbid=" + rsi.getId()
				+ "&i=" + settings.getNzbsOrgId() + "&h="
				+ settings.getNzbsOrgHash();
	}

	public String getSearchResultHtml(HttpClient client, Episode episode, Show show)
			throws ClientProtocolException, IOException {
		return getSearchResultHtml(client, episode, show, 1);
	}

	public String getSearchResultHtml(HttpClient client, Episode episode,
			Show show, int iterationNumber) throws ClientProtocolException, IOException {

		log.debug("Doing search for " + episode + " in iteration "
				+ iterationNumber);

		// Create uri
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
		queryParams.add(new BasicNameValuePair("action", "search"));
		queryParams.add(new BasicNameValuePair("q", getSearchQuery(episode, show)));
		queryParams.add(new BasicNameValuePair("catid", settings
				.getNzbsOrgCategory()));
		queryParams.add(new BasicNameValuePair("age", ""));

		String searchURI = HttpUtils.createGetURI("http://nzbs.org/index.php",
				queryParams);

		log.debug("NZBs.org search URI: " + searchURI);
		
		if (cache.isInCache(searchURI)) {
			lastSearchFromCache.set(true);
			log.debug("Got search for " + episode + " from cache");
			return cache.getFromCache(searchURI);
		}

		if (!isLoggedInCheckSimple(client) || !isLoggedIn(client)) {
			client = login();
		}
		
		lastSearchFromCache.set(false);
		HttpGet get = new HttpGet(searchURI);

		HttpResponse response = client.execute(get);
		ThreadUtils.sleep(randomSleepTime());
		String html = HttpUtils.getContentAsString(response.getEntity());

		// Check for Service Unavailable (probably because of hitting to hard)
		if (HtmlParseUtil.assertContains(html, "Service Unavailable")) {
			if (MAX_NOOF_RETRIES - iterationNumber == 0)
				throw new ServiceUnavailableException("Tried "
						+ MAX_NOOF_RETRIES
						+ " times to search NZBs.org but failed, aborting.");

			log.warn("Got a Service Unavailable from NZBs.org, waiting for "
					+ WAIT_MS + "ms, retrying "
					+ (MAX_NOOF_RETRIES - iterationNumber) + " times more.");

			ThreadUtils.sleep(WAIT_MS);

			return getSearchResultHtml(client, episode, show, iterationNumber + 1);
		}
		
		log.debug("Search succeeded, caching html response");
		cache.put(searchURI, html);
		return html;
	}

	public String getSearchQuery(Episode episode, Show show) {
		StringBuilder sb = new StringBuilder();
		sb.append("^"); // Should start with the show name
		if (StringUtils.isEmpty(show.getSearchString()))
			sb.append(episode.getShow());
		else
			sb.append(show.getSearchString());
		sb.append(" ").append("S").append(episode.getSeason());
		// sb.append(" ").append("E").append(episode.getEpisodeNo());
		return sb.toString();
	}

	public List<SearchResultItem> parseSearchResult(final String html) {
		List<SearchResultItem> result = new ArrayList<NzbsOrgSearcher.SearchResultItem>();

		// No hits at all!
		if (HtmlParseUtil.assertContains(html,
				"No NZBs found matching your search.")) {

			log.debug("Definitely no search hits");
			throw new NoSearchHitException();
		}

		String htmlToParse = html;

		boolean moreToFind = true;
		while (moreToFind) {
			String name = HtmlParseUtil.findString(htmlToParse, TAG_NAME_START,
					TAG_NAME_END);
			String id = HtmlParseUtil.findString(htmlToParse, TAG_ID_START,
					TAG_ID_END);

			if (StringUtils.isEmpty(name) || StringUtils.isEmpty(id)) {
				moreToFind = false;
				continue;
			}

			result.add(new SearchResultItem(name, id));

			htmlToParse = HtmlParseUtil.stripFrom(htmlToParse, TAG_STRIP_FROM);

			moreToFind = !StringUtils.isEmpty(htmlToParse);
		}

		if (result.size() == 0) {
			log.warn("Hmm... search did not indicate a no hit but could not find any search results while parsing:\n"
					+ html);
		}

		return result;
	}

	private long randomSleepTime() {
		return NOMINAL_WAIT_BETWEEN_REQUESTS + (long)(RandomUtils.nextDouble() * EXTRA_RANDOM_WAIT_MAX);
	}
	
	private class SearchResultItem {
		private final String name;
		private final String id;

		public SearchResultItem(String name, String id) {
			this.name = name;
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public String getId() {
			return id;
		}

		public String toString() {
			return name + " id: " + id;
		}
	}
}
