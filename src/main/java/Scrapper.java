import java.io.IOException;

public interface Scrapper {
    Home scrape(String url) throws IOException;
}
