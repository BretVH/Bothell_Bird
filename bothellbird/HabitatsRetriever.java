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
class HabitatsRetriever {

    private static final String habitatsQuery = "SELECT b.habitatId, "
            + "b.uniqueBirdId, h.habitatNames "
            + "habitatNames FROM BirdDatabase.dbo.BirdHabitats b, "
            + "BirdDatabase.dbo.Habitats h WHERE uniqueBirdId = ";

    private static final String join = "AND b.habitatId = h.habitatId";

    public static List<Feature> getHabitats(int birdId) throws SQLException {
        int count = SqlUtilities.getBirdFeaturesCount("BirdHabitats", birdId);
        Connection conn = SimpleDataSource.getconnection();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(habitatsQuery + birdId + join);
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
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(habitatsQuery + birdId + join);
        ArrayList<Integer> habitatIdsList = new ArrayList<>();
        rs.next();
        for (int i = 0; i < count; i++) {
            habitatIdsList.add(rs.getInt("habitatId"));
            rs.next();
        }
        return habitatIdsList;
    }
}
