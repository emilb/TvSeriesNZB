package com.greyzone.util;

import org.apache.commons.lang.StringUtils;

import com.greyzone.domain.tv.Episode;

public class FuzzyStringUtils {

    public static boolean fuzzyMatch(Episode episode, String rssTitle) {

        // System.out.println("Comparing episode: " + episode);
        // System.out.println("to: " + rssTitle);

        String normalizedRssTitle = removeNonAlphaNumericChars(rssTitle);

        try {
            // Check show name
            String[] showName = StringUtils.split(episode.getShow());
            for (String s : showName) {
                s = removeNonAlphaNumericChars(s);
                verifyContains(normalizedRssTitle, s);
            }

            // Season
            verifyContains(normalizedRssTitle, "S" + zeroPad(episode.getSeason()));

            // Episode no
            verifyContains(normalizedRssTitle, "E" + zeroPad(episode.getEpisodeNo()));

        } catch (Exception e) {
            // System.out.println("no match");
            return false;
        }

        // System.out.println("is a match");
        return true;
    }

    protected static void verifyContains(String str, String searchStr) {
        if (!StringUtils.containsIgnoreCase(str, searchStr)) {
            throw new RuntimeException();
        }
    }

    protected static String removeNonAlphaNumericChars(String str) {
        if (str == null)
            return null;

        StringBuilder sb = new StringBuilder();

        for (char c : str.toCharArray()) {
            if (Character.isLetterOrDigit(c))
                sb.append(c);

            if (Character.isWhitespace(c))
                sb.append(" ");
        }

        return sb.toString();
    }

    protected static String zeroPad(String str) {
        if (StringUtils.isBlank(str))
            return "00";

        if (str.length() == 1)
            return "0" + str;

        return str;
    }

}
