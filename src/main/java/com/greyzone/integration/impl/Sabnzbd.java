package com.greyzone.integration.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greyzone.domain.tv.Episode;
import com.greyzone.integration.IntegrationDownloader;
import com.greyzone.integration.impl.xml.QJob;
import com.greyzone.integration.impl.xml.QStatus;
import com.greyzone.settings.ApplicationSettings;
import com.thoughtworks.xstream.XStream;

@Service("Sabnzbd")
public class Sabnzbd implements IntegrationDownloader {

    private final Logger        log = Logger.getLogger(this.getClass());

    @Autowired
    private ApplicationSettings appSettings;

    @Override
    public void orderDownloadByUrl(String url, String name) {
        HttpClient client = new HttpClient();

        GetMethod method = new GetMethod(appSettings.getSabnzbdUrl());

        List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
        queryParams.add(new NameValuePair("apikey", appSettings.getSabnzbdApiKey()));
        queryParams.add(new NameValuePair("mode", "addurl"));
        queryParams.add(new NameValuePair("name", url));
        queryParams.add(new NameValuePair("cat", "tv"));
        if (!StringUtils.isBlank(name)) {
            queryParams.add(new NameValuePair("nzbname", name));
        }

        // Only supply username and password if they are specified in
        // configuration
        if (!StringUtils.isEmpty(appSettings.getSabnzbdUsername())
                || !StringUtils.isEmpty(appSettings.getSabnzbdPassword())) {
            queryParams.add(new NameValuePair("ma_username", appSettings.getSabnzbdUsername()));
            queryParams.add(new NameValuePair("ma_password", appSettings.getSabnzbdPassword()));
        }

        NameValuePair[] valuePairs = queryParams.toArray(new NameValuePair[] {});

        method.setQueryString(valuePairs);

        try {
            log.debug("Instructing SABnzbd to download nzb: " + url + " to: " + appSettings.getSabnzbdUrl());

            // Do real download only if in dry run
            if (!appSettings.isDryRun()) {
                client.executeMethod(method);

                String result = method.getResponseBodyAsString();
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

    @Override
    public void orderDownloadById(String id) {
        HttpClient client = new HttpClient();

        GetMethod method = new GetMethod(appSettings.getSabnzbdUrl());

        List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
        queryParams.add(new NameValuePair("apikey", appSettings.getSabnzbdApiKey()));
        queryParams.add(new NameValuePair("mode", "addid"));
        queryParams.add(new NameValuePair("name", id));

        // Only supply username and password if they are specified in
        // configuration
        if (!StringUtils.isEmpty(appSettings.getSabnzbdUsername())
                || !StringUtils.isEmpty(appSettings.getSabnzbdPassword())) {
            queryParams.add(new NameValuePair("ma_username", appSettings.getSabnzbdUsername()));
            queryParams.add(new NameValuePair("ma_password", appSettings.getSabnzbdPassword()));
        }

        NameValuePair[] valuePairs = queryParams.toArray(new NameValuePair[] {});

        method.setQueryString(valuePairs);

        try {
            log.debug("Instructing SABnzbd to download newzbin id: " + id + " to: "
                    + appSettings.getSabnzbdUrl());

            // Do real download only if in dry run
            if (!appSettings.isDryRun()) {
                client.executeMethod(method);

                String result = method.getResponseBodyAsString();
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

    @Override
    public void orderDownloadByIds(List<String> ids) {
        for (String id : ids) {
            orderDownloadById(id);
        }
    }

    public void orderDownloadByNzb(byte[] nzb) {

    }

    @Override
    public void testIntegration() {
        log.info("Testing SABnzbd integration...");

        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(appSettings.getSabnzbdUrl());

        List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
        queryParams.add(new NameValuePair("apikey", appSettings.getSabnzbdApiKey()));
        queryParams.add(new NameValuePair("mode", "qstatus"));
        queryParams.add(new NameValuePair("output", "xml"));

        // Only supply username and password if they are specified in
        // configuration
        if (!StringUtils.isEmpty(appSettings.getSabnzbdUsername())
                || !StringUtils.isEmpty(appSettings.getSabnzbdPassword())) {
            queryParams.add(new NameValuePair("ma_username", appSettings.getSabnzbdUsername()));
            queryParams.add(new NameValuePair("ma_password", appSettings.getSabnzbdPassword()));
        }

        NameValuePair[] valuePairs = queryParams.toArray(new NameValuePair[] {});

        method.setQueryString(valuePairs);
        String resp = null;
        try {
            log.info("Querying SABnzbd status...");
            client.executeMethod(method);
            resp = method.getResponseBodyAsString();
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
        if (StringUtils.isBlank(ep.getIndexId()) && (ep.getNzbFile() == null || ep.getNzbFile().length == 0)
                && (StringUtils.isEmpty(ep.getNzbFileUri()))) {
            log.debug("Could not download episode because no newzbin id or nzb file was specified");
            return;
        }

        if (!StringUtils.isBlank(ep.getIndexId())) {
            List<String> ids = new ArrayList<String>();
            ids.add(ep.getIndexId());
            orderDownloadByIds(ids);
        }

        else if (!StringUtils.isEmpty(ep.getNzbFileUri())) {
            orderDownloadByUrl(ep.getNzbFileUri(), ep.getFullName());
        }
    }
}
