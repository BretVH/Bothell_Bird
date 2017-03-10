package bothell_bird;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Bret
 */
public class SqlUtilities {

    public static int getFeatureCount(String tn) throws SQLException {
        String countQuery = "SELECT COUNT (*) AS myCount FROM BirdDatabase.dbo." + tn;
        Connection conn = SimpleDataSource.getconnection();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(countQuery);
        int numberOfFeatures = 0;
        while (rs.next()) {
            numberOfFeatures = rs.getInt("myCount");
        }
        return numberOfFeatures;
    }

    static int getBirdFeaturesCount(String tn, int birdId) throws SQLException {
        String countQuery = "SELECT COUNT (*) AS myCount FROM BirdDatabase.dbo." + tn;
        String constraint = " WHERE uniqueBirdId = ";
        Connection conn = SimpleDataSource.getconnection();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(countQuery + constraint + birdId);
        int numberOfBirdFeatures = 0;
        while (rs.next()) {
            numberOfBirdFeatures = rs.getInt("myCount");
        }
        return numberOfBirdFeatures;
    }
}
