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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greyzone.domain.tv.Episode;
import com.greyzone.integration.IntegrationDownloader;
import com.greyzone.integration.impl.xml.QJob;
import com.greyzone.integration.impl.xml.QStatus;
import com.greyzone.settings.ApplicationSettings;
import com.greyzone.util.HttpUtils;
import com.thoughtworks.xstream.XStream;

@Service("Sabnzbd")
public class Sabnzbd implements IntegrationDownloader {

    private final Logger        log = Logger.getLogger(this.getClass());

    @Autowired
    private ApplicationSettings appSettings;

    @Override
    public void orderDownloadByUrl(String url, String name) {
        HttpClient client = new DefaultHttpClient();

        List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
        queryParams.add(new BasicNameValuePair("apikey", appSettings.getSabnzbdApiKey()));
        queryParams.add(new BasicNameValuePair("mode", "addurl"));
        queryParams.add(new BasicNameValuePair("name", url));
        queryParams.add(new BasicNameValuePair("cat", "tv"));
        if (!StringUtils.isBlank(name)) {
            queryParams.add(new BasicNameValuePair("nzbname", name));
        }

        // Only supply username and password if they are specified in
        // configuration
        if (!StringUtils.isEmpty(appSettings.getSabnzbdUsername())
                || !StringUtils.isEmpty(appSettings.getSabnzbdPassword())) {
            queryParams.add(new BasicNameValuePair("ma_username", appSettings.getSabnzbdUsername()));
            queryParams.add(new BasicNameValuePair("ma_password", appSettings.getSabnzbdPassword()));
        }

        HttpGet method = new HttpGet(HttpUtils.createGetURI(appSettings.getSabnzbdUrl(), queryParams));

        try {
            log.debug("Instructing SABnzbd to download nzb: " + url + " to: " + appSettings.getSabnzbdUrl());

            // Do real download only if in dry run
            if (!appSettings.isDryRun()) {
                HttpResponse response = client.execute(method);

                String result = HttpUtils.getContentAsString(response.getEntity());
                if (!result.startsWith("ok")) {
                    log.error("SABnzbd reported an error, check your configuration! Message from SABnzbd: "
                            + result);

                    // Check if the error might be because of wrong url
                    if ((appSettings.getSabnzbdUrl().endsWith("api"))
                            || (appSettings.getSabnzbdUrl().endsWith("api/"))) {
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

        List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
        queryParams.add(new BasicNameValuePair("apikey", appSettings.getSabnzbdApiKey()));
        queryParams.add(new BasicNameValuePair("mode", "qstatus"));
        queryParams.add(new BasicNameValuePair("output", "xml"));

        // Only supply username and password if they are specified in
        // configuration
        if (!StringUtils.isEmpty(appSettings.getSabnzbdUsername())
                || !StringUtils.isEmpty(appSettings.getSabnzbdPassword())) {
            queryParams.add(new BasicNameValuePair("ma_username", appSettings.getSabnzbdUsername()));
            queryParams.add(new BasicNameValuePair("ma_password", appSettings.getSabnzbdPassword()));
        }

        HttpGet method = new HttpGet(HttpUtils.createGetURI(appSettings.getSabnzbdUrl(), queryParams));
        
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
//        if (StringUtils.isBlank(ep.getIndexId()) && (ep.getNzbFile() == null || ep.getNzbFile().length == 0)
//                && (StringUtils.isEmpty(ep.getNzbFileUri()))) {
//            log.debug("Could not download episode because no newzbin id or nzb file was specified");
//            return;
//        }
//
//        else 
        	
        if (!StringUtils.isEmpty(ep.getNzbFileUri())) {
            orderDownloadByUrl(ep.getNzbFileUri(), ep.getFullName());
        } else {
        	log.warn("Could not download episode because no URI was specified for : " + ep);
        }
    }
}
