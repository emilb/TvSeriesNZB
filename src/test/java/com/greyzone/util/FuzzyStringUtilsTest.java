package com.greyzone.util;

import org.junit.Test;

import junit.framework.TestCase;

import com.greyzone.domain.tv.Episode;
import com.greyzone.domain.tv.Format;
import com.greyzone.domain.tv.Quality;
import com.greyzone.domain.tv.Show;

public class FuzzyStringUtilsTest extends TestCase {

	@Test
    public void testZeroPad() {
        assertEquals("00", FuzzyStringUtils.zeroPad(""));
        assertEquals("00", FuzzyStringUtils.zeroPad(null));
        assertEquals("01", FuzzyStringUtils.zeroPad("1"));
        assertEquals("12", FuzzyStringUtils.zeroPad("12"));
        assertEquals("01", FuzzyStringUtils.zeroPad("01"));
    }

	@Test
    public void testRemoveNonAlphaNumericChars() {
        assertEquals("emils test", FuzzyStringUtils.removeNonAlphaNumericChars("emil's test"));
        assertEquals("tab test with point", FuzzyStringUtils
                .removeNonAlphaNumericChars("tab\ttest. with point"));
        assertEquals(null, FuzzyStringUtils.removeNonAlphaNumericChars(null));
        assertEquals("", FuzzyStringUtils.removeNonAlphaNumericChars(""));
        assertEquals("   ", FuzzyStringUtils.removeNonAlphaNumericChars("   "));
        assertEquals("line break", FuzzyStringUtils.removeNonAlphaNumericChars("line\nbreak"));
        assertEquals("SouthParkS14E06720pHDTVx264IMMERSE", FuzzyStringUtils
                .removeNonAlphaNumericChars("South.Park.S14E06.720p.HDTV.x264-IMMERSE"));
    }

	@Test
    public void testFuzzyMatch() {
        // The.Mentalist.S02E19.720p.HDTV.x264-CTU
        Episode episode = new Episode();
        episode.setEpisodeNo("19");
        episode.setSeason("02");
        episode.setShow("The Mentalist");
        
        Show show = new Show();
        assertTrue(FuzzyStringUtils.fuzzyMatch(show, episode, Quality.SevenTwenty, Format.x264, "The.Mentalist.S02E19.720p.HDTV.x264-CTU"));

        // Greys.Anatomy.S06E15.720p.HDTV.X264-DIMENSION
        episode = new Episode();
        episode.setEpisodeNo("15");
        episode.setSeason("6");
        episode.setShow("Grey's Anatomy");
        assertTrue(FuzzyStringUtils.fuzzyMatch(show, episode, Quality.SevenTwenty, Format.x264, "Greys.Anatomy.S06E15.720p.HDTV.X264-DIMENSION"));
    }
}
