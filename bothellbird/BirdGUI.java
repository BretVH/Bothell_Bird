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
    private Bird bird;

    /**
     * Create application GUI.
     *
     * @param bird
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public BirdGUI(Bird bird) throws SQLException, IOException {
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

    private void populateBirdPicButtons() throws SQLException, IOException {
        List<JLabel> birdPicButtonLabels = new ArrayList<>();
        List<String> birdPicButtonLabelsText;
        birdPicButtonLabelsText = new ArrayList<>();
        birdPicButtonLabelsText.add(this.namesBirdIsKnownBy.toString());
        List<BirdName> names = bird.getNames();
        birdPicButtons = new JButton[names.size()];

        int counter = 0;

        for (BirdName name : names) {
            StringBuilder alias = new StringBuilder(name.getName());
            birdPicButtons[counter] = new JButton("");
            birdPicButtons[counter].setMargin(new Insets(0, 0, 0, 0));
            ActionListener soundActionListener = new SoundAction(name);
            birdPicButtons[counter].addActionListener(soundActionListener);
            switch (name.getGender()) {
                case 'f':
                case 'F':
                    alias.append("(Female)");
                    makeButton(counter, alias, birdPicButtonLabels, birdPicButtonLabelsText, name);
                    break;
                case 'm':
                    alias.append("(Male)");
                    makeButton(counter, alias, birdPicButtonLabels, birdPicButtonLabelsText, name);
                    break;
                default:
                    makeButton(counter, alias, birdPicButtonLabels, birdPicButtonLabelsText, name);
            }
            birdPicButtonLabels.get(counter).setText(birdPicButtonLabelsText.get(counter));
            birdGUIMainPanel.add(birdPicButtonLabels.get(counter));
            counter++;
        }
    }

    private void makeButton(int counter, StringBuilder alias, List<JLabel> birdPicButtonLabels, List<String> birdPicButtonLabelsText, BirdName birdName) throws SQLException, IOException {
        ImageIcon image = ImageRetriever.getImageIcon(bird.getBirdId(), birdName.getGender(), true);
        birdPicButtonLabelsText.add(alias.toString());
        birdPicButtonLabels.add(new JLabel(birdPicButtonLabelsText.get(counter)));
        birdGUIMainPanel.add(birdPicButtonLabels.get(counter));
        birdPicButtons[counter].setIcon(image);
        birdGUIMainPanel.add(birdPicButtons[counter]);
    }

    class SoundAction implements ActionListener {

        private final BirdName name;

        SoundAction(BirdName name) {
            this.name = name;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try { 
                MakeSound.playSound(WavFileRetriever.getSound(bird.getBirdId(), name.getGender()));
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
