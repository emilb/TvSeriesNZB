package com.greyzone.domain.movie;

import com.greyzone.domain.SearchSettings;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Movie")
public class Movie {

    @XStreamAlias("Title")
    private String         title;

    @XStreamAlias("Year")
    private String         year;

    @XStreamAlias("IsDownloaded")
    private boolean        downloaded;

    @XStreamAlias("SearchSettings")
    private SearchSettings searchSettings;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    public SearchSettings getSearchSettings() {
        return searchSettings;
    }

    public void setSearchSettings(SearchSettings searchSettings) {
        this.searchSettings = searchSettings;
    }

}
