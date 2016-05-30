package bothell_bird;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

public class GenderRetriever {

    private static final LinkedHashMap<String, String> gender = new LinkedHashMap<>();
    private static final String genderQuery = "SELECT [gender], [uniqueBirdName] FROM"
            + " BirdDatabase.dbo.Gender where uniqueBirdId = '";
    private static final StringBuilder genderQueryBuilder = new StringBuilder(genderQuery);;
    private static final String nameIDQuery = "Select [nameID] from BirdDatabase.dbo.name where name = '";
    private static final StringBuilder nameIDQueryBuilder = new StringBuilder(nameIDQuery);
    private static final String nameQuery = "SELECT [name] FROM BirdDatabase.dbo.name where nameId = ";
    private static final StringBuilder nameQueryBuilder = new StringBuilder(nameQuery);;


    public static LinkedHashMap<String, String> getGender(int id) throws SQLException {
        try (Connection conn = SimpleDataSource.getconnection()) {
            Statement stat = conn.createStatement();
            genderQueryBuilder.append(id).append("'");  //get database table
            ResultSet rs = stat.executeQuery(genderQueryBuilder.toString());
            int resultCount = 0;
            while (rs.next()) {
                resultCount++;
            }
            rs = stat.executeQuery(genderQuery);
            rs.next();
            Map<Integer, String> genders = new LinkedHashMap<>();
            int[] genderSpecificNames = new int[resultCount];
            for (int j = 0; j < resultCount; j++)//populate arraylist
            {
                genders.put(rs.getInt("uniqueBirdName"), rs.getString("gender"));
                genderSpecificNames[j] = rs.getInt("uniqueBirdName");
            }
            for (int j = 0; j < resultCount; j++)//populate arraylist
            {
                nameQueryBuilder.append(genderSpecificNames[j]);
                ResultSet a = stat.executeQuery(nameQueryBuilder.toString());
                a.next();
                gender.put(a.getString("name"), genders.get(genderSpecificNames[j]));
            }
        }
        return gender;
    }

    public static int getNameId(int id, String bName) throws SQLException {
        Connection conn = SimpleDataSource.getconnection();
        Statement stat = conn.createStatement();
        nameIDQueryBuilder.append(bName).append("'");
        ResultSet rs = stat.executeQuery(nameIDQueryBuilder.toString());
        rs.next();
        return rs.getInt("nameID");
    }
}
