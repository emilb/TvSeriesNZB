package com.greyzone.domain.tv;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class Episode implements Comparable<Episode> {
    private String show;
    private String season;
    private String episodeNo;
    private String episodeName;
    private Date   dateAired;
    //private String indexId;
    private byte[] nzbFile;
    private String nzbFileUri;
    private String rageId;

    public Episode() {

    }

    public Episode(String show, String season, String episodeNo, String episodeName, String rageId) {
        this.show = show;
        setSeason(season);
        this.episodeNo = episodeNo;
        this.episodeName = episodeName;
        this.rageId = rageId;
    }

    public Episode(String show, String season, String episodeNo, String rageId, String episodeName, Date dateAired) {
        this.show = show;
        setSeason(season);
        this.episodeNo = episodeNo;
        this.episodeName = episodeName;
        this.rageId = rageId;
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
    	if (StringUtils.isBlank(season))
            this.season= "00";

        if (season.length() == 1)
            this.season = "0" + season;
        else
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

    public String getRageId() {
        return rageId;
    }

    public void setRageId(String rageId) {
        this.rageId = rageId;
    }

    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        sb.append(show);
        sb.append(" ");
        sb.append(getSeason());
        sb.append("x");
        sb.append(getEpisodeNo());
        sb.append(" ");
        sb.append(episodeName);
        return sb.toString();
    }

    @Override
    public String toString() {
        return getFullName();
    }

    /**
     * a          negative integer, zero,     or a positive integer 
     * as this object is less than, equal to, or greater than the specified object.
     */
	@Override
	public int compareTo(Episode o) {

        // @ToDo: This can't be correct
        if (this.show.equals(o.getShow()))
			return this.show.compareTo(o.getShow());
		
		if (this.getSeason().equals(o.getSeason()))
			return this.getSeason().compareTo(o.getSeason());
		
		return this.getEpisodeNo().compareTo(o.getEpisodeNo());
	}
}
