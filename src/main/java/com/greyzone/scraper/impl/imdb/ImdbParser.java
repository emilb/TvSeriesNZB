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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.greyzone.domain.movie.ActorInfo;
import com.greyzone.domain.movie.Person;
import com.greyzone.domain.movie.enumerated.CONST;
import com.greyzone.domain.movie.enumerated.Country;
import com.greyzone.domain.movie.enumerated.Genre;
import com.greyzone.domain.movie.enumerated.Language;


public class ImdbParser {
	private String html;
	
	/**
	 * The default constructor
	 * @param html - a HTML document 
	 */
	protected ImdbParser(String html) {
		this.html = html;
	}
	
	/**
	 * Finds the movie's IMDb ID, if the open document is a movie page.
	 * The ID is usually already known, so this method is used only in certain special cases. 
	 * @return IMDb ID
	 */
	protected String getID() {
		//TODO Is this safe? What if there are links to other movies on the page?
		Pattern patternID = Pattern.compile("/title/tt(\\d{7})/");
		Matcher matcherID = patternID.matcher(html);
		if (matcherID.find())
			return matcherID.group(1);
		return null;
	}
	
	/**
	 * Returns the title of the movie, if the open document is a movie page.
	 * @return the movie title
	 */
	protected String getTitle() {
		/*
		 * Examples:
		 * <h1>The Arrival <span>(<a href="/Sections/Years/1996">1996</a>)</span></h1>
		 * <h1>Mazes and Monsters <span>(<a href="/Sections/Years/1982">1982</a>) (TV)</span></h1>
		 * <h1>Clone Wars: Bridging the Saga <span>(<a href="/Sections/Years/2005">2005</a>) (V)</span></h1>
		 * <h1>&#34;The Simpsons&#34; <span>(<a href="/Sections/Years/1989">1989</a>)<span>TV series&#160;1989-????</span></span></h1>
		 * <h1>&#34;Star Trek&#34; <span>(<a href="/Sections/Years/1973">1973</a>)<span>TV series&#160;1973-1975</span></span></h1>
		 * <h1>&#34;Star Trek&#34;<br><span><em>The Cage</em> (1966)</span></h1>
		 */
		Pattern patternTitle = Pattern.compile("<h1>([^<]+)(?:<br>)?<span>(?:<em>)?([^<]+)?(?:</em>)?\\s?\\(");
		Matcher matcherTitle = patternTitle.matcher(html);
		if (matcherTitle.find()) {
			String title = CONST.fixHtmlCharacters(matcherTitle.group(1).trim());
			
			//Remove quote at beginning and end of title for TV-series
			if(title.startsWith("\"") && title.endsWith("\""))
				title = title.substring(1, title.length()-1);
			
			//Add "sub" title, as at http://www.imdb.com/title/tt0059753/
			System.out.println("group count! "+matcherTitle.groupCount());
			if(matcherTitle.group(2) != null)
				title += ": "+CONST.fixHtmlCharacters(matcherTitle.group(2).trim());
			
			return title;
		}
		return null;
	}
		
	/**
	 * Returns the movie's production year, if the open document is a movie page.
	 * @return a year
	 */
	protected int getYear() {
		Pattern patternYear = Pattern.compile("<a href=\"/Sections/Years/\\d{4}/\">(\\d{4})</a>");
		Matcher matcherYear = patternYear.matcher(html);
		if (matcherYear.find()) {
			return Integer.parseInt(matcherYear.group(1));	
		}
		return 0;
	}
	
	/**
	 * Returns an array of the movie's genres, if the open document is a movie page.
	 * @return an array of genres, or an empty array if none were found.
	 */
	protected ArrayList<Genre> getGenres() {
		/*
		 * Examples:
		 * <a href="/Sections/Genres/Crime/">Crime</a>
		 * <a href="/Sections/Genres/Film-Noir/">Film-Noir</a> 
		 * <a href="/Sections/Genres/Thriller/">Thriller</a>
		 */
		Pattern patternGenre = Pattern.compile("<a href=\"/Sections/Genres/[^/]+/\">([^<]+)</a>");
		Matcher matcherGenre = patternGenre.matcher(html);
		ArrayList<Genre> temp = new ArrayList<Genre>();
		while (matcherGenre.find()) {
			temp.add(Genre.StringToEnum(matcherGenre.group(1)));
		}
		return temp;
	}
	
	/**
	 * Returns the movie's tagline, if the open document is a movie page.
	 * @return the tagline
	 */
	protected String getTagline() {
		Pattern patternTagline = Pattern.compile("<h5>Tagline:</h5>\\s*([^<]+)\\s*<a");
		Matcher matcherTagline = patternTagline.matcher(html);
		if (matcherTagline.find()) {
			return matcherTagline.group(1);	
		}
		return "";
	}
	
