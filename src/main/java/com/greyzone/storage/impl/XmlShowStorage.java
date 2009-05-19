package com.greyzone.storage.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.greyzone.domain.SearchSettings;
import com.greyzone.domain.Show;
import com.greyzone.domain.TvSeriesNzb;
import com.greyzone.storage.ShowStorage;
import com.thoughtworks.xstream.XStream;

@Service
public class XmlShowStorage implements ShowStorage {

	@Override
	public List<Show> getShows() {
		Show house = new Show();
		house.setName("House");
		house.setCurrentlyWatchingSeason("5");
		house.setLastDownloadedEpisode("0");
		
		Show greys = new Show();
		greys.setName("Grey's Anatomy");
		greys.setCurrentlyWatchingSeason("5");
		greys.setLastDownloadedEpisode("23");
		
		SearchSettings ss = new SearchSettings();
		ss.getSources().add("HDTV");
		ss.getFormats().add("720p");
		ss.getFormats().add("x264");
		ss.setExtraSearchParams("-a:sub~German -a:sub~French -a:sub~Dutch");
		
		house.setSearchSettings(ss);
		greys.setSearchSettings(ss);
		
		List<Show> showss = new ArrayList<Show>();
//		showss.add(house);
		showss.add(greys);
		
		return showss;
	}

	@Override
	public void storeShows(List<Show> shows) {
		TvSeriesNzb t = new TvSeriesNzb();
		
		t.setShows(shows);
		
		XStream x = new XStream();
		x.processAnnotations(TvSeriesNzb.class);
		String xml = x.toXML(t);
		System.out.println(xml);
	}
}
