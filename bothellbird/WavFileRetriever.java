package bothell_bird;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class WavFileRetriever {

    private final FileMaker maker;
    private File myFile;

    public WavFileRetriever() {
        maker = new FileMaker();
    }

    public File getSound(int ID, int nameId) throws SQLException {
        Connection conn = SimpleDataSource.getconnection();
        Statement stat =  conn.createStatement();
        //get database table
        String query = "SELECT [hasMaleSound], [hasFemaleSound], [hasAmbiguousSound] FROM"
                + " BirdDatabase.dbo.Files where uniqueBirdID = '" + ID + "'";
        ResultSet rs = stat.executeQuery(query);
        rs.next();
        int[] id = new int[3];
        id[0] = rs.getInt("hasMaleSound");
        id[1] = rs.getInt("hasFemaleSound");
        id[2] = rs.getInt("hasAmbiguousSound");
        String query2 = "SELECT [gender] FROM BirdDatabase.dbo.gender where uniqueBirdName"
                + "= '" + nameId + "'";
        rs = stat.executeQuery(query2);
        rs.next();
        String gender = rs.getString("gender");
        if (gender.charAt(0) == 'm' || gender.charAt(0) == 'M') {
            if (id[0] > 0) {
                myFile = maker.make(ID, 'm', 1, "wav");
            }
        } else if (gender.charAt(0) == 'f' || gender.charAt(0) == 'F') {
            if (id[1] > 0) {
                myFile = maker.make(ID, 'f', 1, "wav");
            }
        } else if (id[2] > 0) {
            myFile = maker.make(ID, 'a', 1, "wav");
        } else {
            myFile = maker.make(0, 'a', 0, "wav");
        }
        conn.close();
        return myFile;
    }

}
