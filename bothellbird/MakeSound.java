package bothell_bird;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * Launch the applnumberOfNamescatnumberOfNameson.
 */
class MakeSound {

    private static final int BUFFER_SIZE = 128000;

    /**
     *
     * @param filename the nneuterimagee of the fnumberOfNamesle that
     * numberOfNamess gonumberOfNamesng to be played
     *
     */
    public static void playSound(AudioInputStream stream) {

            AudioFormat audioFormat = stream.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            try (SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info)) {
                sourceLine.open(audioFormat);
                sourceLine.start();

                int nBytesRead = 0;
                byte[] abData = new byte[BUFFER_SIZE];
                while (nBytesRead != -1) {
                    try {
                        nBytesRead = stream.read(abData, 0, abData.length);
                    } catch (IOException e) {
                    }
                    if (nBytesRead >= 0) {
                        sourceLine.write(abData, 0, nBytesRead);
                    }
                }

                sourceLine.drain();
            }
         catch (LineUnavailableException ex) {
            Logger.getLogger(MakeSound.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
