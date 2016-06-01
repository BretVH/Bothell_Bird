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
public class BirdNamesRetriever {

    private static final String query = "SELECT * FROM BirdDatabase.dbo.names Where uniqueBirdId = ";

    public static List<BirdName> getAliasList(int birdId) throws SQLException {
        int count = SqlUtilities.getFeatureCount("names");
        Connection conn = SimpleDataSource.getconnection();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(query + birdId);
        List<BirdName> namesList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            char gender = rs.getString("gender").charAt(0);
            String name = rs.getString("uniqueName");
            int nameId = rs.getInt("nameId");
            BirdName birdName = new BirdName(gender, name, nameId, birdId);
            namesList.add(birdName);
        }
        return namesList;
    }
}
