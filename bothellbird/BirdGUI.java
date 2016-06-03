package bothell_bird;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class BirdGUI extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 7801651004788925924L;
    private JPanel birdGUIMainPanel;
    private JPanel birdDescriptionPanel;
    private JPanel controlPanel;
    private JTextArea birdDescriptionTextArea;
    private JButton[] birdPicButtons;
    private JButton back;
    private JScrollPane jscroll = new JScrollPane();
    private StringBuilder namesBirdIsKnownBy;
    private DisplayBird bird;

    /**
     * Create application GUI.
     *
     * @param bird
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public BirdGUI(DisplayBird bird) throws SQLException, IOException {
        namesBirdIsKnownBy = new StringBuilder();
        namesBirdIsKnownBy.append("Species: ");
        namesBirdIsKnownBy.append(bird.getName());
        namesBirdIsKnownBy.append(". Common Names: ");
        this.bird = bird;
        for (BirdName currentName : bird.getNames()) {
            namesBirdIsKnownBy.append(currentName.getName());
            switch (currentName.getGender()) {
                case 'f':
                case 'F':
                    namesBirdIsKnownBy.append("(Female)");
                    break;
                case 'm':
                case 'M':
                    namesBirdIsKnownBy.append("(Male)");
                    break;
            }
            namesBirdIsKnownBy.append(", ");
        }
        initComponents();
    }

    private void initComponents() throws SQLException, IOException {
        birdGUIMainPanel = new JPanel();
        birdGUIMainPanel.setLayout(new GridLayout());
        birdDescriptionPanel = new JPanel();
        controlPanel = new JPanel();
        birdDescriptionTextArea = new JTextArea();
        String birdDescription = getDescriptionText();
        setBirdDescription(birdDescription);
        revalidate();
        populateBirdPicButtons();
        initControlsPanel();
        this.setLayout(new BorderLayout());
        this.add(controlPanel, BorderLayout.NORTH);
        this.add(birdGUIMainPanel, BorderLayout.CENTER);
        this.add(birdDescriptionPanel, BorderLayout.SOUTH);
    }

    public void disposer() {
        this.dispose();
    }

    private String getDescriptionText() {
        return bird.toString();
    }

    private void setBirdDescription(String description) {
        birdDescriptionTextArea.setText(description);
        birdDescriptionTextArea.setEditable(false);
        jscroll = new JScrollPane(birdDescriptionTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        birdDescriptionPanel.add(jscroll);
    }

    private void initControlsPanel() {
        back = new JButton("Close Bird display and return to Bothell Bird");
        ActionListener close = new closeAction();
        back.addActionListener(close);
        controlPanel.add(back);
    }
    //probably should refactor this....
    private void populateBirdPicButtons() throws SQLException, IOException {
        List<JLabel> birdPicButtonLabels = new ArrayList<>();
        List<String> birdPicButtonLabelsText;
        birdPicButtonLabelsText = new ArrayList<>();
        birdPicButtonLabelsText.add(this.namesBirdIsKnownBy.toString());
        List<String> femaleNames = new ArrayList<>();
        List<String> maleNames = new ArrayList<>();
        List<String> neuterNames = new ArrayList<>();
        int nS = WavFileRetriever.getNumberOfSounds(bird.getBirdId());
        int nI = ImageRetriever.getNumberOfImages(bird.getBirdId());
        int numberOfButtons = nS > nI ? nS : nI;
        numberOfButtons = numberOfButtons == 0 ? 1 : numberOfButtons;
        birdPicButtons = new JButton[numberOfButtons];
        for (BirdName name : bird.getNames()) {
            if (name.getGender() == 'f') {
                femaleNames.add(name.getName());
            } else if (name.getGender() == 'm') {
                maleNames.add(name.getName());
            } else {
                neuterNames.add(name.getName());
            }
        }

        if (numberOfButtons < 2) {
            StringBuilder alias = new StringBuilder();
            for (String name : neuterNames) {
                alias.append(name + " ");
            }
            birdPicButtons[0] = new JButton("");
            birdPicButtons[0].setMargin(new Insets(0, 0, 0, 0));
            ActionListener soundActionListener = new SoundAction('n');
            birdPicButtons[0].addActionListener(soundActionListener);
            makeButton(0, alias, birdPicButtonLabels, birdPicButtonLabelsText, 'n');
            birdPicButtonLabels.get(0).setText(birdPicButtonLabelsText.get(0));
            birdGUIMainPanel.add(birdPicButtonLabels.get(0));
        } else {
            StringBuilder alias = new StringBuilder();
            alias.append("Female Names: ");
            for (String name : femaleNames) {
                alias.append(name + " ");
            }
            birdPicButtons[0] = new JButton("");
            birdPicButtons[0].setMargin(new Insets(0, 0, 0, 0));
            ActionListener soundActionListenerF = new SoundAction('f');
            birdPicButtons[0].addActionListener(soundActionListenerF);
            makeButton(0, alias, birdPicButtonLabels, birdPicButtonLabelsText, 'f');
            birdPicButtonLabels.get(0).setText(birdPicButtonLabelsText.get(0));
            birdGUIMainPanel.add(birdPicButtonLabels.get(0));
            alias = new StringBuilder();
            alias.append("Male Names: ");
            for (String name : maleNames) {
                alias.append(name + " ");
            }
            birdPicButtons[1] = new JButton("");
            birdPicButtons[1].setMargin(new Insets(0, 0, 0, 0));
            ActionListener soundActionListenerM = new SoundAction('m');
            birdPicButtons[1].addActionListener(soundActionListenerM);
            makeButton(1, alias, birdPicButtonLabels, birdPicButtonLabelsText, 'm');
            birdPicButtonLabels.get(1).setText(birdPicButtonLabelsText.get(1));
            birdGUIMainPanel.add(birdPicButtonLabels.get(1));
        }
    }

    private void makeButton(int counter, StringBuilder alias, List<JLabel> birdPicButtonLabels, List<String> birdPicButtonLabelsText, char gender) throws SQLException, IOException {
        ImageIcon image = ImageRetriever.getImageIcon(bird.getBirdId(), gender, true);
        birdPicButtonLabelsText.add(alias.toString());
        birdPicButtonLabels.add(new JLabel(birdPicButtonLabelsText.get(counter)));
        birdGUIMainPanel.add(birdPicButtonLabels.get(counter));
        birdPicButtons[counter].setIcon(image);
        birdGUIMainPanel.add(birdPicButtons[counter]);

    }

    class SoundAction implements ActionListener {

        private final char gender;

        SoundAction(char gender) {
            this.gender = gender;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                MakeSound.playSound(WavFileRetriever.getSound(bird.getBirdId(), gender));
            } catch (SQLException | IOException | UnsupportedAudioFileException ex) {
                Logger.getLogger(BirdGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    class closeAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            disposer();
        }
    }
}
