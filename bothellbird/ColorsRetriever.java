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

    private static final String getBirdColorIdsQuery = "SELECT * FROM BirdDatatbase.dbo.uniqueBirdColors WHERE uniqueBirdId = ";

    private static final String getBirdColorsQuery = "SELECT * FROM"
            + " BirdDatabase.dbo.uniqueBirdColors u, BirdDatabase.dbo.BirdColor b WHERE uniqueBirdId = ";

    private static final String join = " AND u.birdColorId = b.birdColorId";

    public static List<Feature> getPrimaryColors(int birdId) throws SQLException {
        return getColors("primaryColorId", "primaryColor", birdId);
    }

    public static List<Feature> getSecondaryColors(int birdId) throws SQLException {
        return getColors("secondaryColorId", "secondaryColor", birdId);
    }

    public static List<Integer> getPrimaryColorIds(int birdId) throws SQLException {
        return getColorIds("primaryColorId", birdId);
    }

    public static List<Integer> getSecondaryColorIds(int birdId) throws SQLException {
        return getColorIds("secondaryColorId", birdId);
    }

    private static List<Feature> getColors(String IdColumn, String colorColumn, int id) throws SQLException {
        int i = SqlUtilities.getFeatureCount("uniqueBirdColors");
        List<Feature> colors = new ArrayList<>();
        Connection conn = SimpleDataSource.getconnection();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(getBirdColorsQuery + id + join);
        rs.next();
        for (int j = 0; j < i; j++) {
            colors.add(new Feature(rs.getInt(IdColumn), rs.getString(colorColumn)));
            rs.next();
        }
        return colors;
    }

    private static List<Integer> getColorIds(String columnName, int birdId) throws SQLException {
        int i = SqlUtilities.getFeatureCount("uniqueBirdColors");
        List<Integer> colorIds = new ArrayList<>();
        Connection conn = SimpleDataSource.getconnection();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(getBirdColorIdsQuery + birdId);
        rs.next();
        for (int j = 0; j < i; j++) {
            colorIds.add(rs.getInt(columnName));
            rs.next();
        }
        return colorIds;
    }
}
