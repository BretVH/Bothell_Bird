package bothell_bird;

import java.sql.*;
import java.util.ArrayList;

public class BirdNameRetriever {

    private static ArrayList<BirdInfo> birds;
    private static final String getBirdInfoQuery = "SELECT [nameID], [name], [uniqueBirdID] FROM"
                + " BirdDatabase.dbo.name";  //get database table
    /**
     * reads data from an s.q.l server and creates an array list of Bird objects
     *
     * @return list of birdss
     * @throws SQLException
     */
    public static ArrayList<BirdInfo> readData() throws SQLException {
        Connection conn = SimpleDataSource.getconnection();
        Statement stat = conn.createStatement();
        
        ResultSet rs = stat.executeQuery(getBirdInfoQuery);
        int i = 0;
        while (rs.next()) {
            i++;
        }
        birds = new ArrayList<BirdInfo>();

        rs = stat.executeQuery(getBirdInfoQuery);
        rs.next();
        for (int j = 0; j < i; j++)//populate arraylist
        {
            BirdInfo aBirdName = new BirdInfo(rs.getString("Name"),
                    rs.getInt("nameID"), rs.getInt("uniqueBirdID"));
            birds.add(aBirdName);
            rs.next();
        }
        conn.close();
        return birds;
    }

    public static int getFeatureID(String name) throws SQLException {
        int ID = 0;
        if (name.equals("Primary Color")) {
            ID = 2;
        } else if (name.equals("Conservation Status")) {
            ID = 5;
        } else if (name.equals("Feeding Frequency")) {
            ID = 3;
        } else if (name.equals("Location")) {
            ID = 7;
        } else if (name.equals("Family")) {
            ID = 0;
        } else if (name.equals("Secondary Color")) {
            ID = 1;
        } else if (name.equals("Habitat")) {
            ID = 4;
        } else if (name.equals("Size")) {
            ID = 6;
        }
        return ID;
    }

    public static ArrayList<BirdInfo> updateData(int feature, int iDS) throws SQLException {
        Connection conn = SimpleDataSource.getconnection();
        Statement stat = conn.createStatement();
        String featured = "";
        boolean fakeOut = false;
        boolean fakeOut2 = false;
        String featuredA = "UniqueFamilyID";
        String featuredB = "BirdSecondaryColor";
        switch (feature) {
            case 0:
                fakeOut = true;
                featured = "BirdFamilies";
                break;
            case 1:
                fakeOut2 = true;
                featured = "BirdSecondaryColors";
                break;
            case 2:
                featured = "BirdPrimaryColor";
                break;
            case 3:
                featured = "BirdFeederFrequency";
                break;
            case 4:
                featured = "BirdHabitat";
                break;
            case 5:
                featured = "BirdConservationStatus";
                break;
            case 6:
                featured = "BirdSize";
                break;
            case 7:
                featured = "BirdLocations";
                break;
        }
        String query = "SELECT * FROM BirdDatabase.dbo." + featured + " Where " + featured + " = " + iDS; //get database table
        if (fakeOut) {
            query = "SELECT * FROM BirdDatabase.dbo." + featured + " WHERE " + featuredA + " = " + iDS;  //get database table
        }
        if (fakeOut2) {
            query = "SELECT * FROM BirdDatabase.dbo." + featured + " WHERE " + featuredB + " = " + iDS;  //get database table
        }
        if (featured.equalsIgnoreCase("BirdLocations")) {
            query = "SELECT * FROM BirdDatabase.dbo." + featured + " WHERE BirdLocation = " + iDS;  //get database table
        }
        ResultSet rs = stat.executeQuery(query);
        int i = 0;
        while (rs.next()) {
            i++;
        }
        rs = stat.executeQuery(query);
        rs.next();
        ArrayList<Integer> birdIDS = new ArrayList<>();
        for (int z = 0; z < i; z++) {
            birdIDS.add(rs.getInt("uniqueBirdId"));
            rs.next();
        }
        birds = new ArrayList<>();
        if (birdIDS.size() > 0) {
            String querye = "SELECT * FROM"
                    + " BirdDatabase.dbo.name where uniqueBirdID = " + birdIDS.get(0);  //get database table
            for (int y = 1; y < birdIDS.size(); y++) {
                querye += " or uniqueBirdId = " + birdIDS.get(y);
            }
            rs = stat.executeQuery(querye);
            i = 0;
            while (rs.next()) {
                i++;
            }
            rs = stat.executeQuery(querye);
            rs.next();
            for (int j = 0; j < i; j++)//populate arraylist
            {
                BirdInfo aBirdName = new BirdInfo(rs.getString("name"),
                        rs.getInt("nameID"), rs.getInt("uniqueBirdID"));
                birds.add(aBirdName);
                rs.next();
            }
        }
        conn.close();
        return birds;
    }

    public static String getScientificName(int id) throws SQLException {
        Connection conn = SimpleDataSource.getconnection();
        Statement stat = null;
        stat = conn.createStatement();
        String query = "SELECT [scientificName] FROM"
                + " BirdDatabase.dbo.BirdID where id = '" + id + "'";  //get database table
        ResultSet rs = null;
        rs = stat.executeQuery(query);
        rs.next();
        String sciName = rs.getString("scientificName");
        conn.close();
        return sciName;
    }

    public static ArrayList<String> getCommonNames(int id) throws SQLException {
        ArrayList<String> common = new ArrayList<>();
        Connection conn = SimpleDataSource.getconnection();
        Statement stat = conn.createStatement();
        String query = "SELECT [name] FROM"
                + " BirdDatabase.dbo.BirdID where id = '" + id + "'";  //get database table
        ResultSet rs = stat.executeQuery(query);
        rs.next();
        for (int j = 0; j < common.size(); j++)//populate arraylist
        {
            common.add(rs.getString("name"));
            rs.next();
        }
        conn.close();
        return common;
    }

}
