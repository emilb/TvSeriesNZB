package com.greyzone.domain.tv;

public enum Quality {
	HDTV("HDTV"),
	SevenTwenty("720p"),
	ThousandEighty("1080p");
	
	private String name;
	
	private Quality(String name) {
		this.name = name;
	}
	
	public static Quality fromName(String name) {
		for (Quality q : Quality.values()) {
			if (q.name.equals(name))
				return q;
		}
		
		return Quality.valueOf(name);
	}
	
	public String toString() {
		return name;
	}
}
