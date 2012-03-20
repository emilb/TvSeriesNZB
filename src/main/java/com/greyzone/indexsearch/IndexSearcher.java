package com.greyzone.indexsearch;

import java.util.List;

import com.greyzone.domain.tv.Episode;
import com.greyzone.domain.tv.Show;

public interface IndexSearcher {

    public List<Episode> getIndexIds(Show show, List<Episode> episodes);

}
