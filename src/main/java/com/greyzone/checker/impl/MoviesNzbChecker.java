package com.greyzone.checker.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.greyzone.checker.NzbChecker;
import com.greyzone.domain.movie.Movie;
import com.greyzone.exceptions.AuthenticationException;
import com.greyzone.indexsearch.IndexSearcher;
import com.greyzone.integration.IntegrationDownloader;
import com.greyzone.settings.ApplicationSettings;
import com.greyzone.storage.Storage;

@Service("MoviesNzbChecker")
public class MoviesNzbChecker implements NzbChecker {

    @Autowired
    private IndexSearcher         indexSearcher;

    @Autowired
    private IntegrationDownloader integrationDownloader;

    @Autowired
    @Qualifier("XmlMovieStorage")
    private Storage<Movie>        xmlMovieStorage;

    @Autowired
    private ApplicationSettings   appSettings;

    private final Logger          log = Logger.getLogger(this.getClass());

    /*
     * (non-Javadoc)
     * 
     * @see com.greyzone.NzbChecker#checkForDownloads()
     */
    public void checkForDownloads() {

        // Get wanted shows from storage
        List<Movie> movies = xmlMovieStorage.getItems();

        for (Movie movie : movies) {
            if (movie.isDownloaded()) {
                continue;
            }

            log.debug("Checking for new releases for movie: " + movie.getTitle());

            try {
                String nzbId = indexSearcher.getIndexId(movie);

                if (nzbId != null) {
                    log.debug("Ordering download of " + movie.getTitle());
                    integrationDownloader.orderDownloadById(nzbId);
                    movie.setDownloaded(true);
                } else {
                    log.debug("No relase for " + movie.getTitle() + " found.");
                }
            } catch (AuthenticationException ae) {
                log.error(ae.getMessage());
                throw new RuntimeException("Fatal exception, exit.");
            } catch (Exception e) {
                log.error("Something failed when checking/downloading movie " + movie.getTitle() + " Err: "
                        + e.getMessage());
            }
        }

        // Save updated state only if not in Dry run mode
        if (!appSettings.isDryRun()) {
            xmlMovieStorage.storeItems(movies);
        }
    }
}
