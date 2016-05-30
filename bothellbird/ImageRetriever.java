package bothell_bird;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageRetriever {

    private static final FileMaker maker = new FileMaker();;
    private static ImageIcon myIcon;

    public static ImageIcon readData(int ID, int nameId) throws SQLException, IOException {
        Connection conn = SimpleDataSource.getconnection();
        Statement stat = conn.createStatement();
        //get database table
        String query = "SELECT [hasMaleImage], [hasFemaleImage], [hasAmbiguousImage] FROM"
                + " BirdDatabase.dbo.Files where uniqueBirdID = '" + ID + "'";
        ResultSet rs = stat.executeQuery(query);
        rs.next();
        int id[] = new int[3];
        id[0] = rs.getInt("hasMaleImage");
        id[1] = rs.getInt("hasFemaleImage");
        id[2] = rs.getInt("hasAmbiguousImage");
        String query2 = "SELECT * FROM BirdDatabase.dbo.gender where uniqueBirdName"
                + " = " + nameId;
        ResultSet rs2 = stat.executeQuery(query2);
        rs2.next();
        String gender = rs2.getString("gender");
        if (gender.charAt(0) == 'm' || gender.charAt(0) == 'M') {
            if (id[0] > 0) {
                myIcon = new ImageIcon(ImageIO.read(maker.make(ID, 'm', 1, "Jpg")));
            }
        } else if (gender.charAt(0) == 'f' || gender.charAt(0) == 'F') {
            if (id[1] > 0) {
                myIcon = new ImageIcon(ImageIO.read(maker.make(ID, 'f', 1, "Jpg")));
            }
        } else if (id[2] > 0) {
            try {
                File myImage = maker.make(ID, 'A', 1, "jpg");
                BufferedImage img = ImageIO.read(myImage);
                myIcon = new ImageIcon(img);
            } catch (IOException e) {
            }
        } else {
            myIcon = new ImageIcon(ImageIO.read(maker.make(0, 'a', 0, "Jpg")));
        }
        conn.close();
        return myIcon;
    }

    public static ImageIcon bigImage(int ID, int nameId) throws SQLException, IOException {
        Connection conn = SimpleDataSource.getconnection();
        Statement stat = conn.createStatement();
        //get database table
        String query = "SELECT [hasMaleImage], [hasFemaleImage], [hasAmbiguousImage] FROM"
                + " BirdDatabase.dbo.Files where uniqueBirdID = " + ID;
        ResultSet rs = stat.executeQuery(query);
        rs.next();
        int id[] = new int[3];
        id[0] = rs.getInt("hasMaleImage");
        id[1] = rs.getInt("hasFemaleImage");
        id[2] = rs.getInt("hasAmbiguousImage");
        String query2 = "SELECT [gender] FROM BirdDatabase.dbo.gender where uniqueBirdName"
                + "= " + nameId;
        rs = stat.executeQuery(query2);
        rs.next();
        String gender = rs.getString("gender");
        if (gender.charAt(0) == 'm' || gender.charAt(0) == 'M') {
            if (id[0] > 1) {
                myIcon = new ImageIcon(ImageIO.read(maker.make(ID, 'm', 2, "Jpg")));
            }
        } else if (gender.charAt(0) == 'f' || gender.charAt(0) == 'F') {
            if (id[1] > 1) {
                myIcon = new ImageIcon(ImageIO.read(maker.make(ID, 'f', 2, "Jpg")));
            }
        } else if (id[2] > 1) {
            myIcon = new ImageIcon(ImageIO.read(maker.make(ID, 'a', 2, "Jpg")));
        } else {
            myIcon = new ImageIcon(ImageIO.read(maker.make(0, 'a', 0, "Jpg")));
        }
        conn.close();
        return myIcon;
    }
}
