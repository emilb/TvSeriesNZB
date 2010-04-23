import java.io.InputStreamReader;
import java.net.URL;

import junit.framework.TestCase;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;

public class RomeTeset extends TestCase {

    public void testRss() throws Exception {

        URL rss = new URL(
                "http://nzbs.org/rss.php?catid=14&i=44607&h=7cb81760db5239105bd9b703a4aa532e&dl=1&num=3");
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new InputStreamReader(rss.openStream()));

        // System.out.println(feed);

        for (Object entryObj : feed.getEntries()) {
            SyndEntry entry = (SyndEntry) entryObj;
            System.out.println(entry.getTitle());
            System.out.println(entry.getUri());
        }
    }
}
