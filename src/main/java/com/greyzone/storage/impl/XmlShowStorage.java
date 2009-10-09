package com.greyzone.storage.impl;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greyzone.domain.tv.Show;
import com.greyzone.domain.tv.TvSeriesNzb;
import com.greyzone.settings.ApplicationSettings;
import com.greyzone.storage.Storage;
import com.thoughtworks.xstream.XStream;

@Service("XmlShowStorage")
public class XmlShowStorage implements Storage<Show> {

    private final Logger        log = Logger.getLogger(this.getClass());

    @Autowired
    private ApplicationSettings appSettings;

    @Override
    public List<Show> getItems() {

        XStream x = new XStream();
        x.processAnnotations(TvSeriesNzb.class);

        log.debug("Reading " + appSettings.getShowsFileName());
        try {
            FileInputStream fis = new FileInputStream(appSettings.getShowsFileName());
            TvSeriesNzb t = (TvSeriesNzb) x.fromXML(fis);
            fis.close();
            return t.getShows();
        } catch (Exception e) {
            log.error("Could not read or parse " + appSettings.getShowsFileName(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void storeItems(List<Show> shows) {
        TvSeriesNzb t = new TvSeriesNzb();

        t.setShows(shows);

        XStream x = new XStream();
        x.processAnnotations(TvSeriesNzb.class);
        String xml = x.toXML(t);

        try {
            log.debug("Trying to save updated " + appSettings.getShowsFileName());
            FileWriter fw = new FileWriter(appSettings.getShowsFileName());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(xml);
            bw.close();
            log.debug(appSettings.getShowsFileName() + " saved.");
        } catch (Exception e) {
            log.error("Failed to write to file: " + appSettings.getShowsFileName(), e);
        }
    }

    @Override
    public void testParsing() {
        log.info("Testing " + appSettings.getShowsFileName() + " correctness...");
        XStream x = new XStream();
        x.processAnnotations(TvSeriesNzb.class);

        log.debug("Reading " + appSettings.getShowsFileName());
        try {
            FileInputStream fis = new FileInputStream(appSettings.getShowsFileName());
            x.fromXML(fis);
            fis.close();
        } catch (Exception e) {
            log.error("Could not read or parse " + appSettings.getShowsFileName(), e);
            throw new RuntimeException(e);
        }
        log.info("... file " + appSettings.getShowsFileName() + " read and parsed correctly.");
    }

}
