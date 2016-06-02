package bothell_bird;

import java.io.IOException;
import java.sql.*;
import java.util.LinkedHashSet;
import java.util.Set;

public class BirdsListRetriever {

    private static Set<Bird> birds;
    //get database table
    private static final String getBirdInfoQuery = "SELECT * FROM BirdDatabase.dbo.uniqueBirds";  
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
            Bird bird = new Bird(rs.getInt("uniqueBirdId"), 
                rs.getString("scientificName"), rs.getInt
                ("conservationStatusId"), rs.getInt("familyNameId"), 
                rs.getInt("sizeId"), rs.getBoolean("image"), rs.getBoolean("femaleImage"),
                rs.getBoolean("maleImage"), rs.getBoolean("maleSound"), rs.getBoolean("femaleSound"),
                rs.getBoolean("sound"));
            birds.add(bird);
            rs.next();
        }
        conn.close();
        return birds;
    }   
}
