import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class CacheScraper implements Scrapper{
    private Scrapper scrapper = new DefaultScraper();

    @SneakyThrows
    @Override
    public Home scrape(String url) {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:db.sqlite");
        Statement statement = connection.createStatement();

        String query = String.format("select count(*) as count from homes where url='%s'", url);
        ResultSet rs = statement.executeQuery(query);
        if (rs.getInt("count") > 0) {
            query = String.format("select * from homes where url='%s'", url);
            rs = statement.executeQuery(query);
            return new Home(rs.getInt("price"), rs.getInt("beds"), rs.getInt("bathes"), rs.getInt("garages"));
        } else {
            Home home = scrapper.scrape(url);
            query = String.format("insert into homes(url, price, beds, bathes, garages) values('%s', %d, %f, %f, %f)", url, home.getPrice(), home.getBeds(), home.getBathes(), home.getGarages());
            statement.executeUpdate(query);
            return home;
        }
    }
}
