package com.greyzone.indexsearch.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.com.bytecode.opencsv.CSVReader;

import com.greyzone.domain.Episode;
import com.greyzone.domain.SearchSettings;
import com.greyzone.domain.Show;
import com.greyzone.indexsearch.IndexSearcher;
import com.greyzone.settings.ApplicationSettings;

@Service
public class NewzbinSearcher implements IndexSearcher {

	@Autowired
	ApplicationSettings settings;

	private Logger log = Logger.getLogger(this.getClass());

	@Override
	public List<String> getIndexIds(Show show, List<Episode> episodes) {
		List<String> result = new ArrayList<String>();

		for (Episode ep : episodes) {
			String id = null;
			try {
				id = getBestSearchMatch(show, ep);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NeedToWaitException ntwe) {

			}

			if (id != null)
				result.add(id);
		}

		return result;
	}

	/**
	 * 
	 * @param show
	 * @param episode
	 * @return the first hit in the search, null if no hit
	 * @throws IOException
	 */
	private String getBestSearchMatch(Show show, Episode episode)
			throws IOException, NeedToWaitException {
		HttpMethod method = createSearch(show, episode);
		HttpClient client = new HttpClient();

		log.debug("Searching for: " + episode);
		
		client.executeMethod(method);
		// log.debug(method.getResponseBodyAsString());

		if (method.getResponseHeader("Content-type").getValue().startsWith(
				"text/html")) {
			// Wait for 3 seconds
			log.debug("Too many searches, need to wait for 3 seconds.");

			try {
				Thread.currentThread().sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			return getBestSearchMatch(show, episode);
		}

		if (method.getResponseHeader("Content-type").getValue().startsWith(
				"text/csv")) {
			CSVReader reader = new CSVReader(new BufferedReader(
					new InputStreamReader(method.getResponseBodyAsStream())));

			String[] firstLine = reader.readNext();
			log.debug(StringUtils.join(firstLine, " "));

			if (firstLine != null)
				return firstLine[1];
		}

		log.debug("Got unknown response: " + method.getResponseBodyAsString());
		return null;
	}

	private boolean checkForWaitInstruction(String content) {
		return false;
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
		queryParams
				.add(new NameValuePair("q", createSearchQuery(show, episode)));
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

		NameValuePair[] valuePairs = queryParams
				.toArray(new NameValuePair[] {});

		get.setQueryString(valuePairs);
		log.debug(get.getQueryString());

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

		if (show.getSearchSettings() != null) {
			List<String> searchAttributes = show.getSearchSettings()
					.getAllSearchAttributes();
			for (String attrib : searchAttributes) {
				sb.append(attrib);
				sb.append(" ");
			}

			if (!StringUtils.isEmpty(show.getSearchSettings()
					.getExtraSearchParams())) {
				sb.append(show.getSearchSettings().getExtraSearchParams());
				sb.append(" ");
			}
		}

		return sb.toString();
	}

	public void test() {

		Show s = new Show();
		s.setName("House");
		SearchSettings ss = new SearchSettings();
		ss.getSources().add("HDTV");
		ss.getFormats().add("720p");
		ss.getFormats().add("x264");

		s.setSearchSettings(ss);

		Episode ep1 = new Episode("House", "5", "20", "");
		Episode ep2 = new Episode("House", "5", "21", "");
		Episode ep3 = new Episode("House", "5", "22", "");
		Episode ep4 = new Episode("House", "5", "23", "");

		// getIndexIds(s, ep1, ep2, ep3, ep4);

		// HttpClient client = new HttpClient();
		// HttpMethod method = createSearch(s, ep);
		// try {
		// client.executeMethod(method);
		// System.out.println(method.getResponseBodyAsString());
		// } catch (HttpException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

	}

	public static void main(String[] args) {
		NewzbinSearcher s = new NewzbinSearcher();
		s.test();
	}
}
