package bothell_bird;

import java.sql.*;
import java.util.ArrayList;

public class BirdsListRetriever {

    private static ArrayList<Bird> birds;
    //get database table
    private static final String getBirdInfoQuery = "SELECT [uniqueBirdId], "
            + "[ScientificName], [sizeIdId], [familyNameId], "
            + "[conservationStatusId] FROM" + " BirdDatabase.dbo.uniqueBirds";  
    /**
     * queries server to populate a list of Birds
     *
     * @return list of birds
     * @throws SQLException
     */
    public static ArrayList<Bird> readData() throws SQLException {
        int i = SqlUtilities.getFeatureCount("uniqueBirds");
        birds = new ArrayList<>();
        Connection conn = SimpleDataSource.getconnection();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(getBirdInfoQuery);
        rs.next();
        for (int j = 0; j < i; j++)//populate arraylist
        {
            Bird aBirdName = new Bird(rs.getInt("uniqueBirdId"), 
                rs.getString("scientificName"), rs.getInt
                ("conservationStatusId"), rs.getInt("familyNameId"), 
                rs.getInt("sizeId"));
            birds.add(aBirdName);
            rs.next();
        }
        conn.close();
        return birds;
    }   
}
