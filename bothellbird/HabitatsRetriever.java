package bothell_bird;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bret
 */
class HabitatsRetriever {

    private static final String habitatsQuery = "SELECT b.habitatId, "
            + "h.uniqueBirdId, h.habitatId "
            + "b.habitatNames FROM Habitats b, "
            + "BirdHabitats h WHERE uniqueBirdId = ?";

    private static final String join = " AND h.uniqueBirdId = b.habitatId";

    public static List<Feature> getHabitats(int birdId) throws SQLException {
        int count = SqlUtilities.getBirdFeaturesCount("BirdHabitats", birdId);
        Connection conn = SimpleDataSource.getconnection();
        PreparedStatement stat = conn.prepareStatement(habitatsQuery + join);
        stat.setString(1, Integer.toString(birdId));
        ResultSet rs = stat.executeQuery();
        ArrayList<Feature> habitatsList = new ArrayList<>();
        rs.next();
        for (int i = 0; i < count; i++) {
            int habitatId = rs.getInt("habitatId");
            String habitatName = rs.getString("habitatNames");
            habitatsList.add(new Feature(habitatId, habitatName));
            rs.next();
        }
        return habitatsList;
    }

    public static List<Integer> getHabitatIds(int birdId) throws SQLException {
        int count = SqlUtilities.getBirdFeaturesCount("BirdHabitats", birdId);
        Connection conn = SimpleDataSource.getconnection();
        String query = habitatsQuery;
        query += join;
        PreparedStatement stat = conn.prepareStatement(query);
        stat.setString(1, Integer.toString(birdId));
        ResultSet rs = stat.executeQuery();
        ArrayList<Integer> habitatIdsList = new ArrayList<>();
        rs.next();
        for (int i = 0; i < count; i++) {
            habitatIdsList.add(rs.getInt("habitatId"));
            rs.next();
        }
        return habitatIdsList;
    }
}
