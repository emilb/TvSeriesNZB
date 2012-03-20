package com.greyzone.domain.tv;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

public class DomainSerializationTest {

	Show show;
	XStream x;
	
	@Before
	public void init() {
		show = new Show();
		show.setId("5489");
		show.setCurrentlyWatchingSeason("8");
		show.setLastDownloadedEpisode("13");
		show.setName("House");
		show.setQuality(Quality.SevenTwenty);
		show.setFormat(Format.XviD);
		
		x = new XStream();
		x.processAnnotations(TvSeriesNzb.class);
	}
	
	@Test
	public void testSerializeShow() {
		String xml = x.toXML(show);
		System.out.println(xml);
	}
	
	@Test
	public void testDeserializeShow() {
		String xml = "<show>\n" + 
				"  <name>House</name>\n" + 
				"  <season>8</season>\n" + 
				"  <episode>13</episode>\n" + 
				"  <id>5489</id>\n" + 
				"  <quality>720p</quality>\n" + 
				"  <format>XviD</format>\n" + 
				"</show>";
		
		Show s = (Show)x.fromXML(xml);
		
		Assert.assertTrue(show.equals(s));
	}
}
