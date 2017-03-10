package bothell_bird;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InputStreamRetriever {

    public static InputStream make(int ID, char gender, int num, String type) throws SQLException, FileNotFoundException, IOException {
        Blob blob;
        try (Connection conn = SimpleDataSource.getconnection()) {
            Statement stat = conn.createStatement();
            final String prefix = ID + "" + gender + "" + num;
            String query = "SELECT * FROM"
                    + " MyBirdStore.dbo.Store Where name = '"
                    + prefix + "." + type + "'";
            ResultSet rs = stat.executeQuery(query);
            rs.next();
            blob = rs.getBlob("Chart");
        }
        return (InputStream) blob.getBinaryStream();
    }
}
