package com.greyzone.indexsearch;

import java.util.List;

import com.greyzone.domain.Episode;
import com.greyzone.domain.Show;

public interface IndexSearcher {

	public List<Episode> getIndexIds(Show show, List<Episode> episodes);

}
