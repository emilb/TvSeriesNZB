package com.greyzone.indexsearch.impl;

import com.greyzone.domain.tv.Episode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public final class SearchResultItem implements Serializable {
    public final String title;
    public final String url;
    public final String episode;
    public final String season;
    public final String rageId;

    public SearchResultItem(String title, String url, String rageId, String episode, String season) {
        this.title = title;
        this.url = url;
        this.rageId = rageId;
        this.episode = episode;
        this.season = season;
    }

    public boolean isMatch(Episode ep) {
        if (StringUtils.equals(ep.getRageId(), rageId) &&
                StringUtils.equals("S" + ep.getSeason(), season) &&
                StringUtils.equals("E" + ep.getEpisodeNo(), episode)) {
            return true;
        }

        return false;
    }
}