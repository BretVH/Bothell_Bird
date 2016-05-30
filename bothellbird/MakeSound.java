/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bothell_bird;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

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
    public static void playSound(String filename) {

        String strFilename = filename;

        try {
            File soundFile = new File(strFilename);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            AudioFormat audioFormat = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            try (SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info)) {
                sourceLine.open(audioFormat);
                sourceLine.start();

                int nBytesRead = 0;
                byte[] abData = new byte[BUFFER_SIZE];
                while (nBytesRead != -1) {
                    try {
                        nBytesRead = audioStream.read(abData, 0, abData.length);
                    } catch (IOException e) {
                    }
                    if (nBytesRead >= 0) {
                        sourceLine.write(abData, 0, nBytesRead);
                    }
                }

                sourceLine.drain();
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            Logger.getLogger(MakeSound.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
