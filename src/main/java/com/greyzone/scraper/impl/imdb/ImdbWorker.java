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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greyzone.domain.movie.Movie;
import com.greyzone.domain.movie.enumerated.CONST;

@Service
public class ImdbWorker {

	private static String IMDB_URL = "http://www.imdb.com/title/tt";
	private static String IMDB_SEARCH_URL = "http://www.imdb.com/find?s=tt&q=";
	private static String IMDB_PERSON_URL = "http://www.imdb.com/name/nm";
	
	/**
	 * Updates the given movie with new data from IMDb. Presents search results (if any) in a nice dialog. 
	 * @param shell - parent shell 
	 * @param movie - the movie to update
	 * @return the updated movie
	 * @throws IOException
	 */
	public Movie update(Movie movie) throws IOException { //TODO dialog instead of throwing an exception
		if(StringUtils.isEmpty(movie.getImdbID())) {
			/*
			 * We end up here if the movie doesn't have an IMDb URL yet, or if it is malformed.
			 */

			//Can't search if the movie has no title
			if(movie.getTitle() == null || movie.getTitle().length() == 0) {
				throw new RuntimeException("Can't search for a movie without a title.");
			}

			//Run search with progress bar
			ImdbSearcher searcher = new ImdbSearcher(movie);
			try {
				searcher.run();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ImdbSearchResult[] searchResults = searcher.getSearchResults();
			
			//No search results found!
			if(searchResults == null) {
				return movie;
			}

			//Update the movie with the new ID
			movie.setImdbID(searchResults[0].getImdbId());
		}

		//At this point we have a valid IMDb URL
		ImdbDownloader downloader = new ImdbDownloader(movie);
		try {
			downloader.run();
		} catch (InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return downloader.getMovie();
	}

	public class ImdbSearcher {
		private ImdbSearchResult[] searchResults;
		private Movie movie;
		public ImdbSearcher(Movie m) {
			movie = m;
		}
		public void run() throws InvocationTargetException, InterruptedException {
				System.out.println("Searching for "+movie.getTitle());
			try {
				ImdbParser parser = new ImdbParser(DownloadWorker.downloadHtml(IMDB_SEARCH_URL, 
						new NameValuePair("s", "tt"),
						new NameValuePair("q", movie.getTitle())));
				searchResults = parser.getSearchResults();
			} catch (Exception e) {
				throw new InvocationTargetException(e);
			}
		}
		public ImdbSearchResult[] getSearchResults() {
			return searchResults;
		}
	}

	private class ImdbDownloader {
		protected Movie movie;
		public ImdbDownloader(Movie m) {
			movie = m;
		}
		
		public Movie getMovie() {
			return movie;
		}
		
		public void run() throws InvocationTargetException, InterruptedException {
			try {
				ImdbParser parser = new ImdbParser(DownloadWorker.downloadHtml(IMDB_URL + movie.getImdbID()));
			

				System.out.println("votes: " + parser.getNoofVotes());
				movie.setTitle(parser.getTitle());
				movie.setYear(parser.getYear());
				movie.setRunTime(parser.getRuntime());
				movie.setRating(parser.getRating());
				movie.setNoofVotes(parser.getNoofVotes());
				movie.setColor(parser.isColor());
				movie.setPlotOutline(parser.getPlot());
				movie.setTagline(parser.getTagline());

				movie.setLanguages(parser.getLanguages());
				movie.setCountries(parser.getCountries());
				movie.setGenres(parser.getGenres());
				movie.setDirectors(parser.getDirectors());
				movie.setWriters(parser.getWriters());
				movie.setActors(parser.getActors());

				movie.setImageBytes(DownloadWorker.downloadBinary(parser.getImageURL().toString()));
			
			} catch (Exception e) {
				throw new InvocationTargetException(e);
			}
		}
	}
}

