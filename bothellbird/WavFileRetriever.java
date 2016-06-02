package bothell_bird;

import java.io.IOException;
import java.sql.SQLException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class WavFileRetriever {

    public static AudioInputStream getSound(int birdId, char gender, boolean hasSound) throws SQLException, IOException, UnsupportedAudioFileException {
        AudioInputStream inputStream = null;
        if (gender == 'm' || gender == 'M') {
            if (hasSound) {
                inputStream = AudioSystem.getAudioInputStream(InputStreamRetriever.make(birdId, 'm', 1, "wav"));
            }
        } else if (gender == 'f' || gender == 'F') {
            if (hasSound) {
                inputStream = AudioSystem.getAudioInputStream(InputStreamRetriever.make(birdId, 'f', 1, "wav"));
            }
        } else if (hasSound) {
            inputStream = AudioSystem.getAudioInputStream(InputStreamRetriever.make(birdId, 'a', 1, "wav"));
        } else {
            inputStream = AudioSystem.getAudioInputStream(InputStreamRetriever.make(0, 'a', 0, "wav"));
        }
        return inputStream;
    }
}
