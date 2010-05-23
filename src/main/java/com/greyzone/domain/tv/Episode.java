package com.greyzone.domain.tv;

import java.util.Date;

public class Episode {
    private String show;
    private String season;
    private String episodeNo;
    private String episodeName;
    private Date   dateAired;
    private String indexId;
    private byte[] nzbFile;
    private String nzbFileUri;

    public Episode() {

    }

    public Episode(String show, String season, String episodeNo, String episodeName) {
        this.show = show;
        this.season = season;
        this.episodeNo = episodeNo;
        this.episodeName = episodeName;
    }

    public Episode(String show, String season, String episodeNo, String episodeName, Date dateAired) {
        super();
        this.show = show;
        this.season = season;
        this.episodeNo = episodeNo;
        this.episodeName = episodeName;
        this.dateAired = dateAired;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getEpisodeNo() {
        return episodeNo;
    }

    public void setEpisodeNo(String episodeNo) {
        this.episodeNo = episodeNo;
    }

    public String getEpisodeName() {
        return episodeName;
    }

    public void setEpisodeName(String episodeName) {
        this.episodeName = episodeName;
    }

    public String getIndexId() {
        return indexId;
    }

    public void setIndexId(String indexId) {
        this.indexId = indexId;
    }

    public Date getDateAired() {
        return dateAired;
    }

    public void setDateAired(Date dateAired) {
        this.dateAired = dateAired;
    }

    public byte[] getNzbFile() {
        return nzbFile;
    }

    public void setNzbFile(byte[] nzbFile) {
        this.nzbFile = nzbFile;
    }

    public String getNzbFileUri() {
        return nzbFileUri;
    }

    public void setNzbFileUri(String nzbFileUri) {
        this.nzbFileUri = nzbFileUri;
    }

    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        sb.append(show);
        sb.append(" ");
        sb.append(season);
        sb.append("x");
        sb.append(episodeNo);
        sb.append(" ");
        sb.append(episodeName);
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        if (getNzbFile() != null && getNzbFile().length > 0) {
            sb.append(" [nzbfile length: ").append(getNzbFile().length).append("]");
        }
        if (getNzbFileUri() != null) {
            sb.append(" [nzb file uri: ").append(getNzbFileUri()).append("] ");
        }

        return sb.toString();
    }
}
