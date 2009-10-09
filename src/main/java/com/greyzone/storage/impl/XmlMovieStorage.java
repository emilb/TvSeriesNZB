package com.greyzone.storage.impl;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greyzone.domain.movie.Movie;
import com.greyzone.domain.movie.MoviesNzb;
import com.greyzone.domain.tv.TvSeriesNzb;
import com.greyzone.settings.ApplicationSettings;
import com.greyzone.storage.Storage;
import com.thoughtworks.xstream.XStream;

@Service("XmlMovieStorage")
public class XmlMovieStorage implements Storage<Movie> {

    private final Logger        log = Logger.getLogger(this.getClass());

    @Autowired
    private ApplicationSettings appSettings;

    @Override
    public List<Movie> getItems() {

        XStream x = new XStream();
        x.processAnnotations(TvSeriesNzb.class);

        log.debug("Reading " + appSettings.getMoviesFileName());
        try {
            FileInputStream fis = new FileInputStream(appSettings.getMoviesFileName());
            MoviesNzb t = (MoviesNzb) x.fromXML(fis);
            fis.close();
            return t.getMovies();
        } catch (Exception e) {
            log.error("Could not read or parse " + appSettings.getMoviesFileName(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void storeItems(List<Movie> movies) {
        MoviesNzb t = new MoviesNzb();

        t.setMovies(movies);

        XStream x = new XStream();
        x.processAnnotations(MoviesNzb.class);
        String xml = x.toXML(t);

        try {
            log.debug("Trying to save updated " + appSettings.getMoviesFileName());
            FileWriter fw = new FileWriter(appSettings.getMoviesFileName());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(xml);
            bw.close();
            log.debug(appSettings.getMoviesFileName() + " saved.");
        } catch (Exception e) {
            log.error("Failed to write to file: " + appSettings.getMoviesFileName(), e);
        }
    }

    @Override
    public void testParsing() {
        log.info("Testing " + appSettings.getMoviesFileName() + " correctness...");
        XStream x = new XStream();
        x.processAnnotations(MoviesNzb.class);

        log.debug("Reading " + appSettings.getMoviesFileName());
        try {
            FileInputStream fis = new FileInputStream(appSettings.getMoviesFileName());
            x.fromXML(fis);
            fis.close();
        } catch (Exception e) {
            log.error("Could not read or parse " + appSettings.getMoviesFileName(), e);
            throw new RuntimeException(e);
        }
        log.info("... file " + appSettings.getMoviesFileName() + " read and parsed correctly.");
    }

}
