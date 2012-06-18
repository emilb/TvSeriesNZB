package com.greyzone.integration.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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

import com.greyzone.domain.tv.Episode;
import com.greyzone.integration.IntegrationDownloader;
import com.greyzone.integration.impl.xml.QJob;
import com.greyzone.integration.impl.xml.QStatus;
import com.greyzone.settings.ApplicationSettings;
import com.greyzone.util.HttpUtils;
import com.sun.syndication.feed.rss.Item;
import com.sun.syndication.feed.synd.SyndCategory;
import com.sun.syndication.feed.synd.SyndEnclosure;
import com.sun.syndication.feed.synd.SyndEntry;
import com.thoughtworks.xstream.XStream;

@Service("Sabnzbd")
public class Sabnzbd implements IntegrationDownloader {

    private final Logger        log = Logger.getLogger(this.getClass());

    @Autowired
    private ApplicationSettings settings;

    @Override
    public void orderDownloadByUrl(String url, String name, String category) {
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, settings.getHttpClientUserAgent());

        List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
        queryParams.add(new BasicNameValuePair("apikey", settings.getSabnzbdApiKey()));
        queryParams.add(new BasicNameValuePair("mode", "addurl"));
        queryParams.add(new BasicNameValuePair("name", url));
        queryParams.add(new BasicNameValuePair("cat", category));
        if (!StringUtils.isBlank(name)) {
            queryParams.add(new BasicNameValuePair("nzbname", name));
        }

        // Only supply username and password if they are specified in
        // configuration
        if (!StringUtils.isEmpty(settings.getSabnzbdUsername())
                || !StringUtils.isEmpty(settings.getSabnzbdPassword())) {
            queryParams.add(new BasicNameValuePair("ma_username", settings.getSabnzbdUsername()));
            queryParams.add(new BasicNameValuePair("ma_password", settings.getSabnzbdPassword()));
        }

        HttpGet method = new HttpGet(HttpUtils.createGetURI(settings.getSabnzbdUrl(), queryParams));

        try {
            log.debug("Instructing SABnzbd to download nzb: " + url + " to: " + settings.getSabnzbdUrl());

            // Do real download only if in dry run
            if (!settings.isDryRun()) {
                HttpResponse response = client.execute(method);

                String result = HttpUtils.getContentAsString(response.getEntity());
                if (!result.startsWith("ok")) {
                    log.error("SABnzbd reported an error, check your configuration! Message from SABnzbd: "
                            + result);

                    // Check if the error might be because of wrong url
                    if ((settings.getSabnzbdUrl().endsWith("api"))
                            || (settings.getSabnzbdUrl().endsWith("api/"))) {
                        log
                                .warn("The configuration property sabnzbd.url doesn't end with api, check if this really is correct!");
                    }

                    throw new RuntimeException("SABnzbd integration failed");
                }
            }
        } catch (Exception e) {
            log.error("Failed to contact SABnzbd", e);
            throw new RuntimeException("Could not contact SABnzbd.");
        }
    }

    public void orderDownloadByNzb(byte[] nzb) {

    }

    @Override
    public void testIntegration() {
        log.info("Testing SABnzbd integration...");

        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, settings.getHttpClientUserAgent());

        List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
        queryParams.add(new BasicNameValuePair("apikey", settings.getSabnzbdApiKey()));
        queryParams.add(new BasicNameValuePair("mode", "qstatus"));
        queryParams.add(new BasicNameValuePair("output", "xml"));

        // Only supply username and password if they are specified in
        // configuration
        if (!StringUtils.isEmpty(settings.getSabnzbdUsername())
                || !StringUtils.isEmpty(settings.getSabnzbdPassword())) {
            queryParams.add(new BasicNameValuePair("ma_username", settings.getSabnzbdUsername()));
            queryParams.add(new BasicNameValuePair("ma_password", settings.getSabnzbdPassword()));
        }

        HttpGet method = new HttpGet(HttpUtils.createGetURI(settings.getSabnzbdUrl(), queryParams));
        
        String resp = null;
        try {
            log.info("Querying SABnzbd status...");
            HttpResponse response = client.execute(method);
            resp = HttpUtils.getContentAsString(response.getEntity());
            XStream xstream = new XStream();
            xstream.alias("queue", QStatus.class);
            xstream.alias("job", QJob.class);
            QStatus status = new QStatus();
            status = (QStatus) xstream.fromXML(resp);

            log.info("\n" + status);
            log.info("... SABnzbd integration working correctly!");
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to SABnzd correctly, response: " + resp, e);
        }
    }

    @Override
    public void orderDownloadByEpisode(Episode ep) {
        if (!StringUtils.isEmpty(ep.getNzbFileUri())) {
            orderDownloadByUrl(ep.getNzbFileUri(), ep.getFullName(), "TV");
        } else {
        	log.warn("Could not download episode because no URI was specified for : " + ep);
        }
    }

	@Override
	public void orderDownloadByItem(SyndEntry entry) {
		if (entry.getEnclosures().size() > 0) {
			String downloadUrl = ((SyndEnclosure)entry.getEnclosures().get(0)).getUrl();
			String name = entry.getTitle();
			String category = "Misc";
			try {
				SyndCategory syndCategory = (SyndCategory)entry.getCategories().get(0);
				category = syndCategory.getName();
			} catch (Exception e) {
				log.warn("Failed to get category from syndentry", e);
			}
			
			orderDownloadByUrl(downloadUrl, name, category);
		} else {
			log.warn("No download URL specified for entry: " + entry);
		}
	}
}
