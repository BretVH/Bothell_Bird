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

    private static final String locationsQuery = "SELECT b.locationId, b.uniqueBirdId, "
            + "location FROM BirdDatabase.dbo.BirdLocations b, "
            + "BirdDatabase.dbo.Locations l WHERE uniqueBirdId = ";
    
    private static final String join = " AND b.locationId = l.locationId";

    private static final String locationIdsQuery = "SELECT * FROM BirdDatabase.dbo.BirdLocations WHERE uniqueBirdId = ";
    
    static List<Feature> getLocations(int birdId) throws SQLException {
        int count = SqlUtilities.getFeatureCount("BirdLocations");
        Connection conn = SimpleDataSource.getconnection();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(locationsQuery + birdId + join);
        ArrayList<Feature> locations = new ArrayList<>();
        rs.next();
        for (int i = 0; i < count; i++) {
            int locationId = rs.getInt("locationId");
            String location = rs.getString("location");
            locations.add(new Feature(locationId, location));
            rs.next();
        }
        return locations;
    }
    
      public static List<Integer> getLocationIds(int birdId) throws SQLException {
        int count = SqlUtilities.getFeatureCount("BirdLocations");
        Connection conn = SimpleDataSource.getconnection();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(locationIdsQuery + birdId);
        ArrayList<Integer> locationIdsList = new ArrayList<>();
        rs.next();
        for (int i = 0; i < count; i++) {
            locationIdsList.add(rs.getInt("locationId"));
            rs.next();
        }
        return locationIdsList;
    }
}