	/**
	 * Returns the movie's plot outline, if the open document is a movie page.
	 * @return the plot outline
	 */
	protected String getPlot() {
		Pattern patternPlot = Pattern.compile("<h5>Plot:</h5>\\s*([^<]+)\\s*<a");
		Matcher matcherPlot = patternPlot.matcher(html);
		if (matcherPlot.find()) {
			return matcherPlot.group(1);
		}
		return "";
	}
	
	/**
	 * Returns the movie's IMDb rating, if the open document is a movie page.
	 * @return the rating
	 */
	protected double getRating() {
		Pattern patternRating = Pattern.compile("<b>([0-9\\.]+)/10</b>");
		Matcher matcherRating = patternRating.matcher(html);
		if (matcherRating.find()) {
			return Double.valueOf(matcherRating.group(1)).doubleValue();	
		}
		return 0.0;
	}

	/**
	 * Returns the movie's IMDb rating, if the open document is a movie page.
	 * @return the rating
	 */
	protected int getNoofVotes() {
		Pattern patternRating = Pattern.compile("([0-9\\,]+) votes</a>");
		Matcher matcherRating = patternRating.matcher(html);
		
		if (matcherRating.find()) {
			String nvotes = matcherRating.group(1); 
			return Integer.parseInt(nvotes.replaceAll(",", ""));
		}
		return 0;
	}
	
	/**
	 * Returns the movie's runtime, if the open document is a movie page.
	 * @return runtime
	 */
	protected int getRuntime() {
		Pattern patternRuntime = Pattern.compile("<h5>Runtime:</h5>[^0-9]*([0-9]+)\\smin");
		Matcher matcherRuntime = patternRuntime.matcher(html);
		if(matcherRuntime.find())
			return Integer.valueOf(matcherRuntime.group(1));
		return 0;
	}
	
	/**
	 * Returns whether or not the movie has color, if the open document is a movie page.
	 * @return true if the movie is in color, false if it is black and white
	 */
	protected boolean isColor() {
		Pattern patternColor = Pattern.compile("<h5>Color:</h5>\\s*<a[^>]+>([^<]+)</a>");
		Matcher matcherColor = patternColor.matcher(html);
		if(matcherColor.find())
			if(matcherColor.group(1).equals("Black and White"))
				return false;
		return true;
	}
	
	/**
	 * Returns the movie's languages, if the open document is a movie page.
	 * @return an ArrayList of languages
	 */
	protected ArrayList<Language> getLanguages() {
		Pattern patternLanguage1 = Pattern.compile("<h5>Language:</h5>(.+?)</div>");
		Pattern patternLanguage2 = Pattern.compile("<a href=\"/Sections/Languages/([^/]+)/\">");
		Matcher matcherLanguage = patternLanguage1.matcher(html);
		
		ArrayList<Language> tempList = new ArrayList<Language>(); 
		
		if(matcherLanguage.find()) {
			matcherLanguage = patternLanguage2.matcher(matcherLanguage.group(1));

			while(matcherLanguage.find()) {
				Language l = Language.StringToEnum(matcherLanguage.group(1));
				if(l == null)
					throw new RuntimeException("Unknown language: " + matcherLanguage.group(1));
				else {
					if(!tempList.contains(l)) tempList.add(l);
				}
			}
		}
			
		return tempList;
	}
	
	/**
	 * Returns the countries in which the movie is made, if the open document is a movie page.
	 * @return an ArrayList of countries
	 */
	protected ArrayList<Country> getCountries() {
		Pattern patternCountry1 = Pattern.compile("<h5>Country:</h5>(.+?)</div>");
		Pattern patternCountry2 = Pattern.compile("<a href=\"/Sections/Countries/([^/]+)/\">");
		Matcher matchercountry = patternCountry1.matcher(html);
		
		ArrayList<Country> tempList = new ArrayList<Country>(); 
		
		if(matchercountry.find()) {
			matchercountry = patternCountry2.matcher(matchercountry.group(1));

			while(matchercountry.find()) {
				tempList.add(Country.StringToEnum(matchercountry.group(1)));
			}
		}
			
		return tempList;
	}
	
