package com.greyzone.storage.impl;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.greyzone.domain.Show;
import com.greyzone.domain.TvSeriesNzb;
import com.greyzone.storage.ShowStorage;
import com.thoughtworks.xstream.XStream;

@Service
public class XmlShowStorage implements ShowStorage {

	private Logger log = Logger.getLogger(this.getClass());
	
	public static String FILENAME_SHOW = "shows.xml";
	
	@Override
	public List<Show> getShows() {
		
		XStream x = new XStream();
		x.processAnnotations(TvSeriesNzb.class);
		
		log.debug("Reading " + FILENAME_SHOW);
		try {
			FileInputStream fis = new FileInputStream(FILENAME_SHOW);
			TvSeriesNzb t = (TvSeriesNzb)x.fromXML(fis);
			return t.getShows();
		} catch (Exception e) {
			log.error("Could not read or parse " + FILENAME_SHOW, e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void storeShows(List<Show> shows) {
		TvSeriesNzb t = new TvSeriesNzb();
		
		t.setShows(shows);
		
		XStream x = new XStream();
		x.processAnnotations(TvSeriesNzb.class);
		String xml = x.toXML(t);
		
		try {
			log.debug("Trying to save updated " + FILENAME_SHOW);
			FileWriter fw = new FileWriter(FILENAME_SHOW);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(xml);
			bw.close();
			log.debug(FILENAME_SHOW + " saved.");
		} catch (Exception e) {
			log.error("Failed to write to file: " + FILENAME_SHOW, e);
		}
	}
}
