package bothell_bird;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bret
 */
public class ColorsRetriever {

    private static final String getBirdColorsQuery = "SELECT * FROM"
            + " BirdDatabase.dbo.uniqueBirdColors WHERE uniqueBirdId = ";

    public static List<Integer> getPrimaryColors(int birdId) throws SQLException {
        return getColors("primaryColorId", birdId);
    }

    public static List<Integer> getSecondaryColors(int birdId) throws SQLException {
        return getColors("secondaryColorId", birdId);

    }

    private static List<Integer> getColors(String columnName, int id) throws SQLException {
        int i = SqlUtilities.getFeatureCount("uniqueBirdColors");
        List<Integer> colors = new ArrayList<>();
        Connection conn = SimpleDataSource.getconnection();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(getBirdColorsQuery + id);
        rs.next();
        for (int j = 0; i < i; j++) {
            colors.add(rs.getInt(columnName));
        }
        return colors;
    }

}
