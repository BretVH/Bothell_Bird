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

    private static final String getBirdColorsQuery = "SELECT u.birdColorId, u.uniqueBirdId, b.birdColorId,"
            + "b.primaryColorId, b.secondaryColorId, p.primaryColor, s.secondaryColor from "
            + "BirdDatabase.dbo.uniqueBirdColors u, BirdDatabase.dbo.BirdColor b, "
            + "BirdDatabase.dbo.PrimaryColors p, BirdDatabase.dbo.SecondaryColor s WHERE uniqueBirdId = ";

    private static final String birdColorsJoin = " AND u.birdColorId = b.birdColorId AND b.primaryColorId = "
            + "p.primaryColorId AND b.secondaryColorId = s.secondaryColorId";

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
        int i = SqlUtilities.getBirdFeaturesCount("uniqueBirdColors", id);
        List<Feature> colors = new ArrayList<>();
        Connection conn = SimpleDataSource.getconnection();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(getBirdColorsQuery + id + birdColorsJoin);
        rs.next();
        for (int j = 0; j < i; j++) {
            colors.add(new Feature(rs.getInt(IdColumn), rs.getString(colorColumn)));
            rs.next();
        }
        return colors;
    }

    private static List<Integer> getColorIds(String columnName, int birdId) throws SQLException {
        int i = SqlUtilities.getBirdFeaturesCount("uniqueBirdColors", birdId);
        List<Integer> colorIds = new ArrayList<>();
        Connection conn = SimpleDataSource.getconnection();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(getBirdColorsQuery + birdId + birdColorsJoin);
        rs.next();
        for (int j = 0; j < i; j++) {
            colorIds.add(rs.getInt(columnName));
            rs.next();
        }
        return colorIds;
    }
}
