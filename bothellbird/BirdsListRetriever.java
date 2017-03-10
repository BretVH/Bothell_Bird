package bothell_bird;

import java.io.IOException;
import java.sql.*;
import java.util.LinkedHashSet;
import java.util.Set;

public class BirdsListRetriever {

    private static Set<Bird> birds;
    //get database table
    private static final String getBirdInfoQuery = "SELECT * FROM BirdDatabase.dbo.uniqueBirds";
    private static final String sizeQuery = "SELECT size FROM BirdDatabase.dbo.Size WHERE sizeId = ";
    private static final String csQuery = "SELECT conservationStatus FROM BirdDatabase.dbo.ConservationStatus WHERE conservationStatusId = ";
    private static final String fQuery = "SELECT familyName FROM BirdDatabase.dbo.Family WHERE familyNameId = ";
    private static final String bsQuery = "SELECT bill From BirdDatabase.dbo.Bill WHERE billId = ";
    private static final String wsQuery = "SELECT wing From BirdDatabase.dbo.Wing WHERE wingId = ";

    /**
     * queries server to populate a list of Birds
     *
     * @return list of birds
     * @throws SQLException
     * @throws java.io.IOException
     */
    public static Set<Bird> getBirdsList() throws SQLException, IOException {
        int i = SqlUtilities.getFeatureCount("uniqueBirds");
        birds = new LinkedHashSet<>();
        Connection conn = SimpleDataSource.getconnection();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(getBirdInfoQuery);
        rs.next();
        for (int j = 0; j < i; j++)//populate arraylist
        {
            int id = rs.getInt("uniqueBirdId");
            String sn = rs.getString("scientificName");
            int cId = rs.getInt("conservationStatusId");
            int fId = rs.getInt("familyNameId");
            int sId = rs.getInt("sizeId");
            int wId = rs.getInt("wingId");
            int bId = rs.getInt("billId");
            Bird bird = new Bird(id, sn, cId, fId, sId, bId, wId);
            birds.add(bird);
            rs.next();
        }
        conn.close();
        return birds;
    }

    public static Feature getDisplayBirdFeature(int featureId, String columnName) throws SQLException {
        String query;
        switch (columnName) {
            case "familyName":
                query = fQuery;
                break;
            case "size":
                query = fQuery;
                break;
            case "conservationStatus":
                query = csQuery;
                break;
            case "bill":
                query = bsQuery;
                break;
            case "wing":
                query = wsQuery;
                break;
            default:
                return null;
        }
        return getFeature(featureId, columnName, query);

    }

    private static Feature getFeature(int featureId, String columnName, String query) throws SQLException {
        Connection conn = SimpleDataSource.getconnection();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(query + featureId);
        rs.next();
        String featureName = rs.getString(columnName);
        return new Feature(featureId, featureName);

    }
}