	/**
	 * Returns the URL for the movie's poster image, if the open document is a movie page.
	 * @return the image URL, or null if no image was found.
	 */
	protected URL getImageURL() {
		Pattern pattern = Pattern.compile("<a name=\"poster\"[^>]+><img[^>]+src=\"(\\S+)\"[^>]+>");
		Matcher matcher = pattern.matcher(html);
		if (matcher.find()) {
			try {
				return new URL(matcher.group(1));
			} catch (MalformedURLException e) {
				return null;
			}
		}
		return null;
	}
	
	/**
	 * Returns the movie's actors, if the open document is a movie page.
	 * @return an ArrayList of ActorInfo objects
	 */
	protected ArrayList<ActorInfo> getActors() {
		//Typical examples:
		//<td class="nm"><a href="/name/nm0132257/">Bruce Campbell</a></td><td class="ddd"> ... </td><td class="char">Renaldo 'The Heel'</td>
		//<td class="nm"><a href="/name/nm0001511/">Lee Marvin</a></td><td class="ddd"> ... </td><td class="char"><a href="/character/ch0023281/">The Sergeant</a></td>
		//Beware of the following cases:
		//<td class="char"><a href="/character/ch0008987/">Nick</a> (as Nephi Pomaikai Brown)</td>
		//<td class="char"><a href="/character/ch0001439/">Mr. Spock</a> (80&#160;episodes, 1966-1969)</td>
		//<td class="nm"><a href="/name/nm0921942/">Billy West</a></td><td class="ddd"> ... </td><td class="char"><a href="/character/ch0013043/">Philip J. Fry</a> / <a href="/character/ch0013043/">Frydo</a> / <a href="/character/ch0047043/">Professor Hubert Farnsworth</a> / <a href="/character/ch0047043/">The Great Wizard Greyfarn</a> / <a href="/character/ch0013047/">Dr. Zoidberg</a> / <a href="/character/ch0013047/">Monster Zoidberg</a> / Farmer / Dwarf / <a href="/character/ch0047024/">Smitty</a> / Treedledum / Additional Voices (voice)</td>
		
		Pattern actorPattern = Pattern.compile("<td class=\"nm\"><a href=\"/name/nm(\\d+)/\">([^<>]+)</a></td><td class=\"ddd\">\\s\\.\\.\\.\\s</td><td class=\"char\">(.*?)</td>");
		Matcher actorMatcher = actorPattern.matcher(html);
		ArrayList<ActorInfo> templist = new ArrayList<ActorInfo>();
		int counter = 0;
		
		while (actorMatcher.find()) {
		/*
		 * Group 3 matches everything between <td class="char"> and </td>
		 * i.e. the character description. The following statement removes
		 * HTML entities from that string, e.g.:
		 * <a href="/character/ch0008987/">Nick</a> (as Nephi Pomaikai Brown)
		 * is shortened to:
		 * Nick (as Nephi Pomaikai Brown)
		 */
			String character = actorMatcher.group(3).replaceAll("<.+?>", "");
			
			templist.add(new ActorInfo(counter, 
					new Person(actorMatcher.group(1), 
					CONST.fixHtmlCharacters(actorMatcher.group(2))),  
					CONST.fixHtmlCharacters(character)));
			counter++;
		}

		return templist;
	}
	
	/**
	 * Returns the movie's directors, if the open document is a movie page.
	 * @return an array of directors
	 */
	protected ArrayList<Person> getDirectors() {
		Pattern pattern = Pattern.compile("<h5>Directors?:</h5>(.+?)</div>");
		Matcher matcher = pattern.matcher(html);
		ArrayList<Person> personArray = new ArrayList<Person>();

		if(matcher.find()) {
			Pattern personPattern = Pattern.compile("<a\\shref=\"/name/nm(\\d{7})/\">([^<]+)</a>");
			Matcher personMatcher = personPattern.matcher(matcher.group(1));
			while(personMatcher.find()) {
				Person p = new Person(personMatcher.group(1), CONST.fixHtmlCharacters(personMatcher.group(2)));
				if(!personArray.contains(p))
					personArray.add(p);
			}
		}
		
		return personArray;
	}
	
	/**
	 * Returns the movie's writers, if the open document is a movie page.
	 * @return an array of writers
	 */
	protected ArrayList<Person> getWriters() {
		Matcher matcher = Pattern.compile("<h5>Writers?(\\s<a[^<]+</a>)?:</h5>(.+?)</div>").matcher(html);
		ArrayList<Person> personArray = new ArrayList<Person>();
		
		if(matcher.find()) {
			Pattern personPattern = Pattern.compile("<a\\shref=\"/name/nm(\\d{7})/\">([^<]+)</a>");
			Matcher personMatcher = personPattern.matcher(matcher.group(2));
			while(personMatcher.find()) {
				Person p = new Person(personMatcher.group(1), CONST.fixHtmlCharacters(personMatcher.group(2)));
				if(!personArray.contains(p))
					personArray.add(p);
			}
		}

		return personArray;
	}
	
