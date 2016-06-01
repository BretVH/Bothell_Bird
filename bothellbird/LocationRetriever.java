/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
class LocationRetriever {
    
    private static final String query = "SELECT b.locationId, b.uniqueBirdId, " +
            "location FROM BirdDatabase.dbo.BirdLocations b, " + 
            "BirdDatabase.dbo.Locations l WHERE uniqueBirdId = ";
    private static final String join = "AND b.locationId = l.locationId";
    static List<Feature> getLocations(int birdId) throws SQLException {
        int count = SqlUtilities.getFeatureCount("BirdLocations");
        Connection conn = SimpleDataSource.getconnection();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(query + birdId + join);
        ArrayList<Feature> locations = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int habitatId = rs.getInt("habitatId");
            String habitatName = rs.getString("habitatName");
            locations.add(new Feature(habitatId, habitatName));
        }
        return locations;
    }
}
