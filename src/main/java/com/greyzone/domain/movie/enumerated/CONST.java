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

import java.io.ByteArrayInputStream;
import java.net.URL;

/**
 * This class provides a number of public constants and static methods.
 * @author Tor Arne Lye
 *
 */
public class CONST {
	
	/**
	 * Region 0/region free. Value is 1 &lt;&lt; 8. 
	 */
	public static final int R0 = 1 << 8;
	
	/**
	 * Region 1/A. Value is 1.
	 */
	public static final int R1 = 1;
	
	/**
	 * Region 2/B. Value is 1 &lt;&lt; 1.
	 */
	public static final int R2 = 1 << 1;
	
	/**
	 * Region 3/C. Value is 1 &lt;&lt; 2.
	 */
	public static final int R3 = 1 << 2;
	
	/**
	 * Region 4. Value is 1 &lt;&lt; 3.
	 */
	public static final int R4 = 1 << 3;
	
	/**
	 * Region 5. Value is 1 &lt;&lt; 4.
	 */
	public static final int R5 = 1 << 4;
	
	/**
	 * Region 6. Value is 1 &lt;&lt; 5.
	 */
	public static final int R6 = 1 << 5;
	
	/**
	 * Region 7. Value is 1 &lt;&lt; 6.
	 */
	public static final int R7 = 1 << 6;
	
	/**
	 * Region 8. Value is 1 &lt;&lt; 7.
	 */
	public static final int R8 = 1 << 7;
	
	/**
	 * Sort ascending. Value is -1.
	 */
	public static final int SORT_ASCENDING = -1;
	
	/**
	 * Sort descending. Value is -2.
	 */
	public static final int SORT_DESCENDING = -2;
	
	/**
	 * Sort by movie ID. Value is 0.
	 */
	public static final int SORT_BY_ID = 0;
	
	/**
	 * Sort by title. Value is 1.
	 */
	public static final int SORT_BY_TITLE = 1;
	
	/**
	 * Sort by year. Value is 2.
	 */
	public static final int SORT_BY_YEAR = 2;
	
	/**
	 * Sort by format type. Value is 3;
	 */
	public static final int SORT_BY_FORMAT = 3;
	
	/**
	 * Sort by IMDb rating. Value is 4;
	 */
	public static final int SORT_BY_RATING = 4;
	
	/**
	 * Return value when the &quot;Save&quot; button in a message dialog is pressed. Value is 0.
	 */
	public static final int ANSWER_SAVE = 0;
	
	/**
	 * Return value when the &quot;Don't save&quot; button in a message dialog is pressed. Value is 1.
	 */
	public static final int ANSWER_DONT_SAVE = 1;
	
	/**
	 * Return value when the &quot;Cancel&quot; button in a message dialog is pressed. Value us 3.
	 */
	public static final int ANSWER_CANCEL = 2;
	
	/**
	 * Film
	 */
	public static final int MOVIETYPE_FILM = 0;
	
	/**
	 * Straight-to-video movie
	 */
	public static final int MOVIETYPE_VIDEOMOVIE = 1;
	
	/**
	 * TV-movie
	 */
	public static final int MOVIETYPE_TVMOVIE = 2;
	
	/**
	 * TV-series
	 */
	public static final int MOVIETYPE_TVSERIES = 3;
	
	/**
	 * TV miniseries
	 */
	public static final int MOVIETYPE_MINISERIES = 4;
	
	/**
	 * Movie serial
	 */
	public static final int MOVIETYPE_MOVIESERIAL = 5;
	
	
	/**
	 * Converts a boolean to an int.
	 * @param b a boolean
	 * @return 1 if true, 0 if false
	 */
	public static int booleanToInt(boolean b) {
		if(b) return 1;
		else return 0;
	}
	
	/**
	 * Converts an int to a boolean
	 * @param i an int
	 * @return false if 0, true otherwise
	 */
	public static boolean intToBoolean(int i) {
		if(i==0) return false;
		else return true;
	}
	
	/**
	 * Converts an int to a 7-digit IMDb ID.
	 * @param i any int
	 * @return the IMDb ID, as a String
	 */
	public static String intToImdbId(int i) {
		if(i<0 || i>9999999)
			return null;
		
		String s = "" + i;
		while(s.length()>7)
			s = "0" + s;
		
		return s;
	}
	