	/**
	 * This method is called when a search result page should be parsed.
	 * This method will call the necessary private helper methods. 
	 * @return An array of search results, or null if no results were found.
	 */
	protected ImdbSearchResult[] getSearchResults() {
		
		/*
		 * Sometimes a search only gives one result. In these cases IMDb bypasses the
		 * search result page and goes directly to the movie page. The following is
		 * to handle this special case.
		 */
		Pattern moviePagePattern = Pattern.compile("<div\\s+id=\"tn15\"\\s+class=\"maindetails\">");
		Matcher moviePageMatcher = moviePagePattern.matcher(html);
		if(moviePageMatcher.find()) {
			//TODO fix type and altTitles, these are currently hard-coded with default values
			System.out.println("Only one search result, was redirected to movie page");
			return new ImdbSearchResult[]{new ImdbSearchResult(getID(), getTitle(), "" + getYear(), new String[0])};
		}
		
		/*
		 * If we got this far, the current document is a search result page. We will now
		 * try to parse it and get the results
		 */
		
		Pattern pattern = Pattern.compile(
				"<tr>\\s*<td valign=\"top\">(.+?)</td>\\s*" +
				"<td align=\"right\" valign=\"top\">.+?</td>\\s*" +
				"<td valign=\"top\">(.+?)</td>\\s*</tr>"
				);
		Matcher matcher = pattern.matcher(html);
		
		ArrayList<ImdbSearchResult> templist = new ArrayList<ImdbSearchResult>(); 

		while(matcher.find()) {
			//Call a separate method to find the image URL, if any
			String imgUrl = imgSearchResult(matcher.group(1));
			
			//Call a method to parse the title, year, etc. This method will also return the ImdbSearchResult object.
			ImdbSearchResult result = titleSearchResult(matcher.group(2));
			
			//Add the image to the ImdbSearchResult object and add the ImdbSearchResult to the result list. 
			if(result != null) {
				result.setImageURL(imgUrl);
				templist.add(result);
			}
		}
		
		//We want to return an array, so the ArrayList must be converted
		if(templist.size() > 0) {
			return templist.toArray(new ImdbSearchResult[0]);
		}
		
		//if no results were found, return null
		return null;
	}
	
	/**
	 * Internal helper method, only called from getSearchResults()
	 * @param group - a substring of the HTML document
	 * @return an image URL
	 */
	private String imgSearchResult(String group) {
		Pattern pattern = Pattern.compile("<a[^>]+><img\\ssrc=\"([^\"]+)\"[^>]+>\\s*</a>");
		Matcher matcher = pattern.matcher(group);
		
		if(matcher.find()) {
			return matcher.group(1);
		}
		
		return null;
	}
	
	/**
	 * Internal helper method, only called from getSearchResults()
	 * @param group - a substring of the HTML document
	 * @return an ImdbSearchResult instance that will be added to the list of results 
	 */
	private ImdbSearchResult titleSearchResult(String group) {
		Pattern pattern = Pattern.compile(
				"<a\\shref=\"/title/tt(\\d{7})/\">([^<]+)</a>\\s\\((\\d{4})[IVX/]*\\)\\s*([()A-Z]{0,6})(.*)\\z"
				);
		Matcher matcher = pattern.matcher(group);
		
		String title;
		String year;
		String id;
		ImdbSearchResult result = null;
		
		if(matcher.find()) {
			id = matcher.group(1);
			title = matcher.group(2);
			year = matcher.group(3);
			
			title = CONST.fixHtmlCharacters(title);
						
			String[] altTitles = altTitleSearchResult(matcher.group(5));
			
			result = new ImdbSearchResult(id, title, year, altTitles);
		}
		return result;
	}
	
	/**
	 * Internal helper method, only called from titleSearchResult()
	 * @param group - a substring of the HTML document
	 * @return an array of alternative titles
	 */
	private String[] altTitleSearchResult(String group) {
		Pattern pattern = Pattern.compile("&#160;aka\\s*<em>(.+?)((<br>)|\\z)");
		Matcher matcher = pattern.matcher(group);
		
		ArrayList<String> templist = new ArrayList<String>();
		
		while (matcher.find()) {
			String title = matcher.group(1);
			title = title.replaceAll("<.+?>", "");
			
			if(title.length() > 0)
				templist.add(title);
		}
				
		if(templist.size() > 0) {
			return templist.toArray(new String[0]);
		}
		return null;
	}
}
