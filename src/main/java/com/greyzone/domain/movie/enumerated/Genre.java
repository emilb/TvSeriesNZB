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

package com.greyzone.domain.movie.enumerated;



public enum Genre {
	action(0, "Action", "Action"),
	adventure(1, "Adventure", "Adventure"),
	animation(2, "Animation", "Animation"),
	biography(3, "Biography", "Biography"),
	comedy(4, "Comedy", "Comedy"),
	crime(5, "Crime", "Crime"),
	documentary(6, "Documentary", "Documentary"),
	drama(7, "Drama", "Drama"),
	family(8, "Family", "Family"),
	fantasy(9, "Fantasy", "Fantasy"),
	filmnoir(10, "Film-Noir", "Film-Noir"), 
	gameshow(11, "Game-Show", "Game-Show"),
	history(12, "History", "History"),
	horror(13, "Horror", "Horror"),
	music(14, "Music", "Music"),
	musical(15, "Musical", "Musical"),
	mystery(16, "Mystery", "Mystery"),
	news(17, "News", "News"),
	realitytv(18, "Reality-TV", "Reality-TV"), 
	romance(19, "Romance", "Romance"),
	scifi(20, "Sci-Fi", "Sci-Fi"), 
	shortmovie(21, "Short", "Short"),
	sport(22, "Sport", "Sport"),
	talkshow(23, "Talk-Show", "Talk-Show"),
	thriller(24, "Thriller", "Thriller"),
	war(25, "War", "War"),
	western(26, "Western", "Western");
	
	private int id;
	private String IMDBname;
	private String guiLanguageName;
	
	Genre(int id, String IMDBname, String guiLanguageName) {
		this.id = id;
		this.IMDBname = IMDBname;
		this.guiLanguageName = guiLanguageName;
	}
	
	public int getID() {
		return id;
	}
	
	public String getIMDBname() {
		return IMDBname;
	}
	
	public String getGuiLanguageName() {
		return guiLanguageName;
	}
	
	public String toString() {
		return IMDBname;
	}
	
	public static Genre StringToEnum(String string) {
		if(string == null) {
			return null;
		}
		for(Genre g : Genre.values())
			if(string.toLowerCase().equals(g.toString().toLowerCase()))
				return g;
		
		return null;
	}
	
	/**
	 * Looks up the correct enum value from an int ID
	 * @param i the ID to look up
	 * @return the associated enum
	 */
	public static Genre intToEnum(int id) {
		for(Genre g : Genre.values())
			if(id == g.getID())
				return g;
			System.out.println("Unrecognised genre ID: " + id);
		return null;
	}
}
