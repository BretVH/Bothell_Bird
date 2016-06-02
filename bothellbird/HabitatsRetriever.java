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

    private static final String query = "SELECT b.habitatId, b.uniqueBirdId, "
            + "habitatNames FROM BirdDatabase.dbo.BirdHabitats b, "
            + "BirdDatabase.dbo.Habitats h WHERE uniqueBirdId = ";
    private static final String join = "AND b.habitatId = h.habitatId";

    static List<Feature> getHabitats(int birdId) throws SQLException {
        int count = SqlUtilities.getFeatureCount("BirdHabitats");
        Connection conn = SimpleDataSource.getconnection();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(query + birdId + join);
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
}
