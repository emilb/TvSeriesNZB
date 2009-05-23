package com.greyzone.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class SearchSettings {

	public static final Map<String, String> sourceMap;
	public static final Map<String, String> formatMap;
	public static final Map<String, String> languageMap;
	public static final Map<String, String> subtitleMap;

	static {
		sourceMap = new HashMap<String, String>();
		formatMap = new HashMap<String, String>();
		languageMap = new HashMap<String, String>();
		subtitleMap = new HashMap<String, String>();

		sourceMap.put("CAM", "a:videos~CAM");
		sourceMap.put("Screener", "a:videos~Screener");
		sourceMap.put("TeleCine", "a:videos~TeleCine");
		sourceMap.put("R5Retail", "a:videos~R5Retail");
		sourceMap.put("TeleSync", "a:videos~TeleSync");
		sourceMap.put("Workprint", "a:videos~Workprint");
		sourceMap.put("VHS", "a:videos~VHS");
		sourceMap.put("DVD", "a:videos~DVD");
		sourceMap.put("HD-DVD", "videos~HD-DVD");
		sourceMap.put("Blu-Ray", "videos~Blu-Ray");
		sourceMap.put("TVCap", "a:videos~TVCap");
		sourceMap.put("HDTV", "a:videos~HDTV");
		sourceMap.put("Unknown", "a:videos~Unknown");

		formatMap.put("DivX", "a:videof~DivX");
		formatMap.put("XviD", "a:videof~XviD");
		formatMap.put("DVD", "a:videof~DVD");
		formatMap.put("Blu-Ray", "videof~Blu-Ray");
		formatMap.put("HD-DVD", "videof~HD-DVD");
		formatMap.put("HD.TS", "videof~HD.TS");
		formatMap.put("H.264", "videof~H.264");
		formatMap.put("x264", "a:videof~x264");
		formatMap.put("AVCHD", "a:videof~AVCHD");
		formatMap.put("SVCD", "a:videof~SVCD");
		formatMap.put("VCD", "a:videof~VCD");
		formatMap.put("WMV", "a:videof~WMV");
		formatMap.put("iPod", "a:videof~iPod");
		formatMap.put("PSP", "a:videof~PSP");
		formatMap.put("ratDVD", "a:videof~ratDVD");
		formatMap.put("Other", "a:videof~Other");
		formatMap.put("720p", "a:videof~720p");
		formatMap.put("1080i", "a:videof~1080i");
		formatMap.put("1080p", "a:videof~1080p");
		formatMap.put("Unknown", "a:videof~Unknown");

		languageMap.put("English", "a:lang~English");
		languageMap.put("French", "a:lang~French");
		languageMap.put("Spanish", "a:lang~Spanish");
		languageMap.put("German", "a:lang~German");
		languageMap.put("Italian", "a:lang~Italian");
		languageMap.put("Danish", "a:lang~Danish");
		languageMap.put("Dutch", "a:lang~Dutch");
		languageMap.put("Japanese", "a:lang~Japanese");
		languageMap.put("Cantonese", "a:lang~Cantonese");
		languageMap.put("Mandarin", "a:lang~Mandarin");
		languageMap.put("Korean", "a:lang~Korean");
		languageMap.put("Russian", "a:lang~Russian");
		languageMap.put("Polish", "a:lang~Polish");
		languageMap.put("Vietnamese", "a:lang~Vietnamese");
		languageMap.put("Swedish", "a:lang~Swedish");
		languageMap.put("Norwegian", "a:lang~Norwegian");
		languageMap.put("Finnish", "a:lang~Finnish");
		languageMap.put("Turkish", "a:lang~Turkish");
		languageMap.put("Unknown", "a:lang~Unknown");

		subtitleMap.put("English", "a:sub~English");
		subtitleMap.put("French", "a:sub~French");
		subtitleMap.put("Spanish", "a:sub~Spanish");
		subtitleMap.put("German", "a:sub~German");
		subtitleMap.put("Italian", "a:sub~Italian");
		subtitleMap.put("Danish", "a:sub~Danish");
		subtitleMap.put("Dutch", "a:sub~Dutch");
		subtitleMap.put("Japanese", "a:sub~Japanese");
		subtitleMap.put("Cantonese", "a:sub~Cantonese");
		subtitleMap.put("Mandarin", "a:sub~Mandarin");
		subtitleMap.put("Korean", "a:sub~Korean");
		subtitleMap.put("Russian", "a:sub~Russian");
		subtitleMap.put("Polish", "a:sub~Polish");
		subtitleMap.put("Vietnamese", "a:sub~Vietnamese");
		subtitleMap.put("Swedish", "a:sub~Swedish");
		subtitleMap.put("Norwegian", "a:sub~Norwegian");
		subtitleMap.put("Finnish", "a:sub~Finnish");
		subtitleMap.put("Turkish", "a:sub~Turkish");
		subtitleMap.put("Unknown", "a:sub~Unknown");
	}

	@XStreamImplicit(itemFieldName = "Source")
	private List<String> sources = new ArrayList<String>();

	@XStreamImplicit(itemFieldName = "Format")
	private List<String> formats = new ArrayList<String>();

	@XStreamImplicit(itemFieldName = "Subtitle")
	private List<String> subtitles = new ArrayList<String>();

	@XStreamImplicit(itemFieldName = "Language")
	private List<String> languages = new ArrayList<String>();

	@XStreamAlias("ExtraSearchParameters")
	private String extraSearchParams;

	public List<String> getSources() {
		return sources;
	}

	public void setSources(List<String> sources) {
		this.sources = sources;
	}

	public List<String> getFormats() {
		return formats;
	}

	public void setFormats(List<String> formats) {
		this.formats = formats;
	}

	public List<String> getSubtitles() {
		return subtitles;
	}

	public void setSubtitles(List<String> subtitles) {
		this.subtitles = subtitles;
	}

	public List<String> getLanguages() {
		return languages;
	}

	public void setLanguages(List<String> languages) {
		this.languages = languages;
	}

	public String getExtraSearchParams() {
		return extraSearchParams;
	}

	public void setExtraSearchParams(String extraSearchParams) {
		this.extraSearchParams = extraSearchParams;
	}

	public List<String> getSearchAttributeSources() {
		return getSearchAttributes(sources, sourceMap);
	}

	public List<String> getSearchAttributeFormats() {
		return getSearchAttributes(formats, formatMap);
	}

	public List<String> getSearchAttributeLanguages() {
		return getSearchAttributes(languages, languageMap);
	}

	public List<String> getSearchAttributeSubtitles() {
		return getSearchAttributes(subtitles, subtitleMap);
	}

	public List<String> getAllSearchAttributes() {
		ArrayList<String> all = new ArrayList<String>();
		all.addAll(getSearchAttributeSources());
		all.addAll(getSearchAttributeFormats());
		all.addAll(getSearchAttributeLanguages());
		all.addAll(getSearchAttributeSubtitles());

		return all;
	}

	private static List<String> getSearchAttributes(List<String> keys,
			Map<String, String> map) {
		List<String> sAttribs = new ArrayList<String>();
		if (keys == null)
			keys = new ArrayList<String>();

		for (String key : keys) {
			if (map.containsKey(key)) {
				sAttribs.add(map.get(key));
			}
		}

		return sAttribs;
	}

}
