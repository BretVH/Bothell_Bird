package bothell_bird;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import javax.swing.ImageIcon;

public class ImageRetriever {

    public static ImageIcon getImageIcon(int uniqueBirdId, int nameId, char gender, boolean hasImage, boolean isLarge) throws SQLException, IOException {
        if (hasImage) {
            if (isLarge) {
                return getMyIcon(uniqueBirdId, nameId, gender, 2);
            } else {
                return getMyIcon(uniqueBirdId, nameId, gender, 1);
            }
        } else {
            InputStream in = InputStreamRetriever.make(0, 'a', 0, "Jpg");
            byte[] imageData = org.apache.commons.io.IOUtils.toByteArray(in);
            return new ImageIcon(imageData);
        }
    }

    private static ImageIcon getMyIcon(int uniqueBirdId, int nameId, char gender, int imageSize) throws SQLException, IOException {
        if (gender == 'm' || gender == 'M') {
            InputStream in = InputStreamRetriever.make(nameId, 'm', imageSize, "Jpg");
            byte[] imageData = org.apache.commons.io.IOUtils.toByteArray(in);
            return new ImageIcon(imageData);
        } else if (gender == 'f' || gender == 'F') {
            InputStream in = InputStreamRetriever.make(nameId, 'f', imageSize, "Jpg");
            byte[] imageData = org.apache.commons.io.IOUtils.toByteArray(in);
            return new ImageIcon(imageData);
        } else {
            InputStream in = InputStreamRetriever.make(nameId, 'a', imageSize, "Jpg");
            byte[] imageData = org.apache.commons.io.IOUtils.toByteArray(in);
            return new ImageIcon(imageData);
        }
    }
}