	/**
	 * Replaces special character codes (ISO/IEC 8859-1) in 
	 * strings extracted from HTML code with UTF characters
	 * @param string input
	 * @return The cleaned-up version of the input
	 */
	public static String fixHtmlCharacters(String string) {
		//0-31 are unused
		//32 is space
		
		string = string.replace("&#33;", "!");
		string = string.replace("&#34;", "\"");
		string = string.replace("&#x22;", "\"");
		string = string.replace("&#35;", "#");
		string = string.replace("&#36;", "$");
		string = string.replace("&#37;", "%");
		string = string.replace("&#38;", "&");
		string = string.replace("&#39;", "'");
		string = string.replace("&#40;", "(");
		string = string.replace("&#41;", ")");
		string = string.replace("&#42;", "*");
		string = string.replace("&#43;", "+");
		string = string.replace("&#44;", ",");
		string = string.replace("&#45;", "-");
		string = string.replace("&#46;", ".");
		string = string.replace("&#47;", "/");
		
		//48-57 are digits 0-9

		string = string.replace("&#58;", ":");
		string = string.replace("&#59;", ";");
		string = string.replace("&#60;", "<");
		string = string.replace("&#61;", "=");
		string = string.replace("&#62;", ">");
		string = string.replace("&#63;", "?");
		
		string = string.replace("&#64;", "@");
		//65-90 are letters A-Z
		string = string.replace("&#91;", "[");
		string = string.replace("&#92;", "\\");
		string = string.replace("&#93;", "]");
		string = string.replace("&#94;", "^");
		string = string.replace("&#95;", "_");
		
		string = string.replace("&#96;", "`");
		//97-122 are letters a-z
		string = string.replace("&#123;", "{");
		string = string.replace("&#124;", "|");
		string = string.replace("&#125;", "}");
		string = string.replace("&#126;", "~");
		
		//127-159 are unused
		
		string = string.replace("&#160;", " "); //A non-breaking space is replaced with normal space
		string = string.replace("&#161;", "¡");
		string = string.replace("&#162;", "¢");
		string = string.replace("&#163;", "£");
		string = string.replace("&#164;", "¤");
		string = string.replace("&#165;", "¥");
		string = string.replace("&#166;", "¦");
		string = string.replace("&#167;", "§");
		string = string.replace("&#168;", "¨");
		string = string.replace("&#169;", "©");
		string = string.replace("&#170;", "ª");
		string = string.replace("&#171;", "«");
		string = string.replace("&#172;", "¬");
		string = string.replace("&#173;", ""); //A soft hyphen is just removed
		string = string.replace("&#174;", "®");
		string = string.replace("&#175;", "¯");
		
		string = string.replace("&#176;", "°");
		string = string.replace("&#177;", "±");
		string = string.replace("&#178;", "²");
		string = string.replace("&#179;", "³");
		string = string.replace("&#180;", "´");
		string = string.replace("&#181;", "µ");
		string = string.replace("&#182;", "¶");
		string = string.replace("&#183;", "·");
		string = string.replace("&#184;", "¸");
		string = string.replace("&#185;", "¹");
		string = string.replace("&#186;", "º");
		string = string.replace("&#187;", "»");
		string = string.replace("&#188;", "¼");
		string = string.replace("&#189;", "½");
		string = string.replace("&#190;", "¾");
		string = string.replace("&#191;", "¿");
		
		string = string.replace("&#192;", "À");
		string = string.replace("&#193;", "Á");
		string = string.replace("&#194;", "Â");
		string = string.replace("&#195;", "Ã");
		string = string.replace("&#196;", "Ä");
		string = string.replace("&#197;", "Å");
		string = string.replace("&#198;", "Æ");
		string = string.replace("&#199;", "Ç");
		string = string.replace("&#200;", "È");
		string = string.replace("&#201;", "É");
		string = string.replace("&#202;", "Ê");
		string = string.replace("&#203;", "Ë");
		string = string.replace("&#204;", "Ì");
		string = string.replace("&#205;", "Í");
		string = string.replace("&#206;", "Î");
		string = string.replace("&#207;", "Ï");
		
		string = string.replace("&#208;", "Ð");
		string = string.replace("&#209;", "Ñ");
		string = string.replace("&#210;", "Ò");
		string = string.replace("&#211;", "Ó");
		string = string.replace("&#212;", "Ô");
		string = string.replace("&#213;", "Õ");
		string = string.replace("&#214;", "Ö");
		string = string.replace("&#215;", "×");
		string = string.replace("&#216;", "Ø");
		string = string.replace("&#217;", "Ù");
		string = string.replace("&#218;", "Ú");
		string = string.replace("&#219;", "Û");
		string = string.replace("&#220;", "Ü");
		string = string.replace("&#221;", "Ý");
		string = string.replace("&#222;", "Þ");
		string = string.replace("&#223;", "ß");
		
		string = string.replace("&#224;", "à");
		string = string.replace("&#225;", "á");
		string = string.replace("&#226;", "â");
		string = string.replace("&#227;", "ã");
		string = string.replace("&#228;", "ä");
		string = string.replace("&#229;", "å");
		string = string.replace("&#230;", "æ");
		string = string.replace("&#231;", "ç");
		string = string.replace("&#232;", "è");
		string = string.replace("&#233;", "é");
		string = string.replace("&#234;", "ê");
		string = string.replace("&#235;", "ë");
		string = string.replace("&#236;", "ì");
		string = string.replace("&#237;", "í");
		string = string.replace("&#238;", "î");
		string = string.replace("&#239;", "ï");
		
		string = string.replace("&#240;", "ð");
		string = string.replace("&#241;", "ñ");
		string = string.replace("&#242;", "ò");
		string = string.replace("&#243;", "ó");
		string = string.replace("&#244;", "ô");
		string = string.replace("&#245;", "õ");
		string = string.replace("&#246;", "ö");
		string = string.replace("&#247;", "÷");
		string = string.replace("&#248;", "ø");
		string = string.replace("&#249;", "ù");
		string = string.replace("&#250;", "ú");
		string = string.replace("&#251;", "û");
		string = string.replace("&#252;", "ü");
		string = string.replace("&#253;", "ý");
		string = string.replace("&#254;", "þ");
		string = string.replace("&#255;", "ÿ");
		
		return string;
	}
	
}
