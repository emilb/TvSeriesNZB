package com.greyzone.storage;

import java.util.List;

import com.greyzone.domain.tv.Show;

public interface ShowStorage {

	public List<Show> getShows();

	public void storeShows(List<Show> shows);

	public void testShowParsing();
}
