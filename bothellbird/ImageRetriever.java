package bothell_bird;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.ImageIcon;

public class ImageRetriever {

    private static final String query = "SELECT [hasMaleImage], [hasFemaleImage], [hasImage] FROM"
            + " BirdDatabase.dbo.Files where uniqueBirdID = ";

    public static ImageIcon getImageIcon(int uniqueBirdId, char gender, boolean isLarge) throws SQLException, IOException {
        Connection conn = SimpleDataSource.getconnection();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(query + uniqueBirdId);
        rs.next();
        boolean hasImage;
        switch (gender) {
            case 'f':
                hasImage = rs.getBoolean("hasFemaleImage");
                break;
            case 'm':
                hasImage = rs.getBoolean("hasMaleImage");
                break;
            default:
                hasImage = rs.getBoolean("hasImage");
        }
        if (hasImage) {
            if (isLarge) {
                return getMyIcon(uniqueBirdId, gender, 2);
            } else {
                return getMyIcon(uniqueBirdId, gender, 1);
            }
        } else {
            InputStream in = InputStreamRetriever.make(0, 'a', 0, "Jpg");
            byte[] imageData = org.apache.commons.io.IOUtils.toByteArray(in);
            return new ImageIcon(imageData);
        }
    }

    private static ImageIcon getMyIcon(int uniqueBirdId, char gender, int imageSize) throws SQLException, IOException {
        if (gender == 'm' || gender == 'M') {
            InputStream in = InputStreamRetriever.make(uniqueBirdId, 'm', imageSize, "Jpg");
            byte[] imageData = org.apache.commons.io.IOUtils.toByteArray(in);
            return new ImageIcon(imageData);
        } else if (gender == 'f' || gender == 'F') {
            InputStream in = InputStreamRetriever.make(uniqueBirdId, 'f', imageSize, "Jpg");
            byte[] imageData = org.apache.commons.io.IOUtils.toByteArray(in);
            return new ImageIcon(imageData);
        } else {
            InputStream in = InputStreamRetriever.make(uniqueBirdId, 'a', imageSize, "Jpg");
            byte[] imageData = org.apache.commons.io.IOUtils.toByteArray(in);
            return new ImageIcon(imageData);
        }
    }
}
