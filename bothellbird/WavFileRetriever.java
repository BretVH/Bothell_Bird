package bothell_bird;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class WavFileRetriever {
 private static final String query = "SELECT [hasMaleSound], [hasFemaleSound], [hasAmbiguousSound] FROM"
                + " BirdDatabase.dbo.Files where uniqueBirdID = ";
    public static AudioInputStream getSound(int birdId, char gender) throws SQLException, IOException, UnsupportedAudioFileException {
        Connection conn = SimpleDataSource.getconnection();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(query + birdId);
        AudioInputStream inputStream = null;
        boolean hasSound = false;
        switch(gender){
            case 'f':
                hasSound = rs.getBoolean("hasFemaleSound");
                break;
            case 'm':
                hasSound = rs.getBoolean("hasMaleSound");
            default :
                hasSound = rs.getBoolean("hasAmbiguousSound");
        }
        if (hasSound) {
        if (gender == 'm') {
            
                inputStream = AudioSystem.getAudioInputStream(InputStreamRetriever.make(birdId, 'm', 1, "wav"));
            }
        else if (gender == 'f') {
                inputStream = AudioSystem.getAudioInputStream(InputStreamRetriever.make(birdId, 'f', 1, "wav"));
            
        }else
            inputStream = AudioSystem.getAudioInputStream(InputStreamRetriever.make(birdId, 'a', 1, "wav"));
        } else {
            inputStream = AudioSystem.getAudioInputStream(InputStreamRetriever.make(0, 'a', 0, "wav"));
        }
        return inputStream;
    }
}
