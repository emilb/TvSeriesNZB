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

package com.greyzone.scraper.impl.imdb;


public class ImdbSearchResult {
	private String imdbId;
	private String title;
	private String year;
	private String[] altTitle;
	private String imageURL;
	
	public ImdbSearchResult(String imdbId, String title, String year, String[] altTitle) {
		this.imdbId = imdbId;
		this.year = year;
		this.title = title;
		this.altTitle = altTitle;
		this.imageURL = "";
	}

	public String[] getAltTitles() {
		return altTitle;
	}

	public String getImdbId() {
		return imdbId;
	}

	public String getTitle() {
		return title;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
//		if (imageURL != null && imageURL.length() > 0) {
//			try {
//				URL url = new URL(imageURL);
//				byte[] bytes = new DownloadWorker(url).downloadBytes();
//				id = new ImageData(new ByteArrayInputStream(bytes));
//			} catch (IOException e) {
//				e.printStackTrace();
//				id = null;
//			}
//		}
	}
		
	public String toString() {
		String altTitles = "";
		
		if(altTitle != null) {	
			for(String t : altTitle) {
				altTitles += "\naka " + t;
			}
		}
		return title + " (" + year + ") " + imdbId + " " + imageURL + altTitles + "\n";
	}

	public String getYear() {
		return year;
	}
}
