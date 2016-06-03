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
            ResultSet temp = stat.executeQuery(fQuery + fId);
            temp.next();
            String f = temp.getString("familyName");
            temp = stat.executeQuery(sizeQuery + sId);
            temp.next();
            String s = temp.getString("size");
            temp = stat.executeQuery(csQuery + cId);
            temp.next();
            String cs = temp.getString("conservationStatus");
            temp = stat.executeQuery(bsQuery + bId);
            temp.next();
            String b = temp.getString("bill");
            temp = stat.executeQuery(wsQuery + wId);
            String w = temp.getString("wing");
            Feature conStat = new Feature(cId, cs);
            Feature fam = new Feature(fId, f);
            Feature size = new Feature(sId, s);
            Feature wing = new Feature(wId, w);
            Feature bill = new Feature(bId, b);
            String description = rs.getString("description");
            Bird bird = new Bird(id, sn, conStat, fam, size, bill, wing, description);
            birds.add(bird);
            rs.next();
        }
        conn.close();
        return birds;
    }
}
