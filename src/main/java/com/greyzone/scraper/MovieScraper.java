package com.greyzone.scraper;

import java.util.List;

import com.greyzone.domain.movie.Movie;

public interface MovieScraper {
	
	public Movie getMovie(String title);
	public List<String> searchTitle(String title);
	
}
