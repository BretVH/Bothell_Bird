package bothell_bird;

import java.sql.*;

/**
 *
 * @author Bret
 */
public class SqlUtilities {

    public static int getFeatureCount(String tn) throws SQLException {
        String countQuery = "SELECT COUNT (*) AS myCount FROM " + tn;
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
        String countQuery = "SELECT COUNT (*) AS myCount FROM " + tn;
        String constraint = " WHERE uniqueBirdId = ?";
        Connection conn = SimpleDataSource.getconnection();
        PreparedStatement stat = conn.prepareStatement(countQuery + constraint);
        stat.setInt(1, birdId);
        ResultSet rs = stat.executeQuery();
        int numberOfBirdFeatures = 0;
        while (rs.next()) {
            numberOfBirdFeatures = rs.getInt("myCount");
        }
        return numberOfBirdFeatures;
    }
}
