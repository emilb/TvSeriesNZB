/*
 * This file is part of JMoviedb.
 * 
 * Copyright (C) Tor Arne Lye torarnelye@gmail.com
 * 
 * JMoviedb is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * JMoviedb is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.greyzone.domain.movie;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.greyzone.domain.movie.enumerated.Country;
import com.greyzone.domain.movie.enumerated.Genre;
import com.greyzone.domain.movie.enumerated.Language;

public class Movie {

	// imdb data
	private String imdbID;
	private String title;
	private int year;
	private double rating;
	private int noofVotes;
	private String plotOutline;
	private String tagline;
	private boolean color;
	private int runTime;
	private ArrayList<Genre> genres;
	private ArrayList<Country> countries;
	private ArrayList<Language> imdbLanguages;
	private ArrayList<Person> directors;
	private ArrayList<Person> writers;
	private ArrayList<ActorInfo> actors;

	private byte[] imageBytes;

	public String toString() {
		return getDisplayTitle() + " (" + getYear() + ")";
	}

	public ArrayList<ActorInfo> getActors() {
		return actors;
	}

	/**
	 * Returns a list of all actors in a single string
	 * 
	 * @return actors
	 */
	public String getActorsAsString() {
		String s = "";
		for (ActorInfo a : actors) {
			s += a.getPerson().getName() + ", ";
		}
		if (s.endsWith(", "))
			s = s.substring(0, s.length() - 2);
		return s;
	}

	public void setActors(ArrayList<ActorInfo> actors) {
		this.actors = actors;
	}

	public int getNoofVotes() {
		return noofVotes;
	}

	public void setNoofVotes(int noofVotes) {
		this.noofVotes = noofVotes;
	}

	public boolean isColor() {
		return color;
	}

	public int getColorInt() {
		if (color)
			return 1;
		return 0;
	}

	public void setColor(boolean color) {
		this.color = color;
	}

	public void setColorInt(int color) {
		if (color == 0)
			this.color = false;
		else if (color == 1)
			this.color = true;
	}

	public ArrayList<Country> getCountries() {
		return countries;
	}

	public String getCountriesAsString() {
		String s = "";
		for (Country c : countries) {
			s += c.getName() + " / ";
		}
		if (s.endsWith(" / "))
			s = s.substring(0, s.length() - 3);
		return s;
	}

	public void setCountries(ArrayList<Country> countries) {
		this.countries = countries;
	}

	public ArrayList<Person> getDirectors() {
		return directors;
	}

	/**
	 * Returns a list of all directors in a single string
	 * 
	 * @return directors
	 */
	public String getDirectorsAsString() {
		String s = "";
		for (Person d : directors) {
			s += d.getName() + ", ";
		}
		if (s.endsWith(", "))
			s = s.substring(0, s.length() - 2);
		return s;
	}

	public void setDirectors(ArrayList<Person> directors) {
		this.directors = directors;
	}

	public ArrayList<Genre> getGenres() {
		return genres;
	}

	public String getGenresAsString() {
		String s = "";
		for (Genre g : genres) {
			s += g.getGuiLanguageName() + " / ";
		}
		if (s.endsWith(" / "))
			s = s.substring(0, s.length() - 3);
		return s;
	}

	public void setGenres(ArrayList<Genre> genres) {
		this.genres = genres;
	}

	public String getImdbID() {
		return imdbID;
	}

	/**
	 * Sets the IMDb ID from a string containing exactly seven consecutive
	 * numerical digits. The String is parsed and only the numerical part is
	 * stored.
	 * 
	 * @param imdbID
	 *            a string containing the IMDb ID
	 */
	public void setImdbID(String imdbID) {
		if (imdbID == null || imdbID.length() == 0)
			this.imdbID = "";
		Matcher matcher = Pattern.compile("\\D*(\\d{7})\\D*").matcher(imdbID);
		if (matcher.find())
			this.imdbID = matcher.group(1);
	}

	public ArrayList<Language> getLanguages() {
		return imdbLanguages;
	}

	public String getLanguagesAsString() {
		String s = "";
		for (Language l : imdbLanguages) {
			s += l.getName() + " / ";
		}
		if (s.endsWith(" / "))
			s = s.substring(0, s.length() - 3);
		return s;
	}

	public void setLanguages(ArrayList<Language> imdbLanguages) {
		this.imdbLanguages = imdbLanguages;
	}

	public String getPlotOutline() {
		return plotOutline;
	}

	public void setPlotOutline(String plotOutline) {
		this.plotOutline = plotOutline;
	}

	public double getRating() {
		return rating;
	}

	public int getRatingAsInt() {
		Double r = new Double(rating * 10);
		return r.intValue();
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public void setRatingAsInt(int rating) {
		this.rating = (double) rating / 10;
	}

	public int getRunTime() {
		return runTime;
	}

	public void setRunTime(int runTime) {
		this.runTime = runTime;
	}

	public void setRunTime(String runTime) {
		try {
			setRunTime(Integer.valueOf(runTime));
		} catch (NumberFormatException e) {
			setRunTime(0);
		}
	}

	public String getRuntimeAsHourMinuteString() {
		if (runTime > 60)
			return runTime / 60 + "h " + runTime % 60 + "m";
		return runTime % 60 + "m";
	}

	public String getTagline() {
		return tagline;
	}

	public void setTagline(String tagline) {
		this.tagline = tagline;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ArrayList<Person> getWriters() {
		return writers;
	}

	/**
	 * Returns a list of all writers in a single string
	 * 
	 * @return writers
	 */
	public String getWritersAsString() {
		String s = "";
		for (Person w : writers) {
			s += w.getName() + ", ";
		}
		if (s.endsWith(", "))
			s = s.substring(0, s.length() - 2);
		return s;
	}

	public void setWriters(ArrayList<Person> writers) {
		this.writers = writers;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		if (year > 0 && year < 9999)
			this.year = year;
		else
			this.year = 0;
	}

	public void setYear(String year) {
		try {
			int yearInt = Integer.valueOf(year).intValue();
			setYear(yearInt);
		} catch (NumberFormatException e) {
			setYear(0);
		}
	}

	/**
	 * Returns the movie title as it should be displayed in the movie list. If
	 * at custom title exists, it is returned. If not, the normal title is used.
	 * 
	 * @return the movie title
	 */
	public String getDisplayTitle() {
		return getTitle() + "(" + getYear() + ")";
	}

	/**
	 * Returns a modified version of the movie title, to be used for
	 * alphabetical sorting.
	 * 
	 * @return the modified movie title
	 */
	public String getSortTitle() {
		String sortString = getDisplayTitle();

		if (sortString.toLowerCase().startsWith("the "))
			sortString = sortString.substring(4);
		else if (sortString.toLowerCase().startsWith("a "))
			sortString = sortString.substring(2);
		if (sortString.startsWith("'"))
			sortString = sortString.substring(1);

		return sortString;
	}

	/**
	 * Returns the movie's image data
	 * 
	 * @return an array of bytes
	 */
	public byte[] getImageBytes() {
		return imageBytes;
	}

	/**
	 * Sets the movie's image data
	 * 
	 * @param imageBytes
	 */
	public void setImageBytes(byte[] imageBytes) {
		this.imageBytes = imageBytes;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Movie) {
			Movie movie = (Movie) o;
			return this.getImdbID().equals(movie.getImdbID());
		}
		return false;
	}

}