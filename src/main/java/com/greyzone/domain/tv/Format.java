package com.greyzone.domain.tv;

public enum Format {
	XviD("xvid"),
	DVDR("dvd"),
	x264("x264");
	
	private String name;
	
	private Format(String name) {
		this.name = name;
	}
	
	public static Format fromName(String name) {
		for (Format f : Format.values()) {
			if (f.name.equals(name))
				return f;
		}
		
		return Format.valueOf(name);
	}
	
	public String toString() {
		return name;
	}
}
