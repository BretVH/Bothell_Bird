package bothell_bird;

import net.ucanaccess.complex.Attachment;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class InputStreamRetriever {

    public static Attachment make(int ID, char gender, int num, String type) throws SQLException, FileNotFoundException, IOException {
        Connection conn = SimpleDataSource.getconnection();
            String prefix = Integer.toString(ID) + gender + num;
            String query = "SELECT Chart FROM"
                    + " Store Where fileName = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, prefix + "." + type);
            ResultSet rs = statement.executeQuery();
            rs.next();
            Attachment[] att = (Attachment[]) rs.getObject("Chart");
            return att[0];
    }
}
